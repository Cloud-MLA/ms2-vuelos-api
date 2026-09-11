package com.example.ms2_vuelos_api.service;

import com.example.ms2_vuelos_api.dto.AeronaveRequest;
import com.example.ms2_vuelos_api.dto.AeronaveResponse;
import com.example.ms2_vuelos_api.dto.AsientoResponse;
import com.example.ms2_vuelos_api.exception.ConflictException;
import com.example.ms2_vuelos_api.exception.NotFoundException;
import com.example.ms2_vuelos_api.model.Aeronave;
import com.example.ms2_vuelos_api.repository.AeronaveRepository;
import com.example.ms2_vuelos_api.repository.AsientoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AeronaveService {

    private final AeronaveRepository repository;
    private final AsientoRepository asientoRepository;

    public AeronaveService(AeronaveRepository repository, AsientoRepository asientoRepository) {
        this.repository = repository;
        this.asientoRepository = asientoRepository;
    }

    public List<AeronaveResponse> listar() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public AeronaveResponse buscarPorPlaca(String placa) {
        Aeronave a = repository.findById(placa)
            .orElseThrow(() -> new NotFoundException("Aeronave con placa " + placa + " no existe"));
        return toResponse(a);
    }

    public AeronaveResponse crear(AeronaveRequest request) {
        if (repository.existsById(request.getPlaca())) {
            throw new ConflictException("Ya existe una aeronave con placa " + request.getPlaca());
        }
        Aeronave a = new Aeronave();
        a.setPlaca(request.getPlaca());
        a.setModelo(request.getModelo());
        a.setFabricante(request.getFabricante());
        a.setCapacidad(request.getCapacidad());
        a.setClase(request.getClase());
        repository.save(a);
        return toResponse(a);
    }

    public List<AsientoResponse> listarAsientos(String placa) {
        if (!repository.existsById(placa)) {
            throw new NotFoundException("Aeronave con placa " + placa + " no existe");
        }
        return asientoRepository.findByAeronave_Placa(placa).stream()
            .map(s -> new AsientoResponse(s.getId(), s.getCodigo()))
            .toList();
    }

    private AeronaveResponse toResponse(Aeronave a) {
        return new AeronaveResponse(a.getPlaca(), a.getModelo(), a.getFabricante(), a.getCapacidad(), a.getClase());
    }
}
