package org.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor @RequiredArgsConstructor
public class Factura {
    @Id @EqualsAndHashCode.Include
    @NonNull private Integer idFactura;

    @Temporal(TemporalType.DATE)
    @NonNull private Date dataFact;

    @NonNull private Double tvaFacturata;
    @NonNull private Double discountFactura;

    @ManyToOne
    private Cumparator cumparator;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LinieFactura> liniiFact = new ArrayList<LinieFactura>();

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Livrare> livrare = new ArrayList<Livrare>();

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    @NonNull private List<Incasare> incasare = new ArrayList<Incasare>();

    public Factura(@NonNull Integer idFactura, @NonNull Date dataFact, @NonNull Double tvaFacturata, @NonNull Double discountFactura, Cumparator cumparator) {
        this.idFactura = idFactura;
        this.dataFact = dataFact;
        this.tvaFacturata = tvaFacturata;
        this.discountFactura = discountFactura;
        this.cumparator = cumparator;
    }

    @Setter(AccessLevel.NONE)
    private Double valoareTotalaFac;

    private Double calculValoareTotala(){
        Double valoare = 0.0;
        for (LinieFactura linFac : liniiFact)
            valoare += linFac.getValoareLinieFactura();
        return valoare;
    }

    public Double getValoareTotala(){
        if (valoareTotalaFac == null)
            this.valoareTotalaFac = calculValoareTotala();
//        return valoareTotalaFac;
        return this.valoareTotalaFac = calculValoareTotala();
    }

    public void adaugaLinieFactura(LinieFactura linFac){
        if (!this.liniiFact.contains(linFac)){
            this.liniiFact.add(linFac);
            this.valoareTotalaFac = calculValoareTotala();
        }
    }

    public void adaugaProdus(Produs produs, Double cantitate, Double pretUnit){
        LinieFactura linieFactura = new LinieFactura(this, produs, cantitate, pretUnit);
        this.liniiFact.add(linieFactura);
    }

    @Override
    public String toString() {
        return "Factura=" + this.idFactura + ", valaoreTotalaFactura " + this.getValoareTotala();
    }
}
