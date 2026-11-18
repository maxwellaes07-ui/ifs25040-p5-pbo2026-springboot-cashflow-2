package org.delcom.ifs25040springbootcashflow.framework.auth;

import com.vaadin.flow.server.VaadinSession;
import org.delcom.ifs25040springbootcashflow.domain.entity.User;

public final class AuthSession {
    private static final String KEY="CASHFLOW_USER";
    private AuthSession() {}
    public static void login(User user) { VaadinSession.getCurrent().setAttribute(KEY,user); }
    public static User current() { return VaadinSession.getCurrent()==null?null:(User)VaadinSession.getCurrent().getAttribute(KEY); }
    public static void logout() { if(VaadinSession.getCurrent()!=null) VaadinSession.getCurrent().setAttribute(KEY,null); }
}
