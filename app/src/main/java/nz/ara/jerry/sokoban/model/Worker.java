package nz.ara.jerry.sokoban.model;

class Worker extends Placeable {

    final String symbol = "w";

    Worker(int x, int y) {
        super(x, y);
        super.symbol = this.symbol;
    }
}
