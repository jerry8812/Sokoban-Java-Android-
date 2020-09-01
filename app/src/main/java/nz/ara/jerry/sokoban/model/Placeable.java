package nz.ara.jerry.sokoban.model;

public abstract class Placeable {

    private int coordinateX;
    private int coordinateY;
    String symbol;

    Placeable(int x, int y) {
        this.coordinateX = x;
        this.coordinateY = y;
    }

    void setSymbol(String newSymbol) {
        this.symbol = newSymbol;
    }

    public int getCoordinateX(){
        return this.coordinateX;
    }

    public int getCoordinateY(){
        return this.coordinateY;
    }
    @Override
    public String toString() {
        return this.symbol;
    }
}
