package org.sigma.code



import org.junit.*
import grails.test.mixin.*
import grails.converters.JSON

@TestFor(CampoTablaController)
@Mock([CampoTabla, Usuario, Perfil])
class CampoTablaControllerTests {

    def populateValidParams(params) {
      	 params['descripcion'] = 'valid_descripcion'
  	 	 params['nombre'] = 'valid_nombre'
  	 
	  assert params != null
	  
    }

    void testIndex() {
        controller.index()
        assert "/campoTabla/list" == response.redirectedUrl
    }

    void testList() {
		request.method = "GET"
		populateValidParams(params)
		
        def campoTabla = new CampoTabla(params)
		assert campoTabla.save() != null
		
		response.format = "json"
		
		controller.list()
		
		assert response.status == 200
		assert response.json.size() == 1
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
        assert response.json != null
    }

    void testShow() {
		request.method = "GET"
		controller.show()

        assert response.status == 404
        assert flash.message != null

		response.reset()
		response.format = "json"
		
        populateValidParams(params)
        def campoTabla = new CampoTabla(params)
        assert campoTabla.save() != null

        params.id = campoTabla.id

		mockDomain(CampoTabla, [campoTabla])
        controller.show()

        assert response.status == 200
		assert response.json != null
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
        def campoTabla = new CampoTabla(params)
        assert campoTabla.save() != null

        // test invalid parameters in update
        params.id = campoTabla.id
         	 params.descripcion = '' 
 	 	 params.nombre = '' 
 	

		request.setJson(params as JSON)
		
		mockDomain(CampoTabla, [campoTabla])
		response.format = "json"
        controller.update()

        assert response.status == 500
        assert response.json != null
	}
	
	void testUpdateValido(){
		request.method  = "PUT"
		response.format = "json"
		
        populateValidParams(params)
        def campoTabla = new CampoTabla(params)
		assert campoTabla.save() != null
		
		params.id = campoTabla.id
		
		request.setJson(params as JSON)
		
		mockDomain(CampoTabla, [campoTabla])
		
		controller.update()

        assert response.status == 200
		assert response.json != null
	}
	
	void testUpdateConcurrente(){
		request.method = "PUT"
		response.format = "json"
		
        populateValidParams(params)
		def campoTabla = new CampoTabla(params)
		campoTabla.version = 1
		assert campoTabla.save() != null
		
        params.id = campoTabla.id
        params.version = -1
        request.setJson(params as JSON)
		
		mockDomain(CampoTabla, [campoTabla])
		
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
        def campoTabla = new CampoTabla(params)
        assert campoTabla.save() != null

        params.id = campoTabla.id
		request.setJson(params as JSON)
		
		mockDomain(CampoTabla, [campoTabla])
		response.format = "json"
        controller.delete()

        assert CampoTabla.count() == 0
        assert CampoTabla.get(campoTabla.id) == null
        assert response.status == 200
		assert flash.message != null
    }
}
