package org.sigma.code

class Novedad {

	Date fecha
	String titulo
	String cuerpo
	Boolean leido = false
	
	Novedad_Sidebar categoria
	
    static constraints = {
    	fecha(nullable: false, blank: false)
		titulo(nullable: false, blank: false)
		cuerpo(nullable: false, blank: true)
		leido(nullable: false)
		
	}
}
