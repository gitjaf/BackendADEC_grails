
include "menuFixtures", "categoriaFixtures", "novedadFixtures", "campoTablaFixtures", "seccionFixtures"

build{
	perfil1(org.sigma.code.Perfil, descripcion: "ADEC", menues:[menu1, menu2], categorias: [categoria1, categoria2], novedades: [novedad1, novedad2],
		secciones: [seccion1, seccion2], campos: [campo1, campo2])
	perfil2(org.sigma.code.Perfil, descripcion: "Empresas", menues:[menu1, menu2], categorias: [categoria1, categoria2], novedades: [novedad1, novedad2],
		secciones: [seccion1, seccion2], campos: [campo1, campo2])
	perfil3(org.sigma.code.Perfil, descripcion: "Empleos", menues:[menu1, menu2], categorias: [categoria1, categoria2], novedades: [novedad1, novedad2],
		secciones: [seccion1, seccion2], campos: [campo1, campo2])
	perfil4(org.sigma.code.Perfil, descripcion: "Gremios", menues:[menu1, menu2], categorias: [categoria1, categoria2], novedades: [novedad1, novedad2],
		secciones: [seccion1, seccion2], campos: [campo1, campo2])
}