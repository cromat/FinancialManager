package com.example.mat.financialmanager.activity.tax;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mat.financialmanager.AppConfig;
import com.example.mat.financialmanager.R;
import com.example.mat.financialmanager.enums.Currencies;
import com.example.mat.financialmanager.model.Tax;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    private Calendar myCalendar;


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

        AppConfig.connectToParse(getApplicationContext());

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

        bttnTaxDateIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myCalendar = Calendar.getInstance();

                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel(bttnTaxDateIssue, myCalendar);
                    }
                };

                new DatePickerDialog(AddEditTaxActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        bttnTaxDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCalendar = Calendar.getInstance();

                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel(bttnTaxDateTo, myCalendar);
                    }
                };

                new DatePickerDialog(AddEditTaxActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

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

                    Date dateDue = new Date();
                    Date dateIssue = new Date();

                    DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);
                    try {
                        dateDue = format.parse(bttnTaxDateTo.getText().toString());
                        dateIssue = format.parse(bttnTaxDateIssue.getText().toString());
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }


                    taxObj.put("user_id", ParseUser.getCurrentUser().getObjectId());
                    taxObj.put("name", editTaxName.getText().toString());
                    taxObj.put("value", editTaxValue.getText().toString());
                    taxObj.put("company", editTaxCompany.getText().toString());
                    taxObj.put("date_due", dateDue.toString());
                    taxObj.put("date_issue", dateIssue.toString());
                    taxObj.put("currency", spinnerTaxCurrency.getSelectedItem().toString());
                    taxObj.put("invoice_number", editTaxInvoiceNumber.getText().toString());

                    if (AppConfig.isNetworkAvailable(getApplicationContext()))
                        taxObj.saveInBackground();
                    else
                    taxObj.pinInBackground();

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
                !(bttnTaxDateIssue.getText().equals(getResources().getString(R.string.btn_choose_date))))
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



    private void updateLabel(TextView textView, Calendar myCalendar) {

        String myFormat = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        textView.setText(sdf.format(myCalendar.getTime()));
    }
}
