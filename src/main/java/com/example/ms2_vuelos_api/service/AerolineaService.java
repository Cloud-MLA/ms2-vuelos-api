package com.example.ms2_vuelos_api.service;

import com.example.ms2_vuelos_api.dto.AerolineaRequest;
import com.example.ms2_vuelos_api.dto.AerolineaResponse;
import com.example.ms2_vuelos_api.exception.ConflictException;
import com.example.ms2_vuelos_api.exception.NotFoundException;
import com.example.ms2_vuelos_api.model.Aerolinea;
import com.example.ms2_vuelos_api.repository.AerolineaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AerolineaService {

    private final AerolineaRepository repository;

    public AerolineaService(AerolineaRepository repository) {
        this.repository = repository;
    }

    public List<AerolineaResponse> listar() {
        return repository.findAll().stream()
            .map(this::toResponse)
            .toList();
    }

    public AerolineaResponse buscarPorRuc(String ruc) {
        Aerolinea aerolinea = repository.findById(ruc)
            .orElseThrow(() -> new NotFoundException("Aerolínea con RUC " + ruc + " no existe"));
        return toResponse(aerolinea);
    }

    public AerolineaResponse crear(AerolineaRequest request) {
        if (repository.existsById(request.getRuc())) {
            throw new ConflictException("Ya existe una aerolínea con RUC " + request.getRuc());
        }
        Aerolinea aerolinea = new Aerolinea();
        aerolinea.setRuc(request.getRuc());
        aerolinea.setNombre(request.getNombre());
        aerolinea.setAlianza(request.getAlianza());
        repository.save(aerolinea);
        return toResponse(aerolinea);
    }

    private AerolineaResponse toResponse(Aerolinea a) {
        return new AerolineaResponse(a.getRuc(), a.getNombre(), a.getAlianza());
    }
}
