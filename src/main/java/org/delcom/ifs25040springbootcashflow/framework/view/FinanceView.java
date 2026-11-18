package org.delcom.ifs25040springbootcashflow.framework.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.router.*;
import org.delcom.ifs25040springbootcashflow.adapter.presenter.FinancePresenter;
import org.delcom.ifs25040springbootcashflow.domain.entity.*;
import org.delcom.ifs25040springbootcashflow.framework.auth.AuthSession;
import org.delcom.ifs25040springbootcashflow.usecase.FinanceUseCase;
import java.time.format.DateTimeFormatter;

@Route("")
@PageTitle("Cashflow")
@RouteAlias("cashflow")
@Layout(MainLayout.class)
public class FinanceView extends VerticalLayout {
    private final FinanceUseCase finance;
    private final User user;
    private final Grid<Transaction> grid=new Grid<>(Transaction.class,false);
    private final FinancePresenter presenter=new FinancePresenter();
    private final Span income=new Span(), expense=new Span(), balance=new Span();
    private final TextField search=new TextField("Cari keterangan");
    private final ComboBox<SortOption> sort=new ComboBox<>("Urutkan");
    public FinanceView(FinanceUseCase finance) {
        this.finance=finance; this.user=AuthSession.current();
        setPadding(true); setSpacing(true); setMaxWidth("1200px"); getStyle().set("margin","0 auto");
        if(user==null){UI.getCurrent().navigate("login");return;}
        add(new H1("Catatan Keuangan"),new H3("Halo, "+user.name()));
        HorizontalLayout cards=new HorizontalLayout(card("Pemasukan",income),card("Pengeluaran",expense),card("Saldo",balance));
        cards.setWidthFull(); cards.setFlexGrow(1,cards.getComponentAt(0),cards.getComponentAt(1),cards.getComponentAt(2)); add(cards);
        TextField description=new TextField("Keterangan"); description.setWidth("300px");
        NumberField amount=new NumberField("Jumlah (Rupiah)"); amount.setMin(1); amount.setStep(1);
        ComboBox<TransactionType> type=new ComboBox<>("Jenis"); type.setItems(TransactionType.values()); type.setItemLabelGenerator(t->t==TransactionType.INCOME?"Pemasukan":"Pengeluaran");
        Button save=new Button("Simpan transaksi",e->{try{finance.add(user.id(),description.getValue(),amount.getValue().longValue(),type.getValue(),null);
            description.clear();amount.clear();refresh();}catch(Exception ex){show(ex.getMessage());}});
        add(new H3("Tambah transaksi"),new HorizontalLayout(description,amount,type,save));
        search.setClearButtonVisible(true); search.setValueChangeMode(com.vaadin.flow.data.value.ValueChangeMode.LAZY);
        sort.setItems(SortOption.values()); sort.setItemLabelGenerator(SortOption::label); sort.setValue(SortOption.AMOUNT_DESC);
        Button find=new Button("Terapkan",e->refresh());
        add(new HorizontalLayout(search,sort,find));
        grid.addColumn(Transaction::description).setHeader("Keterangan").setAutoWidth(true);
        grid.addColumn(t->t.type()==TransactionType.INCOME?"Pemasukan":"Pengeluaran").setHeader("Jenis");
        grid.addColumn(t->presenter.money(t.amount())).setHeader("Jumlah");
        grid.addColumn(t->t.createdAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))).setHeader("Tanggal");
        grid.addComponentColumn(t->new Button("Hapus",e->{try{finance.delete(t.id(),user.id(),false);refresh();}catch(Exception ex){show(ex.getMessage());}}));
        grid.setWidthFull(); add(new H3("Daftar transaksi"),grid); refresh();
    }
    private VerticalLayout card(String title,Span value) {
        VerticalLayout c=new VerticalLayout(new Span(title),value); c.getStyle().set("border","1px solid #ddd").set("border-radius","10px").set("padding","16px"); return c;
    }
    private void refresh() {
        if(user==null)return;
        long in=finance.income(user.id()), out=finance.expense(user.id());
        income.setText(presenter.money(in)); expense.setText(presenter.money(out)); balance.setText(presenter.money(in-out));
        grid.setItems(finance.list(user.id(),search.getValue(),sort.getValue()));
    }
    private void show(String message) { add(new NotificationLabel(message)); }
    private static class NotificationLabel extends Span { NotificationLabel(String text){super(text);getStyle().set("color","red");} }
}
