package com.example.mat.financialmanager.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mat.financialmanager.R;

public class AddInvoiceActivity extends AppCompatActivity {

    private EditText editInvoiceName;
    private EditText editInvoiceNumber;
    private EditText editCardNumber;
    private Spinner spinnerExpiryMonth;
    private EditText editExpiryYear;
    private Spinner spinnerCardType;
    private EditText editBalance;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invoice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editBalance = (EditText)findViewById(R.id.edit_balance);
        editCardNumber = (EditText)findViewById(R.id.edit_card_number);
        spinnerCardType = (Spinner)findViewById(R.id.spinner_card_type);
        editInvoiceName = (EditText)findViewById(R.id.edit_invoice_name);
        editExpiryYear = (EditText)findViewById(R.id.edit_year_expiry);
        editInvoiceNumber = (EditText)findViewById(R.id.edit_invoice_number);
        spinnerExpiryMonth = (Spinner)findViewById(R.id.spinner_month_expiry);

        ArrayAdapter<CharSequence> adapterMonths = ArrayAdapter.createFromResource(this,
                R.array.months_array, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> adapterCardTypes = ArrayAdapter.createFromResource(this,
                R.array.months_array, android.R.layout.simple_spinner_item);

        adapterMonths.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterCardTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerExpiryMonth.setAdapter(adapterMonths);
        spinnerCardType.setAdapter(adapterCardTypes);

        //TODO: On Save Button Click -> parse

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
