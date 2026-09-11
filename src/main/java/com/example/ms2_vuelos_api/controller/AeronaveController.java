package com.example.ms2_vuelos_api.controller;

import com.example.ms2_vuelos_api.dto.AeronaveRequest;
import com.example.ms2_vuelos_api.dto.AeronaveResponse;
import com.example.ms2_vuelos_api.dto.AsientoResponse;
import com.example.ms2_vuelos_api.service.AeronaveService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vuelos/aeronaves")
public class AeronaveController {

    private final AeronaveService service;

    public AeronaveController(AeronaveService service) {
        this.service = service;
    }

    @GetMapping
    public List<AeronaveResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{placa}")
    public AeronaveResponse buscarPorPlaca(@PathVariable String placa) {
        return service.buscarPorPlaca(placa);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AeronaveResponse crear(@Valid @RequestBody AeronaveRequest request) {
        return service.crear(request);
    }

    @GetMapping("/{placa}/asientos")
    public List<AsientoResponse> listarAsientos(@PathVariable String placa) {
        return service.listarAsientos(placa);
    }
}
