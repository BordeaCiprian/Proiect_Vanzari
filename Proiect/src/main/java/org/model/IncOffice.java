package org.model;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class IncOffice extends Incaltaminte{
    @NonNull String modelCategorie;

    public IncOffice(@NonNull Integer idProdus, @NonNull String denumire, @NonNull String UM, @NonNull String dimensiune, @NonNull String material, @NonNull String genPurtator, @NonNull String model) {
        super(idProdus, denumire, UM, dimensiune, material, genPurtator);
        this.modelCategorie = model;
    }
}
