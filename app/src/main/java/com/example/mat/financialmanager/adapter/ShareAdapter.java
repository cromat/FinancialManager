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
import android.widget.TextView;

import com.example.mat.financialmanager.AppConfig;
import com.example.mat.financialmanager.DecimalDigitsInputFilter;
import com.example.mat.financialmanager.R;
import com.example.mat.financialmanager.activity.share.ShareDetailsActivity;
import com.example.mat.financialmanager.enums.Currencies;
import com.example.mat.financialmanager.model.Share;
import com.example.mat.financialmanager.sqlite.SQLiteHelper;

import java.util.List;

/**
 * Created by mat on 05.06.16..
 */
public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ShareViewHolder> {

    private static List<Share> shares;

    @Override
    public int getItemCount() {
        return shares.size();
    }

    @Override
    public ShareAdapter.ShareViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_share, parent, false);
        return new ShareViewHolder(v);
    }

    public ShareAdapter (List<Share> list) {
        this.shares = list;

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(ShareViewHolder ivh, int i) {
        ivh.name.setText(shares.get(i).getName());
        ivh.company.setText(shares.get(i).getCompany());
        ivh.quantity.setText(Integer.toString(shares.get(i).getQuantity()));
        ivh.dateBought.setText(shares.get(i).getStringCroDate());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ivh.context);
        boolean useDefaultCurrency = prefs.getBoolean(AppConfig.PREF_DEFAULT_CURRENCY, false);

        ivh.value.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});

        if (useDefaultCurrency){
            String defaultCurr = prefs.getString(AppConfig.PREF_CURRENCY, Currencies.HRK.toString());
            SQLiteHelper dbCurr = new SQLiteHelper(ivh.context);
            try {
                dbCurr.getWritableDatabase();
            }
            catch (IllegalStateException e){
                e.printStackTrace();
            }
            double balanceRecalc = dbCurr.getFromTo(shares.get(i).getCurrency(), defaultCurr, shares.get(i).getValue());

            ivh.value.setText(Double.toString(balanceRecalc));
            ivh.currency.setText(defaultCurr);
        }
        else {
            ivh.value.setText(Double.toString(shares.get(i).getValue()));
            ivh.currency.setText(shares.get(i).getCurrency());
        }
    }

    public static class ShareViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name;
        private TextView company;
        private TextView quantity;
        private TextView dateBought;
        private TextView value;
        private TextView currency;
        public Context context;

        public ShareViewHolder(View view) {
            super(view);
            name = (TextView)view.findViewById( R.id.item_text_share_name );
            company = (TextView)view.findViewById( R.id.item_text_share_company );
            quantity = (TextView)view.findViewById( R.id.item_text_share_quantity );
            dateBought = (TextView)view.findViewById( R.id.item_text_share_date_bought );
            value = (TextView)view.findViewById( R.id.item_share_text_value );
            currency = (TextView)view.findViewById( R.id.item_share_text_currency );
            context = itemView.getContext();
        }


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ShareDetailsActivity.class);
            intent.putExtra("share",shares.get(getAdapterPosition()));
            context.startActivity(intent);
        }

    }
}
