package com.xuan.game.tetris;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class ActivityRank extends Activity {

    private final RankDatabase mDatabase = null;
    private final ListView mListView = null;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        setTitle("排行");
        setContentView(R.layout.rank);
    }

}
