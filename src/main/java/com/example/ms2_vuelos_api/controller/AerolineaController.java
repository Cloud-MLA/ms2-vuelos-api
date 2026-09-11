package com.example.ms2_vuelos_api.controller;

import com.example.ms2_vuelos_api.dto.AerolineaRequest;
import com.example.ms2_vuelos_api.dto.AerolineaResponse;
import com.example.ms2_vuelos_api.service.AerolineaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vuelos/aerolineas")
public class AerolineaController {

    private final AerolineaService service;

    public AerolineaController(AerolineaService service) {
        this.service = service;
    }

    @GetMapping
    public List<AerolineaResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{ruc}")
    public AerolineaResponse buscarPorRuc(@PathVariable String ruc) {
        return service.buscarPorRuc(ruc);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AerolineaResponse crear(@Valid @RequestBody AerolineaRequest request) {
        return service.crear(request);
    }
}
