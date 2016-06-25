package com.example.mat.financialmanager.activity.fund;

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
import com.example.mat.financialmanager.adapter.FundAdapter;
import com.example.mat.financialmanager.enums.FundTypes;
import com.example.mat.financialmanager.model.Fund;
import com.example.mat.financialmanager.model.PensionFund;
import com.example.mat.financialmanager.model.TermSaving;
import com.example.mat.financialmanager.sqlite.SQLiteHelper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FundListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerFunds;
    private TextView textNoFunds;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapterFunds;
    public List<Fund> funds;
    private boolean searching = false;
    private SQLiteHelper db;

    private String fundType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try{
            Intent i = getIntent();
            fundType = (String) i.getSerializableExtra(AppConfig.FUND_TYPE.toString());
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

        db = new SQLiteHelper(getApplicationContext());
        db.getWritableDatabase();


        funds = new ArrayList<>();

        recyclerFunds = (RecyclerView)findViewById(R.id.recycler_fund);
        textNoFunds= (TextView)findViewById(R.id.text_no_funds);

        AppConfig.connectToParse(getApplicationContext());


        recyclerFunds.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerFunds.setLayoutManager(layoutManager);
        adapterFunds = new FundAdapter(funds);
        recyclerFunds.setAdapter(adapterFunds);

        parseFetchFunds();
        dataSetChanged();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_funds);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!searching)
                    parseFetchFunds();
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
        funds.clear();
        dataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fund_menu, menu);
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
        else if (id == R.id.action_add_fund) {
            startActivity(new Intent(getApplicationContext(), AddEditFundActivity.class));
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

        if(name.equals(LoginActivity.class.getName())) {
            ParseUser.logOut();
            startActivity(intent);
        }

        if(name.equals(FundListActivity.class.getName())) {
            String ft = (String) intent.getSerializableExtra(AppConfig.FUND_TYPE.toString());
            if (ft.equals(fundType))
                dataSetChanged();
            else{
                fundType = ft;
                Intent i = new Intent(getApplicationContext(), FundListActivity.class);
                i.putExtra(AppConfig.FUND_TYPE.toString(), ft);
                i.putExtra("name", FundListActivity.class.getName());
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                finish();
                startActivity(i);
            }
        }
        else
            startActivity(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void parseFetchFunds(){
        searching = true;
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Funds");
        query.whereMatches("fund_type", fundType).whereMatches("user_id", ParseUser.getCurrentUser().getObjectId()).findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> fundsParse, ParseException e) {
                if (e == null) {

                    for (ParseObject fundObject : fundsParse) {
                        Fund fund = Fund.getTypeConstructor(fundType);

                        fund.setId(fundObject.getObjectId());
                        fund.setUserId(fundObject.getString("user_id"));
                        fund.setName(fundObject.getString("name"));
                        fund.setBank(fundObject.getString("bank"));
                        fund.setValue(Double.parseDouble(fundObject.getString("value")));
                        fund.setCurrency(fundObject.getString("currency"));
                        fund.setFundType(fundObject.getString("fund_type"));
                        fund.setDateDue(Fund.getDateFromString("date_due"));

                        if (fundType.equals(FundTypes.PENSION_FUND))
                            ((PensionFund)fund).setMonthlyTax(Double.parseDouble(fundObject.getString("monthly_tax")));

                        else if(fundType.equals(FundTypes.TERM_SAVING)){
                            ((TermSaving)fund).setValueAfter(Double.parseDouble(fundObject.getString("value_after")));
                            ((TermSaving)fund).setInterest(Double.parseDouble("interest"));

                        }

                        db.updateOrInsertFund(fund);
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

    public void getFunds(){
        try {
            funds = db.getFunds(ParseUser.getCurrentUser().getObjectId(), fundType);
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

        if (funds.size() == 0){
            textNoFunds.setVisibility(View.VISIBLE);
            recyclerFunds.setVisibility(View.GONE);
        }

        else if (funds.size() > 0){
            textNoFunds.setVisibility(View.GONE);
            recyclerFunds.setVisibility(View.VISIBLE);
        }
    }

    @UiThread
    protected void dataSetChanged() {
        try {
            funds.clear();
            getFunds();
            adapterFunds = new FundAdapter(funds);
            recyclerFunds.setAdapter(adapterFunds);
            adapterFunds.notifyItemInserted(funds.size());
            adapterFunds.notifyDataSetChanged();
        }
        catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

}
