
include "usuarioFixtures"

build{
		historial1(org.sigma.code.Historial, registro: "Registro 1", fecha: new Date(), leido: "false", usuario: usuario1)
		historial2(org.sigma.code.Historial, registro: "Registro 2", fecha: new Date(), leido: "false", usuario: usuario2)
		historial3(org.sigma.code.Historial, registro: "Registro 3", fecha: new Date(), leido: "false", usuario: usuario1)
		historial4(org.sigma.code.Historial, registro: "Registro 4", fecha: new Date(), leido: "false", usuario: usuario3)
		historial5(org.sigma.code.Historial, registro: "Registro 5", fecha: new Date(), leido: "false", usuario: usuario4)
	}