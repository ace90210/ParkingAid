package uk.ac.uea.nostromo.bishop;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import uk.ac.uea.nostromo.mother.Game;
import uk.ac.uea.nostromo.mother.Screen;
import uk.ac.uea.nostromo.mother.implementation.MyCountDownTimer;

/**
 * Created by Barry on 02/01/2016.
 */
public class ParkingScreen extends Screen {
    private final TableRow remindMe;
    private final TableRow timeLeft;
    private final TableRow currentTime;

    TextView title;

    MyCountDownTimer ctimer =  new MyCountDownTimer(10000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            updateTime();
        }

        @Override
        public void onFinish() {
            timer.start();
        }
    };

    MyCountDownTimer timer = new MyCountDownTimer(10000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            updateTime();
        }

        @Override
        public void onFinish() {
            timer.start();
        }
    };

    MyCountDownTimer remindLeft = new MyCountDownTimer(TimeUnit.MINUTES.toMillis(15), 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
            Log.d("DEBUGTEST", "minutes: " + minutes);
        }

        @Override
        public void onFinish() { }
    }
            ;


    ParkingScreen(Game game, Context context){
        super(game, context);
        timer.start();
        remindLeft.start();

        List<String> labels = new ArrayList<>();
        labels.add("15");
        labels.add("30");
        labels.add("60");
        labels.add("120");
        labels.add("240");

        remindMe = game.getGraphics().newOptionSpinner("Remind Me (mins)", labels, android.R.layout.simple_spinner_item, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int r = Integer.parseInt((String)((Spinner)remindMe.getChildAt(1)).getSelectedItem());

                Log.d("DEBUGTEST", "Remind Selection Changed");
                remindLeft.resetTimer(TimeUnit.MINUTES.toMillis(r));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        timeLeft = game.getGraphics().newOptionText("Remaining Time", true);
        currentTime = game.getGraphics().newOptionText("Current Time", true);

        screenLayout.addView(remindMe);
        screenLayout.addView(timeLeft);
        screenLayout.addView(currentTime);


        title = new TextView(context);
        title.setTextColor(Color.BLACK);
        screenLayout.addView(title);

        Button secondScreenButton = new Button(context);
        secondScreenButton.setText("Goto Home Screen");
        secondScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSecondView(v);
            }
        });
        screenLayout.addView(secondScreenButton);

        updateTime();
    }

    private void updateTime(){

        String currentCarparking = ((MainActivity)game).parking.getString(((MainActivity)game).CURRENT_PARKING, "not found");
        String currentZone = ((MainActivity)game).parking.getString(((MainActivity)game).CURRENT_ZONE, "not found");

        String currentTimeHours = ((MainActivity)game).parking.getString(((MainActivity)game).CURRENT_TOA_HOURS, "not found");
        String currentTimeMinutes = ((MainActivity)game).parking.getString(((MainActivity)game).CURRENT_TOA_MINUTES, "not found");

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDateandTime = sdf.format(new Date());

        long tleftMinutes = TimeUnit.MILLISECONDS.toMinutes(remindLeft.getTimeLeft());
        long tleftSeconds = TimeUnit.MILLISECONDS.toSeconds(remindLeft.getTimeLeft()) - tleftMinutes * 60;


        Log.d("DEBUGTEST", "Seconds: " + TimeUnit.MILLISECONDS.toSeconds(remindLeft.getTimeLeft()) + " - " + tleftMinutes * 60);

        ((EditText)timeLeft.getChildAt(1)).setText(tleftMinutes + ":" + tleftSeconds);

        ((EditText)currentTime.getChildAt(1)).setText(currentDateandTime);

        title.setText("Time: " + currentDateandTime + " - Parking: " + currentCarparking + " - Zone: " + currentZone + " - currentTimeHours: " + currentTimeHours + " currentTimeMinutes: " + currentTimeMinutes);
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
