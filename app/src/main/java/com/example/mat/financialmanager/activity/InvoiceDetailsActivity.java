package com.example.mat.financialmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mat.financialmanager.R;
import com.example.mat.financialmanager.model.Invoice;

public class InvoiceDetailsActivity extends AppCompatActivity {

    private Invoice invoice;
    private TextView textInvoiceName;
    private TextView textInvoiceNumber;
    private TextView textBank;
    private TextView textExpiryDate;
    private TextView textCardType;
    private TextView textBalance;
    private TextView textCardBank;
    private ImageView imageCardLogo;
    private TextView textCardNumber;
    private TextView textCardExpiry;
    private TextView textCardInvoiceNumber;

    private void findViews() {
        textInvoiceName = (TextView)findViewById( R.id.text_invoice_name );
        textInvoiceNumber = (TextView)findViewById( R.id.text_invoice_number );
        textBank = (TextView)findViewById( R.id.text_bank );
        textExpiryDate = (TextView)findViewById( R.id.text_expiry_date );
        textCardType = (TextView)findViewById( R.id.text_card_type );
        textBalance = (TextView)findViewById( R.id.text_balance );
        textCardBank = (TextView)findViewById( R.id.text_card_bank );
        imageCardLogo = (ImageView)findViewById( R.id.image_card_logo );
        textCardNumber = (TextView)findViewById( R.id.text_card_number );
        textCardExpiry = (TextView)findViewById( R.id.text_card_expiry );
        textCardInvoiceNumber = (TextView)findViewById( R.id.text_card_invoice_number );
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViews();

        Intent i = getIntent();
        invoice = (Invoice) i.getSerializableExtra("invoice");

        textInvoiceName.setText(invoice.getName());
        textInvoiceNumber.setText(invoice.getInvoiceNumber());
        textBank.setText(invoice.getBank());
        textExpiryDate.setText(invoice.getCardExpiryMonth() + " " + invoice.getCardExpiryYear());
        textCardType.setText(invoice.getCardType());
        textBalance.setText(Double.toString(invoice.getBalance()));
        textCardBank.setText(invoice.getBank());
        imageCardLogo.setImageDrawable(invoice.getCardImage(getApplicationContext()));

        String cardNum = invoice.getCardNumber();
        cardNum = cardNum.substring(0,4) + " " + cardNum.substring(4,8) + " " + cardNum.substring(8,12) +
                " " + cardNum.substring(12,16);

        textCardNumber.setText(cardNum);
        textCardExpiry.setText(invoice.getCardExpiryMonth() + " " + invoice.getCardExpiryYear());
        textCardInvoiceNumber.setText(invoice.getInvoiceNumber());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.invoice_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit_invoice) {
            Intent intent = new Intent(getApplicationContext(), AddEditInvoiceActivity.class);
            intent.putExtra("invoice", invoice);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
