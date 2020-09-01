package nz.ara.jerry.sokoban;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayLevelsActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "nz.ara.jerry.sokoban.DisplayLevelsActivity";
    private ArrayList<String> allLevelsName;
    private ConstraintLayout layout;
    private TextView chooseLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_levels);
        layout = findViewById(R.id.levelsLayout);
        chooseLevel = findViewById(R.id.textView);
        allLevelsName = (ArrayList<String>) getIntent().getSerializableExtra(MainActivity.ALL_LEVELS);
        for (String levelName : allLevelsName){
            createButton(levelName);
        }

    }

    private void createButton(String levelName){
        final Button btn = new Button(this);
        final int btnId = allLevelsName.indexOf(levelName);
        btn.setId(btnId);
        btn.setText(levelName);
        btn.setHeight(50);
        btn.setWidth(1060);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = v.getId();
                returnReply(index);
            }
        });
        ConstraintSet constraintSet = new ConstraintSet();
        layout.addView(btn);
        constraintSet.clone(layout);
        if (btnId == 0){
            constraintSet.connect(btn.getId(), ConstraintSet.TOP, chooseLevel.getId(), ConstraintSet.BOTTOM,30);
        }else{
            int margin = 30 + 100 * btnId;
            constraintSet.connect(btn.getId(), ConstraintSet.TOP, chooseLevel.getId(), ConstraintSet.BOTTOM, margin);
        }
        constraintSet.connect(btn.getId(), ConstraintSet.LEFT, layout.getId(), ConstraintSet.LEFT,10);
        constraintSet.applyTo(layout);
    }
    public void returnReply(int index) {
        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_REPLY, String.valueOf(index));
        setResult(RESULT_OK, replyIntent);
        finish();
    }
}
