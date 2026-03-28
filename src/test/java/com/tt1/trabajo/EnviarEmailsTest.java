package com.tt1.trabajo;

import modelo.Destinatario;
import org.junit.jupiter.api.Test;
import servicios.EnviarEmails;

import static org.junit.jupiter.api.Assertions.assertTrue;

class EnviarEmailsTest {
    @Test
    void enviarEmailTest() {
        EnviarEmails ee = new EnviarEmails();
        Destinatario dest = new Destinatario();
        dest.setDireccion("user@email.com");
        assertTrue(ee.enviarEmail(dest, "TEST"));
    }
}