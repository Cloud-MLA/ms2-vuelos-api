package com.example.ms2_vuelos_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VueloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listarVuelos_sinFiltros_devuelve200() throws Exception {
        mockMvc.perform(get("/api/vuelos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void buscarVuelo_conIdInexistente_devuelve404() throws Exception {
        mockMvc.perform(get("/api/vuelos/999999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error.code").value("NO_ENCONTRADO"));
    }

    @Test
    void verificarExistencia_conIdInexistente_devuelve404() throws Exception {
        mockMvc.perform(get("/api/vuelos/999999/exists"))
            .andExpect(status().isNotFound());
    }

    @Test
    void verificarExistencia_conIdValido_devuelveExistsTrue() throws Exception {
        mockMvc.perform(get("/api/vuelos/1/exists"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.exists").value(true));
    }
}
