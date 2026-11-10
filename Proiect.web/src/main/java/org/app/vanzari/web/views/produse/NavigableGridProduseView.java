package org.app.vanzari.web.views.produse;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.app.vanzari.web.MainView;
import org.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@PageTitle("produse")
@Route(value = "produse", layout = MainView.class)
public class NavigableGridProduseView extends VerticalLayout implements HasUrlParameter<Integer> {
    private EntityManager em;
    private List<Produs> produse = new ArrayList<>();
    private Produs produs = null;

    private H1 titluForm = new H1("Lista Produse");

    private VerticalLayout gridLayoutToolbar;
    private TextField filterText = new TextField();

    private ComboBox<String> filtrareProduse = new ComboBox<>();

    private Button cmdEditProdus = new Button("Editeaza produs...");
    private Button cmdAdaugaProdus = new Button("Adauga produs...");
    private Button cmdStergeProdus = new Button("Sterge produs...");
    private Grid<Produs> grid = new Grid<>(Produs.class);
    private Grid<Imbracaminte> gridImbracaminte = new Grid<>(Imbracaminte.class);
    private Grid<Incaltaminte> gridIncaltaminte = new Grid<>(Incaltaminte.class);
    private Grid<Accesoriu> gridAccesorii = new Grid<>(Accesoriu.class);

    private void initDataModel(){
        System.out.println("DEBUG START FORM>>> ");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProduseJPA");
        em = emf.createEntityManager();
        if (filtrareProduse.getValue() != null) {
            if (filtrareProduse.getValue().equals("Imbracaminte")) {
                List<Imbracaminte> lst = em.createQuery("SELECT i FROM Imbracaminte i ORDER BY i.idProdus", Imbracaminte.class).getResultList();
                produse.removeAll(produse);
                produse.addAll(lst);

                if (lst != null && !lst.isEmpty()) {
                    Collections.sort(this.produse, (p1, p2) -> p1.getIdProdus().compareTo(p2.getIdProdus()));
                    this.produs = lst.get(0);
                    System.out.println("DEBUG: produs init>>> " + produs.getIdProdus());
                }

                gridImbracaminte.setItems(lst);
                if (lst != null && !lst.isEmpty())
                    gridImbracaminte.asSingleSelect().setValue(lst.get(0));
                else
                    gridImbracaminte.asSingleSelect().setValue(null);
            }
            if (filtrareProduse.getValue().equals("Incaltaminte")) {
                List<Incaltaminte> lst = em.createQuery("SELECT i FROM Incaltaminte i ORDER BY i.idProdus", Incaltaminte.class).getResultList();
                produse.removeAll(produse);
                produse.addAll(lst);

                if (lst != null && !lst.isEmpty()) {
                    Collections.sort(this.produse, (p1, p2) -> p1.getIdProdus().compareTo(p2.getIdProdus()));
                    this.produs = lst.get(0);
                    System.out.println("DEBUG: produs init>>> " + produs.getIdProdus());
                }

                gridIncaltaminte.setItems(lst);
                if (lst != null && !lst.isEmpty())
                    gridIncaltaminte.asSingleSelect().setValue(lst.get(0));
                else
                    gridIncaltaminte.asSingleSelect().setValue(null);
            }
            if (filtrareProduse.getValue().equals("Accesorii")) {
                List<Accesoriu> lst = em.createQuery("SELECT a FROM Accesoriu a ORDER BY a.idProdus", Accesoriu.class).getResultList();
                produse.removeAll(produse);
                produse.addAll(lst);

                if (lst != null && !lst.isEmpty()) {
                    Collections.sort(this.produse, (p1, p2) -> p1.getIdProdus().compareTo(p2.getIdProdus()));
                    this.produs = lst.get(0);
                    System.out.println("DEBUG: produs init>>> " + produs.getIdProdus());
                }

                gridAccesorii.setItems(lst);
                if (lst != null && !lst.isEmpty())
                    gridAccesorii.asSingleSelect().setValue(lst.get(0));
                else
                    gridAccesorii.asSingleSelect().setValue(null);
            }
        }
        else{
            List<Produs> lst = em.createQuery("SELECT p FROM Produs p ORDER BY p.idProdus", Produs.class).getResultList();
            produse.addAll(lst);

            if (lst != null && !lst.isEmpty()) {
                Collections.sort(this.produse, (p1, p2) -> p1.getIdProdus().compareTo(p2.getIdProdus()));
                this.produs = produse.get(0);
                System.out.println("DEBUG: produs init>>> " + produs.getIdProdus());
            }

            grid.setItems(this.produse);
            grid.asSingleSelect().setValue(this.produs);
        }
    }

