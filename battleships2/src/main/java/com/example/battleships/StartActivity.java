package com.example.battleships;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import com.example.battleships.Database.DBHelper;
import com.example.battleships.Fragments.TableFragment;
import com.example.battleships.Logic.*;
import com.example.battleships.View.RowAdapter;

public class StartActivity extends AppCompatActivity {

    private Button btn;
    private Button btn2;
    private Utility.Difficulty dif;
    private ProgressBar progressBar;
    private MediaPlayer click1;
    private MediaPlayer start;
    private MediaPlayer battle;
    TableFragment tableFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        click1 = MediaPlayer.create(this, R.raw.click1);
        start = MediaPlayer.create(this, R.raw.start);
        battle = MediaPlayer.create(this, R.raw.battlestart1);
        btn = ((Button) findViewById(R.id.difficultybtn));
        dif = Utility.Difficulty.EASY;
        progressBar = ((ProgressBar) findViewById(R.id.ProgressBarStart));
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn.getText().equals(getString(R.string.DifficaltyEasy))) {
                    btn.setText(getString(R.string.DifficaltyMedium));
                    dif = Utility.Difficulty.MEDIUM;
                    tableFragment.setAdapter(2);
                } else if (btn.getText().equals(getString(R.string.DifficaltyMedium))) {
                    btn.setText(getString(R.string.DifficaltyHard));
                    dif = Utility.Difficulty.HARD;
                    tableFragment.setAdapter(3);
                } else {
                    btn.setText(getString(R.string.DifficaltyEasy));
                    dif = Utility.Difficulty.EASY;
                    tableFragment.setAdapter(1);
                }
                tableFragment.chooseNone();
                tableFragment.updateViews();
                click1.start();
            }
        });

        btn2 = ((Button) findViewById(R.id.startbtn));
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(ProgressBar.VISIBLE);
                startGame();
            }
        });
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        tableFragment = new TableFragment();
        tableFragment.setStartActivity(StartActivity.this);
        fragmentTransaction.add(R.id.FragmentTable,tableFragment);
        fragmentTransaction.commit();
        battle.start();
    }

    public void startGame() {
        Intent intent = new Intent(this, BoardActivity.class);
        intent.putExtra(Utility.DIFFICULTYTAG, dif.toString());
        start.start();
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        battle.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        battle.start();
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        battle.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        battle.start();
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        battle.pause();
    }
}
