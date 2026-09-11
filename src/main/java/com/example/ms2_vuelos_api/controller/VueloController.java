package com.example.ms2_vuelos_api.controller;

import com.example.ms2_vuelos_api.dto.*;
import com.example.ms2_vuelos_api.service.VueloService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/vuelos")
public class VueloController {

    private final VueloService service;

    public VueloController(VueloService service) {
        this.service = service;
    }

    @GetMapping
    public List<VueloResponse> buscar(
        @RequestParam(required = false) String num,
        @RequestParam(required = false) String estado,
        @RequestParam(required = false) String tipo,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return service.buscar(num, estado, tipo, fecha);
    }

    @GetMapping("/{id}")
    public VueloResponse buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VueloResponse crear(@Valid @RequestBody VueloRequest request) {
        return service.crear(request);
    }

    @PatchMapping("/{id}/estado")
    public VueloResponse cambiarEstado(@PathVariable Integer id, @Valid @RequestBody CambioEstadoRequest request) {
        return service.cambiarEstado(id, request.getEstado());
    }

    @GetMapping("/{id}/exists")
    public VueloExistsResponse verificarExistencia(@PathVariable Integer id) {
        return service.verificarExistencia(id);
    }

    @GetMapping("/{id}/tripulacion")
    public List<TripulanteResponse> listarTripulacion(@PathVariable Integer id) {
        return service.listarTripulacion(id);
    }

    @PostMapping("/{id}/tripulacion")
    @ResponseStatus(HttpStatus.CREATED)
    public void asignarTripulacion(@PathVariable Integer id, @Valid @RequestBody AsignarTripulacionRequest request) {
        service.asignarTripulacion(id, request.getEmpleadoId());
    }
}
