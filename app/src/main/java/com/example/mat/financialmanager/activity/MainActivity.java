package com.example.mat.financialmanager.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import com.example.mat.financialmanager.R;
import com.example.mat.financialmanager.adapter.InvoiceAdapter;
import com.example.mat.financialmanager.model.Invoice;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerInvoices;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapterInvoices;
    public List<Invoice> invoices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        invoices = new ArrayList<Invoice>();
        recyclerInvoices = (RecyclerView)findViewById(R.id.recycler_main);



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

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Invoices");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> invoicesParse, ParseException e) {
                if (e == null) {

                    for (ParseObject invoiceObject : invoicesParse) {
                        Invoice invoice = new Invoice();

                        invoice.setId(invoiceObject.getObjectId());
                        invoice.setName(invoiceObject.getString("name"));
                        invoice.setInvoiceNumber(invoiceObject.getString("invoice_number"));
                        invoice.setCardNumber(invoiceObject.getString("card_number"));
                        invoice.setCardExpiry(invoiceObject.getString("card_expiry"));
                        invoice.setCardType(invoiceObject.getString("card_type"));
                        invoice.setBalance(invoiceObject.getDouble("balance"));
                        invoice.setCurrency(invoiceObject.getString("currency"));

                        invoices.add(invoice);
                        recyclerInvoices.setAdapter(adapterInvoices);
                        adapterInvoices.notifyItemInserted(invoices.size());
                        adapterInvoices.notifyDataSetChanged();
                    }
                }
                else {
                    e.printStackTrace();
                }
            }
        });


        recyclerInvoices.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerInvoices.setLayoutManager(layoutManager);

        adapterInvoices = new InvoiceAdapter(invoices);
        recyclerInvoices.setAdapter(adapterInvoices);
        adapterInvoices.notifyItemInserted(invoices.size());
        adapterInvoices.notifyDataSetChanged();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        adapterInvoices.notifyDataSetChanged();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_invoices) {
            // Handle the camera action
        } else if (id == R.id.nav_shares) {

        } else if (id == R.id.nav_mutual_funds) {

        } else if (id == R.id.nav_pension_funds) {

        } else if (id == R.id.nav_savings) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
