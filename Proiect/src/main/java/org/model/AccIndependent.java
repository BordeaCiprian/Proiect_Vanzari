package org.model;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class AccIndependent extends Accesoriu{
    @NonNull private String model;

    public AccIndependent(@NonNull Integer idProdus, @NonNull String denumire, @NonNull String UM, @NonNull String brand, @NonNull String model) {
        super(idProdus, denumire, UM, brand);
        this.model = model;
    }
}
