package org.delcom.ifs25040springbootcashflow.adapter.presenter;

import java.text.NumberFormat;
import java.util.Locale;

public class FinancePresenter {
    private static final NumberFormat IDR=NumberFormat.getCurrencyInstance(new Locale("id","ID"));
    public String money(long amount) { return IDR.format(amount); }
}
