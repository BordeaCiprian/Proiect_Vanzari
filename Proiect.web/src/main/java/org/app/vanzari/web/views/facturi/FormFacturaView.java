package org.app.vanzari.web.views.facturi;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.LocalDateToDateConverter;
import com.vaadin.flow.router.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.app.vanzari.web.MainView;
import org.model.Cumparator;
import org.model.Factura;
import org.model.LinieFactura;
import org.model.Produs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@PageTitle("factura")
@Route(value = "factura", layout = MainView.class)
public class FormFacturaView extends VerticalLayout implements HasUrlParameter<Integer> {
    private EntityManager em;
    private Factura factura = null;
    private List<Cumparator> cumparatori = new ArrayList<Cumparator>();
    private List<Produs> produse = new ArrayList<Produs>();
    private Binder<Factura> binder = new BeanValidationBinder<>(Factura.class);

    private VerticalLayout formLayoutToolbar;
    private H1 titluForm = new H1("Form Factura");
    private IntegerField idFactura = new IntegerField("ID factura:");
    private DatePicker dataFactura = new DatePicker("Data factura:");

    private NumberField tva = new NumberField("TVA factura:");
    private NumberField discount = new NumberField("Discount factura:");
    private NumberField valoare = new NumberField("Valoare factura:");
    private ComboBox<Cumparator> cumparator = new ComboBox<Cumparator>("cumparator");

    private Button cmdAdaugare = new Button("Adauga");
    private Button cmdSterge = new Button("Sterge");
    private Button cmdAbandon = new Button("Abandon");
    private Button cmdSalveaza = new Button("Salveaza");

    private Grid<LinieFactura> liniiFacturiDetailGrid = new Grid<LinieFactura>(LinieFactura.class, false);
    private Button cmdAdaugareLinieFactura = new Button("Adauga linie factura");
    private Button cmdStergeLinieFactura = new Button("Sterge linie factura");

    public FormFacturaView(){
        initDataModel();
        initViewLayout();
        initControllerActions();
    }

    private void initDataModel(){
        System.out.println("DEBUG START FORM>>> ");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProduseJPA");
        this.em = emf.createEntityManager();
        this.factura = em.createQuery("SELECT f FROM Factura f ORDER BY f.idFactura", Factura.class)
                .getResultStream().findFirst().get();

        System.out.println(">>> init model Cumparatori >>> ");
        this.cumparatori = em.createQuery("SELECT c FROM Cumparator c ORDER BY c.idCumparator", Cumparator.class)
                .getResultList();
        if (cumparatori != null && !this.cumparatori.isEmpty())
            Collections.sort(this.cumparatori, (c1, c2) -> c1.getIdCumparator().compareTo(c2.getIdCumparator()));

        System.out.println(">>> init model Produse >>> ");
        this.produse = em.createQuery("SELECT p FROM Produs p ORDER BY p.idProdus", Produs.class)
                .getResultList();
        if (produse != null && !this.produse.isEmpty())
            Collections.sort(this.produse, (p1, p2) -> p1.getIdProdus().compareTo(p2.getIdProdus()));

        this.cumparator.setItems(this.cumparatori);

        binder.forField(idFactura).bind("idFactura");
        binder.forField(dataFactura).withConverter(new LocalDateToDateConverter()).bind("dataFact");
        binder.forField(tva).bind("tvaFacturata");
        binder.forField(discount).bind("discountFactura");
        binder.forField(valoare).bind("valoareTotalaFac");
        binder.forField(cumparator).bind("cumparator");

        refreshForm();
    }

    private void initViewLayout(){

        cumparator.setItemLabelGenerator(c -> c.getDenumireCumparator());

        FormLayout formLayout = new FormLayout();
        formLayout.add(idFactura, dataFactura, tva, discount, valoare, cumparator);
        formLayout.setResponsiveSteps(new ResponsiveStep("0", 1));
        formLayout.setMaxWidth("400px");

        HorizontalLayout actionToolbar = new HorizontalLayout(cmdAdaugare, cmdSterge, cmdAbandon, cmdSalveaza);
        actionToolbar.setPadding(false);

        liniiFacturiDetailGrid.addColumn("linie");
        liniiFacturiDetailGrid.addColumn(p -> p.getProdus().getDenumire()).setKey("produs").setHeader("Produs");
        liniiFacturiDetailGrid.addColumn("cantitate");
        liniiFacturiDetailGrid.addColumn("pretUnit");
        liniiFacturiDetailGrid.addColumn("valoareLinieFactura");
        liniiFacturiDetailGrid.getColumns().forEach(col -> col.setAutoWidth(true));

        initDetailsGridEditor();

        HorizontalLayout gridDetailsToolbar = new HorizontalLayout(cmdAdaugareLinieFactura, cmdStergeLinieFactura);

        VerticalLayout releaseDetailGridLayout = new VerticalLayout(liniiFacturiDetailGrid, gridDetailsToolbar);

        this.formLayoutToolbar = new VerticalLayout(formLayout, actionToolbar, releaseDetailGridLayout);

        this.add(titluForm, formLayoutToolbar);

        this.idFactura.setEnabled(false);
    }

