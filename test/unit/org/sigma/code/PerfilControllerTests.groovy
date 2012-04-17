package org.sigma.code



import org.junit.*
import grails.test.mixin.*

@TestFor(PerfilController)
@Mock(Perfil)
class PerfilControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/perfil/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.perfilInstanceList.size() == 0
        assert model.perfilInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.perfilInstance != null
    }

    void testSave() {
        controller.save()

        assert model.perfilInstance != null
        assert view == '/perfil/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/perfil/show/1'
        assert controller.flash.message != null
        assert Perfil.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/perfil/list'


        populateValidParams(params)
        def perfil = new Perfil(params)

        assert perfil.save() != null

        params.id = perfil.id

        def model = controller.show()

        assert model.perfilInstance == perfil
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/perfil/list'


        populateValidParams(params)
        def perfil = new Perfil(params)

        assert perfil.save() != null

        params.id = perfil.id

        def model = controller.edit()

        assert model.perfilInstance == perfil
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/perfil/list'

        response.reset()


        populateValidParams(params)
        def perfil = new Perfil(params)

        assert perfil.save() != null

        // test invalid parameters in update
        params.id = perfil.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/perfil/edit"
        assert model.perfilInstance != null

        perfil.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/perfil/show/$perfil.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        perfil.clearErrors()

        populateValidParams(params)
        params.id = perfil.id
        params.version = -1
        controller.update()

        assert view == "/perfil/edit"
        assert model.perfilInstance != null
        assert model.perfilInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/perfil/list'

        response.reset()

        populateValidParams(params)
        def perfil = new Perfil(params)

        assert perfil.save() != null
        assert Perfil.count() == 1

        params.id = perfil.id

        controller.delete()

        assert Perfil.count() == 0
        assert Perfil.get(perfil.id) == null
        assert response.redirectedUrl == '/perfil/list'
    }
}
