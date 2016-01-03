package uk.ac.uea.nostromo.bishop;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import uk.ac.uea.nostromo.mother.ExcelDatastrategy;
import uk.ac.uea.nostromo.mother.Game;
import uk.ac.uea.nostromo.mother.Screen;
import uk.ac.uea.nostromo.mother.XMLDatastrategy;
import uk.ac.uea.nostromo.mother.implementation.AndroidDataIO;

/**
 * Created by Barry on 02/01/2016.
 */
public class HomeScreen extends Screen {

    HomeScreen(Game game, Context context){
        super(game, context);

        TextView title = new TextView(context);
        title.setText("we are in first view");
        title.setTextColor(Color.BLACK);
        screenLayout.addView(title);

        Button secondScreenButton = new Button(context);

        AndroidDataIO adio = new AndroidDataIO();
        AssetManager am = context.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = am.open("mapdata.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        adio.readDataList(new XMLDatastrategy("mapdata.xml", inputStream));

        secondScreenButton.setText("Goto Second Screen");
        secondScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSecondView(v);
            }
        });
        screenLayout.addView(secondScreenButton);
    }

    public void onClickSecondView(View view) {
        game.setScreen(new ParkingScreen(game, context));
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
