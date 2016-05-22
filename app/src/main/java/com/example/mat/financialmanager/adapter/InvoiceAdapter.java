package com.example.mat.financialmanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mat.financialmanager.R;
import com.example.mat.financialmanager.model.Invoice;

import java.util.Date;
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
    public void onBindViewHolder(InvoiceViewHolder ivh, int i) {
        ivh.name.setText(invoices.get(i).getName());
        ivh.invoiceNumber.setText(invoices.get(i).getInvoiceNumber());
        ivh.cardExpiry.setText(invoices.get(i).getCardExpiry().toString());
        ivh.balance.setText(Double.toString(invoices.get(i).getBalance()));
        ivh.currency.setText(invoices.get(i).getCurrency());
//        ivh.cardType.setText(invoices.get(i).getName());
    }

    public static class InvoiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public TextView invoiceNumber;
        public TextView cardExpiry;
        public TextView balance;
        public TextView currency;
        public ImageView cardType;
        public Context context;

        public InvoiceViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            name = (TextView)view.findViewById(R.id.item_text_invoice_name);
            invoiceNumber = (TextView)view.findViewById(R.id.item_text_invoice_number);
            cardExpiry = (TextView) view.findViewById(R.id.item_card_expiry);
            balance = (TextView)view.findViewById(R.id.item_text_balance);
            currency = (TextView)view.findViewById(R.id.item_text_currency);
            cardType = (ImageView)view.findViewById(R.id.item_image_card_type);
            context = itemView.getContext();
        }

        @Override
        public void onClick(View v) {

        }

    }
}