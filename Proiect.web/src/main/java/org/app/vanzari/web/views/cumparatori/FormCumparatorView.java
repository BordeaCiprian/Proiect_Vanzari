package org.app.vanzari.web.views.cumparatori;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H1;
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
import org.model.Cumparator;

@PageTitle("cumparator")
@Route(value = "cumparator", layout = MainView.class)
public class FormCumparatorView extends VerticalLayout implements HasUrlParameter<Integer> {
    private EntityManager em;
    private Cumparator cumparator = null;
    private Binder<Cumparator> binder = new BeanValidationBinder<>(Cumparator.class);

    private VerticalLayout formLayoutToolbar;
    private H1 titluForm = new H1("Form Cumparator");
    private IntegerField idCumparator = new IntegerField("ID cumparator:");
    private TextField denumire = new TextField("Denumire cumparator:");
    private TextField adresaCumparator = new TextField("Adresa cumparator:");

    private Button cmdAdaugare = new Button("Adauga");
    private Button cmdSterge = new Button("Sterge");
    private Button cmdAbandon = new Button("Abandon");
    private Button cmdSalveaza = new Button("Salveaza");

    private void initDataModel(){
        System.out.println("DEBUG START FORM>>> ");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProduseJPA");
        this.em = emf.createEntityManager();
        this.cumparator = em.createQuery("SELECT c FROM Cumparator c ORDER BY c.idCumparator", Cumparator.class)
                .getResultStream().findFirst().get();
        binder.forField(idCumparator).bind("idCumparator");
        binder.forField(denumire).bind("denumireCumparator");
        binder.forField(adresaCumparator).bind("adresaCumparator");

        refreshForm();
    }

    private void initViewLayout(){
        FormLayout formLayout = new FormLayout();
        formLayout.add(idCumparator, denumire, adresaCumparator);
        formLayout.setResponsiveSteps(new ResponsiveStep("0", 1));
        formLayout.setMaxWidth("400px");

        HorizontalLayout actionToolbar = new HorizontalLayout(cmdAdaugare, cmdSterge, cmdAbandon, cmdSalveaza);
        actionToolbar.setPadding(false);

        this.formLayoutToolbar = new VerticalLayout(formLayout, actionToolbar);

        this.add(titluForm, formLayoutToolbar);
    }

    private void initControllerActions(){
        cmdAdaugare.addClickListener(e -> {
            adaugaCumparator();
            refreshForm();
        });
        cmdSterge.addClickListener(e -> {
            stergeCumparator();
            this.getUI().ifPresent(ui -> ui.navigate(NavigableGridCumparatorView.class));
        });
        cmdAbandon.addClickListener(e -> {
            this.getUI().ifPresent(ui -> ui.navigate(NavigableGridCumparatorView.class, this.cumparator.getIdCumparator()));
        });
        cmdSalveaza.addClickListener(e -> {
            salveazaCumparator();
            //refreshForm();
            this.getUI().ifPresent(ui -> ui.navigate(NavigableGridCumparatorView.class, this.cumparator.getIdCumparator()));
        });
    }

    private void refreshForm(){
        System.out.println("Cumparator curent: " + this.cumparator);
        if (this.cumparator != null){
            binder.setBean(this.cumparator);
        }
    }

    private void salveazaCumparator(){
        try{
            this.em.getTransaction().begin();
            this.cumparator = this.em.merge(this.cumparator);
            this.em.getTransaction().commit();
            System.out.println("Cumparator Salvat");
        } catch (Exception ex){
            if (this.em.getTransaction().isActive())
                this.em.getTransaction().rollback();
            System.out.println("*** EntityManager Validation ex: " + ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }
    }

    private void adaugaCumparator(){
        this.cumparator = new Cumparator();
        this.cumparator.setIdCumparator(999);
        this.cumparator.setDenumireCumparator("Cumparator Nou");
        this.cumparator.setAdresaCumparator("Iasi");
    }

    private void stergeCumparator(){
        System.out.println("To remove: " + this.cumparator);
        if (this.em.contains(this.cumparator)){
            this.em.getTransaction().begin();
            this.em.remove(this.cumparator);
            this.em.getTransaction().commit();
        }
    }

    public FormCumparatorView(){
        initDataModel();
        initViewLayout();
        initControllerActions();
    }

    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Integer id) {
        System.out.println("Cumparator ID: " + id);
        if (id != null){
            this.cumparator = em.find(Cumparator.class, id);
            System.out.println("Selected cumparator to edit: " + cumparator);
            if (this.cumparator == null){
                System.out.println("ADD cumparator: " + cumparator);

                this.adaugaCumparator();
                this.cumparator.setIdCumparator(id);
                this.cumparator.setDenumireCumparator("Cumparator NOU " + id);
                this.cumparator.setAdresaCumparator("Iasi");
            }
        }
        this.refreshForm();
    }
}
