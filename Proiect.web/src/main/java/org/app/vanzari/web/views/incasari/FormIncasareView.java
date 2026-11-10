package org.app.vanzari.web.views.incasari;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.LocalDateToDateConverter;
import com.vaadin.flow.router.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.app.vanzari.web.MainView;
import org.model.Factura;
import org.model.Incasare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@PageTitle("incasare")
@Route(value = "incasare", layout = MainView.class)
public class FormIncasareView extends VerticalLayout implements HasUrlParameter<Integer> {
    private EntityManager em;
    private Incasare incasare = null;
    private List<Factura> facturi = new ArrayList<Factura>();
    private Binder<Incasare> binder = new BeanValidationBinder<>(Incasare.class);

    private VerticalLayout formLayoutToolbar;
    private H1 titluForm = new H1("Form Incasare");
    private IntegerField idIncasare = new IntegerField("ID incasare:");
    private DatePicker dataIncasare = new DatePicker("Data incasare:");
    private TextField docIncasare = new TextField("Document incasare:");
    private IntegerField nrIncasare = new IntegerField("Numar incasare:");
    private NumberField sumaIncasare = new NumberField("Suma incasare:");
    private ComboBox<Factura> factura = new ComboBox<Factura>("Factura incasare:");

    private Button cmdAdaugare = new Button("Adauga");
    private Button cmdSterge = new Button("Sterge");
    private Button cmdAbandon = new Button("Abandon");
    private Button cmdSalveaza = new Button("Salveaza");

    private void initDataModel(){
        System.out.println("DEBUG START FORM>>> ");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProduseJPA");
        this.em = emf.createEntityManager();
        this.incasare = em.createQuery("SELECT i FROM Incasare i ORDER BY i.idIncasare", Incasare.class)
                .getResultStream().findFirst().get();

        System.out.println(">>> init model Facturi >>> ");
        this.facturi = em.createQuery("SELECT f FROM Factura f ORDER BY f.idFactura", Factura.class)
                .getResultList();
        if (facturi != null && !this.facturi.isEmpty())
            Collections.sort(this.facturi, (f1, f2) -> f1.getIdFactura().compareTo(f2.getIdFactura()));

        this.factura.setItems(this.facturi);

        binder.forField(idIncasare).bind("idIncasare");
        binder.forField(dataIncasare).withConverter(new LocalDateToDateConverter()).bind("dataIncasare");
        binder.forField(docIncasare).bind("documentIncasare");
        binder.forField(nrIncasare).bind("nrIncasare");
        binder.forField(sumaIncasare).bind("sumaIncasare");
        binder.forField(factura).bind("factura");

        refreshForm();
    }

    private void initViewLayout(){
        factura.setItemLabelGenerator(f -> f.getIdFactura().toString());

        FormLayout formLayout = new FormLayout();
        formLayout.add(idIncasare, dataIncasare, docIncasare, nrIncasare, sumaIncasare, factura);
        formLayout.setResponsiveSteps(new ResponsiveStep("0", 1));
        formLayout.setMaxWidth("400px");

        HorizontalLayout actionToolbar = new HorizontalLayout(cmdAdaugare, cmdSterge, cmdAbandon, cmdSalveaza);
        actionToolbar.setPadding(false);

        this.formLayoutToolbar = new VerticalLayout(formLayout, actionToolbar);

        this.add(titluForm, formLayoutToolbar);
    }

    private void initControllerActions(){
        cmdAdaugare.addClickListener(e -> {
            adaugaIncasare();
            refreshForm();
        });
        cmdSterge.addClickListener(e -> {
            stergeIncasare();
            this.getUI().ifPresent(ui -> ui.navigate(NavigableGridIncasariView.class));
        });
        cmdAbandon.addClickListener(e -> {
            this.getUI().ifPresent(ui -> ui.navigate(NavigableGridIncasariView.class, this.incasare.getIdIncasare()));
        });
        cmdSalveaza.addClickListener(e -> {
            salveazaIncasare();
            //refreshForm();
            this.getUI().ifPresent(ui -> ui.navigate(NavigableGridIncasariView.class, this.incasare.getIdIncasare()));
        });
    }

    private void refreshForm(){
        System.out.println("Incasare curenta: " + this.incasare);
        if (this.incasare != null){
            binder.setBean(this.incasare);
        }
    }

    private void salveazaIncasare(){
        try{
            this.em.getTransaction().begin();
            this.incasare = this.em.merge(this.incasare);
            this.em.getTransaction().commit();
            System.out.println("Incasare Salvata");
        } catch (Exception ex){
            if (this.em.getTransaction().isActive())
                this.em.getTransaction().rollback();
            System.out.println("*** EntityManager Validation ex: " + ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    private void adaugaIncasare(){
        this.incasare = new Incasare();
        this.incasare.setIdIncasare(999);
        this.incasare.setDataIncasare(new Date());
        this.incasare.setDocumentIncasare("OP");
        this.incasare.setNrIncasare(1);
        this.incasare.setFactura(this.facturi.get(0));

        this.idIncasare.setEnabled(true);
    }

    private void stergeIncasare(){
        System.out.println("To remove: " + this.incasare);
        if (this.em.contains(this.incasare)){
            this.em.getTransaction().begin();
            this.em.remove(this.incasare);
            this.em.getTransaction().commit();
        }
    }

    public FormIncasareView(){
        initDataModel();
        initViewLayout();
        initControllerActions();
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        System.out.println("Incasare ID: " + id);
        if (id != null){
            this.incasare = em.find(Incasare.class, id);
            System.out.println("Selected incasare to edit: " + incasare);
            if (this.incasare == null){
                System.out.println("ADD incasare: " + incasare);

                this.adaugaIncasare();
                this.incasare.setIdIncasare(id);
            }
        }
        this.refreshForm();
    }
}
