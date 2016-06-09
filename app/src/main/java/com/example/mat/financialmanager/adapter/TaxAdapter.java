package com.example.mat.financialmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mat.financialmanager.DecimalDigitsInputFilter;
import com.example.mat.financialmanager.R;
import com.example.mat.financialmanager.activity.tax.TaxDetailsActivity;
import com.example.mat.financialmanager.model.Tax;

import java.util.List;

/**
 * Created by mat on 09.06.16..
 */
public class TaxAdapter extends RecyclerView.Adapter<TaxAdapter.TaxViewHolder> {

    private static List<Tax> taxes;

    @Override
    public int getItemCount() {
        return taxes.size();
    }

    @Override
    public TaxAdapter.TaxViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tax, parent, false);
        return new TaxViewHolder(v);
    }

    public TaxAdapter (List<Tax> list) {
        this.taxes = list;

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(TaxViewHolder ivh, int i) {
        ivh.name.setText(taxes.get(i).getName());
        ivh.company.setText(taxes.get(i).getCompany());
        ivh.dateIssue.setText(taxes.get(i).getCroDateIssue());
        ivh.dateDue.setText(taxes.get(i).getCroDateDue());
        ivh.value.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});
        ivh.value.setText(Double.toString(taxes.get(i).getValue()));
        ivh.currency.setText(taxes.get(i).getCurrency());
        ivh.invoiceNumber.setText(taxes.get(i).getInvoiceNumber());
    }

    public static class TaxViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name;
        private TextView company;
        private TextView dateIssue;
        private TextView dateDue;
        private TextView value;
        private TextView currency;
        private TextView invoiceNumber;
        public Context context;

        public TaxViewHolder(View view) {
            super(view);
            name = (TextView)view.findViewById( R.id.item_text_tax_name );
            company = (TextView)view.findViewById( R.id.item_text_tax_company );
            dateIssue = (TextView)view.findViewById( R.id.item_text_tax_date_issue );
            dateDue = (TextView)view.findViewById( R.id.item_text_tax_date_due );
            value = (TextView)view.findViewById( R.id.item_tax_text_value );
            currency = (TextView)view.findViewById( R.id.item_tax_text_currency );
            context = itemView.getContext();
        }


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), TaxDetailsActivity.class);
            intent.putExtra("tax",taxes.get(getAdapterPosition()));
            context.startActivity(intent);
        }

    }
}
