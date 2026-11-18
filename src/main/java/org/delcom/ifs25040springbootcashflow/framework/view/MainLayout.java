package org.delcom.ifs25040springbootcashflow.framework.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import org.delcom.ifs25040springbootcashflow.domain.entity.Role;
import org.delcom.ifs25040springbootcashflow.framework.auth.AuthSession;

public class MainLayout extends AppLayout {
    public MainLayout() {
        var user=AuthSession.current();
        if(user==null){ UI.getCurrent().navigate("login"); return; }
        H2 title=new H2("Cashflow");
        Button home=new Button("Transaksi",e->UI.getCurrent().navigate(""));
        Button profile=new Button("Profil",e->UI.getCurrent().navigate("profile"));
        Button users=new Button("Pengguna",e->UI.getCurrent().navigate("users"));
        Button logout=new Button("Keluar",e->{AuthSession.logout();UI.getCurrent().getPage().setLocation("/login");});
        HorizontalLayout bar=new HorizontalLayout(title,home,profile);
        if(user.role()==Role.ADMIN) bar.add(users);
        bar.add(logout); bar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        bar.setWidthFull(); bar.expand(title); addToNavbar(bar);
    }
}
