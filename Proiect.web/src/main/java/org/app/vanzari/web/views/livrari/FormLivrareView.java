package org.app.vanzari.web.views.livrari;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
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
import org.model.Livrare;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@PageTitle("livrare")
@Route(value = "livrare", layout = MainView.class)
public class FormLivrareView extends VerticalLayout implements HasUrlParameter<Integer> {
    private EntityManager em;
    private Livrare livrare = null;
    private List<Factura> facturi = new ArrayList<Factura>();
    private Binder<Livrare> binder = new BeanValidationBinder<>(Livrare.class);

    private VerticalLayout formLayoutToolbar;
    private H1 titluForm = new H1("Form Livrare");
    private IntegerField idLivrare = new IntegerField("ID livrare:");
    private DatePicker dataLivrare = new DatePicker("Data livrare:");
    private TextField docLivrare = new TextField("Document livrare:");
    private TextField observatiiLivrare = new TextField("Observatii livrare:");
    private ComboBox<Factura> factura = new ComboBox<Factura>("Factura livrare:");

    private Button cmdAdaugare = new Button("Adauga");
    private Button cmdSterge = new Button("Sterge");
    private Button cmdAbandon = new Button("Abandon");
    private Button cmdSalveaza = new Button("Salveaza");

    private void initDataModel(){
        System.out.println("DEBUG START FORM>>> ");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProduseJPA");
        this.em = emf.createEntityManager();
        this.livrare = em.createQuery("SELECT l FROM Livrare l ORDER BY l.idLivrare", Livrare.class)
                .getResultStream().findFirst().get();

        System.out.println(">>> init model Facturi >>> ");
        this.facturi = em.createQuery("SELECT f FROM Factura f ORDER BY f.idFactura", Factura.class)
                .getResultList();
        if (facturi != null && !this.facturi.isEmpty())
            Collections.sort(this.facturi, (f1, f2) -> f1.getIdFactura().compareTo(f2.getIdFactura()));

        this.factura.setItems(this.facturi);

        binder.forField(idLivrare).bind("idLivrare");
        binder.forField(dataLivrare).withConverter(new LocalDateToDateConverter()).bind("dataLivrare");
        binder.forField(docLivrare).bind("docLivrare");
        binder.forField(observatiiLivrare).bind("observatiiLivrare");
        binder.forField(factura).bind("factura");

        refreshForm();
    }

    private void initViewLayout(){
        factura.setItemLabelGenerator(f -> f.getIdFactura().toString());

        FormLayout formLayout = new FormLayout();
        formLayout.add(idLivrare, dataLivrare, docLivrare, observatiiLivrare, factura);
        formLayout.setResponsiveSteps(new ResponsiveStep("0", 1));
        formLayout.setMaxWidth("400px");

        HorizontalLayout actionToolbar = new HorizontalLayout(cmdAdaugare, cmdSterge, cmdAbandon, cmdSalveaza);
        actionToolbar.setPadding(false);

        this.formLayoutToolbar = new VerticalLayout(formLayout, actionToolbar);

        this.add(titluForm, formLayoutToolbar);
    }

    private void initControllerActions(){
        cmdAdaugare.addClickListener(e -> {
            adaugaLivrare();
            refreshForm();
        });
        cmdSterge.addClickListener(e -> {
            stergeLivrare();
            this.getUI().ifPresent(ui -> ui.navigate(NavigableGridLivrariView.class));
        });
        cmdAbandon.addClickListener(e -> {
            this.getUI().ifPresent(ui -> ui.navigate(NavigableGridLivrariView.class, this.livrare.getIdLivrare()));
        });
        cmdSalveaza.addClickListener(e -> {
            salveazaLivrare();
            //refreshForm();
            this.getUI().ifPresent(ui -> ui.navigate(NavigableGridLivrariView.class, this.livrare.getIdLivrare()));
        });
    }

    private void refreshForm(){
        System.out.println("Livrare curenta: " + this.livrare);
        if (this.livrare != null){
            binder.setBean(this.livrare);
        }
    }

    private void salveazaLivrare(){
        try{
            this.em.getTransaction().begin();
            this.livrare = this.em.merge(this.livrare);
            this.em.getTransaction().commit();
            System.out.println("Livrare Salvata");
        } catch (Exception ex){
            if (this.em.getTransaction().isActive())
                this.em.getTransaction().rollback();
            System.out.println("*** EntityManager Validation ex: " + ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    private void adaugaLivrare(){
        this.livrare = new Livrare();
        this.livrare.setIdLivrare(999);
        this.livrare.setDataLivrare(new Date());
        this.livrare.setDocLivrare("Aviz");
        this.livrare.setObservatiiLivrare("");
        this.livrare.setFactura(this.facturi.get(0));

        this.idLivrare.setEnabled(true);
    }

    private void stergeLivrare(){
        System.out.println("To remove: " + this.livrare);
        if (this.em.contains(this.livrare)){
            this.em.getTransaction().begin();
            this.em.remove(this.livrare);
            this.em.getTransaction().commit();
        }
    }

    public FormLivrareView(){
        initDataModel();
        initViewLayout();
        initControllerActions();
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        System.out.println("Livrare ID: " + id);
        if (id != null){
            this.livrare = em.find(Livrare.class, id);
            System.out.println("Selected livrare to edit: " + livrare);
            if (this.livrare == null){
                System.out.println("ADD livrare: " + livrare);

                this.adaugaLivrare();
                this.livrare.setIdLivrare(id);
            }
        }
        this.refreshForm();
    }
}
