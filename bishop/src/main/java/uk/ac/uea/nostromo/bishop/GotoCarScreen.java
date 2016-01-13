package uk.ac.uea.nostromo.bishop;

import android.content.Context;
import android.graphics.Point;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import uk.ac.uea.nostromo.mother.Game;
import uk.ac.uea.nostromo.mother.Location;
import uk.ac.uea.nostromo.mother.LocationManager;
import uk.ac.uea.nostromo.mother.Screen;
import uk.ac.uea.nostromo.mother.implementation.AndroidGame;
import uk.ac.uea.nostromo.mother.implementation.Graphics;
import uk.ac.uea.nostromo.mother.implementation.MyCountDownTimer;

/**
 * Created by Barry on 02/01/2016.
 */
public class GotoCarScreen extends Screen {
    private ParkingRecord record;

    GotoCarScreen(Game game, final Context context, ParkingRecord record){
        super(game, context);
        this.record = record;

        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        LinearLayout ll = this.game.getGraphics().newLinearLayout((int)(width * 0.9), (int)(height * 0.5f));

        final String currentLat = ((MainActivity)game).parking.getString(((MainActivity)game).CURRENT_LAT, "52.621827");
        final String currentLong = ((MainActivity)game).parking.getString(((MainActivity)game).CURRENT_LONG, "1.239867");
        final String currentName = ((MainActivity)game).parking.getString(((MainActivity)game).CURRENT_PARKING, "Unknown");
        final String currentZone = ((MainActivity)game).parking.getString(((MainActivity)game).CURRENT_ZONE, "Unknown");
        final String currentFee = ((MainActivity)game).parking.getString(((MainActivity)game).CURRENT_FEES, "Unknown");

        final LatLng carPosition = new LatLng(Double.parseDouble(currentLat), Double.parseDouble(currentLong));
        Graphics.MyGoogleMap map = game.getGraphics().newMap((AndroidGame) game, ll.getId(), new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                googleMap.addMarker(new MarkerOptions().title(currentName).snippet(currentZone + "\n" + currentFee).position(carPosition));
            }
        });
        map.setTargetLocation(carPosition);


        screenLayout.addView(ll);

        TableRow doneButton = game.getGraphics().newButton("Done", context, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDoneView(v);
            }
        }, 30);
        screenLayout.addView(doneButton);
    }

    public void onClickDoneView(View view) {
        Calendar now  = Calendar.getInstance();
        record.setEndTime(String.format(Locale.UK, "%02d:%02d", now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE)));
        ((MainActivity)game).prdh.updateParkingRecord(record);
        game.setScreen(new HomeScreen(game, context));
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void paint(float deltaTime) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void backButton() {
        game.setScreen(new ParkingScreen(game, context, record));
    }
}
