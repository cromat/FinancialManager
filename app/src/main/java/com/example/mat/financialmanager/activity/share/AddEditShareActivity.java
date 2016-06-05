package com.example.mat.financialmanager.activity.share;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mat.financialmanager.AppConfig;
import com.example.mat.financialmanager.R;
import com.example.mat.financialmanager.enums.CardTypes;
import com.example.mat.financialmanager.enums.Currencies;
import com.example.mat.financialmanager.model.Share;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

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


                    shareObj.put("user_id", ParseUser.getCurrentUser().getObjectId());
                    shareObj.put("name", editShareName.getText().toString());
                    shareObj.put("value", editShareValue.getText().toString());
                    shareObj.put("value_per_share", editShareValuePerOne.getText().toString());
                    shareObj.put("date_bought", bttnShareDateBought.getText());
                    shareObj.put("currency", spinnerShareCurrency.getSelectedItem().toString());
                    shareObj.put("company", editShareCompany.getText().toString());
                    shareObj.put("quantity", editShareQuantity.getText().toString());

                    // TODO: Check for network. If not available save to sql base and add to
                    // updating queue or generate id than save it locally

                    shareObj.saveInBackground();
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
