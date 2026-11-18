package org.delcom.ifs25040springbootcashflow.framework.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.router.*;
import org.delcom.ifs25040springbootcashflow.framework.auth.AuthSession;
import org.delcom.ifs25040springbootcashflow.usecase.UserUseCase;

@Route("profile") @Layout(MainLayout.class) @PageTitle("Profil")
public class ProfileView extends VerticalLayout {
    public ProfileView(UserUseCase users) {
        var user=AuthSession.current(); if(user==null){UI.getCurrent().navigate("login");return;}
        TextField name=new TextField("Nama");name.setValue(user.name());
        TextField email=new TextField("Email");email.setValue(user.email());email.setReadOnly(true);
        PasswordField password=new PasswordField("Password baru (opsional)");
        Span msg=new Span();
        add(new H1("Profil Pengguna"),name,email,password,new Button("Simpan profil",e->{try{
            var updated=users.update(user.id(),name.getValue(),password.getValue(),null);AuthSession.login(updated);msg.setText("Profil berhasil diperbarui.");
        }catch(Exception ex){msg.setText(ex.getMessage());}}),msg);
    }
}
