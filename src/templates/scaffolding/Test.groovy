<%=packageName ? "package ${packageName}\n\n" : ''%>

import org.junit.*
import grails.test.mixin.*
import grails.converters.JSON

@TestFor(${className}Controller)
@Mock(${className})
class ${className}ControllerTests {

    def populateValidParams(params) {
      <%domainClass.properties.each{
		  print ((it.type == String.class)? "\t params['${it.name}'] = 'valid_${it.name}'\n  \t " :
		  (it.type == Boolean.class)? "\t params['${it.name}'] = ${true} \n  \t" : 
		  (it.type == Date.class)? "\t params['${it.name}'] = new Date() \n  \t" :
		  (it.type == Long.class & it.name != "id" & it.name != "version")? "\t params['${it.name}'] = ${1} \n  \t " :
		  (it.type == Integer.class & it.name != "id" & it.name != "version")? "\t params['${it.name}'] = ${1} \n  \t " :
		  (it.type == Double.class & it.name != "id" & it.name != "version")? "\t params['${it.name}'] = ${1.0d} \n  \t " :
		  (it.type == Float.class & it.name != "id" & it.name != "version")? "\t params['${it.name}'] = ${1.0f} \n  \t " : "")
	  }%>
	  assert params != null
	  
    }

    void testIndex() {
        controller.index()
        assert "/$propertyName/list" == response.redirectedUrl
    }

    void testList() {
		request.method = "GET"
		populateValidParams(params)
		
        def ${propertyName} = new ${className}(params)
		assert ${propertyName}.save() != null
		
		response.format = "json"
		
		controller.list()
		
		assert response.status == 200
		assert response.json.size() == 1
		assert response.json.
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
        def ${propertyName} = new ${className}(params)
        assert ${propertyName}.save() != null

        params.id = ${propertyName}.id

		mockDomain(${className}, [${propertyName}])
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
        def ${propertyName} = new ${className}(params)
        assert ${propertyName}.save() != null

        // test invalid parameters in update
        params.id = ${propertyName}.id
        <%domainClass.properties.each{
		print((!it.optional & it.name != "id" & it.name != "version") ? " \t params.${it.name} = null \n \t" : "" )
		}%>

		request.setJson(params as JSON)
		
		mockDomain(${className}, [${propertyName}])
		response.format = "json"
        controller.update()

        assert response.status == 500
        assert response.json != null
	}
	
	void testUpdateValido(){
		request.method  = "PUT"
		response.format = "json"
		
        populateValidParams(params)
        def ${propertyName} = new ${className}(params)
		assert ${propertyName}.save() != null
		
		params.id = ${propertyName}.id
		
		request.setJson(params as JSON)
		
		mockDomain(${className}, [${propertyName}])
		
		controller.update()

        assert response.status == 200
		assert response.json != null
	}
	
	void testUpdateConcurrente(){
		request.method = "PUT"
		response.format = "json"
		
        populateValidParams(params)
		def ${propertyName} = new ${className}(params)
		${propertyName}.version = 1
		assert ${propertyName}.save() != null
		
        params.id = ${propertyName}.id
        params.version = -1
        request.setJson(params as JSON)
		
		mockDomain(${className}, [${propertyName}])
		
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
        def ${propertyName} = new ${className}(params)
        assert ${propertyName}.save() != null

        params.id = ${propertyName}.id
		request.setJson(params as JSON)
		
		mockDomain(${className}, [${propertyName}])
		response.format = "json"
        controller.delete()

        assert ${className}.count() == 0
        assert ${className}.get(${propertyName}.id) == null
        assert response.status == 200
		assert flash.message != null
    }
}
