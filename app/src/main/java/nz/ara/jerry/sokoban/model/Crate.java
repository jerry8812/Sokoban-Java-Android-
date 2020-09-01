package nz.ara.jerry.sokoban.model;

class Crate extends Placeable {

    String symbol = "x";

    Crate(int x, int y) {
        super(x, y);
        super.symbol = this.symbol;
    }
    void setSymbol(){
        super.symbol = "X";
    }
}
