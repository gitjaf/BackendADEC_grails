package org.sigma.code

class Curso {
	
	String curso
	String codigo

	static belongsTo = Institucion 
	
	static hasMany = [instituciones : Institucion]
	
    static constraints = {
		curso(nullable: false, blank: false, required: true)
		codigo(nullable: false, blank: false, required: true)
    }
}
