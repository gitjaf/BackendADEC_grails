package org.sigma.code

class Tab {

	String nombre
	
	static belongsTo = Usuario
	
	static hasMany = [campos : CampoTabla]
	
    static constraints = {
    	nombre(nullable: false, required: true, blank: false)
	}
	
	
}
