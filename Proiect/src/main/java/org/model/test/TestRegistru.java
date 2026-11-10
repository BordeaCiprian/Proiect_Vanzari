package org.model.test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.model.*;
import org.model.registru.Registru;

public class TestRegistru {
    public static void main(String[] args) {
        Registru registru = new Registru(100);
        System.out.println("Afiseaza facturi: ******************************************");
        for (Factura f : registru.getFacturi()) {
            System.out.println(f);
            for (LinieFactura linFac : f.getLiniiFact()) {
                System.out.println(linFac);
            }
            System.out.println("---------------------------------------");
        }

        EntityManagerFactory emf = Persistence.
                createEntityManagerFactory("ProduseJPA");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.createQuery("DELETE FROM LinieFactura lf").executeUpdate();
        em.createQuery("DELETE FROM Factura f").executeUpdate();
        em.createQuery("DELETE FROM Produs p").executeUpdate();
        em.createQuery("DELETE FROM Cumparator c").executeUpdate();
        em.getTransaction().commit();

        em.getTransaction().begin();
        registru.getCatalogProduse().stream().forEach(p -> em.persist(p));
        registru.getCumparatori().stream().forEach(em::persist);
        registru.getFacturi().stream().forEach(em::persist);
        em.getTransaction().commit();

        System.out.println("------------------------------------");
        em.createQuery("SELECT p FROM Produs p ORDER BY p.idProdus", Produs.class)
                .getResultStream().forEach(p -> System.out.println(p));
        System.out.println("------------------------------------");
        em.createQuery("SELECT c FROM Cumparator c ORDER BY c.idCumparator", Cumparator.class)
                .getResultStream().forEach(c -> System.out.println(c));
        System.out.println("------------------------------------");
        em.createQuery("SELECT f FROM Factura f ORDER BY f.idFactura", Factura.class)
                .getResultStream().forEach(
                        f -> {
                            System.out.println(f);
                            f.getLiniiFact().stream().forEach(lf -> System.out.println("- " + lf));
                        });
    }
}
