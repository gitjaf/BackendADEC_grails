package org.sigma.code



import org.junit.*
import grails.test.mixin.*
import grails.converters.JSON
import grails.buildtestdata.mixin.Build

@TestFor(CursoAprobadoController)
@Build(CursoAprobado)
class CursoAprobadoControllerTests {

    def populateValidParams(params) {
	    	 params['fechaFinalizacion'] = '2012-10-20' 
  	
  
  			 	 def candidato = Candidato.build()
	 	 assert candidato.save() != null
	 	 params['candidato'] = candidato
	 	 def curso = Curso.build()
	 	 assert curso.save() != null
	 	 params['curso'] = curso
	 	 def institucion = Institucion.build()
	 	 assert institucion.save() != null
	 	 params['institucion'] = institucion

	  assert params != null
	  
    }

    void testIndex() {
        controller.index()
        assert "/cursoAprobado/list" == response.redirectedUrl
    }

    void testList() {
		request.method = "GET"
		
        def cursoAprobado = CursoAprobado.build()
		
		assert cursoAprobado.save() != null
		
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
			 	 params.idCandidato = params.candidato.id
	 	 params.idCurso = params.curso.id
	 	 params.idInstitucion = params.institucion.id

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
		
        def cursoAprobado = CursoAprobado.build()
		
		assert cursoAprobado.save() != null

        params.id = cursoAprobado.id

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

        def cursoAprobado = CursoAprobado.build()
		
		assert cursoAprobado.save() != null

        // Probar actualizar con parametros no-validos
        params.id = cursoAprobado.id
         	 	 params.candidato = '' 
 	 	 	 params.curso = '' 
 	 	 	 params.fechaFinalizacion = '' 
 	 	 	 params.institucion = '' 
 	

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
        def cursoAprobado = CursoAprobado.build()
		
		assert cursoAprobado.save() != null
		
		params.id = cursoAprobado.id
		
		request.setJson(params as JSON)
		
		controller.update()

        assert response.status == 200
		assert response.json != null
	}
	
	void testUpdateConcurrente(){
		request.method = "PUT"
		response.format = "json"
		
        populateValidParams(params)
		def cursoAprobado = CursoAprobado.build()
		
		assert cursoAprobado.save() != null
		
		cursoAprobado.version = 1
		assert cursoAprobado.save() != null
		
        params.id = cursoAprobado.id
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

        def cursoAprobado = CursoAprobado.build()
		
		assert cursoAprobado.save() != null

        params.id = cursoAprobado.id
		request.setJson(params as JSON)
		
		response.format = "json"
        controller.delete()

        assert CursoAprobado.count() == 0
        assert CursoAprobado.get(cursoAprobado.id) == null
        assert response.status == 200
		assert flash.message != null
    }
}
