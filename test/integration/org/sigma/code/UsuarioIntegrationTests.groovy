package org.sigma.code

import static org.junit.Assert.*
import org.junit.*


class UsuarioIntegrationTests {

	def fixtureLoader
	
    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testFixtureUsuario() {
	
    
       def fixture = fixtureLoader.build {
		   usuario1(Usuario, nombre: "Nombre 1", apellido: "Apellido 1", username: "root1", password: "password")
		   usuario2(Usuario, nombre: "Nombre 2", apellido: "Apellido 2", username: "root2", password: "password")
		   usuario3(Usuario, nombre: "Nombre 3", apellido: "Apellido 3", username: "root3", password: "password")
		   usuario4(Usuario, nombre: "Nombre 4", apellido: "Apellido 4", username: "root4", password: "password")
		   usuario5(Usuario, nombre: "Nombre 5", apellido: "Apellido 5", username: "root5", password: "password")
	   }
	   
	   assert fixture.usuario1.nombre == "Nombre 1"
	   assert fixture.usuario2.nombre == "Nombre 2"
	   assert fixture.usuario3.nombre == "Nombre 3"
	   assert fixture.usuario4.nombre == "Nombre 4"
	   assert fixture.usuario5.nombre == "Nombre 5"
	   assert fixture.usuario5.email != null
    
    }
}
