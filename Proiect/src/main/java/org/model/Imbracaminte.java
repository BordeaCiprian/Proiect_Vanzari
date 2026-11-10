package org.model;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor
public abstract class Imbracaminte extends Produs{
    @NonNull String material;
    @NonNull String marime;
    @NonNull String genPurtator;
    @NonNull String categorieVarstaPurtator;

    public Imbracaminte(@NonNull Integer idProdus, @NonNull String denumire, @NonNull String UM, @NonNull String material, @NonNull String marime, @NonNull String genPurtator, @NonNull String categorieVarstaPurtator) {
        super(idProdus, denumire, UM);
        this.material = material;
        this.marime = marime;
        this.genPurtator = genPurtator;
        this.categorieVarstaPurtator = categorieVarstaPurtator;
    }
}
