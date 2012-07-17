package org.sigma.code

class Historial {

	Date fecha
	String registro
	
	static belongsTo = [usuario: Usuario]
	
    static constraints = {
    	fecha(nullable: false, required: true)
		registro(nullable: false)
		
	}
	
	static mapping = {
		usuario cascade: "save-update"
	}
	
}

