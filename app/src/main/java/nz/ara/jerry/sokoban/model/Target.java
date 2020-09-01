package nz.ara.jerry.sokoban.model;

public class Target extends Placeable implements Enterable {

    private final String symbol = "+";

    Target(int x, int y) {
        super(x, y);
        super.symbol = this.symbol;
    }

    public void addCrate(Crate crate) {
        setSymbol(crate.symbol.toUpperCase());
        crate.setSymbol();
    }
}
