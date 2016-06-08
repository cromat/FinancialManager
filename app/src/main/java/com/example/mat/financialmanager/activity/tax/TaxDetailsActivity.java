package com.example.mat.financialmanager.activity.tax;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mat.financialmanager.R;
import com.example.mat.financialmanager.activity.share.AddEditShareActivity;
import com.example.mat.financialmanager.model.Tax;

public class TaxDetailsActivity extends AppCompatActivity {

    private TextView textTaxName;
    private TextView textTaxValue;
    private TextView textTaxCurrency;
    private TextView textTaxCompany;
    private TextView textTaxDateIssue;
    private TextView textTaxDateDue;
    private TextView textTaxInvoiceNumber;

    private Tax tax;


    private void findViews() {
        textTaxName = (TextView)findViewById( R.id.text_tax_name );
        textTaxValue = (TextView)findViewById( R.id.text_tax_value );
        textTaxCurrency = (TextView)findViewById( R.id.text_tax_currency );
        textTaxCompany = (TextView)findViewById( R.id.text_tax_company );
        textTaxDateIssue = (TextView)findViewById( R.id.text_tax_date_issue );
        textTaxDateDue = (TextView)findViewById( R.id.text_tax_date_due );
        textTaxInvoiceNumber = (TextView)findViewById( R.id.text_tax_invoice_number );
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tax_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViews();

        Intent i = getIntent();
        tax = (Tax) i.getSerializableExtra("tax");

        textTaxName.setText(tax.getName());
        textTaxValue.setText(Double.toString(tax.getValue()));
        textTaxCurrency.setText(tax.getCurrency());
        textTaxCompany.setText(tax.getCompany());
        textTaxDateIssue.setText(tax.getCroDateIssue());
        textTaxDateDue.setText(tax.getCroDateDue());
        textTaxInvoiceNumber.setText(tax.getInvoiceNumber());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddEditTaxActivity.class);
                intent.putExtra("tax", tax);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tax_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit_tax) {
            Intent intent = new Intent(getApplicationContext(), AddEditTaxActivity.class);
            intent.putExtra("tax", tax);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
