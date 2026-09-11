package com.example.ms2_vuelos_api.service;

import com.example.ms2_vuelos_api.dto.VueloRequest;
import com.example.ms2_vuelos_api.dto.VueloResponse;
import com.example.ms2_vuelos_api.exception.NotFoundException;
import com.example.ms2_vuelos_api.model.Aerolinea;
import com.example.ms2_vuelos_api.model.Aeronave;
import com.example.ms2_vuelos_api.model.Vuelo;
import com.example.ms2_vuelos_api.repository.AerolineaRepository;
import com.example.ms2_vuelos_api.repository.AeronaveRepository;
import com.example.ms2_vuelos_api.repository.VueloRepository;
import com.example.ms2_vuelos_api.repository.spec.VueloSpecifications;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VueloService {

    private final VueloRepository repository;
    private final AerolineaRepository aerolineaRepository;
    private final AeronaveRepository aeronaveRepository;

    public VueloService(VueloRepository repository, AerolineaRepository aerolineaRepository,
                        AeronaveRepository aeronaveRepository) {
        this.repository = repository;
        this.aerolineaRepository = aerolineaRepository;
        this.aeronaveRepository = aeronaveRepository;
    }

    public List<VueloResponse> buscar(String numero, String estado, String tipo, LocalDate fecha) {
        var spec = VueloSpecifications.conFiltros(numero, estado, tipo, fecha);
        return repository.findAll(spec).stream().map(this::toResponse).toList();
    }

    public VueloResponse buscarPorId(Integer id) {
        Vuelo v = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Vuelo con id " + id + " no existe"));
        return toResponse(v);
    }

    public VueloResponse crear(VueloRequest request) {
        Aerolinea aerolinea = aerolineaRepository.findById(request.getAerolineaRuc())
            .orElseThrow(() -> new NotFoundException("Aerolínea con RUC " + request.getAerolineaRuc() + " no existe"));
        Aeronave aeronave = aeronaveRepository.findById(request.getAeronavePlaca())
            .orElseThrow(() -> new NotFoundException("Aeronave con placa " + request.getAeronavePlaca() + " no existe"));

        Vuelo v = new Vuelo();
        v.setId(siguienteId());
        v.setNumero(request.getNumero());
        v.setOrigen(request.getOrigen());
        v.setDestino(request.getDestino());
        v.setHoraProgramada(request.getHoraProgramada());
        v.setEstado("Programado");
        v.setTipo(request.getTipo());
        v.setAerolinea(aerolinea);
        v.setAeronave(aeronave);
        repository.save(v);
        return toResponse(v);
    }

    private Integer siguienteId() {
        return repository.obtenerMaxId() + 1;
    }

    private VueloResponse toResponse(Vuelo v) {
        return new VueloResponse(v.getId(), v.getNumero(), v.getOrigen(), v.getDestino(),
            v.getHoraProgramada(), v.getHoraReal(), v.getEstado(), v.getTipo(),
            v.getAerolinea().getRuc(), v.getAeronave().getPlaca());
    }
}
