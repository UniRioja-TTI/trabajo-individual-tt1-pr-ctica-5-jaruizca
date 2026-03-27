package com.tt1.trabajo;

import modelo.Destinatario;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import servicios.EnviarEmails;

import static org.junit.jupiter.api.Assertions.assertTrue;

class EnviarEmailsTest {
    @Test
    void enviarEmailTest() {
        EnviarEmails ee = new EnviarEmails(LoggerFactory.getLogger("Tests"));
        Destinatario dest = new Destinatario();
        assertTrue(ee.enviarEmail(dest, "TEST"));
    }
}