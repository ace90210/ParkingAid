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

    GotoCarScreen(Game game, final Context context){
        super(game, context);

        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        LinearLayout ll = this.game.getGraphics().newLinearLayout((int)(width * 0.75f), (int)(height * 0.5f));

        final String currentLat = ((MainActivity)game).parking.getString(((MainActivity)game).CURRENT_LAT, "52.621827");
        final String currentLong = ((MainActivity)game).parking.getString(((MainActivity)game).CURRENT_LONG, "1.239867");
        final String currentName = ((MainActivity)game).parking.getString(((MainActivity)game).CURRENT_PARKING, "Unknown");

        Graphics.MyGoogleMap map = game.getGraphics().newMap((AndroidGame) game, ll.getId(), new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng UEA = new LatLng(Double.parseDouble(currentLat), Double.parseDouble(currentLong));

                googleMap.addMarker(new MarkerOptions().title(currentName).position(UEA));
            }
        });

        screenLayout.addView(ll);

        Button secondScreenButton = new Button(context);
        secondScreenButton.setText("Done");
        secondScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSecondView(v);
            }
        });
        screenLayout.addView(secondScreenButton);
    }

    public void onClickSecondView(View view) {
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
    }
}
