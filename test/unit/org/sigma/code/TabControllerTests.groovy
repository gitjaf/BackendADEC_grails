package org.sigma.code



import org.junit.*
import grails.test.mixin.*
import grails.converters.JSON

@TestFor(TabController)
@Mock([Tab, Usuario, CampoTabla])
class TabControllerTests {

    def populateValidParams(params) {
      	 params['nombre'] = 'valid_nombre'
		 def usuario = new Usuario(id: 1, nombre: 'unNombre', apellido: 'unApellido', username: 'unUsername', password: 'unPass', email: 'unEmail')
		 mockDomain(Usuario, [usuario])
		 params['idUsuario'] = 1

		 assert params != null
	  
    }

    void testIndex() {
        controller.index()
        assert "/tab/list" == response.redirectedUrl
    }

    void testList() {
		request.method = "GET"
		populateValidParams(params)
		
        def tab = new Tab(params)
		assert tab.save() != null
		
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
        def tab = new Tab(params)
        assert tab.save() != null

        params.id = tab.id

		mockDomain(Tab, [tab])
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
        def tab = new Tab(params)
        assert tab.save() != null

        // test invalid parameters in update
        params.id = tab.id
         	 params.nombre = '' 
 	 	 params.usuario = '' 
 	

		request.setJson(params as JSON)
		
		mockDomain(Tab, [tab])
		response.format = "json"
        controller.update()

        assert response.status == 500
        assert response.json != null
	}
	
	void testUpdateValido(){
		request.method  = "PUT"
		response.format = "json"
		
        populateValidParams(params)
        def tab = new Tab(params)
		assert tab.save() != null
		
		params.id = tab.id
		
		request.setJson(params as JSON)
		
		mockDomain(Tab, [tab])
		
		controller.update()

        assert response.status == 200
		assert response.json != null
	}
	
	void testUpdateConcurrente(){
		request.method = "PUT"
		response.format = "json"
		
        populateValidParams(params)
		def tab = new Tab(params)
		tab.version = 1
		assert tab.save() != null
		
        params.id = tab.id
        params.version = -1
        request.setJson(params as JSON)
		
		mockDomain(Tab, [tab])
		
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
        def tab = new Tab(params)
        assert tab.save() != null

        params.id = tab.id
		request.setJson(params as JSON)
		
		mockDomain(Tab, [tab])
		response.format = "json"
        controller.delete()

        assert Tab.count() == 0
        assert Tab.get(tab.id) == null
        assert response.status == 200
		assert flash.message != null
    }
}
