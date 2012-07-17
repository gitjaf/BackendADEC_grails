package org.sigma.code

class Perfil {

	int id
	String descripcion

	static hasMany = [menues: Menu, secciones: Seccion, categorias: Categoria, novedades: Novedad, campos: CampoTabla, usuarios: Usuario]

	static constraints = {
		descripcion(required: true, nullable: false, blank: false)
	}

	static mapping = {
		campos cascade: "save-update"
		usuarios cascade: "save-update"
	}
}
