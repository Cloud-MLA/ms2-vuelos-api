package com.example.ms2_vuelos_api.service;

import com.example.ms2_vuelos_api.dto.TripulanteResponse;
import com.example.ms2_vuelos_api.model.OperaTripulacion;
import com.example.ms2_vuelos_api.model.Tripulacion;
import com.example.ms2_vuelos_api.repository.OperaTripulacionRepository;
import com.example.ms2_vuelos_api.repository.TripulacionRepository;
import com.example.ms2_vuelos_api.exception.ConflictException;
import java.util.List;
import com.example.ms2_vuelos_api.dto.VueloExistsResponse;
import com.example.ms2_vuelos_api.dto.VueloRequest;
import com.example.ms2_vuelos_api.dto.VueloResponse;
import com.example.ms2_vuelos_api.exception.InvalidTransitionException;
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
    private final TripulacionRepository tripulacionRepository;
    private final OperaTripulacionRepository operaTripulacionRepository;

    public VueloService(VueloRepository repository, AerolineaRepository aerolineaRepository,
                        AeronaveRepository aeronaveRepository, TripulacionRepository tripulacionRepository,
                        OperaTripulacionRepository operaTripulacionRepository) {
        this.repository = repository;
        this.aerolineaRepository = aerolineaRepository;
        this.aeronaveRepository = aeronaveRepository;
        this.tripulacionRepository = tripulacionRepository;
        this.operaTripulacionRepository = operaTripulacionRepository;
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

    public VueloResponse cambiarEstado(Integer id, String nuevoEstado) {
        Vuelo v = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Vuelo con id " + id + " no existe"));

        if (!EstadoVueloMaquina.esTransicionValida(v.getEstado(), nuevoEstado)) {
            throw new InvalidTransitionException(
                    "No se puede pasar de " + v.getEstado() + " a " + nuevoEstado);
        }

        v.setEstado(nuevoEstado);
        if (nuevoEstado.equals("Despegado")) {
            v.setHoraReal(java.time.OffsetDateTime.now());
        }
        repository.save(v);
        return toResponse(v);
    }

    public VueloExistsResponse verificarExistencia(Integer id) {
        return repository.findById(id)
                .map(v -> new VueloExistsResponse(true, v.getEstado()))
                .orElseThrow(() -> new NotFoundException("Vuelo con id " + id + " no existe"));
    }

    public List<TripulanteResponse> listarTripulacion(Integer vueloId) {
        if (!repository.existsById(vueloId)) {
            throw new NotFoundException("Vuelo con id " + vueloId + " no existe");
        }
        return operaTripulacionRepository.findByVuelo_Id(vueloId).stream()
            .map(ot -> {
                Tripulacion t = ot.getTripulacion();
                return new TripulanteResponse(
                    t.getEmpleadoId(), t.getEmpleado().getNombre(),
                    t.getEmpleado().getApellido(), t.getNumLicencia());
            })
            .toList();
    }

    public void asignarTripulacion(Integer vueloId, Integer empleadoId) {
        Vuelo vuelo = repository.findById(vueloId)
            .orElseThrow(() -> new NotFoundException("Vuelo con id " + vueloId + " no existe"));

        Tripulacion tripulacion = tripulacionRepository.findById(empleadoId)
            .orElseThrow(() -> new NotFoundException("Empleado " + empleadoId + " no es tripulación"));

        if (operaTripulacionRepository.existsByVuelo_IdAndTripulacion_EmpleadoId(vueloId, empleadoId)) {
            throw new ConflictException("El tripulante " + empleadoId + " ya está asignado a este vuelo");
        }

        OperaTripulacion ot = new OperaTripulacion();
        ot.setVuelo(vuelo);
        ot.setTripulacion(tripulacion);
        operaTripulacionRepository.save(ot);
    }
}
