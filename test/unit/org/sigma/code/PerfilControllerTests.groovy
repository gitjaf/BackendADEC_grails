package org.sigma.code



import org.apache.jasper.compiler.Node.ParamsAction;
import org.junit.*
import grails.test.mixin.*
import grails.converters.JSON

@TestFor(PerfilController)
@Mock(Perfil)
class PerfilControllerTests {


    def populateValidParams(params) {
		params["descripcion"] = "ADEC"
		assert params != null
      
    }

    void testIndex() {
        controller.index()
        assert "/perfil/list" == response.redirectedUrl
    }

    void testList() {
		request.method = "GET"
		populateValidParams(params)
		def perfil = new Perfil(params)
		assert perfil.save() != null
		
		response.format = "json"
		
        controller.list()
		
		assert response.status == 200
        assert response.json.size() == 1
        assert response.json[0].descripcion == "ADEC"
    }

    void testSave() {
		request.method = "POST"
        controller.save()

        assert response.status == 500

        response.reset()
		response.format = "json"
		
        populateValidParams(params)
		request.setJson(params as JSON)
        controller.save()

        assert response.status == 201
        assert controller.flash.message != null
        assert Perfil.count() == 1
		assert response.json.descripcion == "ADEC"
    }

    void testShow() {
		request.method = "GET"
        controller.show()

        assert response.status == 404
        assert flash.message != null

		response.reset()
		response.format = "json"
		
        populateValidParams(params)
        def perfil = new Perfil(params)
        assert perfil.save() != null
		
        params.id = perfil.id

		mockDomain(Perfil, [perfil])
        controller.show()

		assert response.status == 200
        assert response.json != null
		assert response.json.descripcion == "ADEC"
    }

    void testUpdateInexistente() {
		request.method = "PUT"
		
        controller.update()

        assert response.status == 404
        assert flash.message != null

    }

	void testUpdateInvalido(){
		request.method = "PUT"
		
        populateValidParams(params)
        def perfil = new Perfil(params)
        assert perfil.save() != null

        // test invalid parameters in update
        params.id = perfil.id
		params.descripcion = ""
		request.setJson(params as JSON)
		
		mockDomain(Perfil, [perfil])
		
        controller.update()

        assert response.status == 500
		assert response.json != null
        assert response.json.descripcion == ""

	}
	
	void testUpdateValido(){
		request.method = "PUT"
		response.format = "json"
		
        populateValidParams(params)
		def perfil = new Perfil(params)
		assert perfil.save() != null
		
		params.id = perfil.id
		params.descripcion = "ADEC2"
		request.setJson(params as JSON)
		
		mockDomain(Perfil, [perfil])
		
        controller.update()

        assert response.status == 200
        assert response.json != null
		assert response.json.descripcion == "ADEC2"
		
	}
	
	void testUpdateConcurrente(){
		//test outdated version number
		request.method = "PUT"
		response.format = "json"
		
        populateValidParams(params)
		def perfil = new Perfil(params)
		perfil.version = 1
		assert perfil.save() != null
		
        params.id = perfil.id
        params.version = -1
		request.setJson(params as JSON)
		
		mockDomain(Perfil, [perfil])
		
        controller.update()

		assert response.status == 409
		assert flash.message != null
    }

    void testDelete() {
		request.method = "DELETE"
        controller.delete()
        assert response.status == 404
        assert flash.message != null

        response.reset()

        populateValidParams(params)
        def perfil = new Perfil(params)
        assert perfil.save() != null
        assert Perfil.count() == 1

        params.id = perfil.id

		mockDomain(Perfil, [perfil])
		
        controller.delete()

        assert Perfil.count() == 0
        assert Perfil.get(perfil.id) == null
        assert response.status == 200
		assert flash.message != null
		
    }
}
