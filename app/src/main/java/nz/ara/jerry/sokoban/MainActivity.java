package nz.ara.jerry.sokoban;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.gridlayout.widget.GridLayout;

import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nz.ara.jerry.sokoban.model.Direction;
import nz.ara.jerry.sokoban.model.Game;
import nz.ara.jerry.sokoban.model.Level;
import nz.ara.jerry.sokoban.model.LookUpTable;
import nz.ara.jerry.sokoban.model.Observer;
import nz.ara.jerry.sokoban.model.Placeable;

public class MainActivity extends AppCompatActivity {

    public static final String ALL_LEVELS = "nz.ara.jerry.displayLevels";
    private static final int TEXT_REQUEST = 1;
    private Game game;
    private GridLayout gridLayout;
    private Level currentLevel;
    private TextView moveCounts;
    private TextView completed;
    private MediaPlayer mediaPlayer;
    private Chronometer chronometer;
    private Button chooseLevel;
    private Observer observer;
    private int playbackControl = 0;
    @Override
    public void onConfigurationChanged (Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_main);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chronometer = findViewById(R.id.timeCount);
        game = new Game(getResources().openRawResource(R.raw.levels));
        currentLevel = game.getCurrentLevel();
        gridLayout = findViewById(R.id.gameMap);
        moveCounts = findViewById(R.id.movesss);
        completed = findViewById(R.id.completeTargets);
        chooseLevel = findViewById(R.id.chooseLevel);
        Button resetMap = findViewById(R.id.btnReset);
        resetMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetMap();
            }
        });
        observer = new GameObserver();
        game.setObserver(observer);
        this.initGame();
    }

    private void initGame() {
        gridLayout = findViewById(R.id.gameMap);
        int gridColumn = currentLevel.getWidth();
        int gridRow = currentLevel.getHeight();
        Placeable[][] placeables = currentLevel.allPlaceables;
        gridLayout.setColumnCount(gridColumn);
        gridLayout.setRowCount(gridRow);
        for (Placeable[] placeable : placeables) {
            for (Placeable cell : placeable) {
                ImageView imageView = new ImageView(this);
                String symbol = cell.toString();
                imageView.setImageResource(LookUpTable.imageViewMap.get(symbol));
                gridLayout.addView(imageView);
            }
        }
        updateTextView();
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    public void move(View view){
        Direction direction = LookUpTable.hashMap.get(view.getId());
        if (direction != null) {
            game.move(direction, true);
        }
        if (currentLevel.getCompletedCount() == currentLevel.getTargetCount()){
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            AlertDialog alert = alertBuilder.setIcon(R.drawable.gridbk)
                    .setTitle("Congratulations")
                    .setMessage("Completed with " + currentLevel.getMoveCount() + "!").create();
            alert.show();
            playSound();
            chronometer.stop();
        }
        updateTextView();
    }

    private void updateTextView(){
        moveCounts.setText(String.valueOf(currentLevel.getMoveCount()));
        final int completedCount = currentLevel.getCompletedCount();
        String completes = completedCount + "/" + currentLevel.getTargetCount();
        //set completed targets TextView
        completed.setText(completes);
        chooseLevel.setText(currentLevel.getName());
    }

    //play a sound when click on move button
    public void playSound() {
        mediaPlayer = MediaPlayer.create(this, R.raw.movesound);
        mediaPlayer.start();
    }

    public void loadLevel(View view) {
        int buttonId = view.getId();
        int indexOfCurrentLevel = game.getLevels().indexOf(this.currentLevel);
        if (buttonId == R.id.buttonNextLevel) {
            if (indexOfCurrentLevel == game.getLevelCount() -1) {
                return;
            } else {
                this.currentLevel = game.getLevels().get(indexOfCurrentLevel + 1);
            }
        } else {
            if (indexOfCurrentLevel == 0) {
                return;
            } else {
                this.currentLevel = game.getLevels().get(indexOfCurrentLevel - 1);
            }
        }
        game.setCurrentLevel(this.currentLevel);
        this.playbackControl = 1;
        resetMap();
    }

    public void resetMap() {
        currentLevel.setMoveCount(0);
        currentLevel.setTargetCount(0);
        currentLevel.initGameBoard();
        updateTextView();
        gridLayout.removeAllViews();
        gridLayout.setColumnCount(0);
        gridLayout.setRowCount(0);
        this.initGame();
    }

    public void chooseLevel(View view) {
        Intent intent = new Intent(this, DisplayLevelsActivity.class);
        ArrayList<String> allLevelNames = (ArrayList<String>) game.getLevelNames();
        intent.putStringArrayListExtra(ALL_LEVELS, allLevelNames);
        startActivityForResult(intent, TEXT_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                String index = data.getStringExtra(DisplayLevelsActivity.EXTRA_REPLY);
                if (index != null){
                    currentLevel = game.getLevels().get(Integer.parseInt(index));
                    game.setCurrentLevel(currentLevel);
                    resetMap();
                }
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }

    public void undo(View view) {
        int undoRecords = game.undoRecords.size();
        if(undoRecords > 0){
            game.undo(game.undoRecords.get(undoRecords -1), game.moveCrate.get(undoRecords -1));
            game.undoRecords.remove(undoRecords -1);
            game.moveCrate.remove(undoRecords -1);
            updateTextView();
        }
    }

    public void playback(View view) {
        if(this.playbackControl == 0){
            resetMap();
            this.playbackControl += 1;
        }else if(this.playbackControl <= this.currentLevel.records.size()){
            game.move(this.currentLevel.records.get(playbackControl -1), false);
            ++playbackControl;
            updateTextView();
        }
        if(this.playbackControl == this.currentLevel.records.size() +1){
            this.playbackControl = 0;
        }
    }

    private class GameObserver implements Observer{

        @Override
        public void onCellUpdated(Placeable placeable){
            int gridColumn = currentLevel.getWidth();
            int coordinateX = placeable.getCoordinateX();
            int coordinateY = placeable.getCoordinateY();
            String symbol = placeable.toString();
            ImageView imageView = (ImageView) gridLayout.getChildAt(gridColumn * coordinateX + coordinateY);
            imageView.setImageResource(LookUpTable.imageViewMap.get(symbol));
        }
    }
}
