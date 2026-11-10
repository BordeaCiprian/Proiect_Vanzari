package org.model;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class ImbProtectie extends Imbracaminte{
    @NonNull private String domeniuActivitate;

    public ImbProtectie(@NonNull Integer idProdus, @NonNull String denumire, @NonNull String UM, @NonNull String material, @NonNull String marime, @NonNull String genPurtator, @NonNull String categorieVarstaPurtator, @NonNull String domeniuActivitate) {
        super(idProdus, denumire, UM, material, marime, genPurtator, categorieVarstaPurtator);
        this.domeniuActivitate = domeniuActivitate;
    }
}
