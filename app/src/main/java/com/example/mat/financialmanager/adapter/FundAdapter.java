package com.example.mat.financialmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mat.financialmanager.AppConfig;
import com.example.mat.financialmanager.DecimalDigitsInputFilter;
import com.example.mat.financialmanager.R;
import com.example.mat.financialmanager.activity.fund.FundDetailsActivity;
import com.example.mat.financialmanager.enums.Currencies;
import com.example.mat.financialmanager.model.Fund;
import com.example.mat.financialmanager.sqlite.SQLiteCurrencies;

import java.util.Date;
import java.util.List;

/**
 * Created by mat on 03.04.16..
 */
public class FundAdapter extends RecyclerView.Adapter<FundAdapter.FundViewHolder> {

    private static List<Fund> funds;

    @Override
    public int getItemCount() {
        return funds.size();
    }

    @Override
    public FundAdapter.FundViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fund, parent, false);
        return new FundViewHolder(v);
    }

    public FundAdapter (List<Fund> list) {
        this.funds = list;

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(FundViewHolder ivh, int i) {
        ivh.name.setText(funds.get(i).getName());
        ivh.dateDue.setText(funds.get(i).getStringCroDate());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ivh.context);
        boolean useDefaultCurrency = prefs.getBoolean(AppConfig.PREF_DEFAULT_CURRENCY, false);

        ivh.value.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});

        if (useDefaultCurrency){
            String defaultCurr = prefs.getString(AppConfig.PREF_CURRENCY, Currencies.HRK.toString());
            SQLiteCurrencies dbCurr = new SQLiteCurrencies(ivh.context);
            double balanceRecalc = dbCurr.getFromTo(funds.get(i).getCurrency(), defaultCurr, funds.get(i).getValue());

            ivh.value.setText(Double.toString(balanceRecalc));
            ivh.currency.setText(defaultCurr);
        }

        else {
            ivh.value.setText(Double.toString(funds.get(i).getValue()));
            ivh.currency.setText(funds.get(i).getCurrency());
        }

        ivh.bank.setText(funds.get(i).getBank());

        ivh.fundType.setImageDrawable(funds.get(i).getfundImage(ivh.context));
    }

    public static class FundViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public TextView value;
        public TextView dateDue;
        public TextView currency;
        public TextView bank;
        public ImageView fundType;
        public Context context;

        public FundViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            name = (TextView)view.findViewById(R.id.item_text_fund_name);
            value = (TextView)view.findViewById(R.id.item_fund_text_value);
            dateDue = (TextView) view.findViewById(R.id.item_text_fund_date_due);
            currency = (TextView)view.findViewById(R.id.item_fund_text_currency);
            bank = (TextView)view.findViewById(R.id.item_text_fund_bank);
            fundType = (ImageView)view.findViewById(R.id.item_image_fund_type);
            context = itemView.getContext();
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), FundDetailsActivity.class);
            intent.putExtra("fund",funds.get(getAdapterPosition()));
            context.startActivity(intent);
        }

    }
}