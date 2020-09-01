package nz.ara.jerry.sokoban.model;


class EntityFactory {

	/*protected final static Map<Character, Placeable> placeableMap = new HashMap<Character, Placeable>();
	static {
		placeableMap.put('#', new Wall(0, 0));
		placeableMap.put('.', new Empty(0, 0));
		placeableMap.put('+', new Target(0, 0));
		placeableMap.put('x', new Crate(0, 0));
		placeableMap.put('w', new Worker(0, 0));

	}*/

    static Placeable createPlaceable(char symbol, int x, int y) {
        Placeable entity = null;
        switch (symbol) {
            case '#':
                entity = new Wall(x, y);
                break;
            case '.':
                entity = new Empty(x, y);
                break;
            case 'w':
                entity = new Worker(x, y);
                break;
            case 'x':
                entity = new Crate(x, y);
                break;
            case '+':
                entity = new Target(x, y);
                break;
        }

        return entity;
    }
}
