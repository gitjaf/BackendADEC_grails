package org.sigma.code

class Usuario {

	int id
	String nombre
	String apellido
	String username
	String password
	String empresa
	String direccion
	String telefono
	String email
	
	Localidad localidad
	
	static belongsTo = [perfil: Perfil]
	
	static hasMany = [campos: CampoTabla, tabs: Tab, historiales: Historial]
	
    static constraints = {
		nombre(required: true, blank: false)
		apellido(required: true, blank: false)
		username(required: true, blank: false)
		password(required: true, blank: false)
		email(required: true)
		direccion(nullable: true)
		telefono(nullable: true)
		empresa(nullable: true)
    }

	static mapping = {
		tabs cascade: "save-update"
		campos cascade: "save-update"
		perfil cascade: "save-update" 
		historiales cascade: "all"
		
	}
		
	
}