    private void initViewLayout(){

        HorizontalLayout gridToolbar = new HorizontalLayout(filterText, filtrareProduse,cmdEditProdus, cmdAdaugaProdus, cmdStergeProdus);

        if (filtrareProduse.getValue() != null) {
            if (filtrareProduse.getValue().equals("Imbracaminte")) {
                gridImbracaminte.setColumns("idProdus", "denumire", "UM", "material", "marime", "genPurtator", "categorieVarstaPurtator");
                gridImbracaminte.addComponentColumn(item -> createGridActionsButtons(item)).setHeader("Actiuni");

                gridLayoutToolbar = new VerticalLayout(gridToolbar, gridImbracaminte);
            }
            if (filtrareProduse.getValue().equals("Incaltaminte")) {
                gridIncaltaminte.setColumns("idProdus", "denumire", "UM", "dimensiune", "material", "genPurtator");
                gridIncaltaminte.addComponentColumn(item -> createGridActionsButtons(item)).setHeader("Actiuni");

                gridLayoutToolbar = new VerticalLayout(gridToolbar, gridIncaltaminte);
            }
            if (filtrareProduse.getValue().equals("Accesorii")) {
                gridAccesorii.setColumns("idProdus", "denumire", "UM", "brand");
                gridAccesorii.addComponentColumn(item -> createGridActionsButtons(item)).setHeader("Actiuni");

                gridLayoutToolbar = new VerticalLayout(gridToolbar, gridAccesorii);
            }
        }
        else {
            filterText.setPlaceholder("Filter by denumire...");
            filterText.setClearButtonVisible(true);
            filterText.setValueChangeMode(ValueChangeMode.LAZY);

            filtrareProduse.setPlaceholder("Filter by categorie produs...");
            filtrareProduse.setClearButtonVisible(true);
            filtrareProduse.setItems("Imbracaminte", "Incaltaminte", "Accesorii");

            grid.setColumns("idProdus", "denumire", "UM");
            grid.addComponentColumn(item -> createGridActionsButtons(item)).setHeader("Actiuni");

            gridLayoutToolbar = new VerticalLayout(gridToolbar, grid);
        }

        this.add(titluForm, gridLayoutToolbar);
    }

    private HorizontalLayout createGridActionsButtons(Produs item){
        Button cmdEditItem = new Button("Edit");
        cmdEditItem.addClickListener(e -> {
            grid.asSingleSelect().setValue(item);
            editProdus();
        });
        Button cmdDeleteItem = new Button("Sterge");
        cmdDeleteItem.addClickListener(e -> {
            System.out.println("Sterge item: " + item);
            grid.asSingleSelect().setValue(item);
            stergeProdus();
            refreshForm();
        });

        return new HorizontalLayout(cmdEditItem, cmdDeleteItem);
    }

    private void initControllerActions(){
        filterText.addValueChangeListener(e -> updateList());
        filtrareProduse.addValueChangeListener(e -> {
            if (!produse.isEmpty())
                produse.removeAll(produse);
            this.remove(titluForm, gridLayoutToolbar);
            initDataModel(); initViewLayout();
        });
        cmdEditProdus.addClickListener(e -> {
            editProdus();
        });
        cmdAdaugaProdus.addClickListener(e -> {
            adaugaProdus();
        });
        cmdStergeProdus.addClickListener(e -> {
            stergeProdus();
            refreshForm();
        });
    }

    private void adaugaProdus(){
        this.getUI().ifPresent(ui -> ui.navigate(FormProdusView.class, 999));
    }

    private void editProdus(){
        this.produs = this.grid.asSingleSelect().getValue();
        System.out.println("Selected produs: " + produs);
        if (this.produs != null){
            this.getUI().ifPresent(ui -> ui.navigate(FormProdusView.class, this.produs.getIdProdus()));
        }
    }

    private void stergeProdus(){
        this.produs = this.grid.asSingleSelect().getValue();
        System.out.println("To remove: " + this.produs);
        this.produse.remove(this.produs);
        if (this.em.contains(this.produs)){
            this.em.getTransaction().begin();
            this.em.remove(this.produs);
            this.em.getTransaction().commit();
        }

        if (!this.produse.isEmpty())
            this.produs = this.produse.get(0);
        else
            this.produse = null;
    }

    public NavigableGridProduseView(){
        initDataModel();
        initViewLayout();
        initControllerActions();
    }

    private void updateList(){
        try{
            List<Produs> lstProduseFiltered = this.produse;
            if (filterText.getValue() != null){
                lstProduseFiltered = this.produse.stream()
                        .filter(p -> p.getDenumire().contains(filterText.getValue())).toList();
                grid.setItems(lstProduseFiltered);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refreshForm(){
        System.out.println("Produs curent: " + this.produs);
        if (this.produs != null){
            grid.setItems(this.produse);
            grid.select(this.produs);
        }
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        if (id != null){
            this.produs = em.find(Produs.class, id);
            System.out.println("Back produs: " + produs);
            if (this.produs == null){
                if (!this.produse.isEmpty())
                    this.produs = this.produse.get(0);
            }
        }
        this.refreshForm();
    }
}

