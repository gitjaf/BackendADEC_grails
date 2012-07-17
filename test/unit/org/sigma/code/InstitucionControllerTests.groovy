package org.sigma.code

import org.junit.*
import grails.test.mixin.*
import grails.converters.JSON

@TestFor(InstitucionController)
@Mock([Institucion, Curso])
class InstitucionControllerTests {


    def populateValidParams(params) {
		params["nombre"] = "UTN-FRD"
		params["ubicacion"] = "San Martin 1171"
		assert params != null
      
    }

    void testIndex() {
        controller.index()
        assert "/institucion/list" == response.redirectedUrl
    }

    void testList() {
		request.method = "GET"
		populateValidParams(params)
		def institucion = new Institucion(params)
		assert institucion.save() != null
		
		response.format = "json"
		
        controller.list()

        assert response.status == 200
        assert response.json.size() == 1
		assert response.json[0].nombre == "UTN-FRD"
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
        assert response.json.nombre == "UTN-FRD"
    }

    void testShow() {
		request.method = "GET"
        controller.show()

        assert response.status == 404
        assert flash.message != null

		response.reset()
		response.format = "json"
		
        populateValidParams(params)
        def institucion = new Institucion(params)
        assert institucion.save() != null

        params.id = institucion.id
		
		mockDomain(Institucion, [institucion])
        controller.show()

        assert response.status == 200
		assert response.json != null
		assert response.json.nombre == "UTN-FRD"
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
        def institucion = new Institucion(params)
        assert institucion.save() != null

        // test invalid parameters in update
        params.id = institucion.id
		params.nombre = ""
		request.setJson(params as JSON)
		
		mockDomain(Institucion, [institucion])
		response.format = "json"
        controller.update()

        assert response.status == 500
        assert response.json != null
		assert response.json.nombre == ""

	}
	
	void testUpdateValido(){
		request.method = "PUT"
		response.format = "json"
		
        populateValidParams(params)
		def institucion = new Institucion(params)
		assert institucion.save() != null
		
		params.id = institucion.id
		params.nombre = "UTN - FRD"
		request.setJson(params as JSON)
		
		mockDomain(Institucion, [institucion])
		
        controller.update()

        assert response.status == 200
        assert response.json != null
		assert response.json.nombre == "UTN - FRD"

	}
	
	void testUpdateConcurrente(){
        //test outdated version number
        request.method = "PUT"
        response.format = "json"
		
        populateValidParams(params)
		def institucion = new Institucion(params)
		institucion.version = 1
		assert institucion.save() != null
		
		params.id = institucion.id
        params.version = -1
		request.setJson(params as JSON)
		
		mockDomain(Institucion, [institucion])
		
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
        def institucion = new Institucion(params)
        assert institucion.save() != null

        params.id = institucion.id
		request.setJson(params as JSON)
		
		mockDomain(Institucion, [institucion])
		response.format = "json"
        controller.delete()

        assert Institucion.count() == 0
        assert Institucion.get(institucion.id) == null
        assert response.status == 200
		assert flash.message != null
    }
}
