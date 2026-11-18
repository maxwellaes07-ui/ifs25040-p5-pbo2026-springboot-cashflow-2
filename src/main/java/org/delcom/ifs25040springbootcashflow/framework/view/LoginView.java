package org.delcom.ifs25040springbootcashflow.framework.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.router.*;
import org.delcom.ifs25040springbootcashflow.framework.auth.AuthSession;
import org.delcom.ifs25040springbootcashflow.usecase.AuthUseCase;

@Route("login")
public class LoginView extends VerticalLayout {
    public LoginView(AuthUseCase auth) {
        setSizeFull(); setAlignItems(Alignment.CENTER); setJustifyContentMode(JustifyContentMode.CENTER);
        TextField email=new TextField("Email"); email.setWidthFull();
        PasswordField password=new PasswordField("Password"); password.setWidthFull();
        Span message=new Span();
        Button login=new Button("Masuk",e->{try{AuthSession.login(auth.login(email.getValue(),password.getValue())); UI.getCurrent().navigate("");}
            catch(RuntimeException ex){message.setText(ex.getMessage());}});
        Button register=new Button("Buat akun",e->UI.getCurrent().navigate("register"));
        add(new H1("Cashflow"),new H3("Masuk ke akun"),email,password,new HorizontalLayout(login,register),message);
        setMaxWidth("420px"); getStyle().set("margin","auto");
        if(AuthSession.current()!=null) UI.getCurrent().navigate("");
    }
}
