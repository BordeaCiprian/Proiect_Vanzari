package org.model.repository;

import org.model.Imbracaminte;
import org.model.Produs;

import java.util.Collection;
import java.util.List;

public interface IRepositoryProduse {
    Long getNumarProduse();
    Produs getProdusDupaNume(String nume);
    List<Produs> getProduseDupaNume(String nume);
    void refresh(Produs produs);
    void remove(Produs produs);
    void add(Produs produs);
    Produs getProdusDupaCod(Integer cod);
    Collection<Produs> getAll();
    Collection<Imbracaminte> getAllImbracaminte();
    void removeAll();
}
