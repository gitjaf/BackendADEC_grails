package org.sigma.code

class Historial {

	Date fecha
	String registro
	
	Usuario usuario
	
    static constraints = {
    	fecha(nullable: false, required: true)
		registro(nullable: false)
		
	}
}

