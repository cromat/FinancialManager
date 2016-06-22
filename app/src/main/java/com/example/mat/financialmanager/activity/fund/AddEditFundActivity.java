package com.example.mat.financialmanager.activity.fund;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.mat.financialmanager.enums.FundTypes;
import com.example.mat.financialmanager.model.Fund;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.parse.Parse;
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

public class AddEditFundActivity extends AppCompatActivity implements Validator.ValidationListener{

    @NotEmpty
    private EditText editFundName;
    private Spinner spinnerFundType;
    @NotEmpty
    private EditText editFundValue;
    private Spinner spinnerFundCurrency;
    @NotEmpty
    private EditText editFundBank;
    private Button bttnFundDateTo;
    @NotEmpty
    private EditText editFundMonthlyTax;
    @NotEmpty
    private EditText editFundInterest;
    @NotEmpty
    private EditText editFundValueAfter;
    private ArrayAdapter<String> adapterFundTypes;
    private ArrayAdapter<String> adapterCurrencies;
    private Button bttnSave;
    private TextView textValueGiven;

    private Validator validator;
    private boolean validated;
    private Fund fund;
    private Calendar myCalendar;

    private void findViews() {

        editFundName = (EditText)findViewById( R.id.edit_fund_name );
        spinnerFundType = (Spinner)findViewById( R.id.spinner_fund_type );
        editFundValue = (EditText)findViewById( R.id.edit_fund_value );
        spinnerFundCurrency = (Spinner)findViewById( R.id.spinner_fund_currency );
        editFundBank = (EditText)findViewById( R.id.edit_fund_bank );
        bttnFundDateTo = (Button)findViewById( R.id.bttn_fund_date_to );
        editFundMonthlyTax = (EditText)findViewById( R.id.edit_fund_monthly_tax );
        editFundInterest = (EditText)findViewById( R.id.edit_fund_interest );
        editFundValueAfter = (EditText)findViewById( R.id.edit_fund_value_after );
        bttnSave = (Button) findViewById(R.id.bttn_save_fund);
        textValueGiven = (TextView)findViewById(R.id.value_given_curr);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_fund);
        findViews();

        fund = new Fund();
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
            fund = (Fund) i.getSerializableExtra("fund");
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

        adapterCurrencies =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Currencies.getNames());

        adapterFundTypes =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FundTypes.getNames());

        adapterFundTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterCurrencies.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFundCurrency.setAdapter(adapterCurrencies);
        spinnerFundType.setAdapter(adapterFundTypes);

        if (fund != null){
            if (fund.getFundType().equals(FundTypes.PENSION_FUND.toString())){
                editFundMonthlyTax.setVisibility(View.VISIBLE);

                editFundInterest.setText(AppConfig.NULL);
                editFundValueAfter.setText(AppConfig.NULL);
            }

            else if(fund.getFundType().equals(FundTypes.TERM_SAVING.toString())){
                editFundInterest.setVisibility(View.VISIBLE);
                editFundValueAfter.setVisibility(View.VISIBLE);

                editFundMonthlyTax.setText(AppConfig.NULL);
            }

            else if(fund.getFundType().equals(FundTypes.MUTUAL_FUND.toString())){
                editFundInterest.setText(AppConfig.NULL);
                editFundValueAfter.setText(AppConfig.NULL);
                editFundMonthlyTax.setText(AppConfig.NULL);
            }
            editFundName.setText(fund.getName());
            spinnerFundType.setSelection(adapterFundTypes.getPosition(fund.getFundType()));
            editFundValue.setText(Double.toString(fund.getValue()));
            spinnerFundCurrency.setSelection(adapterCurrencies.getPosition(fund.getCurrency()));
            editFundBank.setText(fund.getBank());
            bttnFundDateTo.setText(fund.getStringCroDate());
            editFundMonthlyTax = (EditText)findViewById( R.id.edit_fund_monthly_tax );
            editFundInterest = (EditText)findViewById( R.id.edit_fund_interest );
            editFundValueAfter = (EditText)findViewById( R.id.edit_fund_value_after );
        }

        bttnFundDateTo.setOnClickListener(new View.OnClickListener() {
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

                        bttnFundDateTo.setText(sdf.format(myCalendar.getTime()));
                    }
                };

                new DatePickerDialog(AddEditFundActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        spinnerFundType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selected = spinnerFundType.getSelectedItem().toString();

                if (selected.equals(FundTypes.TERM_SAVING.toString())){

                    editFundInterest.setVisibility(View.VISIBLE);
                    editFundValueAfter.setVisibility(View.VISIBLE);
                    textValueGiven.setVisibility(View.VISIBLE);
                    editFundInterest.setText("");
                    editFundValueAfter.setText("");


                    editFundMonthlyTax.setVisibility(View.GONE);

                    editFundMonthlyTax.setText(AppConfig.NULL);

                }

                else if (selected.equals(FundTypes.PENSION_FUND.toString())){

                    editFundInterest.setVisibility(View.GONE);
                    editFundValueAfter.setVisibility(View.GONE);

                    editFundMonthlyTax.setVisibility(View.VISIBLE);
                    editFundMonthlyTax.setText("");
                    textValueGiven.setVisibility(View.VISIBLE);



                    editFundInterest.setText(AppConfig.NULL);
                    editFundValueAfter.setText(AppConfig.NULL);

                }

                else if (selected.equals(FundTypes.MUTUAL_FUND.toString())){

                    editFundInterest.setVisibility(View.GONE);
                    editFundValueAfter.setVisibility(View.GONE);
                    editFundMonthlyTax.setVisibility(View.GONE);

                    textValueGiven.setVisibility(View.GONE);


                    editFundInterest.setText(AppConfig.NULL);
                    editFundValueAfter.setText(AppConfig.NULL);
                    editFundMonthlyTax.setText(AppConfig.NULL);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        bttnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bttnFundDateTo.getText().equals(getResources().getString(R.string.btn_choose_date))) {
                    String message = "Date not choosen!";
                    bttnFundDateTo.setError(message);
                } else
                    validated = true;

                validator.validate();

                if (validated) {
                    ParseObject fundObj = new ParseObject("Funds");

                    try {
                        if (fund != null) {
                            ParseQuery query = new ParseQuery("Funds");
                            fundObj = query.get(fund.getId());
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Date dateTo = new Date();

                    DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);
                    try {
                        dateTo = format.parse(bttnFundDateTo.getText().toString());
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }

                    fundObj.put("user_id", ParseUser.getCurrentUser().getObjectId());
                    fundObj.put("name", editFundName.getText().toString());
                    fundObj.put("bank", editFundBank.getText().toString());
                    fundObj.put("value", editFundValue.getText().toString());
                    fundObj.put("value_after", editFundValueAfter.getText().toString());
                    fundObj.put("date_due", dateTo.toString());
                    fundObj.put("currency", spinnerFundCurrency.getSelectedItem().toString());
                    fundObj.put("fund_type", spinnerFundType.getSelectedItem().toString());
                    fundObj.put("monthly_tax", editFundMonthlyTax.getText().toString());
                    fundObj.put("interest", editFundInterest.getText().toString());

                    if (AppConfig.isNetworkAvailable(getApplicationContext()))
                        fundObj.saveInBackground();

                    else
                        fundObj.pinInBackground();


                    finish();
                }
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        if (bttnFundDateTo.getText().equals(getResources().getString(R.string.btn_choose_date))){
            String message = "Date not choosen!";
            bttnFundDateTo.setError(message);
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
