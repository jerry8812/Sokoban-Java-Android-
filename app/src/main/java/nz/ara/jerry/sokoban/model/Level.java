package nz.ara.jerry.sokoban.model;


import java.util.ArrayList;
import java.util.List;

public class Level {

    private final String name;
    private final int height;
    private final int width;
    private final String map;
    int moveCount = 0;
    private int targetCount = 0;
    public Placeable[][] allPlaceables;
    private int completedCount = 0;
    Point point;
    public List<Direction> records = new ArrayList<>();

    Level(String levelName, int width, int height, String map) {
        this.name = levelName;
        this.height = height;
        this.width = width;
        this.map = map;
        initGameBoard();
    }

    public void initGameBoard() {
        this.allPlaceables = new Placeable[this.height][this.width];
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                char symbol = map.charAt(i * width + j);
                if ('w' == symbol) {
                    this.point = new Point(i, j);
                } else if ('+' == symbol) {
                    ++this.targetCount;
                }
                this.allPlaceables[i][j] = EntityFactory.createPlaceable(symbol, i, j);
            }
        }
    }

    public int getCompletedCount() {
        this.completedCount = 0;
        for (Placeable[] allPlaceable : this.allPlaceables) {
            for (Placeable placeable : allPlaceable) {
                if ("X".equals(placeable.toString())) {
                    ++completedCount;
                }
            }
        }
        return this.completedCount;
    }

    char getSymbolFromMap(int x, int y){
        return this.map.charAt(width * x + y);
    }
    public void setTargetCount(int targetCount) {
        this.targetCount = targetCount;
    }
    public void setMoveCount(int moveCount){
        this.moveCount = moveCount;
    }

    public String getName(){
        return this.name;
    }
    public int getTargetCount() {
        return this.targetCount;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getMoveCount() {
        return this.moveCount;
    }
}
