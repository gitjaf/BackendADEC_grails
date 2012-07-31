package org.sigma.code



import org.junit.*
import grails.test.mixin.*
import grails.converters.JSON
import grails.buildtestdata.mixin.Build

@TestFor(IdiomaController)
@Build(Idioma)
class IdiomaControllerTests {

    def populateValidParams(params) {
	    	 params['idioma'] = 'valid_idioma'
  	 
  
  		
	  assert params != null
	  
    }

    void testIndex() {
        controller.index()
        assert "/idioma/list" == response.redirectedUrl
    }

    void testList() {
		request.method = "GET"
		
        def idioma = Idioma.build()
		
		assert idioma.save() != null
		
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
		
        def idioma = Idioma.build()
		
		assert idioma.save() != null

        params.id = idioma.id

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

        def idioma = Idioma.build()
		
		assert idioma.save() != null

        // Probar actualizar con parametros no-validos
        params.id = idioma.id
         	 	 params.idioma = '' 
 	

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
        def idioma = Idioma.build()
		
		assert idioma.save() != null
		
		params.id = idioma.id
		
		request.setJson(params as JSON)
		
		controller.update()

        assert response.status == 200
		assert response.json != null
	}
	
	void testUpdateConcurrente(){
		request.method = "PUT"
		response.format = "json"
		
        populateValidParams(params)
		def idioma = Idioma.build()
		
		assert idioma.save() != null
		
		idioma.version = 1
		assert idioma.save() != null
		
        params.id = idioma.id
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

        def idioma = Idioma.build()
		
		assert idioma.save() != null

        params.id = idioma.id
		request.setJson(params as JSON)
		
		response.format = "json"
        controller.delete()

        assert Idioma.count() == 0
        assert Idioma.get(idioma.id) == null
        assert response.status == 200
		assert flash.message != null
    }
}
