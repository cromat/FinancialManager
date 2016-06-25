package com.example.mat.financialmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mat.financialmanager.AppConfig;
import com.example.mat.financialmanager.R;
import com.example.mat.financialmanager.activity.invoice.InvoiceDetailsActivity;
import com.example.mat.financialmanager.enums.Currencies;
import com.example.mat.financialmanager.model.Invoice;
import com.example.mat.financialmanager.sqlite.SQLiteHelper;

import java.util.List;

/**
 * Created by mat on 03.04.16..
 */
public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.InvoiceViewHolder> {

    private static List<Invoice> invoices;

    @Override
    public int getItemCount() {
        return invoices.size();
    }

    @Override
    public InvoiceAdapter.InvoiceViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invoice, parent, false);
        return new InvoiceViewHolder(v);
    }

    public InvoiceAdapter (List<Invoice> list) {
        this.invoices = list;

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(InvoiceViewHolder ivh, int i) {
        ivh.name.setText(invoices.get(i).getName());
        ivh.invoiceNumber.setText(invoices.get(i).getInvoiceNumber());
        ivh.cardExpiry.setText(invoices.get(i).getCardExpiryMonth() + " " + invoices.get(i).getCardExpiryYear());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ivh.context);
        boolean useDefaultCurrency = prefs.getBoolean(AppConfig.PREF_DEFAULT_CURRENCY, false);


        if (useDefaultCurrency){
            String defaultCurr = prefs.getString(AppConfig.PREF_CURRENCY, Currencies.HRK.toString());
            SQLiteHelper dbCurr = new SQLiteHelper(ivh.context);
            try {
                dbCurr.getWritableDatabase();
            }
            catch (IllegalStateException e){
                e.printStackTrace();
            }            double balanceRecalc = dbCurr.getFromTo(invoices.get(i).getCurrency(), defaultCurr, invoices.get(i).getBalance());

            ivh.balance.setText(String.format("%.2f", balanceRecalc));
            ivh.currency.setText(defaultCurr);
        }
        else {
            ivh.balance.setText(String.format("%.2f",invoices.get(i).getBalance()));
            ivh.currency.setText(invoices.get(i).getCurrency());
        }
        ivh.bank.setText(invoices.get(i).getBank());
        ivh.cardType.setImageDrawable(Invoice.getCardImage(invoices.get(i).getCardType().toString(),ivh.context));
    }

    public static class InvoiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public TextView invoiceNumber;
        public TextView cardExpiry;
        public TextView balance;
        public TextView currency;
        public ImageView cardType;
        public TextView bank;
        public Context context;

        public InvoiceViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            name = (TextView)view.findViewById(R.id.item_text_invoice_name);
            invoiceNumber = (TextView)view.findViewById(R.id.item_text_invoice_number);
            cardExpiry = (TextView) view.findViewById(R.id.item_card_expiry);
            balance = (TextView)view.findViewById(R.id.item_text_balance);
            currency = (TextView)view.findViewById(R.id.item_text_currency);
            bank = (TextView)view.findViewById(R.id.item_text_invoice_bank);
            cardType = (ImageView)view.findViewById(R.id.item_image_card_type);
            context = itemView.getContext();
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), InvoiceDetailsActivity.class);
            intent.putExtra("invoice",invoices.get(getAdapterPosition()));
            context.startActivity(intent);
        }

    }
}