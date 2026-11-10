package org.app.vanzari.web.views.produse;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.app.vanzari.web.MainView;
import org.model.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.stream.Stream;


@PageTitle("produs")
@Route(value = "produs", layout = MainView.class)
public class FormProdusView extends VerticalLayout implements HasUrlParameter<Integer> {
    private EntityManager em;
    private Produs produs = null;
    private Imbracaminte imbracaminte = null;
    private Incaltaminte incaltaminte = null;
    private Accesoriu accesoriu = null;

    private Binder<Produs> binderProdus = new BeanValidationBinder<>(Produs.class);
    private Binder<Imbracaminte> binderImbracaminte = new BeanValidationBinder<>(Imbracaminte.class);
    private Binder<Incaltaminte> binderIncaltaminte = new BeanValidationBinder<>(Incaltaminte.class);
    private Binder<Accesoriu> binderAccesoriu = new BeanValidationBinder<>(Accesoriu.class);

    private VerticalLayout formLayoutToolbar;
    private H1 titluForm = new H1("Form Produs");
    private IntegerField idProdus = new IntegerField("ID produs:");
    private TextField denumireProdus = new TextField("Denumire produs:");
    private TextField umProdus = new TextField("Unitate de masura produs:");

    private TextField material = new TextField("Material produs:");
    private ComboBox marime = new ComboBox("Marime produs:");
    private ComboBox genPurtator = new ComboBox("gen Purtator produs:");
    private ComboBox categorieVarstaPurtator = new ComboBox("Categorie Varsta Purtator produs:");

    private ComboBox dimensiune = new ComboBox("Dimensiune produs:");
    private TextField materialIncaltaminte = new TextField("Material produs:");
    private ComboBox genPurtatorIncaltaminte = new ComboBox("Gen Purtator produs:");

    private TextField brand = new TextField("Brand produs:");

    private ComboBox<String> categorieProdus = new ComboBox<>("Categorie produs");

    private Button cmdAdaugare = new Button("Adauga");
    private Button cmdSterge = new Button("Sterge");
    private Button cmdAbandon = new Button("Abandon");
    private Button cmdSalveaza = new Button("Salveaza");

    public FormProdusView(){
        initDataModel();
        initViewLayout();
        initControllerActions();
    }

    private void initDataModel(){
        System.out.println("DEBUG START FORM>>> ");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProduseJPA");
        this.em = emf.createEntityManager();
        this.produs = em.createQuery("SELECT p FROM Produs p ORDER BY p.idProdus", Produs.class)
                .getResultStream().findFirst().get();

        this.imbracaminte = em.createQuery("SELECT i FROM Imbracaminte i ORDER BY i.idProdus", Imbracaminte.class)
                .getResultStream().findFirst().get();

        this.incaltaminte = em.createQuery("SELECT i FROM Incaltaminte i ORDER BY i.idProdus", Incaltaminte.class)
                .getResultStream().findFirst().get();

        this.accesoriu = em.createQuery("SELECT a FROM Accesoriu a ORDER BY a.idProdus", Accesoriu.class)
                .getResultStream().findFirst().get();

        binderProdus.forField(idProdus).bind("idProdus");
        binderProdus.forField(denumireProdus).bind("denumire");
        binderProdus.forField(umProdus).bind("UM");

        categorieProdus.setClearButtonVisible(true);
        categorieProdus.setItems("Imbracaminte", "Incaltaminte", "Accesoriu");

        marime.setClearButtonVisible(true);
        marime.setItems("S", "M", "L");

        genPurtator.setClearButtonVisible(true);
        genPurtator.setItems("Mas", "Fem", "Universal");

        categorieVarstaPurtator.setClearButtonVisible(true);
        categorieVarstaPurtator.setItems("copil", "adult", "varstnic", "universal");
        //
        dimensiune.setClearButtonVisible(true);
        dimensiune.setItems("S", "M", "L");

        genPurtatorIncaltaminte.setClearButtonVisible(true);
        genPurtatorIncaltaminte.setItems("Mas", "Fem", "Universal");
        //

        binderImbracaminte.forField(material).bind("material");
        binderImbracaminte.forField(marime).bind("marime");
        binderImbracaminte.forField(genPurtator).bind("genPurtator");
        binderImbracaminte.forField(categorieVarstaPurtator).bind("categorieVarstaPurtator");

        binderIncaltaminte.forField(dimensiune).bind("dimensiune");
        binderIncaltaminte.forField(materialIncaltaminte).bind("material");
        binderIncaltaminte.forField(genPurtatorIncaltaminte).bind("genPurtator");

        binderAccesoriu.forField(brand).bind("brand");

        if (categorieProdus.getValue() != null) {
            if (categorieProdus.getValue().equals("Imbracaminte"))
                this.produs = this.imbracaminte;
            if (categorieProdus.getValue().equals("Incaltaminte"))
                this.produs = this.incaltaminte;
            if (categorieProdus.getValue().equals("Accesoriu"))
                this.produs = this.accesoriu;
        }

        refreshForm();
    }

