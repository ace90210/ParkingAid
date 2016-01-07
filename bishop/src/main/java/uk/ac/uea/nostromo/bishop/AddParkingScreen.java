package uk.ac.uea.nostromo.bishop;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import uk.ac.uea.nostromo.mother.Game;
import uk.ac.uea.nostromo.mother.Screen;

/**
 * Created by Barry on 02/01/2016.
 */
public class AddParkingScreen extends Screen {

    TableRow carParkName;
    TableRow timeOfArrival;
    TableRow zone;

    AddParkingScreen(Game game, Context context){
        super(game, context);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDateandTime = sdf.format(new Date());

        List<String> labels = new ArrayList<>();
        labels.add("Zone 1");
        labels.add("Zone 2a");
        labels.add("Zone 3c");
        labels.add("Zone D");

        carParkName = game.getGraphics().newOptionSpinner("Carpark Name", labels, android.R.layout.simple_spinner_item, null);
        timeOfArrival = game.getGraphics().newOptionText("Time of Arrival", currentDateandTime, false);
        zone = game.getGraphics().newOptionText("Zone (optional)", true);

        TableRow addButton = game.getGraphics().newButton("Add", context, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAdd(v);
            }
        });

        TableLayout.LayoutParams lp = (TableLayout.LayoutParams)addButton.getLayoutParams();
        lp.topMargin = 30;

        screenLayout.addView(carParkName);
        screenLayout.addView(timeOfArrival);
        screenLayout.addView(zone);
        screenLayout.addView(addButton);
    }
    public void onClickAdd(View view) {
        SharedPreferences.Editor editor = ((MainActivity)game).parking.edit();

        String[] TOA = ((String)getSettingFromTableRow(editor, timeOfArrival)).split(":");

        editor.putString(((MainActivity)game).CURRENT_PARKING, getSettingFromTableRow(editor, carParkName));
        editor.putString(((MainActivity)game).CURRENT_TOA_HOURS, TOA[0]);
        editor.putString(((MainActivity)game).CURRENT_TOA_MINUTES, TOA[1]);
        editor.putString(((MainActivity)game).CURRENT_ZONE, getSettingFromTableRow(editor, carParkName));

        editor.apply();

        game.setScreen(new ParkingScreen(game, context));
    }

    private String getSettingFromTableRow(SharedPreferences.Editor editor, TableRow tr){
        Object o = tr.getChildAt(1);
        String selectedItem = "";
        if(o instanceof Spinner) {
            selectedItem = (String) ((Spinner) o).getSelectedItem();
        }else if(o instanceof EditText){
            selectedItem = ((EditText) o).getText().toString();
        }
        return selectedItem;
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
