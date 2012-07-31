package org.sigma.code



import org.junit.*
import grails.test.mixin.*
import grails.converters.JSON
import grails.buildtestdata.mixin.Build

@TestFor(CandidatoController)
@Build(Candidato)
class CandidatoControllerTests {

    def populateValidParams(params) {
	    	 params['apellido'] = 'valid_apellido'
  	 	 params['celular'] = 'valid_celular'
  	 	 params['cuil'] = 'valid_cuil'
  	 	 params['dni'] = 'valid_dni'
  	 	 params['domicilio'] = 'valid_domicilio'
  	 	 params['email'] = 'valid_email'
  	 	 params['estado'] = 1 
  	 	 params['fechaDeNacimiento'] = '2012-10-20' 
  		 params['institucion'] = 'valid_institucion'
  	 	 params['nivelDeEstudios'] = 'valid_nivelDeEstudios'
  	 	 params['nivelDeInformatica'] = 'valid_nivelDeInformatica'
  	 	 params['nombre'] = 'valid_nombre'
  	 	 params['observacion'] = 'valid_observacion'
  	 	 params['ocupadoEnOficio'] = true 
  		 params['telefono'] = 'valid_telefono'
  	 	 params['titulo'] = 'valid_titulo'
  	 
  
  			 	 def localidad = Localidad.build()
	 	 assert localidad.save() != null
	 	 params['localidad'] = localidad

	  assert params != null
	  
    }

    void testIndex() {
        controller.index()
        assert "/candidato/list" == response.redirectedUrl
    }

    void testList() {
		request.method = "GET"
		
        def candidato = Candidato.build()
		
		assert candidato.save() != null
		
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
		
        def candidato = Candidato.build()
		
		assert candidato.save() != null

        params.id = candidato.id

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

        def candidato = Candidato.build()
		
		assert candidato.save() != null

        // Probar actualizar con parametros no-validos
        params.id = candidato.id
         	 	 params.apellido = '' 
 	 	 	 params.dni = '' 
 	 	 	 params.email = '' 
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
        def candidato = Candidato.build()
		
		assert candidato.save() != null
		
		params.id = candidato.id
		
		request.setJson(params as JSON)
		
		controller.update()

        assert response.status == 200
		assert response.json != null
	}
	
	void testUpdateConcurrente(){
		request.method = "PUT"
		response.format = "json"
		
        populateValidParams(params)
		def candidato = Candidato.build()
		
		assert candidato.save() != null
		
		candidato.version = 1
		assert candidato.save() != null
		
        params.id = candidato.id
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

        def candidato = Candidato.build()
		
		assert candidato.save() != null

        params.id = candidato.id
		request.setJson(params as JSON)
		
		response.format = "json"
        controller.delete()

        assert Candidato.count() == 0
        assert Candidato.get(candidato.id) == null
        assert response.status == 200
		assert flash.message != null
    }
}
