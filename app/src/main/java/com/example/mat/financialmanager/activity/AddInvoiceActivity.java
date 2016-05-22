package com.example.mat.financialmanager.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mat.financialmanager.R;
import com.example.mat.financialmanager.enums.InvoiceTypes;
import com.parse.Parse;
import com.parse.ParseObject;

import org.apache.commons.validator.routines.checkdigit.IBANCheckDigit;

import java.util.Arrays;
import java.util.regex.Pattern;

public class AddInvoiceActivity extends AppCompatActivity {

    private EditText editInvoiceName;
    private EditText editInvoiceNumber;
    private EditText editCardNumber;
    private Spinner spinnerExpiryMonth;
    private EditText editExpiryYear;
    private Spinner spinnerCardType;
    private Spinner spinnerCurrency;
    private EditText editBalance;
    private Button buttonSave;
    private ArrayAdapter<CharSequence> adapterMonths;
    private ArrayAdapter<CharSequence> adapterCardTypes;
    private ArrayAdapter<CharSequence> adapterCurrencies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                    .applicationId("finmgr")
                    .server("http://finmgr-cromat.rhcloud.com/parse/")
                    .build()
            );
        }
        catch (IllegalStateException e){
            e.printStackTrace();
        }

        setContentView(R.layout.activity_add_invoice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editInvoiceName = (EditText)findViewById(R.id.edit_invoice_name);
        editInvoiceNumber = (EditText)findViewById(R.id.edit_invoice_number);
        editCardNumber = (EditText)findViewById(R.id.edit_card_number);
        editExpiryYear = (EditText)findViewById(R.id.edit_year_expiry);
        spinnerExpiryMonth = (Spinner)findViewById(R.id.spinner_month_expiry);
        spinnerCardType = (Spinner)findViewById(R.id.spinner_card_type);
        editBalance = (EditText)findViewById(R.id.edit_balance);
        spinnerCurrency = (Spinner)findViewById(R.id.spinner_currency);
        buttonSave = (Button) findViewById(R.id.bttn_save_invoice);

        adapterMonths = ArrayAdapter.createFromResource(this,
                R.array.months_array, android.R.layout.simple_spinner_item);

        adapterCurrencies = ArrayAdapter.createFromResource(this,
                R.array.currency_array, android.R.layout.simple_spinner_item);

        adapterCardTypes = ArrayAdapter.createFromResource(this,
                R.array.months_array, android.R.layout.simple_spinner_item);

        adapterCurrencies.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        adapterMonths.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterCardTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCurrency.setAdapter(adapterCurrencies);
        spinnerExpiryMonth.setAdapter(adapterMonths);
        spinnerCardType.setAdapter(adapterCardTypes);

        editCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 2){
                    if (s.subSequence(0,1) == "4")
                        spinnerCardType.setSelection(adapterCardTypes.getPosition(InvoiceTypes.VISA.toString()));
                    else if (Arrays.asList(new String[]{"51","52","53","54","55"}).contains(s.subSequence(0,2)))
                        spinnerCardType.setSelection(adapterCardTypes.getPosition(InvoiceTypes.MASTER_CARD.toString()));
                    else if (s.subSequence(0,2) == "37")
                        spinnerCardType.setSelection(adapterCardTypes.getPosition(InvoiceTypes.AMERICAN_EXPRESS.toString()));
                    else if (s.subSequence(0,2) == "65")
                        spinnerCardType.setSelection(adapterCardTypes.getPosition(InvoiceTypes.DISCOVER.toString()));
                    else if (s.subSequence(0,2) == "67")
                        spinnerCardType.setSelection(adapterCardTypes.getPosition(InvoiceTypes.MAESTRO.toString()));
                    else
                        spinnerCardType.setSelection(adapterCardTypes.getPosition(InvoiceTypes.OTHER.toString()));
                }
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseObject invoiceObj = new ParseObject("Invoices");
                invoiceObj.put("name", editInvoiceName.getText().toString());
                invoiceObj.put("invoice_number", editInvoiceNumber.getText().toString());
                invoiceObj.put("card_number", editCardNumber.getText().toString());

                String expDate = new String();
                String expMonth = new String();
                expMonth = Integer.toString(spinnerExpiryMonth.getSelectedItemPosition() + 1);
                expDate = editExpiryYear.getText().toString() + "-" + ("00" + expMonth).substring(expMonth.length());

                invoiceObj.put("card_expiry", expDate);
                invoiceObj.put("card_type", spinnerCardType.getSelectedItem().toString());
                invoiceObj.put("balance", editBalance.getText().toString());
                invoiceObj.put("currency", spinnerCurrency.getSelectedItem().toString());

                invoiceObj.saveInBackground();
                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    boolean isNameValid(String invName) {
        if (invName.length() > 0)
            return true;
        return false;
    }

    boolean isCardNumValid(String cardNum) {
        if ((Pattern.matches("[0-9]+", cardNum) && (cardNum.length() == 16)))
            return true;
        return false;
    }

    boolean isInvoiceNumValid(String invoiceNum) {
        IBANCheckDigit iban = new IBANCheckDigit();
        return iban.isValid(invoiceNum);
    }
}
