package org.app.vanzari.web;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLayout;
import org.app.vanzari.web.views.cumparatori.FormCumparatorView;
import org.app.vanzari.web.views.facturi.FormFacturaView;
import org.app.vanzari.web.views.cumparatori.NavigableGridCumparatorView;
import org.app.vanzari.web.views.facturi.NavigableGridFacturiView;
import org.app.vanzari.web.views.incasari.FormIncasareView;
import org.app.vanzari.web.views.incasari.NavigableGridIncasariView;
import org.app.vanzari.web.views.livrari.FormLivrareView;
import org.app.vanzari.web.views.livrari.NavigableGridLivrariView;
import org.app.vanzari.web.views.produse.FormProdusView;
import org.app.vanzari.web.views.produse.NavigableGridProduseView;

@Route
public class MainView extends VerticalLayout implements RouterLayout {
    public MainView() {
        setMenuBar();
    }

    private void setMenuBar(){
        MenuBar mainMenu = new MenuBar();
        MenuItem homeMenu = mainMenu.addItem("Home");
        homeMenu.addClickListener(event -> UI.getCurrent().navigate(MainView.class));

        MenuItem gridFormsCumparatoriMenu = mainMenu.addItem("Cumparator");
        SubMenu gridFormsCumparatoriMenuBar = gridFormsCumparatoriMenu.getSubMenu();
        gridFormsCumparatoriMenuBar.addItem("Lista Cumparatori...", event -> UI.getCurrent().navigate(NavigableGridCumparatorView.class));
        gridFormsCumparatoriMenuBar.addItem("Form Editare Cumparator...", event -> UI.getCurrent().navigate(FormCumparatorView.class));

        MenuItem gridFormsFacturiMenu = mainMenu.addItem("Facturi");
        SubMenu gridFormsFacturiMenuBar = gridFormsFacturiMenu.getSubMenu();
        gridFormsFacturiMenuBar.addItem("Lista Facturi...", event -> UI.getCurrent().navigate(NavigableGridFacturiView.class));
        gridFormsFacturiMenuBar.addItem("Form Editare Factura...", event -> UI.getCurrent().navigate(FormFacturaView.class));
        
        MenuItem gridFormsProduseMenu = mainMenu.addItem("Produse");
        SubMenu gridFormsProduseMenuBar = gridFormsProduseMenu.getSubMenu();
        gridFormsProduseMenuBar.addItem("Lista Produse...", event -> UI.getCurrent().navigate(NavigableGridProduseView.class));
        gridFormsProduseMenuBar.addItem("Form Editare Produse...", event -> UI.getCurrent().navigate(FormProdusView.class));

        MenuItem gridFormsIncasariMenu = mainMenu.addItem("Incasari");
        SubMenu gridFormsIncasariMenuBar = gridFormsIncasariMenu.getSubMenu();
        gridFormsIncasariMenuBar.addItem("Lista Incasari...", event -> UI.getCurrent().navigate(NavigableGridIncasariView.class));
        gridFormsIncasariMenuBar.addItem("Form Editare Incasare...", event -> UI.getCurrent().navigate(FormIncasareView.class));

        MenuItem gridFormsLivrariMenu = mainMenu.addItem("Livrari");
        SubMenu gridFormsLivrariMenuBar = gridFormsLivrariMenu.getSubMenu();
        gridFormsLivrariMenuBar.addItem("Lista Livrari...", event -> UI.getCurrent().navigate(NavigableGridLivrariView.class));
        gridFormsLivrariMenuBar.addItem("Form Editare Livrare...", event -> UI.getCurrent().navigate(FormLivrareView.class));


        add(new HorizontalLayout(mainMenu));
    }

}
