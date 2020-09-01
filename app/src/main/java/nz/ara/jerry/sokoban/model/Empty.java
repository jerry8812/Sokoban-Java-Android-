package nz.ara.jerry.sokoban.model;

public class Empty extends Placeable implements Enterable {

    private final String symbol = ".";

    Empty(int x, int y) {
        super(x, y);
        super.symbol = this.symbol;
    }

    @Override
    public void addCrate(Crate crate) { setSymbol(crate.symbol); }

}
