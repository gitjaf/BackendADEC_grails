package org.sigma.code

class Novedad {

	String titulo
	String cuerpo
	Boolean leido = false
	Date fecha
	
	Novedad_Sidebar categoria
	
    static constraints = {
		fecha(nullable: false, required: true)
		titulo(nullable: false, required: true, blank: false)
		cuerpo(nullable: false, required: true, blank: true)
		leido(nullable: false, required: true)
	
		
	}
}
