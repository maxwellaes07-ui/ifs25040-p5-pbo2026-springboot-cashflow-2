package org.delcom.ifs25040springbootcashflow.framework.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.*;
import org.delcom.ifs25040springbootcashflow.domain.entity.Role;
import org.delcom.ifs25040springbootcashflow.framework.auth.AuthSession;
import org.delcom.ifs25040springbootcashflow.usecase.UserUseCase;

@Route("users") @Layout(MainLayout.class) @PageTitle("Pengguna")
public class UsersView extends com.vaadin.flow.component.orderedlayout.VerticalLayout {
    public UsersView(UserUseCase users) {
        var current=AuthSession.current();
        if(current==null){UI.getCurrent().navigate("login");return;}
        if(current.role()!=Role.ADMIN){UI.getCurrent().navigate("");return;}
        Grid<org.delcom.ifs25040springbootcashflow.domain.entity.User> grid=new Grid<>(org.delcom.ifs25040springbootcashflow.domain.entity.User.class,false);
        grid.addColumn(org.delcom.ifs25040springbootcashflow.domain.entity.User::name).setHeader("Nama");
        grid.addColumn(org.delcom.ifs25040springbootcashflow.domain.entity.User::email).setHeader("Email");
        grid.addColumn(u->u.role().name()).setHeader("Role");
        grid.setItems(users.all());grid.setWidthFull();add(new H1("Daftar Pengguna"),grid);
    }
}
