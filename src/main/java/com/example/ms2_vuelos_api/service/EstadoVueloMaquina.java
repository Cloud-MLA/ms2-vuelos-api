package com.example.ms2_vuelos_api.service;

import java.util.Map;
import java.util.Set;

public class EstadoVueloMaquina {

    private static final Map<String, Set<String>> TRANSICIONES = Map.of(
        "Programado", Set.of("Embarcando", "Retrasado", "Cancelado"),
        "Embarcando", Set.of("Despegado", "Retrasado", "Cancelado"),
        "Retrasado",  Set.of("Embarcando", "Despegado", "Cancelado"),
        "Despegado",  Set.of("Aterrizado"),
        "Aterrizado", Set.of(),
        "Cancelado",  Set.of()
    );

    public static boolean esTransicionValida(String estadoActual, String estadoNuevo) {
        return TRANSICIONES.getOrDefault(estadoActual, Set.of()).contains(estadoNuevo);
    }
}
