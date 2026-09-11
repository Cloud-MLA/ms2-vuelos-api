package com.example.ms2_vuelos_api.controller;

import com.example.ms2_vuelos_api.dto.EmpleadoResponse;
import com.example.ms2_vuelos_api.service.EmpleadoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vuelos/empleados")
public class EmpleadoController {

    private final EmpleadoService service;

    public EmpleadoController(EmpleadoService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public EmpleadoResponse buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @GetMapping
    public List<EmpleadoResponse> listar(@RequestParam(required = false) String tipo) {
        return service.listarPorTipo(tipo);
    }
}