    private void initControllerActions(){
        cmdAdaugare.addClickListener(e -> {
            adaugaFactura();
            refreshForm();
        });
        cmdSterge.addClickListener(e -> {
            stergeFactura();
            this.getUI().ifPresent(ui -> ui.navigate(NavigableGridFacturiView.class));
        });
        cmdAbandon.addClickListener(e -> {
            this.getUI().ifPresent(ui -> ui.navigate(NavigableGridFacturiView.class, this.factura.getIdFactura()));
        });
        cmdSalveaza.addClickListener(e -> {
            salveazaFactura();
            //refreshForm();
            this.getUI().ifPresent(ui -> ui.navigate(NavigableGridFacturiView.class, this.factura.getIdFactura()));
        });

        cmdAdaugareLinieFactura.addClickListener(e -> adaugaLinieFactura());
        cmdStergeLinieFactura.addClickListener(e -> stergeLinieFactura());
    }

    private void adaugaLinieFactura(){
        LinieFactura newLinieFactura = new LinieFactura(null, this.produse.get(0), 0.0, 0.0, this.factura);

        this.factura.getLiniiFact().add(newLinieFactura);
        updateLiniiFacturiDetailGrid();

        this.liniiFacturiDetailGrid.asSingleSelect().setValue(newLinieFactura);
        this.liniiFacturiDetailGrid.getEditor().editItem(newLinieFactura);

        Focusable gridColumnEditor = (Focusable) this.liniiFacturiDetailGrid.getColumnByKey("linie").getEditorComponent();
        gridColumnEditor.focus();
    }

    private void stergeLinieFactura(){
        LinieFactura curentLinieFactura = liniiFacturiDetailGrid.asSingleSelect().getValue();
        if (curentLinieFactura != null) {
            this.factura.getLiniiFact().remove(curentLinieFactura);
            updateLiniiFacturiDetailGrid();
        }
    }

    private void refreshForm(){
        System.out.println("Factura curenta: " + this.factura);
        if (this.factura != null){
            binder.setBean(this.factura);
            updateLiniiFacturiDetailGrid();
        }
    }

    private void updateLiniiFacturiDetailGrid() {
        if (this.factura != null && this.factura.getLiniiFact() != null) {
            liniiFacturiDetailGrid.setItems(this.factura.getLiniiFact());
        }
    }

    private void initDetailsGridEditor() {
        Binder<LinieFactura> gridBinder = new Binder<>(LinieFactura.class);
        Editor<LinieFactura> gridEditor = liniiFacturiDetailGrid.getEditor();
        gridEditor.setBinder(gridBinder);

        IntegerField idLinieField = new IntegerField();
        idLinieField.setWidthFull();
        idLinieField.setReadOnly(true);
        gridBinder.bind(idLinieField, "linie");
        liniiFacturiDetailGrid.getColumnByKey("linie").setEditorComponent(idLinieField);

        ComboBox<Produs> produsField = new ComboBox<Produs>();
        produsField.setWidthFull();
        produsField.setItemLabelGenerator(p -> p.getDenumire());
        produsField.setAllowCustomValue(true);
        produsField.setItems(this.produse);
        gridBinder.bind(produsField, "produs");
        liniiFacturiDetailGrid.getColumnByKey("produs").setEditorComponent(produsField);

        NumberField cantitateField = new NumberField();
        cantitateField.setValue(0.0);
        cantitateField.setWidthFull();
        gridBinder.bind(cantitateField, "cantitate");
        liniiFacturiDetailGrid.getColumnByKey("cantitate").setEditorComponent(cantitateField);

        NumberField pretUnitField = new NumberField();
        pretUnitField.setValue(0.0);
        pretUnitField.setWidthFull();
        gridBinder.bind(pretUnitField, "pretUnit");
        liniiFacturiDetailGrid.getColumnByKey("pretUnit").setEditorComponent(pretUnitField);

        liniiFacturiDetailGrid.addItemDoubleClickListener(e -> {
            gridEditor.editItem(e.getItem());
            Component editorComponent = e.getColumn().getEditorComponent();
            if (editorComponent instanceof Focusable) {
                ((Focusable) editorComponent).focus();
            }
        });
    }

    private void salveazaFactura(){
        try{
            this.em.getTransaction().begin();
            this.factura = this.em.merge(this.factura);
            this.em.getTransaction().commit();
            System.out.println("Factura Salvata");
        } catch (Exception ex){
            if (this.em.getTransaction().isActive())
                this.em.getTransaction().rollback();
            System.out.println("*** EntityManager Validation ex: " + ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    private void adaugaFactura(){
        this.factura = new Factura();
        this.factura.setIdFactura(999);
        this.factura.setDataFact(new Date());
        this.factura.setTvaFacturata(0.19);
        this.factura.setDiscountFactura(0.0);
        this.factura.setCumparator(this.cumparatori.get(0));

        this.idFactura.setEnabled(true);
    }

    private void stergeFactura(){
        System.out.println("To remove: " + this.factura);
        if (this.em.contains(this.factura)){
            this.em.getTransaction().begin();
            this.em.remove(this.factura);
            this.em.getTransaction().commit();
        }
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        System.out.println("Factura ID: " + id);
        if (id != null){
            this.factura = em.find(Factura.class, id);
            System.out.println("Selected factura to edit: " + factura);
            if (this.factura == null){
                System.out.println("ADD factura: " + factura);

                this.adaugaFactura();
                this.factura.setIdFactura(id);
            }
        }
        this.refreshForm();
    }
}
