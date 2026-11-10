package org.app.vanzari.web.views.cumparatori;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.app.vanzari.web.MainView;
import org.model.Cumparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@PageTitle("cumparatori")
@Route(value = "cumparatori", layout = MainView.class)
public class NavigableGridCumparatorView extends VerticalLayout implements HasUrlParameter<Integer> {
    private EntityManager em;
    private List<Cumparator> cumparatori = new ArrayList<>();
    private Cumparator cumparator = null;

    private H1 titluForm = new H1("Lista Cumparatori");

    private VerticalLayout gridLayoutToolbar;
    private TextField filterText = new TextField();
    private Button cmdEditCumparator = new Button("Editeaza cumparator...");
    private Button cmdAdaugaCumparator = new Button("Adauga cumparator...");
    private Button cmdStergeCumparator = new Button("Sterge cumparator...");
    private Grid<Cumparator> grid = new Grid<>(Cumparator.class);

    private void initDataModel(){
        System.out.println("DEBUG START FORM>>> ");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProduseJPA");
        em = emf.createEntityManager();

        List<Cumparator> lst = em.createQuery("SELECT c FROM Cumparator c ORDER BY c.idCumparator", Cumparator.class).getResultList();
        cumparatori.addAll(lst);

        if (lst != null && !lst.isEmpty()){
            Collections.sort(this.cumparatori, (c1, c2) -> c1.getIdCumparator().compareTo(c2.getIdCumparator()));
            this.cumparator = cumparatori.get(0);
            System.out.println("DEBUG: cumparator init>>> " + cumparator.getIdCumparator());
        }

        grid.setItems(this.cumparatori);
        grid.asSingleSelect().setValue(this.cumparator);
    }

    private void initViewLayout(){
        filterText.setPlaceholder("Filter by denumire...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);

        HorizontalLayout gridToolbar = new HorizontalLayout(filterText, cmdEditCumparator, cmdAdaugaCumparator, cmdStergeCumparator);

        grid.setColumns("idCumparator", "denumireCumparator", "adresaCumparator");
        grid.addComponentColumn(item -> createGridActionsButtons(item)).setHeader("Actiuni");

        gridLayoutToolbar = new VerticalLayout(gridToolbar, grid);

        this.add(titluForm, gridLayoutToolbar);
    }

    private HorizontalLayout createGridActionsButtons(Cumparator item){
        Button cmdEditItem = new Button("Edit");
        cmdEditItem.addClickListener(e -> {
            grid.asSingleSelect().setValue(item);
            editCumparator();
        });
        Button cmdDeleteItem = new Button("Sterge");
        cmdDeleteItem.addClickListener(e -> {
            System.out.println("Sterge item: " + item);
            grid.asSingleSelect().setValue(item);
            stergeCumparator();
            refreshForm();
        });

        return new HorizontalLayout(cmdEditItem, cmdDeleteItem);
    }

    private void initControllerActions(){
        filterText.addValueChangeListener(e -> updateList());
        cmdEditCumparator.addClickListener(e -> {
            editCumparator();
        });
        cmdAdaugaCumparator.addClickListener(e -> {
            adaugaCumparator();
        });
        cmdStergeCumparator.addClickListener(e -> {
            stergeCumparator();
            refreshForm();
        });
    }

    private void adaugaCumparator(){
        this.getUI().ifPresent(ui -> ui.navigate(FormCumparatorView.class, 999));
    }

    private void editCumparator(){
        this.cumparator = this.grid.asSingleSelect().getValue();
        System.out.println("Selected cumparator: " + cumparator);
        if (this.cumparator != null){
            this.getUI().ifPresent(ui -> ui.navigate(FormCumparatorView.class, this.cumparator.getIdCumparator()));
        }
    }

    private void stergeCumparator(){
        this.cumparator = this.grid.asSingleSelect().getValue();
        System.out.println("To remove: " + this.cumparator);
        this.cumparatori.remove(this.cumparator);
        if (this.em.contains(this.cumparator)){
            this.em.getTransaction().begin();
            this.em.remove(this.cumparator);
            this.em.getTransaction().commit();
        }

        if (!this.cumparatori.isEmpty())
            this.cumparator = this.cumparatori.get(0);
        else
            this.cumparatori = null;
    }

    public NavigableGridCumparatorView(){
        initDataModel();
        initViewLayout();
        initControllerActions();
    }

    private void updateList(){
        try{
            List<Cumparator> lstCumparatoriFiltered = this.cumparatori;
            if (filterText.getValue() != null){
                lstCumparatoriFiltered = this.cumparatori.stream()
                        .filter(c -> c.getDenumireCumparator().contains(filterText.getValue())).toList();
                grid.setItems(lstCumparatoriFiltered);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refreshForm(){
        System.out.println("Cumparator curent: " + this.cumparator);
        if (this.cumparator != null){
            grid.setItems(this.cumparatori);
            grid.select(this.cumparator);
        }
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        if (id != null){
            this.cumparator = em.find(Cumparator.class, id);
            System.out.println("Back cumparator: " + cumparator);
            if (this.cumparator == null){
                if (!this.cumparatori.isEmpty())
                    this.cumparator = this.cumparatori.get(0);
            }
        }
        this.refreshForm();
    }
}

