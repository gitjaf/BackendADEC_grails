
include "perfilFixtures", "tabFixtures", "historialFixtures"

	build{
	
		usuario1(org.sigma.code.Usuario, nombre: "Nombre 1", apellido: "Apellido 1", username: "root1", password: "password",
			perfil: perfil1, campos: [campo1, campo2], tabs: [tab1], historiales: [historial1])
		usuario2(org.sigma.code.Usuario, nombre: "Nombre 2", apellido: "Apellido 2", username: "root2", password: "password", 
			perfil: perfil2, campos: [campo1, campo3],  tabs: [tab2], historiales: [historial2])
		usuario3(org.sigma.code.Usuario, nombre: "Nombre 3", apellido: "Apellido 3", username: "root3", password: "password", 
			perfil: perfil1, campos: [campo1, campo4],  tabs: [tab3], historiales: [historial3])
		usuario4(org.sigma.code.Usuario, nombre: "Nombre 4", apellido: "Apellido 4", username: "root4", password: "password", 
			perfil: perfil3, campos: [campo1, campo5],  tabs: [tab4], historiales: [historial4])
		usuario5(org.sigma.code.Usuario, nombre: "Nombre 5", apellido: "Apellido 5", username: "root5", password: "password", 
			perfil: perfil4, campos: [campo2, campo4, campo5],  tabs: [tab5], historiales: [historial5])
	}

	
	