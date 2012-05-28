package org.sigma.code



import org.junit.*
import org.springframework.mock.web.MockHttpServletRequest;

import grails.test.mixin.*
import grails.converters.JSON

@TestFor(UsuarioController)
@Mock(Usuario)
class UsuarioControllerTests {
	
    def populateValidParams(params) {
	  params.clear()
	  params["nombre"] = "Un Nombre"
	  params["apellido"] = "Un Apellido"
	  params["username"] = "Un username"
	  params["password"] = "Un password"
	  params["email"] = "unemail@valido.com"
	  
	  assert params != null
    }

    void testIndex() {
        controller.index()
        assert "/usuario/list" == response.redirectedUrl
    }

    void testList() {
		
		populateValidParams(params)
		def usuario = new Usuario(params)
		assert usuario.save() != null
		
		response.format = "json"
		
		controller.list()
		
		assert response.json.size() == 1
		assert response.json[0].password == 'Un password'
		
// 		TEST GENERADO ORIGINALMENTE.		
//        def model = controller.list()
//        assert model.usuarioInstanceList.size() == 0
//        assert model.usuarioInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.usuarioInstance != null
    }

    void testSave() {
        controller.save()

        assert response.status == 500

        response.reset()

        populateValidParams(params)
		request.setJson(params as JSON)
        controller.save()
		
//		  TEST GENERADO ORIGINALMENTE
//        assert response.redirectedUrl == '/usuario/show/0'
		assert response.status == 201
        assert controller.flash.message != null
        assert Usuario.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/usuario/list'

        populateValidParams(params)
        def usuario = new Usuario(params)

        assert usuario.save() != null

        params.id = usuario.id
		
//		  TEST GENERADO ORIGINALMENTE
//        def model = controller.show()
//        assert model.usuarioInstance == usuario

		controller.show()
		assert response.json != null
		assert response.json.password == "Un password"
    }

    
    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/usuario/list'
		
		response.reset()
		
        populateValidParams(params)
        def usuario = new Usuario(params)
		usuario.version = 1
        assert usuario.save() != null

        // test invalid parameters in update
        params.id = usuario.id
		params.nombre = ""
        request.setJson(params as JSON)
		
        controller.update()

        assert view == "/usuario/show"
        assert model.usuarioInstance != null

        usuario.clearErrors()
		response.reset()
		
        populateValidParams(params)
		params.id = usuario.id
		request.setJson(params as JSON)
		
        controller.update()

//		  TEST GENERADO ORIGINALMENTE		
//        assert response.redirectedUrl == "/usuario/show/$usuario.id"
		
		assert response.status == 200
        assert flash.message != null

        //test outdated version number
        response.reset()
        usuario.clearErrors()

		populateValidParams(params);
		params.id = usuario.id
        params.version = -1
		request.setJson(params as JSON)
        controller.update()

        assert view == "/usuario/show"
        assert model.usuarioInstance != null
        assert model.usuarioInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/usuario/list'

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
		
//		  TEST GENERADO ORIGINALMENTE		
//        assert response.redirectedUrl == '/usuario/list'
    }
}
