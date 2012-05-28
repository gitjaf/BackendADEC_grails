package org.sigma.code

class Usuario {

	int id
	String nombre
	String apellido
	String username
	String password
	String empresa
	String localidad
	String direccion
	String telefono
	String email
	
	
    static constraints = {
		nombre(required: true, blank: false)
		apellido(required: true, blank: false)
		username(required: true, blank: false)
		password(required: true, blank: false)
		email(required: true)
		localidad(nullable: true)
		direccion(nullable: true)
		telefono(nullable: true)
		empresa(nullable: true)
		
    }
	
	
}
