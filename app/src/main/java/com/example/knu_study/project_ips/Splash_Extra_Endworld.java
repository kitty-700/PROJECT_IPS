package com.example.knu_study.project_ips;
/*

su; chmod 777 /data /data/data /data/data/com.example.knu_study.project_ips /data/data/com.example.knu_study.project_ips/files
su; chmod 777 /storage /storage/emulated /storage/emulated/0 //storage/emulated/0 /storage/emulated/0/Download


*/

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

/**
 * Created by KNU-STUDY on 2017-11-29.
 */

public class Splash_Extra_Endworld extends Activity {
    TextView game_over;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_extra_endworld);
        game_over = (TextView) findViewById(R.id.game_over);
        game_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
