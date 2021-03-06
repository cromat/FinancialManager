package com.example.mat.financialmanager.activity.invoice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mat.financialmanager.AppConfig;
import com.example.mat.financialmanager.R;
import com.example.mat.financialmanager.enums.CardTypes;
import com.example.mat.financialmanager.enums.Currencies;
import com.example.mat.financialmanager.enums.Months;
import com.example.mat.financialmanager.model.Invoice;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.CreditCard;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.apache.commons.validator.routines.checkdigit.IBANCheckDigit;

import java.util.Arrays;
import java.util.List;

public class AddEditInvoiceActivity extends AppCompatActivity implements Validator.ValidationListener {

    @NotEmpty
    private EditText editInvoiceName;
    @NotEmpty
    private EditText editInvoiceNumber;
    @NotEmpty
    private EditText editBank;
    @NotEmpty
    @CreditCard
    private EditText editCardNumber;
    private Spinner spinnerExpiryMonth;
    @NotEmpty
    @Length(min = 4, max = 4)
    private EditText editExpiryYear;
    private Spinner spinnerCardType;
    private Spinner spinnerCurrency;
    @NotEmpty
    private EditText editBalance;
    private Button buttonSave;
    private ArrayAdapter<String> adapterMonths;
    private ArrayAdapter<String> adapterCardTypes;
    private ArrayAdapter<String> adapterCurrencies;

    private Validator validator;
    private boolean validated;

    private Invoice invoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        invoice = new Invoice();
        validator = new Validator(this);
        validator.setValidationListener(this);
        validated = false;

        AppConfig.connectToParse(getApplicationContext());

        try{
            Intent i = getIntent();
            invoice = (Invoice) i.getSerializableExtra("invoice");
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

        setContentView(R.layout.activity_add_invoice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editInvoiceName = (EditText)findViewById(R.id.edit_invoice_name);
        editInvoiceNumber = (EditText)findViewById(R.id.edit_invoice_number);
        editBank = (EditText)findViewById(R.id.edit_invoice_bank);
        editCardNumber = (EditText)findViewById(R.id.edit_card_number);
        editExpiryYear = (EditText)findViewById(R.id.edit_year_expiry);
        spinnerExpiryMonth = (Spinner)findViewById(R.id.spinner_month_expiry);
        spinnerCardType = (Spinner)findViewById(R.id.spinner_card_type);
        editBalance = (EditText)findViewById(R.id.edit_balance);
        spinnerCurrency = (Spinner)findViewById(R.id.spinner_currency);
        buttonSave = (Button) findViewById(R.id.bttn_save_invoice);


        adapterMonths =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Months.getNames());

        adapterCurrencies =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Currencies.getNames());

        adapterCardTypes =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, CardTypes.getNames());

        adapterCurrencies.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        adapterMonths.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterCardTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCurrency.setAdapter(adapterCurrencies);
        spinnerExpiryMonth.setAdapter(adapterMonths);
        spinnerCardType.setAdapter(adapterCardTypes);

        if (invoice != null){
            editInvoiceName.setText(invoice.getName());
            editInvoiceNumber.setText(invoice.getInvoiceNumber());
            editBank.setText(invoice.getBank());
            editCardNumber.setText(invoice.getCardNumber());
            editExpiryYear.setText(invoice.getCardExpiryYear());
            spinnerExpiryMonth.setSelection(adapterMonths.getPosition(invoice.getCardExpiryMonth()));
            spinnerCardType.setSelection(adapterCardTypes.getPosition(invoice.getCardType()));
            editBalance.setText(Double.toString(invoice.getBalance()));
            spinnerCurrency.setSelection(adapterCurrencies.getPosition(invoice.getCurrency()));
        }

        editCardNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 2){
                    if (s.toString().substring(0,1).equals("4"))
                        spinnerCardType.setSelection(adapterCardTypes.getPosition(CardTypes.VISA.toString()));
                    else if (Arrays.asList(new String[]{"51","52","53","54","55"}).contains(s.toString().substring(0,2)))
                        spinnerCardType.setSelection(adapterCardTypes.getPosition(CardTypes.MASTER_CARD.toString()));
                    else if (s.toString().substring(0,2) == "37")
                        spinnerCardType.setSelection(adapterCardTypes.getPosition(CardTypes.AMERICAN_EXPRESS.toString()));
                    else if ((s.toString().substring(0,2) == "65")||(s.toString().substring(0,2) == "60"))
                        spinnerCardType.setSelection(adapterCardTypes.getPosition(CardTypes.DISCOVER.toString()));
                    else if (s.toString().substring(0,2) == "67")
                        spinnerCardType.setSelection(adapterCardTypes.getPosition(CardTypes.MAESTRO.toString()));
                    else
                        spinnerCardType.setSelection(adapterCardTypes.getPosition(CardTypes.OTHER.toString()));
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInvoiceNumValid(editInvoiceNumber.getText().toString())){
                    String message = "Wrong IBAN number!";
                    editInvoiceNumber.setError(message);
                }
                validator.validate();

                if (validated) {
                    ParseObject invoiceObj = new ParseObject("Invoices");

                    try {
                        if (invoice != null) {
                            ParseQuery query = new ParseQuery("Invoices");
                            invoiceObj = query.get(invoice.getId());
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    invoiceObj.put("user_id", ParseUser.getCurrentUser().getObjectId());
                    invoiceObj.put("name", editInvoiceName.getText().toString());
                    invoiceObj.put("bank", editBank.getText().toString());
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

                    if (AppConfig.isNetworkAvailable(getApplicationContext()))
                        invoiceObj.saveInBackground();
                    else
                        invoiceObj.pinInBackground();

                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        });

    }


    boolean isInvoiceNumValid(String invoiceNum) {
        IBANCheckDigit iban = new IBANCheckDigit();
        return iban.isValid(invoiceNum);
    }

    @Override
    public void onValidationSucceeded() {
        if (isInvoiceNumValid(editInvoiceNumber.getText().toString()))
            validated = true;
        else{
            String message = "Wrong IBAN number!";
            editInvoiceNumber.setError(message);
            validated = false;
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getApplicationContext(), "Inputs not valid!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
