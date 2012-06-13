package org.sigma.code

class Seccion {

	String img
	String evento
	String nombre
	
	
    static constraints = {
    	img(nullable: true)
		evento(nullable: true)
		nombre(nullable: true)
		
	}
}
