package com.tt1.trabajo;

import interfaces.InterfazContactoSim;
import modelo.DatosSolicitud;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servicios.ContactoSim;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ContactoSimTest {
    InterfazContactoSim ics;

    @BeforeEach
    void setup() {
        int seed = 549015;
        ics = new ContactoSim(seed);
    }

    @Test
    void solicitarSimulationTest() {
        Map<Integer,Integer> map = new HashMap<>();
        map.put(-1,1);
        assertTrue(ics.solicitarSimulation(new DatosSolicitud(map)) < 0);
    }

    @Test
    void isValidEntityTest(){
        assertTrue(ics.isValidEntityId());
    }
}