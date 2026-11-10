package org.model;

import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class ImbSport extends Imbracaminte{
    @NonNull private String disciplinaSportiva;
    @NonNull private String mediuDeDesfasurare;

    public ImbSport(@NonNull Integer idProdus, @NonNull String denumire, @NonNull String UM, @NonNull String material, @NonNull String marime, @NonNull String genPurtator, @NonNull String categorieVarstaPurtator, @NonNull String disciplinaSportiva, @NonNull String mediuDeDesfasurare) {
        super(idProdus, denumire, UM, material, marime, genPurtator, categorieVarstaPurtator);
        this.disciplinaSportiva = disciplinaSportiva;
        this.mediuDeDesfasurare = mediuDeDesfasurare;
    }
}
