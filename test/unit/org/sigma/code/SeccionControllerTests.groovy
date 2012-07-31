package org.sigma.code



import org.junit.*
import grails.test.mixin.*
import grails.converters.JSON
import grails.buildtestdata.mixin.Build

@TestFor(SeccionController)
@Build(Seccion)
class SeccionControllerTests {

    def populateValidParams(params) {
	    	 params['evento'] = 'valid_evento'
  	 	 params['img'] = 'valid_img'
  	 	 params['nombre'] = 'valid_nombre'
  	 
  
  		
	  assert params != null
	  
    }

    void testIndex() {
        controller.index()
        assert "/seccion/list" == response.redirectedUrl
    }

    void testList() {
		request.method = "GET"
		
        def seccion = Seccion.build()
		
		assert seccion.save() != null
		
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
		
        def seccion = Seccion.build()
		
		assert seccion.save() != null

        params.id = seccion.id

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

        def seccion = Seccion.build()
		
		assert seccion.save() != null

        // Probar actualizar con parametros no-validos
        params.id = seccion.id
         	 	 params.evento = '' 
 	 	 	 params.img = '' 
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
        def seccion = Seccion.build()
		
		assert seccion.save() != null
		
		params.id = seccion.id
		
		request.setJson(params as JSON)
		
		controller.update()

        assert response.status == 200
		assert response.json != null
	}
	
	void testUpdateConcurrente(){
		request.method = "PUT"
		response.format = "json"
		
        populateValidParams(params)
		def seccion = Seccion.build()
		
		assert seccion.save() != null
		
		seccion.version = 1
		assert seccion.save() != null
		
        params.id = seccion.id
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

        def seccion = Seccion.build()
		
		assert seccion.save() != null

        params.id = seccion.id
		request.setJson(params as JSON)
		
		response.format = "json"
        controller.delete()

        assert Seccion.count() == 0
        assert Seccion.get(seccion.id) == null
        assert response.status == 200
		assert flash.message != null
    }
}
