package org.sigma.code



import org.junit.*
import grails.test.mixin.*

@TestFor(HistorialController)
@Mock(Historial)
class HistorialControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/historial/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.historialInstanceList.size() == 0
        assert model.historialInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.historialInstance != null
    }

    void testSave() {
        controller.save()

        assert model.historialInstance != null
        assert view == '/historial/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/historial/show/1'
        assert controller.flash.message != null
        assert Historial.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/historial/list'


        populateValidParams(params)
        def historial = new Historial(params)

        assert historial.save() != null

        params.id = historial.id

        def model = controller.show()

        assert model.historialInstance == historial
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/historial/list'


        populateValidParams(params)
        def historial = new Historial(params)

        assert historial.save() != null

        params.id = historial.id

        def model = controller.edit()

        assert model.historialInstance == historial
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/historial/list'

        response.reset()


        populateValidParams(params)
        def historial = new Historial(params)

        assert historial.save() != null

        // test invalid parameters in update
        params.id = historial.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/historial/edit"
        assert model.historialInstance != null

        historial.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/historial/show/$historial.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        historial.clearErrors()

        populateValidParams(params)
        params.id = historial.id
        params.version = -1
        controller.update()

        assert view == "/historial/edit"
        assert model.historialInstance != null
        assert model.historialInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/historial/list'

        response.reset()

        populateValidParams(params)
        def historial = new Historial(params)

        assert historial.save() != null
        assert Historial.count() == 1

        params.id = historial.id

        controller.delete()

        assert Historial.count() == 0
        assert Historial.get(historial.id) == null
        assert response.redirectedUrl == '/historial/list'
    }
}
