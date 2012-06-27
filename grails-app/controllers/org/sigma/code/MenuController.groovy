
package org.sigma.code

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.JSON

class MenuController {

    static allowedMethods = [show: ["GET", "POST"], save: "POST", update: "PUT", delete: "DELETE"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		
		def menuInstanceList = Menu.list()
		
		response.status = 200
		
		render menuInstanceList as JSON
    }
    
    def save() {
        def menuInstance = new Menu(request.JSON)
        if (!menuInstance.save(flush: true)) {
			response.status = 500
			return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'menu.label', default: 'Menu'), menuInstance.id])
        response.status = 201
		render menuInstance as JSON
    }

    def show() {
        def menuInstance = Menu.get(params.id)
        if (!menuInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'menu.label', default: 'Menu'), params.id])
            response.status = 404
			render flash.message
            return
        }
		response.status = 200
        render menuInstance as JSON
    }

    def update() {
        def menuInstance = Menu.get(params.id)
        if (!menuInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'menu.label', default: 'Menu'), params.id])
            response.status = 404
			render flash.message
            return
        }

        if (request.JSON.version) {
            def version = request.JSON.version.toLong()
            if (menuInstance.version > version) {
				flash.message = message(code: 'default.optimistic.locking.failure', args: [message(code: 'menu.label', default: 'Menu'), menuInstance.id])
                response.status = 409
                return
            }
        }

        menuInstance.properties = request.JSON

        if (!menuInstance.save(flush: true)) {
            response.status = 500
			render menuInstance as JSON
            return
        } 

		flash.message = message(code: 'default.updated.message', args: [message(code: 'menu.label', default: 'Menu'), menuInstance.id])
		response.status = 200
        render menuInstance as JSON
    }

    def delete() {
        def menuInstance = Menu.get(params.id)
        if (!menuInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'menu.label', default: 'Menu'), params.id])
            response.status = 404
			render flash.message
            return
        }

        try {
            menuInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'menu.label', default: 'Menu'), params.id])
            response.status = 200
			render flash.message
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'menu.label', default: 'Menu'), params.id])
            response.status = 500
			render flash.message
        }
    }
}
