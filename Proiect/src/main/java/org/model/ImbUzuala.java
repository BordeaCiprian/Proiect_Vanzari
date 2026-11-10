package org.model;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class ImbUzuala extends Imbracaminte{
    @NonNull String sezon;
    @NonNull String brand;

    public ImbUzuala(@NonNull Integer idProdus, @NonNull String denumire, @NonNull String UM, @NonNull String material, @NonNull String marime, @NonNull String genPurtator, @NonNull String categorieVarstaPurtator, @NonNull String sezon, @NonNull String brand) {
        super(idProdus, denumire, UM, material, marime, genPurtator, categorieVarstaPurtator);
        this.sezon = sezon;
        this.brand = brand;
    }

}
