package org.sigma.code



import org.junit.*
import grails.test.mixin.*
import grails.converters.JSON
import java.text.SimpleDateFormat

@TestFor(HistorialController)
@Mock([Historial, Usuario, Perfil])
class HistorialControllerTests {

    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      	 params['fecha'] = "22/01/2012"
  		 params['registro'] = 'valid_registro'
         def perfil = new Perfil(id: 1, descripcion: 'ADEC')
		 def usuario = new Usuario(id: 1, apellido: "unApellido", email: "unEmail", nombre: "unNombre", username: "unUsername", password: "unPassword", perfil: perfil)
		 assert usuario.save() != null
		 params['usuario'] = usuario 
  	 
	  
    }

    void testIndex() {
        controller.index()
        assert "/historial/list" == response.redirectedUrl
    }

    void testList() {
		request.method = "GET"
		populateValidParams(params)
		
        def historial = new Historial(params)
		historial.fecha = params.date("fecha", "dd/MM/yyyy")
		
		/* Es necesario hacer un clearErrors() porque al bindear params con novedad se produce un error de tipos entre
		* la fecha de tipo String que viene en el mapa params, y el atributo de tipo Date de la instancia novedad
		*/
		historial.clearErrors()
		assert historial.save() != null
		
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
        mockDomain(Usuario, [params.usuario])
		params.idUsuario = params.usuario.id
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
		historial.fecha = params.date("fecha", "dd/MM/yyyy")
		
		/* Es necesario hacer un clearErrors() porque al bindear params con novedad se produce un error de tipos entre
		* la fecha de tipo String que viene en el mapa params, y el atributo de tipo Date de la instancia novedad
		*/
		historial.clearErrors()
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
		historial.fecha = params.date("fecha", "dd/MM/yyyy")
		
		/* Es necesario hacer un clearErrors() porque al bindear params con novedad se produce un error de tipos entre
		* la fecha de tipo String que viene en el mapa params, y el atributo de tipo Date de la instancia novedad
		*/
		historial.clearErrors()
        assert historial.save() != null

        // test invalid parameters in update
        params.id = historial.id
         	 params.fecha = "" 
 	 	 params.registro = "" 
 	 	 params.usuario = "" 
 	

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
		historial.fecha = params.date("fecha", "dd/MM/yyyy")
		
		/* Es necesario hacer un clearErrors() porque al bindear params con novedad se produce un error de tipos entre
		* la fecha de tipo String que viene en el mapa params, y el atributo de tipo Date de la instancia novedad
		*/
		historial.clearErrors()
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
		historial.fecha = params.date("fecha", "dd/MM/yyyy")
		
		/* Es necesario hacer un clearErrors() porque al bindear params con novedad se produce un error de tipos entre
		* la fecha de tipo String que viene en el mapa params, y el atributo de tipo Date de la instancia novedad
		*/
		historial.clearErrors()
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
		historial.fecha = params.date("fecha", "dd/MM/yyyy")
		
		/* Es necesario hacer un clearErrors() porque al bindear params con novedad se produce un error de tipos entre
		* la fecha de tipo String que viene en el mapa params, y el atributo de tipo Date de la instancia novedad
		*/
		historial.clearErrors()
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
