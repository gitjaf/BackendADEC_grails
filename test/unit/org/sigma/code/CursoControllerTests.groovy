package org.sigma.code



import org.junit.*
import grails.test.mixin.*
import grails.converters.JSON
import grails.buildtestdata.mixin.Build

@TestFor(CursoController)
@Build(Curso)
class CursoControllerTests {

    def populateValidParams(params) {
	    	 params['codigo'] = 'valid_codigo'
  	 	 params['curso'] = 'valid_curso'
  	 
  
  		
	  assert params != null
	  
    }

    void testIndex() {
        controller.index()
        assert "/curso/list" == response.redirectedUrl
    }

    void testList() {
		request.method = "GET"
		
        def curso = Curso.build()
		
		assert curso.save() != null
		
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
		
        def curso = Curso.build()
		
		assert curso.save() != null

        params.id = curso.id

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

        def curso = Curso.build()
		
		assert curso.save() != null

        // Probar actualizar con parametros no-validos
        params.id = curso.id
         	 	 params.codigo = '' 
 	 	 	 params.curso = '' 
 	

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
        def curso = Curso.build()
		
		assert curso.save() != null
		
		params.id = curso.id
		
		request.setJson(params as JSON)
		
		controller.update()

        assert response.status == 200
		assert response.json != null
	}
	
	void testUpdateConcurrente(){
		request.method = "PUT"
		response.format = "json"
		
        populateValidParams(params)
		def curso = Curso.build()
		
		assert curso.save() != null
		
		curso.version = 1
		assert curso.save() != null
		
        params.id = curso.id
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

        def curso = Curso.build()
		
		assert curso.save() != null

        params.id = curso.id
		request.setJson(params as JSON)
		
		response.format = "json"
        controller.delete()

        assert Curso.count() == 0
        assert Curso.get(curso.id) == null
        assert response.status == 200
		assert flash.message != null
    }
}
