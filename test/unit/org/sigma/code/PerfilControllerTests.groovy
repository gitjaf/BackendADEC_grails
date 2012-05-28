package org.sigma.code



import org.apache.jasper.compiler.Node.ParamsAction;
import org.junit.*
import grails.test.mixin.*
import grails.converters.JSON

@TestFor(PerfilController)
@Mock(Perfil)
class PerfilControllerTests {


    def populateValidParams(params) {
		params["perfil"] = "ADEC"
		params["seccion"] = "Seccion 1"
		assert params != null
      
    }

    void testIndex() {
        controller.index()
        assert "/perfil/list" == response.redirectedUrl
    }

    void testList() {
		populateValidParams(params)
		def perfil = new Perfil(params)
		assert perfil.save() != null
		
		response.format = "json"
		
        controller.list()

        assert response.json.size() == 1
        assert response.json[0].perfil == "ADEC"
    }

    void testCreate() {
       def model = controller.create()

       assert model.perfilInstance != null
    }

    void testSave() {
        controller.save()

        assert response.status == 500

        response.reset()

        populateValidParams(params)
		request.setJson(params as JSON)
        controller.save()

        assert response.status == 201
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

        controller.show()

        assert response.json != null
		assert response.json.perfil == "ADEC"
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/perfil/list'

        response.reset()

        populateValidParams(params)
        def perfil = new Perfil(params)
		perfil.version = 1
        assert perfil.save() != null

        // test invalid parameters in update
        params.id = perfil.id
		params.perfil = ""
		request.setJson(params as JSON)
		
        controller.update()

        assert view == "/perfil/show"
        assert model.perfilInstance != null

        perfil.clearErrors()
		response.reset()

        populateValidParams(params)
		params.id = perfil.id
		request.setJson(params as JSON)
		
        controller.update()

        assert response.status == 200
        assert flash.message != null

        //test outdated version number
        response.reset()
        perfil.clearErrors()

        populateValidParams(params)
        params.id = perfil.id
        params.version = -1
		request.setJson(params as JSON)
        controller.update()

        assert view == "/perfil/show"
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
        assert response.status == 200
		assert flash.message != null
		
    }
}
