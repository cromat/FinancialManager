package com.example.mat.financialmanager.activity.share;

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
import android.widget.Toast;

import com.example.mat.financialmanager.AppConfig;
import com.example.mat.financialmanager.R;
import com.example.mat.financialmanager.enums.Currencies;
import com.example.mat.financialmanager.model.Share;
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

public class AddEditShareActivity extends AppCompatActivity implements Validator.ValidationListener{

    @NotEmpty
    private EditText editShareName;
    @NotEmpty
    private EditText editShareQuantity;
    @NotEmpty
    private EditText editShareValue;
    private Spinner spinnerShareCurrency;
    @NotEmpty
    private EditText editShareValuePerOne;
    @NotEmpty
    private EditText editShareCompany;
    private Button bttnShareDateBought;
    private Button bttnSaveShare;

    private Validator validator;
    private boolean validated;
    private Share share;
    private ArrayAdapter<String> adapterShareCurrencies;
    private Calendar myCalendar;



    private void findViews() {
        editShareName = (EditText)findViewById( R.id.edit_share_name );
        editShareQuantity = (EditText)findViewById( R.id.edit_share_quantity );
        editShareValue = (EditText)findViewById( R.id.edit_share_value );
        spinnerShareCurrency = (Spinner)findViewById( R.id.spinner_share_currency );
        editShareValuePerOne = (EditText)findViewById( R.id.edit_share_value_per_one );
        editShareCompany = (EditText)findViewById( R.id.edit_share_company );
        bttnShareDateBought = (Button)findViewById( R.id.bttn_share_date_bought );
        bttnSaveShare = (Button)findViewById( R.id.bttn_save_share );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_share);
        findViews();

        share = new Share();
        validator = new Validator(this);
        validator.setValidationListener(this);
        validated = false;

        AppConfig.connectToParse(getApplicationContext());

        try{
            Intent i = getIntent();
            share = (Share) i.getSerializableExtra("share");
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

        adapterShareCurrencies =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Currencies.getNames());


        adapterShareCurrencies.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerShareCurrency.setAdapter(adapterShareCurrencies);

        if (share != null){
            editShareName.setText(share.getName());
            editShareQuantity.setText(Integer.toString(share.getQuantity()));
            editShareValue.setText(Double.toString(share.getValue()));
            spinnerShareCurrency.setSelection(adapterShareCurrencies.getPosition(share.getCurrency()));
            editShareValuePerOne.setText(Double.toString(share.getValuePerShare()));
            editShareCompany.setText(share.getCompany());
            bttnShareDateBought.setText(share.getStringCroDate());
        }

        bttnShareDateBought.setOnClickListener(new View.OnClickListener() {
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
                        String myFormat = "dd.MM.yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

                        bttnShareDateBought.setText(sdf.format(myCalendar.getTime()));
                    }
                };

                new DatePickerDialog(AddEditShareActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editShareQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if(!editShareValue.getText().toString().isEmpty() &&
                            !editShareQuantity.getText().toString().isEmpty()){
                        double valueAll = Double.parseDouble(editShareValue.getText().toString());
                        int quantity = Integer.parseInt(editShareQuantity.getText().toString());

                        double perShare = valueAll / quantity;

                        editShareValuePerOne.setText(Double.toString(perShare));
                    }

                    else if(!editShareValuePerOne.getText().toString().isEmpty() &&
                            !editShareQuantity.getText().toString().isEmpty()){
                        double perShare = Double.parseDouble(editShareValuePerOne.getText().toString());
                        int quantity = Integer.parseInt(editShareQuantity.getText().toString());

                        double valueAll = perShare * quantity;

                        editShareValue.setText(Double.toString(perShare));
                    }
                }

            }
        });

        editShareValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if(!editShareValue.getText().toString().isEmpty() &&
                            !editShareQuantity.getText().toString().isEmpty()){
                        double valueAll = Double.parseDouble(editShareValue.getText().toString());
                        int quantity = Integer.parseInt(editShareQuantity.getText().toString());

                        double perShare = valueAll / quantity;

                        editShareValuePerOne.setText(Double.toString(perShare));
                    }
                }

            }
        });

        editShareValuePerOne.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    if(!editShareValuePerOne.getText().toString().isEmpty() &&
                            !editShareQuantity.getText().toString().isEmpty()){
                        double perShare = Double.parseDouble(editShareValuePerOne.getText().toString());
                        int quantity = Integer.parseInt(editShareQuantity.getText().toString());

                        double valueAll = perShare * quantity;

                        editShareValue.setText(Double.toString(valueAll));
                    }
                }

            }
        });

        bttnSaveShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bttnShareDateBought.getText().equals(getResources().getString(R.string.btn_choose_date))) {
                    String message = "Date not choosen!";
                    bttnShareDateBought.setError(message);
                } else
                    validated = true;

                validator.validate();

                if (validated) {
                    ParseObject shareObj = new ParseObject("Shares");

                    try {
                        if (share != null) {
                            ParseQuery query = new ParseQuery("Shares");
                            shareObj = query.get(share.getId());
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Date dateBought = new Date();

                    DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);
                    try {
                        dateBought = format.parse(bttnShareDateBought.getText().toString());
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }

                    shareObj.put("user_id", ParseUser.getCurrentUser().getObjectId());
                    shareObj.put("name", editShareName.getText().toString());
                    shareObj.put("value", editShareValue.getText().toString());
                    shareObj.put("value_per_share", editShareValuePerOne.getText().toString());
                    shareObj.put("date_bought", dateBought.toString());
                    shareObj.put("currency", spinnerShareCurrency.getSelectedItem().toString());
                    shareObj.put("company", editShareCompany.getText().toString());
                    shareObj.put("quantity", editShareQuantity.getText().toString());

                    if (AppConfig.isNetworkAvailable(getApplicationContext()))
                        shareObj.saveInBackground();
                    else
                    shareObj.pinInBackground();

                    finish();
                }
            }
        });
    }


    @Override
    public void onValidationSucceeded() {
        if (bttnShareDateBought.getText().equals(getResources().getString(R.string.btn_choose_date))){
            String message = "Date not choosen!";
            bttnShareDateBought.setError(message);
        }
        else
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
