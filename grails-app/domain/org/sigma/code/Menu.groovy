package org.sigma.code

class Menu {

	String evento
	String img
	String nombre
	String controller
	
    static constraints = {
		evento(nullable: false, blank: false)
		img(nullable: false, blank: false)
		nombre(nullable: false, blank: false)
		controller(nullable: false, blank: false)
    }
}
