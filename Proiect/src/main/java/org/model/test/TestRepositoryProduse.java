package org.model.test;

import org.model.*;
import org.model.repository.IRepositoryProduse;
import org.model.repository.RepositoryProduse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestRepositoryProduse {
    public static void main(String[] args) {
        List<Produs> produse = new ArrayList<Produs>();
        produse.add(new ImbUzuala(1, "Produs 1", "Bucati", "matase", "M", "Fem", "adult", "Vara", "Brand C"));
        produse.add(new ImbSport(2, "Produs 2", "Bucati", "bumbac", "L", "Mas","adult", "atletism", "aer liber"));
        produse.add(new ImbProtectie(3, "Produs 3", "Bucati", "bumbac", "S", "Fem", "copil", "scolar"));
        produse.add(new AccAuxiliar(4, "Produs 4", "Bucati", "Brand A", "pereche"));
        produse.add(new AccIndependent(5, "Produs 5", "Bucati", "Brand B", "Hand Bag"));
        produse.add(new IncOffice(6, "Produs 6", "Bucati", "M", "Piele", "Mas", "adult"));
        produse.add(new IncCasual(7, "Produs 7", "Bucati", "M", "Matase", "Fem", "tenis"));

        IRepositoryProduse repositoryProduse = new RepositoryProduse();

        repositoryProduse.removeAll();

        repositoryProduse.add(produse.get(0));
        repositoryProduse.add(produse.get(1));
        repositoryProduse.add(produse.get(2));
        repositoryProduse.add(produse.get(3));
        repositoryProduse.add(produse.get(4));
        repositoryProduse.add(produse.get(5));
        repositoryProduse.add(produse.get(6));

        Collection<Produs> lstProdusePersistente = repositoryProduse.getAll();

        Collection<Imbracaminte> lstProduseImbracamintePersistente = repositoryProduse.getAllImbracaminte();

        System.out.println("Lista produse persistente/salvate in baza de date");
        for (Produs p : lstProdusePersistente)
            System.out.println("Cod:" + p.getIdProdus() + ", denumire: " + p.getDenumire() + ", um: " + p.getUM());

        Produs p2 = repositoryProduse.getProdusDupaCod(2);
        if (p2 != null)
            p2.setDenumire("Produs 99");

        Produs p3 = repositoryProduse.getProdusDupaCod(3);
        if (p3 != null)
            repositoryProduse.remove(p3);

        //System.out.println(repositoryProduse.getNumarProduse());
        //System.out.println(repositoryProduse.getProdusDupaNume("Produs A"));
        //repositoryProduse.refresh(p2);
        //System.out.println(repositoryProduse.getProduseDupaNume("dus"));

        System.out.println("Lista finala produse persistente/salvate in baza de date");
        repositoryProduse.getAll().stream().forEach(p -> System.out.println("Cod:" + p.getIdProdus() + ", denumire: " + p.getDenumire() + ", um: " + p.getUM()));

        System.out.println("Lista finala produse imbracaminte persistente/salvate in baza de date");
        repositoryProduse.getAllImbracaminte().stream().forEach(i -> System.out.println("Cod:" + i.getIdProdus() + ", denumire: " + i.getDenumire() + ", um: " + i.getUM()
                + ", marime: " + i.getMarime() + ", gen: " +i.getGenPurtator() + ", categorie varsta: " +i.getCategorieVarstaPurtator()));
    }
}
