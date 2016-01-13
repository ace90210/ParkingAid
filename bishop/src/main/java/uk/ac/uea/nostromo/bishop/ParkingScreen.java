package uk.ac.uea.nostromo.bishop;

import android.content.Context;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import uk.ac.uea.nostromo.mother.Game;
import uk.ac.uea.nostromo.mother.Screen;
import uk.ac.uea.nostromo.mother.implementation.MyCountDownTimer;

/**
 * ParkingScreen displays current parking record info.
 * For new/incomplete parking records a reminder option is available.
 *
 * @author    {Barry Wright}
 * @version    v1.0.0
 * @since    v1.0.0-alpha+20160112
 */
public class ParkingScreen extends Screen {
    private final ParkingRecord record;

    private TableRow remindMe = null;
    private TableRow timeLeft = null;

    private TableRow currentPark = null;
    private TableRow currentZone = null;
    private TableRow startTime = null;
    private TableRow endTime = null;
    private TableRow currentFee = null;

    TextView title;

    MyCountDownTimer timer = null;

    MyCountDownTimer remindLeft = null;


    ParkingScreen(Game game, Context context, ParkingRecord record){
        super(game, context);
        this.record = record;

        //if current record (no end time) show goto car, else show delete record button and end time)
        if(record.getEndTime() == null){
            generateCurrentView();
        }
        else {
            generateRecordView();
        }


        updateTime();
    }

    private void generateCurrentView(){
        timer = new MyCountDownTimer(10000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateTime();
            }

            @Override
            public void onFinish() {
                timer.start();
            }
        };
        timer.start();

        remindLeft = new MyCountDownTimer(TimeUnit.MINUTES.toMillis(15), 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                Log.d("DEBUGTEST", "minutes: " + minutes);
            }

            @Override
            public void onFinish() {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(context, notification);
                r.play();}
        };
        remindLeft.start();

        List<String> labels = new ArrayList<>();
        labels.add("1");
        labels.add("15");
        labels.add("30");
        labels.add("60");
        labels.add("120");
        labels.add("240");

        remindMe = game.getGraphics().newOptionSpinner("Remind Me (mins)", labels, android.R.layout.simple_spinner_item, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int r = Integer.parseInt((String) ((Spinner) remindMe.getChildAt(1)).getSelectedItem());

                Log.d("DEBUGTEST", "Remind Selection Changed");
                if (remindLeft != null)
                    remindLeft.resetTimer(TimeUnit.MINUTES.toMillis(r));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        timeLeft = game.getGraphics().newOptionText("Remaining Time", true);


        currentPark = game.getGraphics().newOptionText("Car Park", record.getParkName(), false);
        currentZone = game.getGraphics().newOptionText("Current Zone", record.getZone(), false);
        startTime = game.getGraphics().newOptionText("Time Parked", false);

        screenLayout.addView(currentPark);
        screenLayout.addView(startTime);
        screenLayout.addView(currentZone);
        screenLayout.addView(remindMe);
        screenLayout.addView(timeLeft);

        TableRow gotoCarButton = game.getGraphics().newButton("Go to car", context, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickGotocarView(v);
            }
        }, 30);
        screenLayout.addView(gotoCarButton);
    }

    private void generateRecordView(){
        currentPark = game.getGraphics().newOptionText("Car Park", record.getParkName(), false);
        currentZone = game.getGraphics().newOptionText("Zone", record.getZone(), false);
        startTime = game.getGraphics().newOptionText("Start Time",record.getStartTime(),  false);
        endTime = game.getGraphics().newOptionText("End Time",record.getEndTime(),  false);
        currentFee = game.getGraphics().newOptionText("Fee",record.getFee(),  false);

        screenLayout.addView(currentPark);
        screenLayout.addView(currentZone);
        screenLayout.addView(startTime);
        screenLayout.addView(endTime);
        screenLayout.addView(currentFee);

        final MainActivity mainActivity = (MainActivity)game;

        TableRow deleteButton = game.getGraphics().newButton("Delete!", context, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.prdh.deleteParkingRecord(record);

                mainActivity.setScreen(new HomeScreen(mainActivity, context));
            }
        }, 30);
        screenLayout.addView(deleteButton);
    }

    private void updateTime(){
        String[] TOA = record.getStartTime().split(":");
        String currentTimeHours = TOA[0];
        String currentTimeMinutes = TOA[1];

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(currentTimeHours));
        c.set(Calendar.MINUTE, Integer.parseInt(currentTimeMinutes));

        Calendar now = Calendar.getInstance();
        now.add(Calendar.HOUR_OF_DAY, -c.get(Calendar.HOUR_OF_DAY));
        now.add(Calendar.MINUTE, - c.get(Calendar.MINUTE));


        if(remindLeft != null) {
            long tleftMinutes = TimeUnit.MILLISECONDS.toMinutes(remindLeft.getTimeLeft());
            long tleftSeconds = (TimeUnit.MILLISECONDS.toSeconds(remindLeft.getTimeLeft()) - tleftMinutes * 60) - 1;

            if(tleftSeconds < 0){
                tleftSeconds = 0;
            }

            ((EditText) timeLeft.getChildAt(1)).setText(String.format(Locale.UK, "%02d:%02d" ,tleftMinutes, tleftSeconds));

            ((EditText)startTime.getChildAt(1)).setText(String.format(Locale.UK, "%02d:%02d", now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE)));
        }
    }

    public void onClickGotocarView(View view) {
        if(timer != null)
            timer.cancel();
        if(remindLeft != null)
            remindLeft.cancel();

        game.setScreen(new GotoCarScreen(game, context, record));
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
        if(timer != null)
            timer.cancel();
        if(remindLeft != null)
            remindLeft.cancel();
    }

    @Override
    public void backButton() {
        game.setScreen(new HomeScreen(game, context));
    }
}
