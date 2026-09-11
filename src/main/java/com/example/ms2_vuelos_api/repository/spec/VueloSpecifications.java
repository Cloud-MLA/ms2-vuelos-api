package com.example.ms2_vuelos_api.repository.spec;

import com.example.ms2_vuelos_api.model.Vuelo;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.ZoneOffset;

public class VueloSpecifications {

    public static Specification<Vuelo> conFiltros(String numero, String estado, String tipo, LocalDate fecha) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (numero != null && !numero.isBlank()) {
                predicates = cb.and(predicates, cb.equal(root.get("numero"), numero));
            }
            if (estado != null && !estado.isBlank()) {
                predicates = cb.and(predicates, cb.equal(root.get("estado"), estado));
            }
            if (tipo != null && !tipo.isBlank()) {
                predicates = cb.and(predicates, cb.equal(root.get("tipo"), tipo));
            }
            if (fecha != null) {
                var inicio = fecha.atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
                var fin = fecha.plusDays(1).atStartOfDay(ZoneOffset.UTC).toOffsetDateTime();
                predicates = cb.and(predicates,
                    cb.between(root.get("horaProgramada"), inicio, fin));
            }
            return predicates;
        };
    }
}
