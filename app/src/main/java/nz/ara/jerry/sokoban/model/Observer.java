package nz.ara.jerry.sokoban.model;

public interface Observer {
    void onCellUpdated(Placeable placeable);
}
