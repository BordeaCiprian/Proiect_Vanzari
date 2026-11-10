package org.model.registru;

import org.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Registru {
    List<Produs> catalogProduse = new ArrayList<Produs>();
    List<LinieFactura> liniiFactura = new ArrayList<LinieFactura>();
    List<Factura> facturi =  new ArrayList<Factura>();
    List<Cumparator> cumparatori = new ArrayList<Cumparator>();

    public List<Produs> getCatalogProduse() {
        return catalogProduse;
    }

    public void setCatalogProduse(List<Produs> catalogProduse) {
        this.catalogProduse = catalogProduse;
    }

    public List<Factura> getFacturi() {
        return facturi;
    }

    public void setFacturi(List<Factura> facturi) {
        this.facturi = facturi;
    }

    public List<Cumparator> getCumparatori() {
        return cumparatori;
    }

    public void setCumparatori(List<Cumparator> cumparatori) {
        this.cumparatori = cumparatori;
    }

    public Registru() {
        generateProduse();
        generateRandomComenzi(100);
    }

    public Registru(Integer nrFacturi) {
        generateProduse();
        generateRandomCumparatori();
        generateRandomComenzi(nrFacturi);
    }

    private void generateProduse() {
        int i=0;
        catalogProduse.add(new ImbUzuala(i++, "Rochie seara", "Bucati", "matase", "M", "Fem", "adult", "Vara", "Louis Vitton"));
        catalogProduse.add(new ImbSport(i++, "Pantaloni sport", "Bucati", "bumbac", "L", "Mas","adult", "atletism", "aer liber"));
        catalogProduse.add(new ImbProtectie(i++, "Uniforma elev", "Bucati", "bumbac", "S", "Fem", "copil", "scolar"));
        catalogProduse.add(new AccAuxiliar(i++, "Cercei aur", "Bucati", "Mejuri", "pereche"));
        catalogProduse.add(new AccIndependent(i++, "Geanta dama", "Bucati", "Gucci", "Hand Bag"));
        catalogProduse.add(new IncOffice(i++, "Pantofi birou", "Bucati", "M", "Piele", "Mas", "adult"));
        catalogProduse.add(new IncCasual(i, "Pantofi Sneakers", "Bucati", "M", "Matase", "Fem", "tenis"));
    }

    private void generateRandomCumparatori(){
        for (int i=0; i<5; i++){
            cumparatori.add(i, new Cumparator(i, "Cumparator " + i + " SRL", "Iasi"));
        }
    }

    private void generateRandomComenzi(Integer nrFacturi) {
        Random randomLiniiFact = new Random();
        Random randomPret = new Random();
        Random randomCantitate = new Random();
        Random randomProdus = new Random();
        Random randomDiscount = new Random();
        Random randomCumparator = new Random();
        Factura facturaRandom;
        LinieFactura linieFacturaRandom;
        Integer pretRandom;
        Double cantitateRandom;
        Integer produsPozRandom;
        Produs produsRandom;

        for (int i=1; i<=nrFacturi; i++) {
            facturaRandom = new Factura(i, new Date(), 0.19, randomDiscount.nextDouble(0.2), cumparatori.get(randomCumparator.nextInt(5)));
            for (int j=1; j <= 1 + randomLiniiFact.nextInt(5); j++) {
                pretRandom = 100 + randomPret.nextInt(1400);
                cantitateRandom = 1.0 + randomCantitate.nextInt(10);
                produsPozRandom = randomProdus.nextInt(catalogProduse.size() - 1);
                produsRandom = catalogProduse.get(produsPozRandom);

                linieFacturaRandom = new LinieFactura(Integer.valueOf(String.valueOf(i) + j), produsRandom, cantitateRandom, pretRandom.doubleValue(), facturaRandom);
                liniiFactura.add(linieFacturaRandom);
                facturaRandom.adaugaLinieFactura(linieFacturaRandom);
            }
            facturi.add(facturaRandom);
        }
    }
}
