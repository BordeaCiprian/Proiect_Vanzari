package org.app.vanzari.web.views.livrari;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.app.vanzari.web.MainView;
import org.model.Livrare;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@PageTitle("livrari")
@Route(value = "livrari", layout = MainView.class)
public class NavigableGridLivrariView extends VerticalLayout implements HasUrlParameter<Integer> {
    private EntityManager em;
    private List<Livrare> livrari = new ArrayList<>();
    private Livrare livrare = null;

    private H1 titluForm = new H1("Lista Livrari");

    private VerticalLayout gridLayoutToolbar;
    private DatePicker filterDataMin = new DatePicker("Data livrare MIN...");
    private DatePicker filterDataMax = new DatePicker("Data livrare MAX...");

    private Button cmdEditLivrare = new Button("Editeaza livrare...");
    private Button cmdAdaugaLivrare = new Button("Adauga livrare...");
    private Button cmdStergeLivrare = new Button("Sterge livrare...");
    private Grid<Livrare> grid = new Grid<>(Livrare.class);

    private void initDataModel(){
        System.out.println("DEBUG START FORM>>> ");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProduseJPA");
        em = emf.createEntityManager();

        List<Livrare> lst = em.createQuery("SELECT l FROM Livrare l ORDER BY l.idLivrare", Livrare.class).getResultList();
        livrari.addAll(lst);

        if (lst != null && !lst.isEmpty()){
            Collections.sort(this.livrari, (l1, l2) -> l1.getIdLivrare().compareTo(l2.getIdLivrare()));
            this.livrare = livrari.get(0);
            System.out.println("DEBUG: livrare init>>> " + livrare.getIdLivrare());
        }

        grid.setItems(this.livrari);
        grid.asSingleSelect().setValue(this.livrare);
    }

    private void initViewLayout(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        filterDataMin.setPlaceholder("Filter by data livrare MIN...");
        filterDataMin.setClearButtonVisible(true);

        filterDataMax.setPlaceholder("Filter by data livrare MIN...");
        filterDataMax.setClearButtonVisible(true);

        HorizontalLayout gridToolbar = new HorizontalLayout(filterDataMin, filterDataMax, cmdEditLivrare, cmdAdaugaLivrare, cmdStergeLivrare);

        grid.setColumns("idLivrare");
        grid.addColumn(l -> l.getDataLivrare() == null ? null : dateFormat.format(l.getDataLivrare())).setKey("dataLivrare").setHeader("Data Livrare");
        grid.addColumns("docLivrare", "observatiiLivrare");
        grid.addColumn(f -> f.getFactura().getIdFactura()).setKey("factura").setHeader("Factura");
        grid.addComponentColumn(item -> createGridActionsButtons(item)).setHeader("Actiuni");

        gridLayoutToolbar = new VerticalLayout(gridToolbar, grid);

        this.add(titluForm, gridLayoutToolbar);
    }

    private HorizontalLayout createGridActionsButtons(Livrare item){
        Button cmdEditItem = new Button("Edit");
        cmdEditItem.addClickListener(e -> {
            grid.asSingleSelect().setValue(item);
            editLivrare();
        });
        Button cmdDeleteItem = new Button("Sterge");
        cmdDeleteItem.addClickListener(e -> {
            System.out.println("Sterge item: " + item);
            grid.asSingleSelect().setValue(item);
            stergeLivrare();
            refreshForm();
        });

        return new HorizontalLayout(cmdEditItem, cmdDeleteItem);
    }

    private void initControllerActions(){
        filterDataMin.addValueChangeListener(e -> updateList());
        filterDataMax.addValueChangeListener(e -> updateList());
        cmdEditLivrare.addClickListener(e -> {
            editLivrare();
        });
        cmdAdaugaLivrare.addClickListener(e -> {
            adaugaLivrare();
        });
        cmdStergeLivrare.addClickListener(e -> {
            stergeLivrare();
            refreshForm();
        });
    }

    private void adaugaLivrare(){
        this.getUI().ifPresent(ui -> ui.navigate(org.app.vanzari.web.views.livrari.FormLivrareView.class, 999));
    }

    private void editLivrare(){
        this.livrare = this.grid.asSingleSelect().getValue();
        System.out.println("Selected livrare: " + livrare);
        if (this.livrare != null){
            this.getUI().ifPresent(ui -> ui.navigate(FormLivrareView.class, this.livrare.getIdLivrare()));
        }
    }

    private void stergeLivrare(){
        this.livrare = this.grid.asSingleSelect().getValue();
        System.out.println("To remove: " + this.livrare);
        this.livrari.remove(this.livrare);
        if (this.em.contains(this.livrare)){
            this.em.getTransaction().begin();
            this.em.remove(this.livrare);
            this.em.getTransaction().commit();
        }

        if (!this.livrari.isEmpty())
            this.livrare = this.livrari.get(0);
        else
            this.livrari = null;
    }

    public NavigableGridLivrariView(){
        initDataModel();
        initViewLayout();
        initControllerActions();
    }

    private void updateList(){
        try{
            List<Livrare> lstLivrariFiltered = this.livrari;
            if (filterDataMin.getValue() != null){
                lstLivrariFiltered = this.livrari.stream()
                        .filter(f -> f.getDataLivrare().after(Date.valueOf(filterDataMin.getValue()))).toList();
            }
            if (filterDataMax.getValue() != null){
                lstLivrariFiltered = lstLivrariFiltered.stream()
                        .filter(f -> f.getDataLivrare().before(Date.valueOf(filterDataMax.getValue()))).toList();
            }

            grid.setItems(lstLivrariFiltered);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refreshForm(){
        System.out.println("Livrare curenta: " + this.livrare);
        if (this.livrare != null){
            grid.setItems(this.livrari);
            grid.select(this.livrare);
        }
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        if (id != null){
            this.livrare = em.find(Livrare.class, id);
            System.out.println("Back livrare: " + livrare);
            if (this.livrare == null){
                if (!this.livrari.isEmpty())
                    this.livrare = this.livrari.get(0);
            }
        }
        this.refreshForm();
    }
}

