package com.example.battleships;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.battleships.Database.DBHelper;
import com.example.battleships.Logic.TableRow;
import com.example.battleships.Logic.Utility;

/**
 * Created by User on 16/12/2017.
 */

public class ScoreActivity extends AppCompatActivity {
    private String difString;
    private ProgressBar restartProgressBar;
    private MediaPlayer win;
    private MediaPlayer lose;
    private DBHelper dbHelper;
    private Button enter;
    private EditText winnerName;
    private EditText winnerlocation;
    private String score;
    private AnimationDrawable animation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        win = MediaPlayer.create(this,R.raw.win);
        lose = MediaPlayer.create(this,R.raw.lose);
        Intent intent =  getIntent();
        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.Back);
        ((TextView)findViewById(R.id.HitCNT)).setText((String)intent.getStringExtra(Utility.HITTAG));
        ((TextView)findViewById(R.id.MissCNT)).setText((String)intent.getStringExtra(Utility.MISSTAG));
        score = (String)intent.getStringExtra(Utility.SCORETAG);
        ((TextView)findViewById(R.id.FinalScoreCNT)).setText(score);
        difString = intent.getStringExtra(Utility.DIFFICULTYTAG);
        restartProgressBar = (ProgressBar)findViewById(R.id.restartProgressBar);
        restartProgressBar.setVisibility(ProgressBar.INVISIBLE);
        ((Button) findViewById(R.id.restartbtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartGame();
            }
        });
        ((Button) findViewById(R.id.menubtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackToManu();
            }
        });
        LinearLayout linearLayout = findViewById(R.id.WinLoseLayout);

        TextView winlose = ((TextView)findViewById(R.id.WinLose));
        winnerName = ((EditText)findViewById(R.id.WinnersName));
        enter = ((Button)findViewById(R.id.EnterScore));
        winnerlocation = ((EditText)findViewById(R.id.WinnersLocation));
        if(((String)intent.getStringExtra(Utility.WINLOSETAG)).equals(Utility.WIN)){
            winlose.setText(getString(R.string.Win));
            linearLayout.setBackgroundResource(R.drawable.win);
            dbHelper = new DBHelper(getApplicationContext(), Utility.DBNAME, null,Utility.DBVER);
            enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!winnerName.getText().toString().equals("")) {
                        dbHelper.insertScore(new TableRow(winnerName.getText().toString(), Integer.valueOf(score), winnerlocation.getText().toString()), difString);
                        enter.setClickable(false);
                        enter.setBackgroundColor(Color.GREEN);
                    }else{
                        winnerName.setHintTextColor(Color.RED);
                        enter.setBackgroundColor(Color.RED);
                    }

                }

            });
            win.start();
        }else {
            winlose.setText(getString(R.string.Lose));
            linearLayout.setBackgroundResource(R.drawable.lose);
            constraintLayout.setBackgroundResource(R.drawable.sunkenship2);
            winnerName.setVisibility(View.INVISIBLE);
            winnerlocation.setVisibility(View.INVISIBLE);
            enter.setVisibility(View.INVISIBLE);
            enter.setClickable(false);
            lose.start();
        }
        animation = (AnimationDrawable) linearLayout.getBackground();
        animation.start();
    }

    public void restartGame() {
        Intent intent = new Intent(this,BoardActivity.class);
        restartProgressBar.setVisibility(ProgressBar.VISIBLE);
        intent.putExtra(Utility.DIFFICULTYTAG,difString);
        startActivity(intent);
        restartProgressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    public void BackToManu() {
        Intent intent = new Intent(this,StartActivity.class);
        restartProgressBar.setVisibility(ProgressBar.VISIBLE);
        startActivity(intent);
        restartProgressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        win.stop();
        lose.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        win.stop();
        lose.stop();
    }
}