    private void initViewLayout(){

        FormLayout formLayout = new FormLayout();
        formLayout.add(idProdus, denumireProdus, umProdus, categorieProdus);
        formLayout.setResponsiveSteps(new ResponsiveStep("0", 1));
        formLayout.setMaxWidth("400px");

        HorizontalLayout actionToolbar = new HorizontalLayout(cmdAdaugare, cmdSterge, cmdAbandon, cmdSalveaza);
        actionToolbar.setPadding(false);
        if (formLayoutToolbar != null) {
            this.remove(formLayoutToolbar);
        }

        if (categorieProdus.getValue() != null) {
            if (categorieProdus.getValue().equals("Imbracaminte")) {

                formLayout.add(material, marime, genPurtator, categorieVarstaPurtator);
            }
            if (categorieProdus.getValue().equals("Incaltaminte")) {

                formLayout.add(dimensiune, materialIncaltaminte, genPurtatorIncaltaminte);
            }
            if (categorieProdus.getValue().equals("Accesoriu")) {

                formLayout.add(brand);
            }
        }
        else {

            this.add(titluForm);
        }

        this.formLayoutToolbar = new VerticalLayout(formLayout, actionToolbar);

        this.add(formLayoutToolbar);

        this.idProdus.setEnabled(false);
    }

    private void initControllerActions(){
        categorieProdus.addValueChangeListener(e -> {
            if (categorieProdus.getValue() == null) {
                this.remove(titluForm);
                this.remove(formLayoutToolbar);
            }
            //initDataModel();
            initViewLayout();
        });

        cmdAdaugare.addClickListener(e -> {
            adaugaProdus();
            refreshForm();
        });
        cmdSterge.addClickListener(e -> {
            stergeProdus();
            this.getUI().ifPresent(ui -> ui.navigate(NavigableGridProduseView.class));
        });
        cmdAbandon.addClickListener(e -> {
            this.getUI().ifPresent(ui -> ui.navigate(NavigableGridProduseView.class, this.produs.getIdProdus()));
        });
        cmdSalveaza.addClickListener(e -> {
            salveazaProdus();
            //refreshForm();
            this.getUI().ifPresent(ui -> ui.navigate(NavigableGridProduseView.class, this.produs.getIdProdus()));

            Notification notification = new Notification();
            Timer timer = new Timer();
            notification.add("Produs Salvat");
            notification.open();
            notification.setDuration(10000);
        });
    }

    private void refreshForm(){
        System.out.println("Produs curent: " + this.produs);
        if (this.produs != null){
            binderProdus.setBean(produs);
            binderImbracaminte.setBean(imbracaminte);
            binderIncaltaminte.setBean(incaltaminte);
            binderAccesoriu.setBean(accesoriu);
        }
    }

    private void salveazaProdus(){
        try{
            this.em.getTransaction().begin();
            this.produs = this.em.merge(this.produs);
            this.em.getTransaction().commit();
            System.out.println("Produs Salvat");
        } catch (Exception ex){
            if (this.em.getTransaction().isActive())
                this.em.getTransaction().rollback();
            System.out.println("*** EntityManager Validation ex: " + ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    private void adaugaProdus(){
        this.produs = new ImbProtectie();
        this.produs.setIdProdus(999);
        this.produs.setDenumire("Produs NOU");
        this.produs.setUM("Bucati");

        this.idProdus.setEnabled(true);
    }

    private void stergeProdus(){
        System.out.println("To remove: " + this.produs);
        if (this.em.contains(this.produs)){
            this.em.getTransaction().begin();
            this.em.remove(this.produs);
            this.em.getTransaction().commit();
        }
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        System.out.println("Produs ID: " + id);
        if (id != null){
            this.produs = em.find(Produs.class, id);
            this.imbracaminte = em.find(Imbracaminte.class, id);
            this.incaltaminte = em.find(Incaltaminte.class, id);
            this.accesoriu = em.find(Accesoriu.class, id);
            System.out.println("Selected produs to edit: " + produs);
            if (this.produs == null){
                System.out.println("ADD produs: " + produs);

                this.adaugaProdus();
                this.produs.setIdProdus(id);
            }
        }
        this.refreshForm();
    }
}
