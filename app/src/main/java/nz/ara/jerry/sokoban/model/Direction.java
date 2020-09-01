package nz.ara.jerry.sokoban.model;

public enum Direction {
    UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1);

    public final int adjustX;
    public final int adjustY;

    Direction(int adjustX, int adjustY) {
        this.adjustX = adjustX;
        this.adjustY = adjustY;
    }
}
