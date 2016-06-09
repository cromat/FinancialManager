package com.example.mat.financialmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.mat.financialmanager.activity.fund.AddEditFundActivity;
import com.example.mat.financialmanager.activity.fund.FundListActivity;
import com.example.mat.financialmanager.activity.invoice.AddEditInvoiceActivity;
import com.example.mat.financialmanager.activity.invoice.MainActivity;
import com.example.mat.financialmanager.activity.share.AddEditShareActivity;
import com.example.mat.financialmanager.activity.share.ShareListActivity;
import com.example.mat.financialmanager.activity.tax.AddEditTaxActivity;
import com.example.mat.financialmanager.activity.tax.TaxListActivity;
import com.example.mat.financialmanager.enums.FundTypes;
import com.example.mat.financialmanager.model.TermSaving;

/**
 * Created by mat on 28.05.16..
 */
public class AppConfig {

    public static final String PARSE_LINK = "http://finmgr-cromat.rhcloud.com/parse/";
    public static final String APP_ID = "finmgr";
    public static final String CURRENCY_API_BASE_HRK = "http://api.fixer.io/latest?base=HRK";
    public static final String PREF_DEFAULT_CURRENCY = "pref_check_default_currency";
    public static final String PREF_CURRENCY = "pref_currency";
    public static final String NULL = "null";
    public static final String FUND_TYPE = "fund_type";

    public static Intent getStartingActivity(int id, Context context) {

        if (id == R.id.nav_invoices) {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("name", MainActivity.class.getName());
            return intent;
        }

        else if (id == R.id.nav_shares) {
            Intent intent = new Intent(context, ShareListActivity.class);
            intent.putExtra("name", ShareListActivity.class.getName());
            return intent;
        }

        else if (id == R.id.nav_mutual_funds) {
            Intent intent = new Intent(context, FundListActivity.class);
            intent.putExtra(AppConfig.FUND_TYPE.toString(), FundTypes.MUTUAL_FUND.toString());
            intent.putExtra("name", FundListActivity.class.getName());
            return intent;
        }

        else if (id == R.id.nav_pension_funds) {
            Intent intent = new Intent(context, FundListActivity.class);
            intent.putExtra(AppConfig.FUND_TYPE.toString(), FundTypes.PENSION_FUND.toString());
            intent.putExtra("name", FundListActivity.class.getName());
            return intent;
        }

        else if (id == R.id.nav_savings) {
            Intent intent = new Intent(context, FundListActivity.class);
            intent.putExtra(AppConfig.FUND_TYPE.toString(), FundTypes.TERM_SAVING.toString());
            intent.putExtra("name", FundListActivity.class.getName());
            return intent;
        }

        else if (id == R.id.nav_taxes) {
            Intent intent = new Intent(context, TaxListActivity.class);
            intent.putExtra("name", TaxListActivity.class.getName());
            return intent;
        }


        else if (id == R.id.nav_add_invoices) {
            Intent intent = new Intent(context, AddEditInvoiceActivity.class);
            intent.putExtra("name", AddEditInvoiceActivity.class.getName());
            return intent;
        }

        else if (id == R.id.nav_add_fund) {
            Intent intent = new Intent(context, AddEditFundActivity.class);
            intent.putExtra("name", AddEditFundActivity.class.getName());
            return intent;
        }

        else if (id == R.id.action_add_share) {
            Intent intent = new Intent(context, AddEditShareActivity.class);
            intent.putExtra("name", AddEditShareActivity.class.getName());
            return intent;
        }

        else if (id == R.id.action_add_tax) {
            Intent intent = new Intent(context, AddEditTaxActivity.class);
            intent.putExtra("name", AddEditTaxActivity.class.getName());
            return intent;
        }

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("name", MainActivity.class.getName());
        return intent;

    }
}
