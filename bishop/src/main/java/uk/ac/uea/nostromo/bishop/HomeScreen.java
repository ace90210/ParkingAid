package uk.ac.uea.nostromo.bishop;

import android.content.Context;
import android.view.View;
import android.widget.TableRow;
import java.util.List;

import uk.ac.uea.nostromo.mother.Game;
import uk.ac.uea.nostromo.mother.Screen;

/**
 * Created by Barry on 02/01/2016.
 */
public class HomeScreen extends Screen {
    private List<ParkingRecord> existingRecords;

    HomeScreen(Game game, final Context context){
        super(game, context);

        TableRow parkingButton =  game.getGraphics().newButton("Start", context, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickParkingView(v);
            }
        });

        screenLayout.addView(parkingButton, TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

        TableRow label = game.getGraphics().newTextView("Previous Records",context);

        screenLayout.addView(label, TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);

        existingRecords = ((MainActivity)game).prdh.getAllRecords();


        String filterText = ((MainActivity)game).filterTextView != null ? ((MainActivity)game).filterTextView.getText().toString() : null;

        final MainActivity mainActivity = (MainActivity)game;
        for (ParkingRecord pr : existingRecords) {
            TableRow record = game.getGraphics().newOptionText(pr.getParkName() + " - " + pr.getZone(), "fee: " + pr.getFee(), false);

            if(filterText == null || filterText == "" || (filterText != null && pr.getParkName().toLowerCase().contains(filterText.toLowerCase()))) {
                final ParkingRecord prf = pr;
                record.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainActivity.setScreen(new ParkingScreen(mainActivity, context, prf));
                    }
                });

                screenLayout.addView(record);
            }
        }
    }

    public void onClickParkingView(View view) {
        ParkingRecord existingRecord = null;

        if(existingRecords != null) {
            //check if existing incomplete parking record present
            for (ParkingRecord pr : existingRecords) {
                if (pr.getEndTime() == null) {
                    existingRecord = pr;
                    break;
                }
            }
        }

        if(existingRecord != null)
            game.setScreen(new ParkingScreen(game, context, existingRecord));
        else
            game.setScreen(new AddParkingScreen(game, context));
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
        ((MainActivity)game).finish();
        System.exit(0);
    }
}
