package com.example.mat.financialmanager.activity.tax;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mat.financialmanager.AppConfig;
import com.example.mat.financialmanager.R;
import com.example.mat.financialmanager.SettingsActivity;
import com.example.mat.financialmanager.activity.LoginActivity;
import com.example.mat.financialmanager.activity.fund.AddEditFundActivity;
import com.example.mat.financialmanager.activity.fund.FundListActivity;
import com.example.mat.financialmanager.activity.invoice.AddEditInvoiceActivity;
import com.example.mat.financialmanager.activity.invoice.MainActivity;
import com.example.mat.financialmanager.activity.share.ShareListActivity;
import com.example.mat.financialmanager.adapter.TaxAdapter;
import com.example.mat.financialmanager.enums.FundTypes;
import com.example.mat.financialmanager.model.Tax;
import com.example.mat.financialmanager.sqlite.SQLiteTax;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class TaxListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView recyclerTaxes;
    private TextView textNoTaxes;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapterTaxes;
    public List<Tax> taxes;
    private boolean searching = false;
    private SQLiteTax db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tax_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new SQLiteTax(getApplicationContext());
        db.getWritableDatabase();

        taxes = new ArrayList<>();

        recyclerTaxes = (RecyclerView)findViewById(R.id.recycler_tax);
        textNoTaxes = (TextView)findViewById(R.id.text_no_taxes);

        getTaxes();

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

        recyclerTaxes.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerTaxes.setLayoutManager(layoutManager);
        adapterTaxes = new TaxAdapter(taxes);
        recyclerTaxes.setAdapter(adapterTaxes);

        parseFetchTaxes();
        getTaxes();

        dataSetChanged();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataSetChanged();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tax_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        }
        else if (id == R.id.action_add_tax) {
            startActivity(new Intent(getApplicationContext(), AddEditTaxActivity.class));
        }
        else if (id == R.id.action_logout) {
            ParseUser.logOut();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent = AppConfig.getStartingActivity(id, getApplicationContext());
        String name = (String) intent.getSerializableExtra("name");

        if(name.equals(this.getClass().getName()))
            dataSetChanged();

        else
            startActivity(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void parseFetchTaxes(){
        searching = true;
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Taxes");
        query.whereEqualTo("user_id", ParseUser.getCurrentUser().getObjectId()).findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> taxesParse, ParseException e) {
                if (e == null) {

                    for (ParseObject taxObject : taxesParse) {
                        Tax tax = new Tax();

                        tax.setId(taxObject.getObjectId());
                        tax.setUserId(taxObject.getString("user_id"));
                        tax.setName(taxObject.getString("name"));
                        tax.setCompany(taxObject.getString("company"));
                        tax.setValue(Double.parseDouble(taxObject.getString("value")));
                        tax.setDateDue(taxObject.getString("date_due"));
                        tax.setDateIssue(taxObject.getString("date_issue"));
                        tax.setCurrency(taxObject.getString("currency"));
                        tax.setInvoiceNumber(taxObject.getString("invoice_number"));

                        db.updateOrInsertTax(tax);
                        dataSetChanged();
                    }
                }
                else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getTaxes(){
        try {
            taxes = db.getTaxes();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

        dataSetChanged();

        if (taxes.size() == 0){
            textNoTaxes.setVisibility(View.VISIBLE);
            recyclerTaxes.setVisibility(View.GONE);
        }

        else if (taxes.size() > 0){
            textNoTaxes.setVisibility(View.GONE);
            recyclerTaxes.setVisibility(View.VISIBLE);
        }
    }

    @UiThread
    protected void dataSetChanged() {
        try {
            adapterTaxes = new TaxAdapter(taxes);
            adapterTaxes.notifyItemInserted(taxes.size());
            recyclerTaxes.setAdapter(adapterTaxes);
        }
        catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

}
