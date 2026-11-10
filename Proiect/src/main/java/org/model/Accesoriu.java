package org.model;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor
public abstract class Accesoriu extends Produs{
    @NonNull private String brand;

    public Accesoriu(@NonNull Integer idProdus, @NonNull String denumire, @NonNull String UM, @NonNull String brand) {
        super(idProdus, denumire, UM);
        this.brand = brand;
    }
}
