package org.sigma.code



import org.junit.*
import grails.test.mixin.*
import grails.converters.JSON

@TestFor(LocalidadController)
@Mock(Localidad)
class LocalidadControllerTests {

    def populateValidParams(params) {
      	 params['codigoPostal'] = 'valid_codigoPostal'
  	 	 params['nombre'] = 'valid_nombre'
  	 	 params['provincia'] = 'valid_provincia'
  	 
	  assert params != null
	  
    }

    void testIndex() {
        controller.index()
        assert "/localidad/list" == response.redirectedUrl
    }

    void testList() {
		request.method = "GET"
		populateValidParams(params)
		
        def localidad = new Localidad(params)
		assert localidad.save() != null
		
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
        def localidad = new Localidad(params)
        assert localidad.save() != null

        params.id = localidad.id

		mockDomain(Localidad, [localidad])
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
        def localidad = new Localidad(params)
        assert localidad.save() != null

        // test invalid parameters in update
        params.id = localidad.id
         	 params.nombre = '' 
 	 	 params.provincia = '' 
 	

		request.setJson(params as JSON)
		
		mockDomain(Localidad, [localidad])
		response.format = "json"
        controller.update()

        assert response.status == 500
        assert response.json != null
	}
	
	void testUpdateValido(){
		request.method  = "PUT"
		response.format = "json"
		
        populateValidParams(params)
        def localidad = new Localidad(params)
		assert localidad.save() != null
		
		params.id = localidad.id
		
		request.setJson(params as JSON)
		
		mockDomain(Localidad, [localidad])
		
		controller.update()

        assert response.status == 200
		assert response.json != null
	}
	
	void testUpdateConcurrente(){
		request.method = "PUT"
		response.format = "json"
		
        populateValidParams(params)
		def localidad = new Localidad(params)
		localidad.version = 1
		assert localidad.save() != null
		
        params.id = localidad.id
        params.version = -1
        request.setJson(params as JSON)
		
		mockDomain(Localidad, [localidad])
		
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
        def localidad = new Localidad(params)
        assert localidad.save() != null

        params.id = localidad.id
		request.setJson(params as JSON)
		
		mockDomain(Localidad, [localidad])
		response.format = "json"
        controller.delete()

        assert Localidad.count() == 0
        assert Localidad.get(localidad.id) == null
        assert response.status == 200
		assert flash.message != null
    }
}
