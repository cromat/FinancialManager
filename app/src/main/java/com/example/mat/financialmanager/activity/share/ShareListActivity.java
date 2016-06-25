package com.example.mat.financialmanager.activity.share;

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
import com.example.mat.financialmanager.adapter.ShareAdapter;
import com.example.mat.financialmanager.model.Share;
import com.example.mat.financialmanager.sqlite.SQLiteHelper;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ShareListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private RecyclerView recyclerShares;
    private TextView textNoShares;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapterShares;
    public List<Share> shares;
    private boolean searching = false;
    private SQLiteHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new SQLiteHelper(getApplicationContext());
        db.getWritableDatabase();

        shares = new ArrayList<>();

        recyclerShares= (RecyclerView)findViewById(R.id.recycler_share);
        textNoShares= (TextView)findViewById(R.id.text_no_shares);


        AppConfig.connectToParse(getApplicationContext());

        recyclerShares.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerShares.setLayoutManager(layoutManager);
        adapterShares = new ShareAdapter(shares);
        recyclerShares.setAdapter(adapterShares);

        parseFetchShares();
        getShares();
        dataSetChanged();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!searching)
                    parseFetchShares();
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
        getMenuInflater().inflate(R.menu.share_menu, menu);
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
        else if (id == R.id.action_add_share) {
            startActivity(new Intent(getApplicationContext(), AddEditShareActivity.class));
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

        if(name.equals(this.getClass().getName()))
            dataSetChanged();
        else
            startActivity(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void parseFetchShares(){
        searching = true;
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Shares");
        query.whereEqualTo("user_id", ParseUser.getCurrentUser().getObjectId()).findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> sharesParse, ParseException e) {
                if (e == null) {

                    for (ParseObject shareObject : sharesParse) {
                        Share share = new Share();

                        share.setId(shareObject.getObjectId());
                        share.setUserId(shareObject.getString("user_id"));
                        share.setName(shareObject.getString("name"));
                        share.setQuantity(shareObject.getInt("quantity"));
                        share.setCompany(shareObject.getString("company"));
                        share.setValue(Double.parseDouble(shareObject.getString("value")));
                        share.setValuePerShare(Double.parseDouble(shareObject.getString("value_per_share")));
                        share.setDateBought(shareObject.getString("date_bought"));
                        share.setCurrency(shareObject.getString("currency"));

                        db.updateOrInsertShare(share);
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

    public void getShares(){
        try {
            shares = db.getShares(ParseUser.getCurrentUser().getObjectId());
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

        if (shares.size() == 0){
            textNoShares.setVisibility(View.VISIBLE);
            recyclerShares.setVisibility(View.GONE);
        }

        else if (shares.size() > 0){
            textNoShares.setVisibility(View.GONE);
            recyclerShares.setVisibility(View.VISIBLE);
        }
    }

    @UiThread
    protected void dataSetChanged() {
        try {
            shares.clear();
            parseFetchShares();
            getShares();
            adapterShares = new ShareAdapter(shares);
            adapterShares.notifyItemInserted(shares.size());
            adapterShares.notifyDataSetChanged();
            recyclerShares.setAdapter(adapterShares);
        }
        catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

}
