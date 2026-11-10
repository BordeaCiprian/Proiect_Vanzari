package org.app.vanzari.web.views.incasari;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.app.vanzari.web.MainView;
import org.model.Incasare;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@PageTitle("incasari")
@Route(value = "incasari", layout = MainView.class)
public class NavigableGridIncasariView extends VerticalLayout implements HasUrlParameter<Integer> {
    private EntityManager em;
    private List<Incasare> incasari = new ArrayList<>();
    private Incasare incasare = null;

    private H1 titluForm = new H1("Lista Incasari");

    private VerticalLayout gridLayoutToolbar;
    private IntegerField filterText = new IntegerField();

    private Button cmdEditIncasare = new Button("Editeaza incasare...");
    private Button cmdAdaugaIncasare = new Button("Adauga incasare...");
    private Button cmdStergeIncasare = new Button("Sterge incasare...");
    private Grid<Incasare> grid = new Grid<>(Incasare.class);

    private void initDataModel(){
        System.out.println("DEBUG START FORM>>> ");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProduseJPA");
        em = emf.createEntityManager();

        List<Incasare> lst = em.createQuery("SELECT i FROM Incasare i ORDER BY i.idIncasare", Incasare.class).getResultList();
        incasari.addAll(lst);

        if (lst != null && !lst.isEmpty()){
            Collections.sort(this.incasari, (i1, i2) -> i1.getIdIncasare().compareTo(i2.getIdIncasare()));
            this.incasare = incasari.get(0);
            System.out.println("DEBUG: incasare init>>> " + incasare.getIdIncasare());
        }

        grid.setItems(this.incasari);
        grid.asSingleSelect().setValue(this.incasare);
    }

    private void initViewLayout(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        filterText.setPlaceholder("Filter by id incasare...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);

        HorizontalLayout gridToolbar = new HorizontalLayout(filterText, cmdEditIncasare, cmdAdaugaIncasare, cmdStergeIncasare);

        grid.setColumns("idIncasare");
        grid.addColumn(i -> i.getDataIncasare() == null ? null : dateFormat.format(i.getDataIncasare())).setKey("dataIncasare").setHeader("Data Incasare");
        grid.addColumns("documentIncasare", "nrIncasare", "sumaIncasare");
        grid.addColumn(f -> f.getFactura().getIdFactura()).setKey("factura").setHeader("Factura");
        grid.addComponentColumn(item -> createGridActionsButtons(item)).setHeader("Actiuni");

        gridLayoutToolbar = new VerticalLayout(gridToolbar, grid);

        this.add(titluForm, gridLayoutToolbar);
    }

    private HorizontalLayout createGridActionsButtons(Incasare item){
        Button cmdEditItem = new Button("Edit");
        cmdEditItem.addClickListener(e -> {
            grid.asSingleSelect().setValue(item);
            editIncasare();
        });
        Button cmdDeleteItem = new Button("Sterge");
        cmdDeleteItem.addClickListener(e -> {
            System.out.println("Sterge item: " + item);
            grid.asSingleSelect().setValue(item);
            stergeIncasare();
            refreshForm();
        });

        return new HorizontalLayout(cmdEditItem, cmdDeleteItem);
    }

    private void initControllerActions(){
        filterText.addValueChangeListener(e -> updateList());
        cmdEditIncasare.addClickListener(e -> {
            editIncasare();
        });
        cmdAdaugaIncasare.addClickListener(e -> {
            adaugaIncasare();
        });
        cmdStergeIncasare.addClickListener(e -> {
            stergeIncasare();
            refreshForm();
        });
    }

    private void adaugaIncasare(){
        this.getUI().ifPresent(ui -> ui.navigate(FormIncasareView.class, 999));
    }

    private void editIncasare(){
        this.incasare = this.grid.asSingleSelect().getValue();
        System.out.println("Selected incasare: " + incasare);
        if (this.incasare != null){
            this.getUI().ifPresent(ui -> ui.navigate(FormIncasareView.class, this.incasare.getIdIncasare()));
        }
    }

    private void stergeIncasare(){
        this.incasare = this.grid.asSingleSelect().getValue();
        System.out.println("To remove: " + this.incasare);
        this.incasari.remove(this.incasare);
        if (this.em.contains(this.incasare)){
            this.em.getTransaction().begin();
            this.em.remove(this.incasare);
            this.em.getTransaction().commit();
        }

        if (!this.incasari.isEmpty())
            this.incasare = this.incasari.get(0);
        else
            this.incasari = null;
    }

    public NavigableGridIncasariView(){
        initDataModel();
        initViewLayout();
        initControllerActions();
    }

    private void updateList(){
        try{
            List<Incasare> lstIncasariFiltered = this.incasari;
            if (filterText.getValue() != null){
                lstIncasariFiltered = this.incasari.stream()
                        .filter(i -> i.getIdIncasare().toString().contains(filterText.getValue().toString())).toList();
                grid.setItems(lstIncasariFiltered);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refreshForm(){
        System.out.println("Incasare curenta: " + this.incasare);
        if (this.incasare != null){
            grid.setItems(this.incasari);
            grid.select(this.incasare);
        }
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        if (id != null){
            this.incasare = em.find(Incasare.class, id);
            System.out.println("Back incasare: " + incasare);
            if (this.incasare == null){
                if (!this.incasari.isEmpty())
                    this.incasare = this.incasari.get(0);
            }
        }
        this.refreshForm();
    }
}

