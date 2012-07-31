package org.sigma.code



import org.junit.*
import grails.test.mixin.*
import grails.converters.JSON
import grails.buildtestdata.mixin.Build

@TestFor(NovedadController)
@Build(Novedad)
class NovedadControllerTests {

    def populateValidParams(params) {
	    	 params['cuerpo'] = 'valid_cuerpo'
  	 	 params['fecha'] = '2012-10-20' 
  		 params['leido'] = true 
  		 params['titulo'] = 'valid_titulo'
  	 
  
  			 	 def categoria = Categoria.build()
	 	 assert categoria.save() != null
	 	 params['categoria'] = categoria

	  assert params != null
	  
    }

    void testIndex() {
        controller.index()
        assert "/novedad/list" == response.redirectedUrl
    }

    void testList() {
		request.method = "GET"
		
        def novedad = Novedad.build()
		
		assert novedad.save() != null
		
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
			 	 params.idCategoria = params.categoria.id

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
		
        def novedad = Novedad.build()
		
		assert novedad.save() != null

        params.id = novedad.id

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

        def novedad = Novedad.build()
		
		assert novedad.save() != null

        // Probar actualizar con parametros no-validos
        params.id = novedad.id
         	 	 params.categoria = '' 
 	 	 	 params.cuerpo = '' 
 	 	 	 params.fecha = '' 
 	 	 	 params.leido = '' 
 	 	 	 params.titulo = '' 
 	

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
        def novedad = Novedad.build()
		
		assert novedad.save() != null
		
		params.id = novedad.id
		
		request.setJson(params as JSON)
		
		controller.update()

        assert response.status == 200
		assert response.json != null
	}
	
	void testUpdateConcurrente(){
		request.method = "PUT"
		response.format = "json"
		
        populateValidParams(params)
		def novedad = Novedad.build()
		
		assert novedad.save() != null
		
		novedad.version = 1
		assert novedad.save() != null
		
        params.id = novedad.id
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

        def novedad = Novedad.build()
		
		assert novedad.save() != null

        params.id = novedad.id
		request.setJson(params as JSON)
		
		response.format = "json"
        controller.delete()

        assert Novedad.count() == 0
        assert Novedad.get(novedad.id) == null
        assert response.status == 200
		assert flash.message != null
    }
}
