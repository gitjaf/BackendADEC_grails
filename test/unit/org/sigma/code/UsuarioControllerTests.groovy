package org.sigma.code



import org.junit.*
import grails.test.mixin.*
import grails.converters.JSON
import grails.buildtestdata.mixin.Build

@TestFor(UsuarioController)
@Build(Usuario)
class UsuarioControllerTests {

    def populateValidParams(params) {
	    	 params['apellido'] = 'valid_apellido'
  	 	 params['direccion'] = 'valid_direccion'
  	 	 params['email'] = 'valid_email'
  	 	 params['empresa'] = 'valid_empresa'
  	 	 params['nombre'] = 'valid_nombre'
  	 	 params['password'] = 'valid_password'
  	 	 params['telefono'] = 'valid_telefono'
  	 	 params['username'] = 'valid_username'
  	 
  
  			 	 def localidad = Localidad.build()
	 	 assert localidad.save() != null
	 	 params['localidad'] = localidad
	 	 def perfil = Perfil.build()
	 	 assert perfil.save() != null
	 	 params['perfil'] = perfil

	  assert params != null
	  
    }

    void testIndex() {
        controller.index()
        assert "/usuario/list" == response.redirectedUrl
    }

    void testList() {
		request.method = "GET"
		
        def usuario = Usuario.build()
		
		assert usuario.save() != null
		
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
	 	 params.idPerfil = params.perfil.id

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
		
        def usuario = Usuario.build()
		
		assert usuario.save() != null

        params.id = usuario.id

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

        def usuario = Usuario.build()
		
		assert usuario.save() != null

        // Probar actualizar con parametros no-validos
        params.id = usuario.id
         	 	 params.apellido = '' 
 	 	 	 params.email = '' 
 	 	 	 params.localidad = '' 
 	 	 	 params.nombre = '' 
 	 	 	 params.password = '' 
 	 	 	 params.perfil = '' 
 	 	 	 params.username = '' 
 	

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
        def usuario = Usuario.build()
		
		assert usuario.save() != null
		
		params.id = usuario.id
		
		request.setJson(params as JSON)
		
		controller.update()

        assert response.status == 200
		assert response.json != null
	}
	
	void testUpdateConcurrente(){
		request.method = "PUT"
		response.format = "json"
		
        populateValidParams(params)
		def usuario = Usuario.build()
		
		assert usuario.save() != null
		
		usuario.version = 1
		assert usuario.save() != null
		
        params.id = usuario.id
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

        def usuario = Usuario.build()
		
		assert usuario.save() != null

        params.id = usuario.id
		request.setJson(params as JSON)
		
		response.format = "json"
        controller.delete()

        assert Usuario.count() == 0
        assert Usuario.get(usuario.id) == null
        assert response.status == 200
		assert flash.message != null
    }
}
