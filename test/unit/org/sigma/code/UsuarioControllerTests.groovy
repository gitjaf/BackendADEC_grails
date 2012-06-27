package org.sigma.code



import org.junit.*
import org.springframework.mock.web.MockHttpServletRequest;

import grails.test.mixin.*
import grails.converters.JSON
import groovy.mock.interceptor.MockFor;

@TestFor(UsuarioController)
@Mock([Usuario, Perfil])
class UsuarioControllerTests {
	
    def populateValidParams(Map params) {
	  params.clear()
	  params["nombre"] = "Un Nombre"
	  params["apellido"] = "Un Apellido"
	  params["username"] = "Un username"
	  params["password"] = "Un password"
	  params["email"] = "unemail@valido.com"
	  def perfil = new Perfil(id: 1, descripcion: 'ADEC')
	  params["perfil"] = perfil
	  
	  assert params != null
    }

    void testIndex() {
        controller.index()
        assert "/usuario/list" == response.redirectedUrl
    }

    void testList() {
		request.setMethod("GET")
		// Prueba listado de todos los usuarios 
		populateValidParams(params)
		def usuario = new Usuario(params)
		assert usuario.save() != null
		
		response.format = "json"
		controller.list()
		
		assert response.status == 200
		assert response.json.size() == 1
		assert response.json[0].password != null
		
    }


    void testSave() {
		request.setMethod("POST")
		//Prueba guardar un usuario no valido
        controller.save()
        assert response.status == 500
        response.reset()
		
		//Prueba guardar usuario valido
        populateValidParams(params)
		mockDomain(Perfil, [params.perfil])
		params.idPerfil = params.perfil.id
		request.setJson(params as JSON)
        response.format = "json"
		controller.save()
		
		assert response.status == 201
        assert Usuario.count() == 1
		assert response.json.nombre == "Un Nombre"
		
    }

    void testShow() {
		request.setMethod("GET")
        // Prueba mostrar un objeto no existente
		controller.show()

        assert response.status == 404
        assert flash.message != null

		response.reset()
		
		// Prueba mostrar un objeto existente
        populateValidParams(params)
        def usuario = new Usuario(params)
        assert usuario.save() != null

		response.format = "json"
		
        params.id = usuario.id

		controller.show()
		
		assert response.status == 200
		assert response.json != null
		assert response.json.password == "Un password"
    }

    
    void testUpdateInexistente() {
    	//Prueba actualizar un recurso inexistente
		request.setMethod("PUT")
		
		controller.update()
		
		assert response.status == 404
		assert flash.message != null

    }
	
	void testUpdateConcurrente(){
		// Prueba actualizar un objeto cuya version implica modificacion concurrente
		request.setMethod("PUT")
			
		populateValidParams(params)
		def usuario = new Usuario(params)
		assert usuario.save(flush: true) != null
		
		params.version = 1
		usuario.version = 2
		
		params.id = usuario.id
		mockDomain(Usuario, [usuario])
		
		request.setJson(params as JSON)
		
		controller.update()
		assert response.status == 409
		assert flash.message != null
	}	
	
	void testUpdateInvalido(){
		// Prueba guardar un objeto con parametros no validos
		request.setMethod("PUT")
		populateValidParams(params)

		def usuario = new Usuario(params)
		assert usuario.save() != null
				
		params.id = usuario.id
		params.nombre = ""
		
		request.setJson(params as JSON)
		
		controller.update()
		
		assert response.status == 500
		assert response.json.nombre == ""
		
    }

    void testDelete() {
		request.method = "DELETE"
        controller.delete()
		assert response.status == 404
		assert flash.message != null

        response.reset()

        populateValidParams(params)
        def usuario = new Usuario(params)

        assert usuario.save() != null
        assert Usuario.count() == 1

        params.id = usuario.id

        controller.delete()

        assert Usuario.count() == 0
        assert Usuario.get(usuario.id) == null
		assert response.status == 200
		assert flash.message != null
		
    }
}
