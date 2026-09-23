package com.example.ms2_vuelos_api;

import com.example.ms2_vuelos_api.service.EstadoVueloMaquina;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EstadoVueloMaquinaTest {

    @Test
    void transicionValida_programadoAEmbarcando_esPermitida() {
        assertTrue(EstadoVueloMaquina.esTransicionValida("Programado", "Embarcando"));
    }

    @Test
    void transicionInvalida_embarcandoAAterrizado_noEsPermitida() {
        assertFalse(EstadoVueloMaquina.esTransicionValida("Embarcando", "Aterrizado"));
    }

    @Test
    void estadoFinal_aterrizado_noTienesTransicionesSalientes() {
        assertFalse(EstadoVueloMaquina.esTransicionValida("Aterrizado", "Programado"));
        assertFalse(EstadoVueloMaquina.esTransicionValida("Aterrizado", "Cancelado"));
    }
}
