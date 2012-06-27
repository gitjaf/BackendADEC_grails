
<%=packageName ? "package ${packageName}\n\n" : ''%>import org.springframework.dao.DataIntegrityViolationException
import grails.converters.JSON

class ${className}Controller {

    static allowedMethods = [show: ["GET", "POST"], save: "POST", update: "PUT", delete: "DELETE"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		
		def ${propertyName}List = ${className}.list()
		
		response.status = 200
		
		render ${propertyName}List as JSON
    }
    
    def save() {
        def ${propertyName} = new ${className}(request.JSON)
        if (!${propertyName}.save(flush: true)) {
			response.status = 500
			return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), ${propertyName}.id])
        response.status = 201
		render ${propertyName} as JSON
    }

    def show() {
        def ${propertyName} = ${className}.get(params.id)
        if (!${propertyName}) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), params.id])
            response.status = 404
			render flash.message
            return
        }
		response.status = 200
        render ${propertyName} as JSON
    }

    def update() {
        def ${propertyName} = ${className}.get(params.id)
        if (!${propertyName}) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), params.id])
            response.status = 404
			render flash.message
            return
        }

        if (request.JSON.version) {
            def version = request.JSON.version.toLong()
            if (${propertyName}.version > version) {<% def lowerCaseName = grails.util.GrailsNameUtils.getPropertyName(className) %>
				flash.message = message(code: 'default.optimistic.locking.failure', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), ${propertyName}.id])
                response.status = 409
                return
            }
        }

        ${propertyName}.properties = request.JSON

        if (!${propertyName}.save(flush: true)) {
            response.status = 500
			render ${propertyName} as JSON
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), ${propertyName}.id])
		response.status = 200
        render ${propertyName} as JSON
    }

    def delete() {
        def ${propertyName} = ${className}.get(params.id)
        if (!${propertyName}) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), params.id])
            response.status = 404
			render flash.message
            return
        }

        try {
            ${propertyName}.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), params.id])
            response.status = 200
			render flash.message
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), params.id])
            response.status = 500
			render flash.message
        }
    }
}
