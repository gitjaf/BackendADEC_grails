package org.sigma.code



import org.junit.*
import grails.test.mixin.*
import grails.converters.JSON

@TestFor(CursoController)
@Mock([Institucion, Curso])
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
		populateValidParams(params)
		
        def curso = new Curso(params)
		assert curso.save() != null
		
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
        def curso = new Curso(params)
        assert curso.save() != null

        params.id = curso.id

		mockDomain(Curso, [curso])
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
        def curso = new Curso(params)
        assert curso.save() != null

        // test invalid parameters in update
        params.id = curso.id
         	 params.codigo = '' 
 	 	 params.curso = '' 
 	

		request.setJson(params as JSON)
		
		mockDomain(Curso, [curso])
		response.format = "json"
        controller.update()

        assert response.status == 500
        assert response.json != null
	}
	
	void testUpdateValido(){
		request.method  = "PUT"
		response.format = "json"
		
        populateValidParams(params)
        def curso = new Curso(params)
		assert curso.save() != null
		
		params.id = curso.id
		
		request.setJson(params as JSON)
		
		mockDomain(Curso, [curso])
		
		controller.update()

        assert response.status == 200
		assert response.json != null
	}
	
	void testUpdateConcurrente(){
		request.method = "PUT"
		response.format = "json"
		
        populateValidParams(params)
		def curso = new Curso(params)
		curso.version = 1
		assert curso.save() != null
		
        params.id = curso.id
        params.version = -1
        request.setJson(params as JSON)
		
		mockDomain(Curso, [curso])
		
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
        def curso = new Curso(params)
        assert curso.save() != null

        params.id = curso.id
		request.setJson(params as JSON)
		
		mockDomain(Curso, [curso])
		response.format = "json"
        controller.delete()

        assert Curso.count() == 0
        assert Curso.get(curso.id) == null
        assert response.status == 200
		assert flash.message != null
    }
}
