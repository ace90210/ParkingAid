package uk.ac.uea.nostromo.bishop;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import uk.ac.uea.nostromo.mother.DataObject;
import uk.ac.uea.nostromo.mother.Game;
import uk.ac.uea.nostromo.mother.Location;
import uk.ac.uea.nostromo.mother.LocationManager;
import uk.ac.uea.nostromo.mother.Screen;
import uk.ac.uea.nostromo.mother.XMLDatastrategy;

/**
 * Created by Barry on 02/01/2016.
 */
public class AddParkingScreen extends Screen {

    private final TableRow carParkName;
    private final TableRow timeOfArrival;
    private final TableRow zone;
    private final TableRow fee;

    AddParkingScreen(Game game, Context context){
        super(game, context);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.UK);
        String currentDateandTime = sdf.format(new Date());

        //load map data
        DataObject<XMLDatastrategy.MapRow> mapData = game.getDataIO().readDataList(new XMLDatastrategy("mapdata.xml", context));

        //filter by car parks
        DataObject<XMLDatastrategy.MapRow> carParks = mapData.search(new Comparable<XMLDatastrategy.MapRow>() {
            @Override
            public int compareTo(XMLDatastrategy.MapRow another) {
                if("car_park".equalsIgnoreCase(another.getType()))
                    return 1;
                return 0;
            }
        });

        //convert into list
        List<XMLDatastrategy.MapRow> labels = new ArrayList<>();
        for (DataObject mr : carParks ) {
            Log.d("DEBUG", "added Car Park: " + ((XMLDatastrategy.MapRow)((DataObject)mr.getData()).getData()).getDescription());
            labels.add(((XMLDatastrategy.MapRow)((DataObject)mr.getData()).getData()));
        }

        //create car park name row (spinner selection)
        carParkName = game.getGraphics().newOptionSpinner("Carpark Name", labels, android.R.layout.simple_spinner_item, null);

        Location realTimeLoc = new Location(52.622940, 1.240962);
        try {
            //find nearest car park
            LocationManager lm = new LocationManager(context);
            realTimeLoc = lm.getCurrentLocation();
        }
        catch (NullPointerException ex){ /*handle no gps available*/ }

        if(realTimeLoc != null){
            //found position look for nearest
            double closest = Double.MAX_VALUE;
            XMLDatastrategy.MapRow defaultCarPark = null;
            for (XMLDatastrategy.MapRow row : labels) {
                double xd = realTimeLoc.getLatitude() - row.getLat();
                double yd = realTimeLoc.getLongitude() - row.getLongitude();
                double distance = Math.sqrt(xd*xd + yd*yd);

                if(distance < closest){
                    closest = distance;
                    defaultCarPark = row;
                }
            }
            if(defaultCarPark != null){
                Spinner carParkSpinner = (Spinner) carParkName.getChildAt(1);
                for (int i = 0; i < carParkSpinner.getCount() ; i++) {
                    if(carParkSpinner.getItemAtPosition(i).toString().equalsIgnoreCase(defaultCarPark.getName())){
                        carParkSpinner.setSelection(i);
                        break;
                    }
                }
            }
        }

        //create time of arrival and zone rows (read only time of arrival)
        timeOfArrival = game.getGraphics().newOptionText("Time of Arrival", currentDateandTime, false);
        zone = game.getGraphics().newOptionText("Zone (optional)", true);
        fee = game.getGraphics().newOptionText("Fee (Per Hour)", true);

        ((TextView)fee.getChildAt(1)).setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        //create add button to progress to next screen
        TableRow addButton = game.getGraphics().newButton("Add", context, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickAdd(v);
            }
        });

        //set margin on top of add button
        TableLayout.LayoutParams lp = (TableLayout.LayoutParams)addButton.getLayoutParams();
        lp.topMargin = 30;

        screenLayout.addView(carParkName);
        screenLayout.addView(timeOfArrival);
        screenLayout.addView(zone);
        screenLayout.addView(fee);
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
        editor.putString(((MainActivity)game).CURRENT_FEES, getSettingFromTableRow(editor, fee));
        editor.apply();

        //save to database
        ParkingRecord pr = new ParkingRecord(
                getSettingFromTableRow(editor, carParkName),
                getSettingFromTableRow(editor, zone),
                getSettingFromTableRow(editor, fee),
                getSettingFromTableRow(editor, timeOfArrival),
                null);


        ((MainActivity)game).prdh.addRecord(pr);

        game.setScreen(new ParkingScreen(game, context, pr));
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
        game.setScreen(new HomeScreen(game, context));
    }
}
