
package org.sigma.code

import org.springframework.dao.DataIntegrityViolationException
import grails.converters.JSON



class TabController {

    static allowedMethods = [show: ["GET", "POST"], save: "POST", update: "PUT", delete: "DELETE"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		
		def tabInstanceList = Tab.list()
		
		response.status = 200
		
		render tabInstanceList as JSON
    }
    
    def save() {
        def tabInstance = new Tab(request.JSON)
		
			 	 request.JSON?.idCampos?.each{ id -> tabInstance.addToCampos(CampoTabla.get(id))} 


        
		if (!tabInstance.save(flush: true)) {
			response.status = 500
			return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'tab.label', default: 'Tab'), tabInstance.id])
        response.status = 201
		render tabInstance as JSON
    }

    def show() {
        def tabInstance = Tab.get(params.id)
        if (!tabInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'tab.label', default: 'Tab'), params.id])
            response.status = 404
			render flash.message
            return
        }
		response.status = 200
        render tabInstance as JSON
    }

    def update() {
        def tabInstance = Tab.get(params.id)
        if (!tabInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'tab.label', default: 'Tab'), params.id])
            response.status = 404
			render flash.message
            return
        }

        if (request.JSON.version) {
            def version = request.JSON.version.toLong()
            if (tabInstance.version > version) {
				flash.message = message(code: 'default.optimistic.locking.failure', args: [message(code: 'tab.label', default: 'Tab'), tabInstance.id])
                response.status = 409
                return
            }
        }

        tabInstance.properties = request.JSON
		
		 	 	 if(request.JSON?.idCampos || request.JSON?.idCampos?.isEmpty()){ 
	 	 	 tabInstance.campos?.clear() 
	 	 	 request.JSON.idCampos.each{id -> tabInstance.addToCampos(CampoTabla.get(id))} 
	 	} 


		
        if (!tabInstance.save(flush: true)) {
            response.status = 500
			render tabInstance as JSON
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'tab.label', default: 'Tab'), tabInstance.id])
		response.status = 200
        render tabInstance as JSON
    }

    def delete() {
        def tabInstance = Tab.get(params.id)
        if (!tabInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'tab.label', default: 'Tab'), params.id])
            response.status = 404
			render flash.message
            return
        }

        try {
            tabInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'tab.label', default: 'Tab'), params.id])
            response.status = 200
			render flash.message
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'tab.label', default: 'Tab'), params.id])
            response.status = 500
			render flash.message
        }
    }
}
