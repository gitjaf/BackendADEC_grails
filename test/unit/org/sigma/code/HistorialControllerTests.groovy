package org.sigma.code



import org.junit.*
import grails.test.mixin.*
import grails.converters.JSON

@TestFor(HistorialController)
@Mock(Historial)
class HistorialControllerTests {

    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      	 params['fecha'] = new Date() 
  		 params['registro'] = 'valid_registro'
  	 
	  
    }

    void testIndex() {
        controller.index()
        assert "/historial/list" == response.redirectedUrl
    }

    void testList() {
		request.method = "GET"
		populateValidParams(params)
		
        def historial = new Historial(params)
		assert historial.save() != null
		
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
        def historial = new Historial(params)
        assert historial.save() != null

        params.id = historial.id

		mockDomain(Historial, [historial])
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
        def historial = new Historial(params)
        assert historial.save() != null

        // test invalid parameters in update
        params.id = historial.id
         	 params.fecha = null 
 	 	 params.registro = null 
 	 	 params.usuario = null 
 	

		request.setJson(params as JSON)
		
		mockDomain(Historial, [historial])
		response.format = "json"
        controller.update()

        assert response.status == 500
        assert response.json != null
	}
	
	void testUpdateValido(){
		request.method  = "PUT"
		response.format = "json"
		
        populateValidParams(params)
        def historial = new Historial(params)
		assert historial.save() != null
		
		params.id = historial.id
		
		request.setJson(params as JSON)
		
		mockDomain(Historial, [historial])
		
		controller.update()

        assert response.status == 200
		assert response.json != null
	}
	
	void testUpdateConcurrente(){
		request.method = "PUT"
		response.format = "json"
		
        populateValidParams(params)
		def historial = new Historial(params)
		historial.version = 1
		assert historial.save() != null
		
        params.id = historial.id
        params.version = -1
        request.setJson(params as JSON)
		
		mockDomain(Historial, [historial])
		
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
        def historial = new Historial(params)
        assert historial.save() != null

        params.id = historial.id
		request.setJson(params as JSON)
		
		mockDomain(Historial, [historial])
		response.format = "json"
        controller.delete()

        assert Historial.count() == 0
        assert Historial.get(historial.id) == null
        assert response.status == 200
		assert flash.message != null
    }
}