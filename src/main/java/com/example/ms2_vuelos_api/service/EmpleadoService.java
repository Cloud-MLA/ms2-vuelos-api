package com.example.ms2_vuelos_api.service;

import com.example.ms2_vuelos_api.dto.EmpleadoResponse;
import com.example.ms2_vuelos_api.exception.NotFoundException;
import com.example.ms2_vuelos_api.model.Empleado;
import com.example.ms2_vuelos_api.repository.EmpleadoRepository;
import com.example.ms2_vuelos_api.repository.OperativoTierraRepository;
import com.example.ms2_vuelos_api.repository.TripulacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final TripulacionRepository tripulacionRepository;
    private final OperativoTierraRepository operativoTierraRepository;

    public EmpleadoService(EmpleadoRepository empleadoRepository,
                           TripulacionRepository tripulacionRepository,
                           OperativoTierraRepository operativoTierraRepository) {
        this.empleadoRepository = empleadoRepository;
        this.tripulacionRepository = tripulacionRepository;
        this.operativoTierraRepository = operativoTierraRepository;
    }

    public EmpleadoResponse buscarPorId(Integer id) {
        Empleado e = empleadoRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Empleado con id " + id + " no existe"));
        return toResponse(e);
    }

    public List<EmpleadoResponse> listarPorTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            return empleadoRepository.findAll().stream().map(this::toResponse).toList();
        }
        if (tipo.equalsIgnoreCase("Tripulacion")) {
            return tripulacionRepository.findAll().stream()
                .map(t -> toResponse(t.getEmpleado()))
                .toList();
        }
        if (tipo.equalsIgnoreCase("OperativoTierra")) {
            return operativoTierraRepository.findAll().stream()
                .map(o -> toResponse(o.getEmpleado()))
                .toList();
        }
        return List.of();
    }

    private EmpleadoResponse toResponse(Empleado e) {
        String tipo = tripulacionRepository.findById(e.getId()).isPresent() ? "Tripulacion"
            : operativoTierraRepository.findById(e.getId()).isPresent() ? "OperativoTierra"
            : "Sin asignar";
        return new EmpleadoResponse(e.getId(), e.getNombre(), e.getApellido(), e.getFechaNacimiento(), tipo);
    }
}
