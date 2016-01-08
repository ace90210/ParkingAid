package uk.ac.uea.nostromo.bishop;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import uk.ac.uea.nostromo.mother.DataObject;
import uk.ac.uea.nostromo.mother.Game;
import uk.ac.uea.nostromo.mother.Screen;
import uk.ac.uea.nostromo.mother.XMLDatastrategy;
import uk.ac.uea.nostromo.mother.implementation.AndroidDataIO;

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


        AndroidDataIO adio = new AndroidDataIO();
        AssetManager am = context.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = am.open("mapdata.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        DataObject<XMLDatastrategy.MapRow> mapData = adio.readDataList(new XMLDatastrategy("mapdata.xml", inputStream));

        DataObject<XMLDatastrategy.MapRow> carParks = mapData.search(new Comparable<XMLDatastrategy.MapRow>() {
            @Override
            public int compareTo(XMLDatastrategy.MapRow another) {
                if("car_park".equalsIgnoreCase(another.getType()))
                    return 1;
                return 0;
            }
        });

        List<XMLDatastrategy.MapRow> labels = new ArrayList<>();
        for (DataObject mr : carParks ) {
            Log.d("DEBUG", "added Car Park: " + ((XMLDatastrategy.MapRow)((DataObject)mr.getData()).getData()).getDescription());
            labels.add(((XMLDatastrategy.MapRow)((DataObject)mr.getData()).getData()));
        }

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

        String[] TOA = getSettingFromTableRow(editor, timeOfArrival).split(":");

        editor.putString(((MainActivity)game).CURRENT_PARKING, getSettingFromTableRow(editor, carParkName));
        editor.putString(((MainActivity)game).CURRENT_TOA_HOURS, TOA[0]);
        editor.putString(((MainActivity)game).CURRENT_TOA_MINUTES, TOA[1]);
        editor.putString(((MainActivity)game).CURRENT_ZONE, getSettingFromTableRow(editor, zone));
        editor.putString(((MainActivity)game).CURRENT_LAT, String.valueOf(getSettingFromSpinner(editor, carParkName).getLat()));
        editor.putString(((MainActivity)game).CURRENT_LONG, String.valueOf(getSettingFromSpinner(editor, carParkName).getLongitude()));

        editor.apply();

        game.setScreen(new ParkingScreen(game, context));
    }

    private String getSettingFromTableRow(SharedPreferences.Editor editor, TableRow tr){
        Object o = tr.getChildAt(1);
        String selectedItem = "";
        if(o instanceof Spinner) {
            XMLDatastrategy.MapRow row = (XMLDatastrategy.MapRow)((Spinner) o).getSelectedItem();
            selectedItem = row.getName();
        }else if(o instanceof EditText){
            selectedItem = ((EditText) o).getText().toString();
        }
        return selectedItem;
    }



    private  XMLDatastrategy.MapRow getSettingFromSpinner(SharedPreferences.Editor editor, TableRow tr){
        Object o = tr.getChildAt(1);
        XMLDatastrategy.MapRow selectedItem = null;
        if(o instanceof Spinner) {
            selectedItem = (XMLDatastrategy.MapRow)((Spinner) o).getSelectedItem();
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
