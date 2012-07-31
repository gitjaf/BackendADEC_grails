package org.sigma.code



import org.junit.*
import grails.test.mixin.*
import grails.converters.JSON
import grails.buildtestdata.mixin.Build

@TestFor(InstitucionController)
@Build(Institucion)
class InstitucionControllerTests {

    def populateValidParams(params) {
	    	 params['domicilio'] = 'valid_domicilio'
  	 	 params['nombre'] = 'valid_nombre'
  	 
  
  			 	 def localidad = Localidad.build()
	 	 assert localidad.save() != null
	 	 params['localidad'] = localidad

	  assert params != null
	  
    }

    void testIndex() {
        controller.index()
        assert "/institucion/list" == response.redirectedUrl
    }

    void testList() {
		request.method = "GET"
		
        def institucion = Institucion.build()
		
		assert institucion.save() != null
		
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
			 	 params.idLocalidad = params.localidad.id

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
		
        def institucion = Institucion.build()
		
		assert institucion.save() != null

        params.id = institucion.id

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

        def institucion = Institucion.build()
		
		assert institucion.save() != null

        // Probar actualizar con parametros no-validos
        params.id = institucion.id
         	 	 params.localidad = '' 
 	 	 	 params.nombre = '' 
 	

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
        def institucion = Institucion.build()
		
		assert institucion.save() != null
		
		params.id = institucion.id
		
		request.setJson(params as JSON)
		
		controller.update()

        assert response.status == 200
		assert response.json != null
	}
	
	void testUpdateConcurrente(){
		request.method = "PUT"
		response.format = "json"
		
        populateValidParams(params)
		def institucion = Institucion.build()
		
		assert institucion.save() != null
		
		institucion.version = 1
		assert institucion.save() != null
		
        params.id = institucion.id
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

        def institucion = Institucion.build()
		
		assert institucion.save() != null

        params.id = institucion.id
		request.setJson(params as JSON)
		
		response.format = "json"
        controller.delete()

        assert Institucion.count() == 0
        assert Institucion.get(institucion.id) == null
        assert response.status == 200
		assert flash.message != null
    }
}
