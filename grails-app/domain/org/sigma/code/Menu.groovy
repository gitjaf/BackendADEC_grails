package org.sigma.code

class Menu {

	String evento
	String img
	String nombre
	String controller
	
    static constraints = {
		evento(nullable: true)
		img(nullable: true)
		nombre(nullable: true)
		controller(nullable: true)
    }
}
