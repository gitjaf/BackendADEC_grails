package org.sigma.code



import org.junit.*
import grails.test.mixin.*
import grails.converters.JSON
import grails.buildtestdata.mixin.Build

@TestFor(LenguajeController)
@Build(Lenguaje)
class LenguajeControllerTests {

    def populateValidParams(params) {
	    	 params['nivel'] = 'valid_nivel'
  	 
  
  			 	 def idioma = Idioma.build()
	 	 assert idioma.save() != null
	 	 params['idioma'] = idioma

	  assert params != null
	  
    }

    void testIndex() {
        controller.index()
        assert "/lenguaje/list" == response.redirectedUrl
    }

    void testList() {
		request.method = "GET"
		
        def lenguaje = Lenguaje.build()
		
		assert lenguaje.save() != null
		
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
			 	 params.idIdioma = params.idioma.id

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
		
        def lenguaje = Lenguaje.build()
		
		assert lenguaje.save() != null

        params.id = lenguaje.id

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

        def lenguaje = Lenguaje.build()
		
		assert lenguaje.save() != null

        // Probar actualizar con parametros no-validos
        params.id = lenguaje.id
         	 	 params.idioma = '' 
 	 	 	 params.nivel = '' 
 	

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
        def lenguaje = Lenguaje.build()
		
		assert lenguaje.save() != null
		
		params.id = lenguaje.id
		
		request.setJson(params as JSON)
		
		controller.update()

        assert response.status == 200
		assert response.json != null
	}
	
	void testUpdateConcurrente(){
		request.method = "PUT"
		response.format = "json"
		
        populateValidParams(params)
		def lenguaje = Lenguaje.build()
		
		assert lenguaje.save() != null
		
		lenguaje.version = 1
		assert lenguaje.save() != null
		
        params.id = lenguaje.id
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

        def lenguaje = Lenguaje.build()
		
		assert lenguaje.save() != null

        params.id = lenguaje.id
		request.setJson(params as JSON)
		
		response.format = "json"
        controller.delete()

        assert Lenguaje.count() == 0
        assert Lenguaje.get(lenguaje.id) == null
        assert response.status == 200
		assert flash.message != null
    }
}
