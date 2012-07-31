package org.sigma.code



import org.junit.*
import grails.test.mixin.*
import grails.converters.JSON
import grails.buildtestdata.mixin.Build

@TestFor(LocalidadController)
@Build(Localidad)
class LocalidadControllerTests {

    def populateValidParams(params) {
	    	 params['caracteristica'] = 'valid_caracteristica'
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
		
        def localidad = Localidad.build()
		
		assert localidad.save() != null
		
		response.format = "json"
		
		controller.list()
		
		assert response.status == 200
		assert response.json.size() == 1
    }

    void testSave() {
		request.method = "POST"
		response.format = "json"
        
		controller.save()

        assert response.status == 500
		response.reset()
		
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
		
        def localidad = Localidad.build()
		
		assert localidad.save() != null

        params.id = localidad.id

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

        def localidad = Localidad.build()
		
		assert localidad.save() != null

        // Probar actualizar con parametros no-validos
        params.id = localidad.id
         	 	 params.caracteristica = '' 
 	 	 	 params.nombre = '' 
 	 	 	 params.provincia = '' 
 	

		request.setJson(params as JSON)
		
		response.format = "json"
        controller.update()

        assert response.status == 500
        assert response.json != null
	}
	
	void testUpdateValido(){
		request.method  = "PUT"
		response.format = "json"
		
        populateValidParams(params)
        def localidad = Localidad.build()
		
		assert localidad.save() != null
		
		params.id = localidad.id
		
		request.setJson(params as JSON)
		
		controller.update()

        assert response.status == 200
		assert response.json != null
	}
	
	void testUpdateConcurrente(){
		request.method = "PUT"
		response.format = "json"
		
        populateValidParams(params)
		def localidad = Localidad.build()
		
		assert localidad.save() != null
		
		localidad.version = 1
		assert localidad.save() != null
		
        params.id = localidad.id
        params.version = -1
        request.setJson(params as JSON)
		
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

        def localidad = Localidad.build()
		
		assert localidad.save() != null

        params.id = localidad.id
		request.setJson(params as JSON)
		
		response.format = "json"
        controller.delete()

        assert Localidad.count() == 0
        assert Localidad.get(localidad.id) == null
        assert response.status == 200
		assert flash.message != null
    }
}
