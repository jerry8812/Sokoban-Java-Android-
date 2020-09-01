package nz.ara.jerry.sokoban.model;

class Wall extends Placeable {

    private final String symbol = "#";

    Wall(int x, int y) {
        super(x, y);
        super.symbol = this.symbol;
    }
}
