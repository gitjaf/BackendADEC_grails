package org.sigma.code

class Seccion {

	String img
	String evento
	String nombre
	
	
    static constraints = {
    	img(nullable: false, blank: false)
		evento(nullable: false, blank: false)
		nombre(nullable: false, blank: false)
		
	}
}
