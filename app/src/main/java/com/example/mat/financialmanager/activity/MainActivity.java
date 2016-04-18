package com.example.mat.financialmanager.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.mat.financialmanager.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    int score;
    TextView text;
    Button gumb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("gudicek")
                .server("http://parse-gudicek.rhcloud.com/parse/")
                .build()
        );

        text = (TextView)findViewById(R.id.test);
        gumb = (Button) findViewById(R.id.gumb);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


                text.setText(Integer.toString(score));

            }
        });

        gumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                ParseObject gameScore = new ParseObject("GameScore");
//                gameScore.put("score", 2000);
//                gameScore.put("playerName", "Gudla");
//                gameScore.put("cheatMode", false);
//                gameScore.saveInBackground();

//                ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
//                query.getInBackground("OK7OETu0Z7", new GetCallback<ParseObject>() {
//                    public void done(ParseObject gameScore, ParseException e) {
//                        if (e == null) {
//                            // object will be your game score
//                            score = gameScore.getInt("score");
//                            System.out.println(score);
//
//                        } else {
//                            System.out.println("banana");
//                            e.printStackTrace();
//                        }
//                    }
//                });

                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("GameScore");
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> markers, ParseException e) {
                        if (e == null) {

                            System.out.println(Integer.toString(markers.size()));

                            for (ParseObject parse : markers) {
                                System.out.println(parse.getInt("score"));

                            }

                        } else {
                            // handle Parse Exception here
                        }
                    }
                });
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
