package nz.ara.jerry.sokoban.model;

import java.util.HashMap;

import nz.ara.jerry.sokoban.R;

public class LookUpTable {
    public final static HashMap<Integer, Direction> hashMap = new HashMap<Integer, Direction>() {
        {
            put(R.id.btn_down_arrow_normal, Direction.DOWN);
            put(R.id.btn_up_arrow_normal, Direction.UP);
            put(R.id.btn_left_arrow_normal, Direction.LEFT);
            put(R.id.btn_right_arrow_normal, Direction.RIGHT);
        }
    };
    public final static HashMap<String, Integer> imageViewMap = new HashMap<String, Integer>() {
        {
            put("#", R.drawable.wall);
            put(".", R.drawable.empty);
            put("+", R.drawable.flag_removebg);
            put("w", R.drawable.man_no_background);
            put("X", R.drawable.crateontarget);
            put("x", R.drawable.box);
            put("W", R.drawable.man_no_background);
        }
    };
}
