package org.sigma.code



import org.junit.*
import grails.test.mixin.*
import grails.converters.JSON

@TestFor(MenuController)
@Mock(Menu)
class MenuControllerTests {


    def populateValidParams(params) {
      // TODO: Populate valid properties like...
      params["evento"] = "unEvento"
	  params["img"] = "unImg"
	  params["nombre"] = "unNombre"
	  params["controller"] = "unController"
      assert params != null
    }

    void testIndex() {
        controller.index()
        assert "/menu/list" == response.redirectedUrl
    }

    void testList() {
		request.method = "GET"
		populateValidParams(params)
		
        def menu = new Menu(params)
		assert menu.save() != null
		
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
        def menu = new Menu(params)
        assert menu.save() != null

        params.id = menu.id

		mockDomain(Menu, [menu])
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
        def menu = new Menu(params)
        assert menu.save() != null

        // test invalid parameters in update
        params.id = menu.id
		params.controller = ""

		request.setJson(params as JSON)
		
		mockDomain(Menu, [menu])
		response.format = "json"
        controller.update()

        assert response.status == 500
        assert response.json != null
		

	}
	
	void testUpdateValido(){
		request.method  = "PUT"
		response.format = "json"
		
        populateValidParams(params)
        def menu = new Menu(params)
		assert menu.save() != null
		
		params.id = menu.id
		
		request.setJson(params as JSON)
		
		mockDomain(Menu, [menu])
		
		controller.update()

        assert response.status == 200
		assert response.json != null
	}
	
	void testUpdateConcurrente(){
		
		request.method = "PUT"
		response.format = "json"
		
        populateValidParams(params)
		def menu = new Menu(params)
		menu.version = 1
		assert menu.save() != null
		
        params.id = menu.id
        params.version = -1
        request.setJson(params as JSON)
		
		mockDomain(Menu, [menu])
		
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
        def menu = new Menu(params)
        assert menu.save() != null

        params.id = menu.id
		request.setJson(params as JSON)
		
		mockDomain(Menu, [menu])
		response.format = "json"
        controller.delete()

        assert Menu.count() == 0
        assert Menu.get(menu.id) == null
        assert response.status == 200
		assert flash.message != null
    }
}
