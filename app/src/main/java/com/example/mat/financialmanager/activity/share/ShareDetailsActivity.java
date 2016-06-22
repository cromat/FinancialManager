package com.example.mat.financialmanager.activity.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mat.financialmanager.R;
import com.example.mat.financialmanager.model.Share;

public class ShareDetailsActivity extends AppCompatActivity {

    private TextView textShareName;
    private TextView textShareValue;
    private TextView textShareCurrency;
    private TextView textPerShareValue;
    private TextView textShareQuantity;
    private TextView textShareCompany;
    private TextView textShareDateBought;
    private Share share;


    private void findViews() {
        textShareName = (TextView)findViewById( R.id.text_share_name );
        textShareValue = (TextView)findViewById( R.id.text_share_value );
        textShareCurrency = (TextView)findViewById( R.id.text_share_currency );
        textPerShareValue = (TextView)findViewById( R.id.text_per_share_value );
        textShareQuantity = (TextView)findViewById( R.id.text_share_quantity );
        textShareCompany = (TextView)findViewById( R.id.text_share_company );
        textShareDateBought = (TextView)findViewById( R.id.text_share_date_bought );
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViews();

        Intent i = getIntent();
        share = (Share) i.getSerializableExtra("share");

        textShareName.setText(share.getName());
        textShareValue.setText(Double.toString(share.getValue()));
        textShareCurrency.setText(share.getCurrency());
        textPerShareValue.setText(Double.toString(share.getValuePerShare()));
        textShareQuantity.setText(Integer.toString(share.getQuantity()));
        textShareCompany.setText(share.getCompany());
        textShareDateBought.setText(share.getStringCroDate());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddEditShareActivity.class);
                intent.putExtra("share", share);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.share_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit_share) {
            Intent intent = new Intent(getApplicationContext(), AddEditShareActivity.class);
            intent.putExtra("share", share);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
