package nz.ara.jerry.sokoban.model;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Game {

    private final ArrayList<Level> levels = new ArrayList<>();
    private Level currentLevel = null;
    private InputStream input;
    private Observer observer;
    public List<Direction> undoRecords = new ArrayList<>();
    public List<Boolean> moveCrate = new ArrayList<>();

    public Game(InputStream levelsFile) {
        this.input = levelsFile;
        readFile();
    }

    public void setCurrentLevel(Level level) {
        this.currentLevel = level;
    }

    private void readFile(){
        byte[] bytes = new byte[1024];
        try {
            while (this.input.read(bytes) != -1) {
                String levels = new String(bytes);
                String[] allLevels = levels.split("\n");
                for (String level: allLevels) {
                    String[] levelContent = level.split(",");
                    addLevel(levelContent[0], Integer.parseInt(levelContent[1]), Integer.parseInt(levelContent[2]), levelContent[3]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void addLevel(String levelName, int width, int height, String map) {
        Level level = new Level(levelName, width, height, map);
        this.levels.add(level);
        this.currentLevel = level;
    }

    public int getLevelCount() {
        return this.levels.size();
    }

    public List<Level> getLevels() {
        return levels;
    }
    public List<String> getLevelNames() {
        List<String> levelNames = new ArrayList<>();
        for (Level level : this.levels) {
            levelNames.add(level.getName());
        }
        return levelNames;
    }

    public void undo(Direction direction, Boolean control){
        int workerX = this.currentLevel.point.getX();
        int workerY = this.currentLevel.point.getY();
        int destinationX = this.currentLevel.point.getX() + direction.adjustX;
        int destinationY = this.currentLevel.point.getY() + direction.adjustY;
        Placeable nextEntity = this.currentLevel.allPlaceables[destinationX][destinationY];
        if (control){
            moveWorker(workerX - direction.adjustX, workerY - direction.adjustY);
            int destinationOfCrateX = nextEntity.getCoordinateX() - direction.adjustX;
            int destinationOfCrateY = nextEntity.getCoordinateY() - direction.adjustY;
            Placeable nextOfCrate = this.currentLevel.allPlaceables[destinationOfCrateX][destinationOfCrateY];
            Crate crate = new Crate(destinationOfCrateX, destinationOfCrateY);
            this.currentLevel.allPlaceables[destinationOfCrateX][destinationOfCrateY] = crate;
            ((Enterable) nextOfCrate).addCrate(crate);
            observer.onCellUpdated(crate);
            char symbol = this.currentLevel.getSymbolFromMap(destinationX, destinationY);
            if (symbol == '+') {
                Target target = new Target(destinationX, destinationY);
                this.currentLevel.allPlaceables[destinationX][destinationY] = target;
                observer.onCellUpdated(target);
            } else {
                Empty empty = new Empty(destinationX, destinationY);
                this.currentLevel.allPlaceables[destinationX][destinationY] = empty;
                observer.onCellUpdated(empty);
            }

        }else{
            moveWorker(workerX - direction.adjustX, workerY - direction.adjustY);
        }
        --this.currentLevel.moveCount;
    }

    private void moveWorker(int destinationX, int destinationY){
        int workerX = this.currentLevel.point.getX();
        int workerY = this.currentLevel.point.getY();
        char symbol = this.currentLevel.getSymbolFromMap(workerX, workerY);
        Worker worker = new Worker(destinationX, destinationY);
        this.currentLevel.allPlaceables[destinationX][destinationY] = worker;
        observer.onCellUpdated(worker);
        if (symbol == '+') {
            Target target = new Target(workerX, workerY);
            this.currentLevel.allPlaceables[workerX][workerY] = target;
            observer.onCellUpdated(target);
        } else {
            Empty empty = new Empty(workerX, workerY);
            this.currentLevel.allPlaceables[workerX][workerY] = empty;
            observer.onCellUpdated(empty);
        }
        this.currentLevel.point.set(destinationX, destinationY);
    }

    public void move(Direction direction, Boolean playback) {
        int destinationX = this.currentLevel.point.getX() + direction.adjustX;
        int destinationY = this.currentLevel.point.getY() + direction.adjustY;
        Placeable nextEntity = this.currentLevel.allPlaceables[destinationX][destinationY];
        if (nextEntity instanceof Enterable) {
            moveWorker(destinationX,destinationY);
            if(playback){
                this.currentLevel.records.add(direction);
                undoRecords.add(direction);
                moveCrate.add(false);
            }

            ++this.currentLevel.moveCount;
        } else if (nextEntity instanceof Crate) {
            int destinationOfCrateX = destinationX + direction.adjustX;
            int destinationOfCrateY = destinationY + direction.adjustY;
            Placeable nextOfCrate = this.currentLevel.allPlaceables[destinationOfCrateX][destinationOfCrateY];
            if (nextOfCrate instanceof Enterable) {
                Crate crate = new Crate(destinationOfCrateX, destinationOfCrateY);
                this.currentLevel.allPlaceables[destinationOfCrateX][destinationOfCrateY] = crate;
                ((Enterable) nextOfCrate).addCrate(crate);
                observer.onCellUpdated(crate);
                moveWorker(destinationX,destinationY);
                if(playback){
                    this.currentLevel.records.add(direction);
                    undoRecords.add(direction);
                    moveCrate.add(true);
                }

                ++this.currentLevel.moveCount;
            }
        }
    }

    public Level getCurrentLevel(){
        return this.currentLevel;
    }

    public void setObserver(Observer observer){
        this.observer = observer;
    }

}
