package org.model;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor
public abstract class Incaltaminte extends Produs{
    @NonNull private String dimensiune;
    @NonNull private String material;
    @NonNull private String genPurtator;

    public Incaltaminte(@NonNull Integer idProdus, @NonNull String denumire, @NonNull String UM, @NonNull String dimensiune, @NonNull String material, @NonNull String genPurtator) {
        super(idProdus, denumire, UM);
        this.dimensiune = dimensiune;
        this.material = material;
        this.genPurtator = genPurtator;
    }
}
