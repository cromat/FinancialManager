package com.example.mat.financialmanager.activity.fund;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mat.financialmanager.R;
import com.example.mat.financialmanager.enums.FundTypes;
import com.example.mat.financialmanager.model.Fund;
import com.example.mat.financialmanager.model.PensionFund;
import com.example.mat.financialmanager.model.TermSaving;

public class FundDetailsActivity extends AppCompatActivity {

    private Fund fund;
    private TextView textFundName;
    private TextView textFundValue;
    private TextView textFundBank;
    private TextView textExpiryDate;
    private TextView textFundMonthlyTax;
    private TextView textFundInterest;
    private TextView textFundValueAfter;

    private void findViews() {
        textFundName = (TextView)findViewById( R.id.text_fund_name );
        textFundValue = (TextView)findViewById( R.id.text_fund_value );
        textFundBank = (TextView)findViewById( R.id.text_fund_bank );
        textExpiryDate = (TextView)findViewById( R.id.text_expiry_date );
        textFundMonthlyTax = (TextView)findViewById( R.id.text_fund_monthly_tax );
        textFundInterest = (TextView)findViewById( R.id.text_fund_interest );
        textFundValueAfter = (TextView)findViewById( R.id.text_fund_value_after );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViews();

        Intent i = getIntent();
        fund = (Fund) i.getSerializableExtra("fund");

        textFundName.setText(fund.getName());
        textFundValue.setText(Double.toString(fund.getValue()));
        textFundBank.setText(fund.getBank());
        textExpiryDate.setText(fund.getStringCroDate());

        if(fund.getFundType().equals(FundTypes.PENSION_FUND.toString())) {
            textFundMonthlyTax.setVisibility(View.VISIBLE);
            textFundMonthlyTax.setText(Double.toString(((PensionFund) fund).getMonthlyTax()));
        }

        else if(fund.getFundType().equals(FundTypes.TERM_SAVING.toString())) {
            textFundInterest.setVisibility(View.VISIBLE);
            textFundValueAfter.setVisibility(View.VISIBLE);
            textFundInterest.setText(Double.toString(((TermSaving)fund).getInterest()));
            textFundValueAfter.setText(Double.toString(((TermSaving)fund).getValueAfter()));
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddEditFundActivity.class);
                intent.putExtra("fund", fund);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fund_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit_fund) {
            Intent intent = new Intent(getApplicationContext(), AddEditFundActivity.class);
            intent.putExtra("fund", fund);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
