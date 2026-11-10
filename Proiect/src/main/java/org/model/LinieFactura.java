package org.model;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.AUTO;

@Entity
@Data @EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class LinieFactura {
    @EqualsAndHashCode.Include
    @Id @GeneratedValue(strategy = AUTO)
    private Integer linie;

    @NonNull private Double cantitate;
    @NonNull private Double pretUnit;

    @ManyToOne
    @NonNull @Id private Factura factura;

    @ManyToOne
    private Produs produs;

    @Setter(AccessLevel.NONE)
    private Double valoareLinieFactura;

    private Double calculValaore(){
        Double valoare = 0.0;
        if (produs != null && cantitate != null && pretUnit != null) {}
            valoare = pretUnit * cantitate;
        return valoare;
    }

    public Double getValoareLinieFactura() {
        if (valoareLinieFactura == null)
            valoareLinieFactura = calculValaore();
        return valoareLinieFactura;
    }

    public void setProdus(Produs produs) {
        this.produs = produs;
        this.valoareLinieFactura = calculValaore();
    }

    public void setCantitate(Double cantitate) {
        this.cantitate = cantitate;
        this.valoareLinieFactura = calculValaore();
    }

    public void setPretUnit(Double pretUnit) {
        this.pretUnit = pretUnit;
        this.valoareLinieFactura = calculValaore();
    }

    public LinieFactura(Integer linie, Produs produs, Double cantitate, Double pretUnit, Factura factura) {
        this.linie = linie;
        this.produs = produs;
        this.cantitate = cantitate;
        this.pretUnit = pretUnit;
        this.factura = factura;
        this.valoareLinieFactura = getValoareLinieFactura();
    }

    public LinieFactura(Factura factura, Produs produs, Double cantitate, Double pretUnit) {
        this.factura = factura;
        this.produs = produs;
        this.cantitate = cantitate;
        this.pretUnit = pretUnit;
        this.valoareLinieFactura = calculValaore();
    }

    @Override
    public String toString() {
        return "LinieFactura{" +
                "linie=" + linie +
                ", produs=" + produs.getDenumire() +
                ", cantitate=" + cantitate +
                ", pretUnit=" + pretUnit +
                ", valoareLinieFactura=" + valoareLinieFactura +
                '}';
    }
}
