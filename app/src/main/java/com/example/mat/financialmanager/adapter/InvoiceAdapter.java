package com.example.mat.financialmanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mat.financialmanager.R;
import com.example.mat.financialmanager.model.Invoice;

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

    @Override
    public void onBindViewHolder(InvoiceViewHolder ivh, int i) {

    }


    public static class InvoiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Context context;

        public InvoiceViewHolder(View view) {
            super(view);

            view.setOnClickListener(this);

            context = itemView.getContext();
        }

        @Override
        public void onClick(View v) {

        }

    }
}