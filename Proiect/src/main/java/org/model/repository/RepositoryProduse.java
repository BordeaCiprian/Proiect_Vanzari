package org.model.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.model.Imbracaminte;
import org.model.Produs;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class RepositoryProduse implements IRepositoryProduse {

    private EntityManager entityManager;
    private String sqlDefaultText = "SELECT o FROM Produs o";

    public RepositoryProduse(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public RepositoryProduse() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProduseJPA");
        this.entityManager = emf.createEntityManager();
    }

    @Override
    public Long getNumarProduse() {
        return this.entityManager.createQuery("SELECT COUNT(p) FROM Produs p", Long.class).getSingleResult();
    }

    @Override
    public Produs getProdusDupaNume(String denumire) {
        return this.entityManager.createQuery(sqlDefaultText + " WHERE o.denumire = :denumireProdus", Produs.class)
                .setParameter("denumireProdus", denumire).setMaxResults(1).getSingleResult();
    }

    @Override
    public List<Produs> getProduseDupaNume(String denumire) {
        return this.entityManager.createQuery(sqlDefaultText + " WHERE o.denumire LIKE :denumireProdus", Produs.class)
                .setParameter("denumireProdus", "%" + denumire + "%").getResultList();
    }

    @Override
    public void refresh(Produs produs) {
        this.entityManager.refresh(produs);
    }

    @Override
    public void remove(Produs produs) {
        Produs produsPersistent = entityManager.find(Produs.class, produs.getIdProdus());
        try {
            entityManager.getTransaction().begin();
            //if (this.entityManager.contains(client))
            if (produsPersistent != null)
                this.entityManager.remove(produs);
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void add(Produs produs) {
        try {
            Produs produsPersistent = entityManager.find(Produs.class, produs.getIdProdus());
            entityManager.getTransaction().begin();
            if (produsPersistent != null) {
                System.out.println(">>>> to update-merge produs... " + produs);
                this.entityManager.merge(produs);
            }
            else {
                this.entityManager.persist(produs);
                System.out.println(">>>> to insert produs... " + produs);
            }
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public Produs getProdusDupaCod(Integer cod) {
        Produs p = this.entityManager.find(Produs.class, cod);
        //this.entityManager.refresh(p);
        return p;
    }

    @Override
    public Set<Produs> getAll() {
        List<Produs> result = this.entityManager.createQuery(this.sqlDefaultText, Produs.class).getResultList();

        TreeSet<Produs> entitatiOrdonate = new TreeSet<Produs>((p1, p2) -> p1.getIdProdus().compareTo(p2.getIdProdus()));
        entitatiOrdonate.addAll(result);

        return entitatiOrdonate;
    }

    @Override
    public Set<Imbracaminte> getAllImbracaminte() {
        List<Imbracaminte> result = this.entityManager.createQuery("SELECT i FROM Imbracaminte i", Imbracaminte.class).getResultList();

        TreeSet<Imbracaminte> entitatiOrdonate = new TreeSet<Imbracaminte>((i1, i2) -> i1.getIdProdus().compareTo(i2.getIdProdus()));
        entitatiOrdonate.addAll(result);

        return entitatiOrdonate;
    }

    @Override
    public void removeAll() {
        try {
            entityManager.getTransaction().begin();
//            entityManager.createQuery("DELETE FROM LinieFactura lf").executeUpdate();
//            entityManager.createQuery("DELETE FROM Factura f").executeUpdate();
//            entityManager.createQuery("DELETE FROM Cumparator c").executeUpdate();
            entityManager.createQuery("DELETE FROM Produs p").executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
            throw new RuntimeException(ex.getMessage());
        }
    }
}
