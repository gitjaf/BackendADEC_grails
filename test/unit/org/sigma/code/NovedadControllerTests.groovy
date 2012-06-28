package org.sigma.code



import java.text.SimpleDateFormat;

import org.junit.*
import grails.test.mixin.*
import grails.converters.JSON

@TestFor(NovedadController)
@Mock([Novedad, Novedad_Sidebar])
class NovedadControllerTests {

    def populateValidParams(params) {
      	 params['cuerpo'] = 'valid_cuerpo'
  		 params['leido'] = true 
  		 params['titulo'] = 'valid_titulo'
		 def categoria = new Novedad_Sidebar(id: 1, img: "imagen", evento: "evento", nombre: "nombre")
		 assert categoria.save() != null
		 params['categoria'] = categoria
		 params['fecha'] = "20/01/2012"
  		 assert params != null
	  
    }

    void testIndex() {
        controller.index()
        assert "/novedad/list" == response.redirectedUrl
    }

    void testList() {
		request.method = "GET"
		populateValidParams(params)
        
		def novedad = new Novedad(params)
		novedad.fecha = new SimpleDateFormat("dd/MM/yyyy").parse(params.fecha)
		
		/* Es necesario hacer un clearErrors() porque al bindear params con novedad se produce un error de tipos entre
		* la fecha de tipo String que viene en el mapa params, y el atributo de tipo Date de la instancia novedad
		*/
		novedad.clearErrors()
		
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
		mockDomain(Novedad_Sidebar, [params.categoria])
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
		
        populateValidParams(params)
        def novedad = new Novedad(params)
		novedad.fecha = new SimpleDateFormat("dd/MM/yyyy").parse(params.fecha)
		
		/* Es necesario hacer un clearErrors() porque al bindear params con novedad se produce un error de tipos entre
		* la fecha de tipo String que viene en el mapa params, y el atributo de tipo Date de la instancia novedad
		*/
		novedad.clearErrors()
		assert novedad.save() != null

        params.id = novedad.id

		mockDomain(Novedad, [novedad])
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
		def novedad = new Novedad(params)
		novedad.fecha = new SimpleDateFormat("dd/MM/yyyy").parse(params.fecha)
		
		/* Es necesario hacer un clearErrors() porque al bindear params con novedad se produce un error de tipos entre
		 * la fecha de tipo String que viene en el mapa params, y el atributo de tipo Date de la instancia novedad
		 */
		novedad.clearErrors()
        assert novedad.save() != null

        // test invalid parameters in update
         params.id = novedad.id
         params.categoria = "" 
 	 	 params.cuerpo = "" 
 	 	 params.fecha = "" 
 	 	 params.leido = "" 
 	 	 params.titulo = "" 
 	

		request.setJson(params as JSON)
		
		mockDomain(Novedad, [novedad])
		response.format = "json"
        controller.update()

        assert response.status == 500
        assert response.json != null
	}
	
	void testUpdateValido(){
		request.method  = "PUT"
		response.format = "json"
		
        populateValidParams(params)
        def novedad = new Novedad(params)
		novedad.fecha = new SimpleDateFormat("dd/MM/yyyy").parse(params.fecha)
		
		/* Es necesario hacer un clearErrors() porque al bindear params con novedad se produce un error de tipos entre
		 * la fecha de tipo String que viene en el mapa params, y el atributo de tipo Date de la instancia novedad
		 */
		novedad.clearErrors()
		assert novedad.save() != null
		
		params.id = novedad.id
		
		request.setJson(params as JSON)
		
		mockDomain(Novedad, [novedad])
		
		controller.update()

        assert response.status == 200
		assert response.json != null
	}
	
	void testUpdateConcurrente(){
		request.method = "PUT"
		response.format = "json"
		
        populateValidParams(params)
		def novedad = new Novedad(params)
		novedad.fecha = new SimpleDateFormat("dd/MM/yyyy").parse(params.fecha)
		/* Es necesario hacer un clearErrors() porque al bindear params con novedad se produce un error de tipos entre
		 * la fecha de tipo String que viene en el mapa params, y el atributo de tipo Date de la instancia novedad
		 */
		novedad.clearErrors()
		novedad.version = 1
		assert novedad.save() != null
		
        params.id = novedad.id
        params.version = -1
        request.setJson(params as JSON)
		
		mockDomain(Novedad, [novedad])
		
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
        def novedad = new Novedad(params)
		novedad.fecha = new SimpleDateFormat("dd/MM/yyyy").parse(params.fecha)
		
		/* Es necesario hacer un clearErrors() porque al bindear params con novedad se produce un error de tipos entre
		 * la fecha de tipo String que viene en el mapa params, y el atributo de tipo Date de la instancia novedad
		 */
		novedad.clearErrors()
        assert novedad.save() != null

        params.id = novedad.id
		request.setJson(params as JSON)
		
		mockDomain(Novedad, [novedad])
		response.format = "json"
        controller.delete()

        assert Novedad.count() == 0
        assert Novedad.get(novedad.id) == null
        assert response.status == 200
		assert flash.message != null
    }
}
