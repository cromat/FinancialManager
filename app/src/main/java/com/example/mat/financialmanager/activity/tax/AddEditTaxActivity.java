package com.example.mat.financialmanager.activity.tax;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mat.financialmanager.AppConfig;
import com.example.mat.financialmanager.R;
import com.example.mat.financialmanager.enums.Currencies;
import com.example.mat.financialmanager.model.Share;
import com.example.mat.financialmanager.model.Tax;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class AddEditTaxActivity extends AppCompatActivity implements Validator.ValidationListener{

    @NotEmpty
    private EditText editTaxName;
    @NotEmpty
    private EditText editTaxValue;
    private Spinner spinnerTaxCurrency;
    @NotEmpty
    private EditText editTaxCompany;
    private Button bttnTaxDateIssue;
    private Button bttnTaxDateTo;
    private EditText editTaxInvoiceNumber;
    private Button bttnSaveTax;

    private Tax tax;
    private Validator validator;
    private boolean validated;
    private ArrayAdapter<String> adapterTaxCurrencies;


    private void findViews() {
        editTaxName = (EditText)findViewById( R.id.edit_tax_name );
        editTaxValue = (EditText)findViewById( R.id.edit_tax_value );
        spinnerTaxCurrency = (Spinner)findViewById( R.id.spinner_tax_currency );
        editTaxCompany = (EditText)findViewById( R.id.edit_tax_company );
        bttnTaxDateIssue = (Button)findViewById( R.id.bttn_tax_date_issue );
        bttnTaxDateTo = (Button)findViewById( R.id.bttn_tax_date_to );
        editTaxInvoiceNumber = (EditText)findViewById( R.id.edit_tax_invoice_number );
        bttnSaveTax = (Button)findViewById( R.id.bttn_save_tax );
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_tax);
        findViews();

        tax = new Tax();
        validator = new Validator(this);
        validator.setValidationListener(this);
        validated = false;

        try {
            Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                    .applicationId(AppConfig.APP_ID)
                    .server(AppConfig.PARSE_LINK)
                    .build()
            );

        }
        catch (IllegalStateException e){
            e.printStackTrace();
        }

        try{
            Intent i = getIntent();
            tax = (Tax) i.getSerializableExtra("tax");
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

        adapterTaxCurrencies =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Currencies.getNames());


        adapterTaxCurrencies.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerTaxCurrency.setAdapter(adapterTaxCurrencies);

        if (tax != null){
            editTaxName.setText(tax.getName());
            editTaxValue.setText(Double.toString(tax.getValue()));
            spinnerTaxCurrency.setSelection(adapterTaxCurrencies.getPosition(tax.getCurrency()));
            editTaxCompany.setText(tax.getCompany());
            bttnTaxDateIssue.setText(tax.getCroDateIssue());
            bttnTaxDateTo.setText(tax.getCroDateDue());
            editTaxInvoiceNumber.setText(tax.getInvoiceNumber());
        }

        bttnSaveTax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bttnTaxDateIssue.getText().equals(getResources().getString(R.string.btn_choose_date))) {
                    String message = "Date not choosen!";
                    bttnTaxDateIssue.setError(message);
                }

                if (bttnTaxDateTo.getText().equals(getResources().getString(R.string.btn_choose_date))) {
                    String message = "Date not choosen!";
                    bttnTaxDateTo.setError(message);
                }

                if (!(bttnTaxDateTo.getText().equals(getResources().getString(R.string.btn_choose_date))) &&
                        (bttnTaxDateIssue.getText().equals(getResources().getString(R.string.btn_choose_date))))
                    validated = true;

                validator.validate();

                if (validated) {
                    ParseObject taxObj = new ParseObject("Taxes");

                    try {
                        if (tax != null) {
                            ParseQuery query = new ParseQuery("Taxes");
                            taxObj = query.get(tax.getId());
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    taxObj.put("user_id", ParseUser.getCurrentUser().getObjectId());
                    taxObj.put("name", editTaxName.getText().toString());
                    taxObj.put("value", editTaxValue.getText().toString());
                    taxObj.put("company", editTaxCompany.getText().toString());
                    taxObj.put("date_due", bttnTaxDateTo.getText());
                    taxObj.put("date_issue", bttnTaxDateIssue.getText());
                    taxObj.put("currency", spinnerTaxCurrency.getSelectedItem().toString());
                    taxObj.put("invoice_number", editTaxInvoiceNumber.getText().toString());

                    // TODO: Check for network. If not available save to sql base and add to
                    // updating queue or generate id than save it locally

                    taxObj.saveInBackground();
                    finish();
                }
            }
        });

    }

    @Override
    public void onValidationSucceeded() {
        if (bttnTaxDateIssue.getText().equals(getResources().getString(R.string.btn_choose_date))) {
            String message = "Date not choosen!";
            bttnTaxDateIssue.setError(message);
        }

        if (bttnTaxDateTo.getText().equals(getResources().getString(R.string.btn_choose_date))) {
            String message = "Date not choosen!";
            bttnTaxDateTo.setError(message);
        }

        if (!(bttnTaxDateTo.getText().equals(getResources().getString(R.string.btn_choose_date))) &&
                (bttnTaxDateIssue.getText().equals(getResources().getString(R.string.btn_choose_date))))
            validated = true;
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
