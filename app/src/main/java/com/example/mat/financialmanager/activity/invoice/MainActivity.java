package com.example.mat.financialmanager.activity.invoice;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.mat.financialmanager.AppConfig;
import com.example.mat.financialmanager.R;
import com.example.mat.financialmanager.SettingsActivity;
import com.example.mat.financialmanager.activity.LoginActivity;
import com.example.mat.financialmanager.adapter.InvoiceAdapter;
import com.example.mat.financialmanager.model.Invoice;
import com.example.mat.financialmanager.sqlite.SQLiteHelper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerInvoices;
    private TextView textNoInvoices;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapterInvoices;
    public List<Invoice> invoices;
    private boolean searching = false;
    private SQLiteHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        db = new SQLiteHelper(getApplicationContext());
        db.getWritableDatabase();

        invoices = new ArrayList<>();

        recyclerInvoices = (RecyclerView)findViewById(R.id.recycler_main);
        textNoInvoices = (TextView)findViewById(R.id.text_no_invoices);


        AppConfig.connectToParse(getApplicationContext());

        recyclerInvoices.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerInvoices.setLayoutManager(layoutManager);
        adapterInvoices = new InvoiceAdapter(invoices);
        recyclerInvoices.setAdapter(adapterInvoices);

        parseFetchInvoices();
        getInvoices();
        dataSetChanged();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!searching)
                    parseFetchInvoices();
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
        if (!searching)
            parseFetchInvoices();
        dataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
        else if (id == R.id.action_add_invoice) {
            startActivity(new Intent(getApplicationContext(), AddEditInvoiceActivity.class));
        }
        else if (id == R.id.action_logout) {
            ParseUser.logOut();
            finish();
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

        if(name.equals(LoginActivity.class.getName())) {
            ParseUser.logOut();
            startActivity(intent);
        }

        if(name.equals(this.getClass().getName()))
            dataSetChanged();

        else
            startActivity(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void parseFetchInvoices(){
        searching = true;
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Invoices");
        query.whereEqualTo("user_id", ParseUser.getCurrentUser().getObjectId()).findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> invoicesParse, ParseException e) {
                if (e == null) {

                    for (ParseObject invoiceObject : invoicesParse) {
                        Invoice invoice = new Invoice();

                        invoice.setId(invoiceObject.getObjectId());
                        invoice.setUserId(invoiceObject.getString("user_id"));
                        invoice.setName(invoiceObject.getString("name"));
                        invoice.setInvoiceNumber(invoiceObject.getString("invoice_number"));
                        invoice.setCardNumber(invoiceObject.getString("card_number"));
                        invoice.setCardExpiry(invoiceObject.getString("card_expiry"));
                        invoice.setCardType(invoiceObject.getString("card_type"));
                        String balanceTmp = invoiceObject.getString("balance");
                        if (balanceTmp.equals(""))
                            balanceTmp = "0";
                        invoice.setBalance(Double.parseDouble(balanceTmp));
                        invoice.setCurrency(invoiceObject.getString("currency"));
                        invoice.setBank(invoiceObject.getString("bank"));

                        db.updateOrInsertInvoice(invoice);
                        dataSetChanged();
                    }
                    searching = false;

                }
                else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getInvoices(){
        try {
            invoices = db.getInvoices(ParseUser.getCurrentUser().getObjectId());
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

        if (invoices.size() == 0){
            textNoInvoices.setVisibility(View.VISIBLE);
            recyclerInvoices.setVisibility(View.GONE);
        }

        else if (invoices.size() > 0){
            textNoInvoices.setVisibility(View.GONE);
            recyclerInvoices.setVisibility(View.VISIBLE);
        }
    }

    @UiThread
    protected void dataSetChanged() {
        try {
            invoices.clear();
            getInvoices();
            adapterInvoices = new InvoiceAdapter(invoices);
            adapterInvoices.notifyItemInserted(invoices.size());
            adapterInvoices.notifyDataSetChanged();
            recyclerInvoices.setAdapter(adapterInvoices);
        }
        catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }
}
