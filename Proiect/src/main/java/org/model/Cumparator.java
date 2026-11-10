package org.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @RequiredArgsConstructor
public class Cumparator {
    @EqualsAndHashCode.Include
    @NonNull @Id private Integer idCumparator;
    @NonNull private String denumireCumparator;
    @NonNull private String adresaCumparator;

    @OneToMany(mappedBy = "cumparator", cascade = CascadeType.ALL, orphanRemoval = true)
    @NonNull private List<Factura> facturi = new ArrayList<Factura>();
}
