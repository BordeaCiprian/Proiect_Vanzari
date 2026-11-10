package org.model.test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.model.*;

import java.util.Date;
import java.util.List;

public class TestPersistenta {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProduseJPA");
        EntityManager em = emf.createEntityManager();

        List<Factura> lstFacturi;
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Livrare l").executeUpdate();
        em.createQuery("DELETE FROM Incasare i").executeUpdate();
        em.createQuery("DELETE FROM LinieFactura lf").executeUpdate();
        em.createQuery("DELETE FROM Factura f").executeUpdate();
        em.createQuery("DELETE FROM Cumparator c").executeUpdate();
        em.createQuery("DELETE FROM Produs p").executeUpdate();
        em.getTransaction().commit();

        List<Cumparator> lstCumparatoriPersistenti = em.createQuery("SELECT c FROM Cumparator c", Cumparator.class).getResultList();

        if (!lstCumparatoriPersistenti.isEmpty()) {
            em.getTransaction().begin();
            for (Cumparator c : lstCumparatoriPersistenti)
                em.remove(c);
            em.getTransaction().commit();
        }

        em.getTransaction().begin();
        em.persist(new Cumparator(101, "Cumparator_1 SRL", "Iasi"));
        em.persist(new Cumparator(102, "Cumparator_2 SRL", "Neamt"));
        em.persist(new Cumparator(103, "Cumparator_3 SRL", "Bucuresti"));
        em.persist(new Cumparator(104, "Cumparator_4 SRL", "Iasi"));
        em.getTransaction().commit();

        lstCumparatoriPersistenti = em.createQuery("SELECT c FROM Cumparator c", Cumparator.class).getResultList();

        System.out.println("Lista cumparatori persistenti/salvati in baza de date");
        for (Cumparator c : lstCumparatoriPersistenti)
            System.out.println("Id: " + c.getIdCumparator() + ", nume: " + c.getDenumireCumparator() + ", adresa: " + c.getAdresaCumparator());

        List<Produs> lstProdusePersistente = em.createQuery("SELECT p FROM Produs p", Produs.class).getResultList();

        if (!lstProdusePersistente.isEmpty()) {
            em.getTransaction().begin();
            for (Produs p : lstProdusePersistente)
                em.remove(p);
            em.getTransaction().commit();
        }

        em.getTransaction().begin();
        em.persist(new ImbUzuala(1, "Produs 1", "Bucati", "matase", "M", "Fem", "adult", "Vara", "Louis Vitton"));
        em.persist(new ImbSport(2, "Produs 2", "Bucati", "bumbac", "L", "Mas","adult", "atletism", "aer liber"));
        em.persist(new ImbProtectie(3, "Produs 3", "Bucati", "bumbac", "S", "Fem", "copil", "scolar"));
        em.persist(new AccAuxiliar(4, "Produs 4", "Bucati", "Mejuri", "pereche"));
        em.persist(new AccIndependent(5, "Produs 5", "Bucati", "Nike", "Hand Bag"));
        em.persist(new IncOffice(6, "Produs 6", "Bucati", "M", "Piele", "Mas", "adult"));
        em.persist(new IncCasual(7, "Produs 7", "Bucati", "M", "Matase", "Fem", "tenis"));
        em.getTransaction().commit();

        lstProdusePersistente = em.createQuery("SELECT p FROM Produs p", Produs.class).getResultList();

        System.out.println("Lista produselor persistente/salvate in baza de date");
        for (Produs p : lstProdusePersistente)
            System.out.println("Cod: " + p.getIdProdus() + ", denumire: " + p.getDenumire());

        Factura f1 = new Factura(1, new Date(), 0.19, 0.1);
        f1.setCumparator(lstCumparatoriPersistenti.get(0));
        f1.adaugaProdus(lstProdusePersistente.get(0), 20.0, 150.0);
        f1.adaugaProdus(lstProdusePersistente.get(1), 12.0, 100.0);

        Factura f2 = new Factura(2, new Date(), 0.19, 0.05);
        f2.setCumparator(lstCumparatoriPersistenti.get(1));
        f2.adaugaProdus(lstProdusePersistente.get(2), 10.0, 150.0);
        f2.adaugaProdus(lstProdusePersistente.get(3), 8.0, 100.0);

        Factura f3 = new Factura(3, new Date(), 0.19, 0.15);
        f3.setCumparator(lstCumparatoriPersistenti.get(2));
        f3.adaugaProdus(lstProdusePersistente.get(4), 15.0, 90.0);
        f3.adaugaProdus(lstProdusePersistente.get(5), 10.0, 250.0);
        f3.adaugaProdus(lstProdusePersistente.get(6), 25.0, 140.0);

        em.getTransaction().begin();
        em.persist(f1);
        em.persist(f2);
        em.persist(f3);
        em.getTransaction().commit();
        //em.getTransaction().rollback();

        lstFacturi = em.createQuery("SELECT f FROM Factura f", Factura.class).getResultList();

        System.out.println("Lista facturilor persistente/salvate in baza de date");
        for (Factura f : lstFacturi)
            System.out.println("Id: " + f.getIdFactura() + ", valoare: " + f.getValoareTotala());

        List<Incasare> lstIncasariPersistente = em.createQuery("SELECT i FROM Incasare i", Incasare.class).getResultList();

        if (!lstIncasariPersistente.isEmpty()) {
            em.getTransaction().begin();
            for (Incasare i : lstIncasariPersistente)
                em.remove(i);
            em.getTransaction().commit();
        }

        Incasare i1 = new Incasare(1001, new Date(), "Card", 1, 1000.0);
        i1.setFactura(f1);

        Incasare i2 = new Incasare(1002, new Date(), "OP", 2, 500.0);
        i2.setFactura(f1);

        Incasare i3 = new Incasare(1003, new Date(), "OP", 1, 2300.0);
        i3.setFactura(f2);

        em.getTransaction().begin();
        em.persist(i1);
        em.persist(i2);
        em.persist(i3);
        em.getTransaction().commit();

        lstIncasariPersistente = em.createQuery("SELECT i FROM Incasare i", Incasare.class).getResultList();

        System.out.println("Lista incasarilorlor persistente/salvate in baza de date");
        for (Incasare i : lstIncasariPersistente)
            System.out.println("Id: " + i.getIdIncasare() + ", valoare: " + i.getSumaIncasare());

        List<Livrare> lstLivrariPersistente = em.createQuery("SELECT l FROM Livrare l", Livrare.class).getResultList();

        if (!lstLivrariPersistente.isEmpty()) {
            em.getTransaction().begin();
            for (Livrare l : lstLivrariPersistente)
                em.remove(l);
            em.getTransaction().commit();
        }

        Livrare l1 = new Livrare(123, new Date(), "Aviz");
        l1.setFactura(f1);

        Livrare l2 = new Livrare(124, new Date(), "Aviz");
        l2.setFactura(f2);

        em.getTransaction().begin();
        em.persist(l1);
        em.persist(l2);
        em.getTransaction().commit();

        lstLivrariPersistente = em.createQuery("SELECT l FROM Livrare l", Livrare.class).getResultList();

        System.out.println("Lista livrarilor persistente/salvate in baza de date");
        for (Livrare l : lstLivrariPersistente)
            System.out.println("Id: " + l.getIdLivrare() + ", valoare: " + l.getDocLivrare());
    }
}
