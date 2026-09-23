package com.example.ms2_vuelos_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AerolineaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void crearAerolinea_conDatosValidos_devuelve201() throws Exception {
        mockMvc.perform(post("/api/vuelos/aerolineas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"ruc":"20999888777","nombre":"Test Airlines","alianza":"Oneworld"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ruc").value("20999888777"));
    }

    @Test
    void crearAerolinea_conAlianzaInvalida_devuelve400() throws Exception {
        mockMvc.perform(post("/api/vuelos/aerolineas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"ruc":"20111222444","nombre":"Test","alianza":"NoExiste"}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.code").value("VALIDACION"));
    }

    @Test
    void buscarAerolinea_conRucInexistente_devuelve404() throws Exception {
        mockMvc.perform(get("/api/vuelos/aerolineas/00000000000"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.code").value("NO_ENCONTRADO"));
    }
}
