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
		nombre(required: true)
		apellido(required: true)
		username(required: true)
		password(required: true)
		email(required: true)
//		localidad(nullable: true)
//		direccion(nullable: true)
//		telefono(nullable: true)
//		empresa(nullable: true)
//		
    }
	
	
}
