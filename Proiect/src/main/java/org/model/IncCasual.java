package org.model;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class IncCasual extends Incaltaminte{
    @NonNull private String domeniu;

    public IncCasual(@NonNull Integer idProdus, @NonNull String denumire, @NonNull String UM, @NonNull String dimensiune, @NonNull String material, @NonNull String genPurtator, @NonNull String domeniu) {
        super(idProdus, denumire, UM, dimensiune, material, genPurtator);
        this.domeniu = domeniu;
    }
}
