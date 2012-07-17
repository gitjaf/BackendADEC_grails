package org.sigma.code



import org.junit.*
import grails.test.mixin.*
import grails.converters.JSON

@TestFor(CategoriaController)
@Mock(Categoria)
class CategoriaControllerTests {

    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      params["img"] = 'unImg'
	  params["evento"] = 'unEvento'
	  params["nombre"] = 'unNombre'
    }

    void testIndex() {
        controller.index()
        assert "/categoria/list" == response.redirectedUrl
    }

    void testList() {
		request.method = "GET"
		populateValidParams(params)
		
        def novedad_Sidebar = new Categoria(params)
		assert novedad_Sidebar.save() != null
		
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
        def novedad_Sidebar = new Categoria(params)
        assert novedad_Sidebar.save() != null

        params.id = novedad_Sidebar.id

		mockDomain(Categoria, [novedad_Sidebar])
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
        def novedad_Sidebar = new Categoria(params)
        assert novedad_Sidebar.save() != null

        // test invalid parameters in update
        params.id = novedad_Sidebar.id
        params.nombre = ""

		request.setJson(params as JSON)
		
		mockDomain(Categoria, [novedad_Sidebar])
		response.format = "json"
        controller.update()

        assert response.status == 500
        assert response.json != null
	}
	
	void testUpdateValido(){
		request.method  = "PUT"
		response.format = "json"
		
        populateValidParams(params)
        def novedad_Sidebar = new Categoria(params)
		assert novedad_Sidebar.save() != null
		
		params.id = novedad_Sidebar.id
		
		request.setJson(params as JSON)
		
		mockDomain(Categoria, [novedad_Sidebar])
		
		controller.update()

        assert response.status == 200
		assert response.json != null
	}
	
	void testUpdateConcurrente(){
		request.method = "PUT"
		response.format = "json"
		
        populateValidParams(params)
		def novedad_Sidebar = new Categoria(params)
		novedad_Sidebar.version = 1
		assert novedad_Sidebar.save() != null
		
        params.id = novedad_Sidebar.id
        params.version = -1
        request.setJson(params as JSON)
		
		mockDomain(Categoria, [novedad_Sidebar])
		
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
        def novedad_Sidebar = new Categoria(params)
        assert novedad_Sidebar.save() != null

        params.id = novedad_Sidebar.id
		request.setJson(params as JSON)
		
		mockDomain(Categoria, [novedad_Sidebar])
		response.format = "json"
        controller.delete()

        assert Categoria.count() == 0
        assert Categoria.get(novedad_Sidebar.id) == null
        assert response.status == 200
		assert flash.message != null
    }
}
