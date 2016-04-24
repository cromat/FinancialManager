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
    private EditText editBalance;
    private Button buttonSave;
    private ArrayAdapter<CharSequence> adapterMonths;
    ArrayAdapter<CharSequence> adapterCardTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("finmgr")
                .server("http://finmgr-cromat.rhcloud.com/parse/")
                .build()
        );

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
        buttonSave = (Button) findViewById(R.id.bttn_save_invoice);

        adapterMonths = ArrayAdapter.createFromResource(this,
                R.array.months_array, android.R.layout.simple_spinner_item);

        adapterCardTypes = ArrayAdapter.createFromResource(this,
                R.array.months_array, android.R.layout.simple_spinner_item);

        adapterMonths.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterCardTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

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
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            //TODO Parse on save!

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
