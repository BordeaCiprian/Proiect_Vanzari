package org.app.vanzari.web.views.facturi;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.app.vanzari.web.MainView;
import org.model.Factura;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@PageTitle("facturi")
@Route(value = "facturi", layout = MainView.class)
public class NavigableGridFacturiView extends VerticalLayout implements HasUrlParameter<Integer> {
    private EntityManager em;
    private List<Factura> facturi = new ArrayList<>();
    private Factura factura = null;

    private H1 titluForm = new H1("Lista Facturi");

    private VerticalLayout gridLayoutToolbar;
    private NumberField filterTextMin = new NumberField();
    private NumberField filterTextMax = new NumberField();
    private Button cmdEditFactura = new Button("Editeaza factura...");
    private Button cmdAdaugaFactura = new Button("Adauga factura...");
    private Button cmdStergeFactura = new Button("Sterge factura...");
    private Grid<Factura> grid = new Grid<>(Factura.class);

    private void initDataModel(){
        System.out.println("DEBUG START FORM>>> ");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProduseJPA");
        em = emf.createEntityManager();

        List<Factura> lst = em.createQuery("SELECT f FROM Factura f ORDER BY f.idFactura", Factura.class).getResultList();
        facturi.addAll(lst);

        if (lst != null && !lst.isEmpty()){
            Collections.sort(this.facturi, (f1, f2) -> f1.getIdFactura().compareTo(f2.getIdFactura()));
            this.factura = facturi.get(0);
            System.out.println("DEBUG: factura init>>> " + factura.getIdFactura());
        }

        grid.setItems(this.facturi);
        grid.asSingleSelect().setValue(this.factura);
    }

    private void initViewLayout(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        filterTextMin.setPlaceholder("Filter by valoare MIN...");
        filterTextMin.setClearButtonVisible(true);
        filterTextMin.setValueChangeMode(ValueChangeMode.LAZY);

        filterTextMax.setPlaceholder("Filter by valoare MAX...");
        filterTextMax.setClearButtonVisible(true);
        filterTextMax.setValueChangeMode(ValueChangeMode.LAZY);

        HorizontalLayout gridToolbar = new HorizontalLayout(filterTextMin, filterTextMax, cmdEditFactura, cmdAdaugaFactura, cmdStergeFactura);

        grid.setColumns("idFactura");
        grid.addColumn(c -> c.getDataFact() == null ? null : dateFormat.format(c.getDataFact())).setKey("dataFact").setHeader("Data Factura");
        grid.addColumns("tvaFacturata", "discountFactura", "valoareTotalaFac");
        grid.addColumn(c -> c.getCumparator().getDenumireCumparator()).setKey("cumparator").setHeader("Cumparator");
        grid.addComponentColumn(item -> createGridActionsButtons(item)).setHeader("Actiuni");

        gridLayoutToolbar = new VerticalLayout(gridToolbar, grid);

        this.add(titluForm, gridLayoutToolbar);
    }

    private HorizontalLayout createGridActionsButtons(Factura item){
        Button cmdEditItem = new Button("Edit");
        cmdEditItem.addClickListener(e -> {
            grid.asSingleSelect().setValue(item);
            editFactura();
        });
        Button cmdDeleteItem = new Button("Sterge");
        cmdDeleteItem.addClickListener(e -> {
            System.out.println("Sterge item: " + item);
            grid.asSingleSelect().setValue(item);
            stergeFactura();
            refreshForm();
        });

        return new HorizontalLayout(cmdEditItem, cmdDeleteItem);
    }

    private void initControllerActions(){
        filterTextMin.addValueChangeListener(e -> updateList());
        filterTextMax.addValueChangeListener(e -> updateList());
        cmdEditFactura.addClickListener(e -> {
            editFactura();
        });
        cmdAdaugaFactura.addClickListener(e -> {
            adaugaFactura();
        });
        cmdStergeFactura.addClickListener(e -> {
            stergeFactura();
            refreshForm();
        });
    }

    private void adaugaFactura(){
        this.getUI().ifPresent(ui -> ui.navigate(FormFacturaView.class, 999));
    }

    private void editFactura(){
        this.factura = this.grid.asSingleSelect().getValue();
        System.out.println("Selected factura: " + factura);
        if (this.factura != null){
            this.getUI().ifPresent(ui -> ui.navigate(FormFacturaView.class, this.factura.getIdFactura()));
        }
    }

    private void stergeFactura(){
        this.factura = this.grid.asSingleSelect().getValue();
        System.out.println("To remove: " + this.factura);
        this.facturi.remove(this.factura);
        if (this.em.contains(this.factura)){
            this.em.getTransaction().begin();
            this.em.remove(this.factura);
            this.em.getTransaction().commit();
        }

        if (!this.facturi.isEmpty())
            this.factura = this.facturi.get(0);
        else
            this.facturi = null;
    }

    public NavigableGridFacturiView(){
        initDataModel();
        initViewLayout();
        initControllerActions();
    }

    private void updateList(){
        try{
            List<Factura> lstFacturiFiltered = this.facturi;
            if (filterTextMin.getValue() != null){
                lstFacturiFiltered = this.facturi.stream()
                        .filter(f -> f.getValoareTotala() >= Double.valueOf(filterTextMin.getValue())).toList();
            }
            if (filterTextMax.getValue() != null){
                lstFacturiFiltered = lstFacturiFiltered.stream()
                        .filter(f -> f.getValoareTotala() <= Double.valueOf(filterTextMax.getValue())).toList();
            }

            grid.setItems(lstFacturiFiltered);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refreshForm(){
        System.out.println("Factura curenta: " + this.factura);
        if (this.factura != null){
            grid.setItems(this.facturi);
            grid.select(this.factura);
        }
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        if (id != null){
            this.factura = em.find(Factura.class, id);
            System.out.println("Back factura: " + factura);
            if (this.factura == null){
                if (!this.facturi.isEmpty())
                    this.factura = this.facturi.get(0);
            }
        }
        this.refreshForm();
    }
}

