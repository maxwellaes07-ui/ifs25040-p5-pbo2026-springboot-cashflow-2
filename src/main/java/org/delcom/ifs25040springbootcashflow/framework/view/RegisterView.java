package org.delcom.ifs25040springbootcashflow.framework.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.router.*;
import org.delcom.ifs25040springbootcashflow.framework.auth.AuthSession;
import org.delcom.ifs25040springbootcashflow.usecase.AuthUseCase;

@Route("register")
public class RegisterView extends VerticalLayout {
    public RegisterView(AuthUseCase auth) {
        setSizeFull(); setAlignItems(Alignment.CENTER); setJustifyContentMode(JustifyContentMode.CENTER);
        TextField name=new TextField("Nama"); name.setWidthFull();
        EmailField email=new EmailField("Email"); email.setWidthFull();
        PasswordField password=new PasswordField("Password (min. 6 karakter)"); password.setWidthFull();
        Span message=new Span();
        Button save=new Button("Daftar",e->{try{AuthSession.login(auth.register(name.getValue(),email.getValue(),password.getValue()));UI.getCurrent().navigate("");}
            catch(RuntimeException ex){message.setText(ex.getMessage());}});
        Button back=new Button("Kembali ke login",e->UI.getCurrent().navigate("login"));
        add(new H1("Registrasi Cashflow"),name,email,password,new HorizontalLayout(save,back),message);
        setMaxWidth("420px"); getStyle().set("margin","auto");
        if(AuthSession.current()!=null) UI.getCurrent().navigate("");
    }
}
