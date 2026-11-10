package org.model;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class AccAuxiliar extends Accesoriu{
    @NonNull private String categoriePereche;

    public AccAuxiliar(@NonNull Integer idProdus, @NonNull String denumire, @NonNull String UM, @NonNull String brand, @NonNull String categoriePereche) {
        super(idProdus, denumire, UM, brand);
        this.categoriePereche = categoriePereche;
    }
}
