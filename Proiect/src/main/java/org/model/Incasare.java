package org.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @RequiredArgsConstructor
public class Incasare {
    @EqualsAndHashCode.Include
    @NonNull @Id private Integer idIncasare;

    @Temporal(TemporalType.DATE)
    @NonNull private Date dataIncasare;

    @NonNull private String documentIncasare;
    @NonNull private Integer nrIncasare;
    @NonNull private Double sumaIncasare;

    @ManyToOne
    private Factura factura;
}
