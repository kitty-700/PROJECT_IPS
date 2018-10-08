package com.example.knu_study.project_ips;
/*

su; chmod 777 /data /data/data /data/data/com.example.knu_study.project_ips /data/data/com.example.knu_study.project_ips/files
su; chmod 777 /storage /storage/emulated /storage/emulated/0 //storage/emulated/0 /storage/emulated/0/Download


*/

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by KNU-STUDY on 2017-11-29.
 */

public class SplashActivity extends Activity {
    //android:theme="@style/SplashTheme" 메니페스토 이 액티비티에 적용하는 방법을 원래 썼지만 새롭게 해보고싶음
    final String password = "242";

    SplashActivity splashActivity;
    ImageView door;
    ImageView house_center;
    ImageView f1_left;
    ImageView f1_right;
    ImageView f2_left;
    ImageView f2_right;
    ImageView car;
    ImageView yasuo;
    ImageView kitty;

    FrameLayout developer;
    FrameLayout includer;
    LinearLayout quote_layout;
    ImageView quote_picture;
    TextView quote_name;
    TextView quote;
    MediaPlayer mp;
    String password_input = "";
    int yasuo_turn_count = 0;
    int ya_kill = 0;
    int ya_death = 0;
    int ya_assist = 0;

    boolean is_updating_sites = false;
    boolean is_developer_menu_on = false;
    boolean is_music_on_order = false;
    boolean is_door_opened = false;
    boolean is_f1_left_light = false;
    boolean is_f1_right_light = false;
    boolean is_f2_left_light = false;
    boolean is_f2_right_light = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        door = (ImageView) findViewById(R.id.door);
        f1_left = (ImageView) findViewById(R.id.f1_left);
        f1_right = (ImageView) findViewById(R.id.f1_right);
        f2_left = (ImageView) findViewById(R.id.f2_left);
        f2_right = (ImageView) findViewById(R.id.f2_right);
        house_center = (ImageView) findViewById(R.id.house_center);
        car = (ImageView) findViewById(R.id.car);
        yasuo = (ImageView) findViewById(R.id.yasuo);
        kitty = (ImageView) findViewById(R.id.kitty);
        developer = (FrameLayout) findViewById(R.id.developer);
        includer = (FrameLayout) findViewById(R.id.includer);
        quote_layout = (LinearLayout) findViewById(R.id.quote_layout);
        quote_picture = (ImageView) findViewById(R.id.quote_picture);
        quote_name = (TextView) findViewById(R.id.quote_name);
        quote = (TextView) findViewById(R.id.quote);
        mp = MediaPlayer.create(this, R.raw.myhouse);
        mp.setLooping(true);
        splashActivity = this;
        kitty.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (password_input.equals("1234")) {
                    Person_Management.fastToast(splashActivity,"만드는거 개힘듬 ㅠ");
                    password_input = "";
                } else {
                    startActivity(new Intent(splashActivity, Splash_Extra_Endworld.class));
                    finish();
                }
                return true;
            }
        });
        kitty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_developer_menu_on == false)
                    developer.setVisibility(VISIBLE);
            }
        });
        developer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                developer.setVisibility(GONE);
            }
        });
        yasuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ya_kill += ((int) (Math.random() * 10)) % 3;
                ya_death += ((int) (Math.random() * 10)) % 6;
                ya_assist += ((int) (Math.random() * 10)) % 2;
                quote.setText("[ " + ya_kill + " / " + ya_death + " / " + ya_assist + " ]");
                if (yasuo_turn_count++ >= 5) {
                    double kda = (double) (ya_kill + ya_assist) / (double) ya_death;
                    if (kda >= 1.2) {
                        password_input = password_input + "0";
                        quote.setText("[ 승리 ]");
                    } else
                        quote.setText("[ 패배 ]");
                    ya_kill = ya_death = ya_assist = yasuo_turn_count = 0;
                }
                quote_name.setText("야스오");
                quote_picture.setImageResource(R.mipmap.yasuopicture);
                quote_layout.setVisibility(VISIBLE);
            }
        });
        yasuo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                password_input = password_input + "5";
                return true;
            }
        });
        quote_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                quote.setText("");
                quote_name.setText("");
                quote_picture.setImageResource(R.drawable.none);
                quote_layout.setVisibility(GONE);
                return true;
            }
        });
        car.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mp.isPlaying()) {
                    is_music_on_order = false;
                    mp.pause();
                    car.setImageResource(R.drawable.car);
                } else {
                    mp.start();
                    is_music_on_order = true;
                    car.setImageResource(R.drawable.car_light);
                }
                return true;
            }
        });
        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quote_name.setText("자동차");
                if (password_input.equals(""))
                    quote.setText("idle ...");
                else {
                    quote.setText(password_input + " rpm ...");
                    password_input = "";
                }
                quote_picture.setImageResource(R.mipmap.car_pic);
                quote_layout.setVisibility(VISIBLE);
            }
        });
        house_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password_input = password_input + "8";
            }
        });
        house_center.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                password_input = password_input + "9";
                return true;
            }
        });
        door.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (is_door_opened == true)
                    if (password_input.equals(password)) {
                        password_input = "";
                        is_updating_sites = true;
                        startActivity(new Intent(splashActivity, MainActivity.class));
                    }
                return true;
            }
        });
        door.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (is_door_opened == false) {
                    door.setImageResource(R.drawable.opend_door);
                    is_door_opened = true;
                } else {
                    door.setImageResource(R.drawable.closed_door);
                    is_door_opened = false;
                }
            }
        });
        f1_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password_input = password_input + "1";
                if (is_f1_left_light == false) {
                    f1_left.setImageResource(R.drawable.f1_left_light);
                    is_f1_left_light = true;
                } else {
                    f1_left.setImageResource(R.drawable.f1_left);
                    is_f1_left_light = false;
                }
            }
        });
        f1_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password_input = password_input + "2";
                if (is_f1_right_light == false) {
                    f1_right.setImageResource(R.drawable.f1_right_light);
                    is_f1_right_light = true;
                } else {
                    f1_right.setImageResource(R.drawable.f1_right);
                    is_f1_right_light = false;
                }
            }
        });
        f2_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password_input = password_input + "3";
                if (is_f2_left_light == false) {
                    f2_left.setImageResource(R.drawable.f2_left_light);
                    is_f2_left_light = true;
                } else {
                    f2_left.setImageResource(R.drawable.f2_left);
                    is_f2_left_light = false;
                }
            }
        });
        f2_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password_input = password_input + "4";
                if (is_f2_right_light == false) {
                    f2_right.setImageResource(R.drawable.f2_right_light);
                    is_f2_right_light = true;
                } else {
                    f2_right.setImageResource(R.drawable.f2_right);
                    is_f2_right_light = false;
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        door.setImageResource(R.drawable.closed_door);
        is_door_opened = false;
        password_input = "";
        f1_left.setImageResource(R.drawable.f1_left);
        is_f1_left_light = false;
        f1_right.setImageResource(R.drawable.f1_right);
        is_f1_right_light = false;
        f2_left.setImageResource(R.drawable.f2_left);
        is_f2_left_light = false;
        f2_right.setImageResource(R.drawable.f2_right);
        is_f2_right_light = false;
        if (is_music_on_order == true)
            mp.pause();
        if (is_updating_sites == false)
            finish();
    }

    protected void onRestart() {
        super.onRestart();
        is_updating_sites = false;
        if (is_music_on_order == true)
            mp.start();
    }
}
