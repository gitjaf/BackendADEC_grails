package org.sigma.code



import org.junit.*
import grails.test.mixin.*
import grails.converters.JSON
import grails.buildtestdata.mixin.Build

@TestFor(HistorialController)
@Build(Historial)
class HistorialControllerTests {

    def populateValidParams(params) {
	    	 params['fecha'] = '2012-10-20' 
  		 params['registro'] = 'valid_registro'
  	 
  
  			 	 def usuario = Usuario.build()
	 	 assert usuario.save() != null
	 	 params['usuario'] = usuario

	  assert params != null
	  
    }

    void testIndex() {
        controller.index()
        assert "/historial/list" == response.redirectedUrl
    }

    void testList() {
		request.method = "GET"
		
        def historial = Historial.build()
		
		assert historial.save() != null
		
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
			 	 params.idUsuario = params.usuario.id

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
		
        def historial = Historial.build()
		
		assert historial.save() != null

        params.id = historial.id

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

        def historial = Historial.build()
		
		assert historial.save() != null

        // Probar actualizar con parametros no-validos
        params.id = historial.id
         	 	 params.fecha = '' 
 	 	 	 params.registro = '' 
 	 	 	 params.usuario = '' 
 	

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
        def historial = Historial.build()
		
		assert historial.save() != null
		
		params.id = historial.id
		
		request.setJson(params as JSON)
		
		controller.update()

        assert response.status == 200
		assert response.json != null
	}
	
	void testUpdateConcurrente(){
		request.method = "PUT"
		response.format = "json"
		
        populateValidParams(params)
		def historial = Historial.build()
		
		assert historial.save() != null
		
		historial.version = 1
		assert historial.save() != null
		
        params.id = historial.id
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

        def historial = Historial.build()
		
		assert historial.save() != null

        params.id = historial.id
		request.setJson(params as JSON)
		
		response.format = "json"
        controller.delete()

        assert Historial.count() == 0
        assert Historial.get(historial.id) == null
        assert response.status == 200
		assert flash.message != null
    }
}
