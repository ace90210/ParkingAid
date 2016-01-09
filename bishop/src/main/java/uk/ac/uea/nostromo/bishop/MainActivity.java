package uk.ac.uea.nostromo.bishop;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import uk.ac.uea.nostromo.mother.Screen;
import uk.ac.uea.nostromo.mother.implementation.AndroidGame;


public class MainActivity extends AndroidGame {
    SharedPreferences parking;

    final String CURRENT_PARKING = "currentParkingName";
    final String CURRENT_TOA_HOURS = "currentTimeOfArrivalHours";
    final String CURRENT_TOA_MINUTES = "currentTimeOfArrivalMinutes";
    final String CURRENT_ZONE = "currentZone";
    final String CURRENT_LAT = "currentLat";
    final String CURRENT_LONG = "currentLong";
    final String CURRENT_FEES = "currentFees";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState, R.layout.activity_main, R.id.table_layout);

        ImageButton home = (ImageButton)findViewById(R.id.imageButton2);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScreen(new ParkingScreen(MainActivity.this, getBaseContext()));
            }
        });

        parking = getDataIO().getSharedPref("UEA_MAP_DATA", getBaseContext());
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

    @Override
    public Screen getInitScreen() {
        try {
            Context context = getBaseContext();
            return new HomeScreen(this, context);
        }
        catch (Exception ex){
            String exs = ex.getMessage();
            System.out.print(exs);
        }
        return null;
    }
}
