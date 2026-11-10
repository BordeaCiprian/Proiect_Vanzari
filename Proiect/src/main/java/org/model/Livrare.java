package org.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.Date;

@Entity
@Data @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @RequiredArgsConstructor
public class Livrare {
    @EqualsAndHashCode.Include
    @NonNull @Id private Integer idLivrare;
    @NonNull private Date dataLivrare;
    @NonNull private String docLivrare;
    private String observatiiLivrare ;

    @ManyToOne
    private Factura factura;
}
