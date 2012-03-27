package org.sigma.code

import static org.junit.Assert.*
import org.junit.*
import groovy.util.GroovyTestCase


class UsuarioIntegrationTests extends GroovyTestCase{

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
	   
		def u = Usuario.findByApellido("Apellido 1")
		assertNotNull u
       	assertEquals "Nombre 1", u.nombre
    }
}
