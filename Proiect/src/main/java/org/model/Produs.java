package org.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @RequiredArgsConstructor
public abstract class Produs {
   @EqualsAndHashCode.Include
   @NonNull @Id Integer idProdus;
   @NonNull String denumire;
   @NonNull String UM;
//
}
