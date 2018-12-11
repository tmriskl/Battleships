package com.example.battleships;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.battleships.Logic.*;
import com.example.battleships.Services.SensorsService;
import com.example.battleships.View.TileAdapter;

public class BoardActivity extends AppCompatActivity {

    private Game game;
    private GridView gridBoard;
    private TileAdapter playerAdapter;
    private TileAdapter comAdapter;
    private Intent intent;
    private TextView ship1;
    private TextView ship2;
    private TextView ship3;
    private TextView ship4;
    private TextView turn;
    private ComputerPlayer comp;
    private ProgressBar progressBar;
    private MediaPlayer explosion;
    private MediaPlayer battle;
    private boolean canPlay;
    private String difString;
    private Button switchBoard;
    public  Button moveShip;
    private MediaPlayer click1;
    private SensorsService sensorsService;
    private boolean isBind = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SensorsService.LocalService localService = (SensorsService.LocalService) iBinder;
            sensorsService = localService.getService();
            sensorsService.setGame(game);
            sensorsService.setBoardActivity(BoardActivity.this);
            sensorsService.setSensor((SensorManager) getSystemService(Context.SENSOR_SERVICE));
            isBind = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBind = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        explosion = MediaPlayer.create(this, R.raw.explosion1);
        battle = MediaPlayer.create(this, R.raw.battle1);
        battle.setLooping(true);
        click1 = MediaPlayer.create(this,R.raw.click1);
        intent = getIntent();
        difString = intent.getStringExtra(Utility.DIFFICULTYTAG);
        int dif = Utility.difOrdinal(difString);
        game = new Game(Utility.Difficulty.values()[dif]);
        canPlay = true;
        switchBoard = (Button)findViewById(R.id.switchbtn);
        comp = new ComputerPlayer(game);
        gridBoard = (GridView) findViewById(R.id.playerTerritory);
        ship1 = (TextView) findViewById(R.id.Remaining1View);
        ship2 = (TextView) findViewById(R.id.Remaining2View);
        ship3 = (TextView) findViewById(R.id.Remaining3View);
        ship4 = (TextView) findViewById(R.id.Remaining4View);
        turn = (TextView) findViewById(R.id.turnView);
        comp = new ComputerPlayer(game);
        updateShips(true);
        playerAdapter = new TileAdapter(getApplicationContext(), game.getPlayerBattlefiedBoard());
        comAdapter = new TileAdapter(getApplicationContext(), game.getComBattlefiedBoard());
        gridBoard.setAdapter(playerAdapter);
        gridBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view,
                                    int position,
                                    long id) {
                if (canPlay) {
                    if (game.isPlayerTurn()) {
                        if (game.playTurn(position / 10, position % 10)) {
                            progressBar.setVisibility(ProgressBar.VISIBLE);
                            scoreBoard(Utility.WIN);
                            canPlay = false;
                        }
                        canPlay = game.isPlayerTurn();
                        explosion.start();
                        updateShips(true);
                        updateViews();
                    }

                    if (!game.isPlayerTurn()) {
                        progressBar.setVisibility(ProgressBar.VISIBLE);
                        Thread t = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                pause();
                                toggleBoard();
                                while (!game.isPlayerTurn()) {
                                    pause();
                                    if (comp.playTurn()) {
                                        scoreBoard(Utility.LOSE);
                                        canPlay = false;
                                    }
                                    explosion.start();
                                    updateViews();
                                    pause();
                                }

                                canPlay = true;
                                toggleBoard();
                                progressBar.setVisibility(ProgressBar.INVISIBLE);


                            }

                        });

                        t.start();
                    }
                }
            }
        });
        switchBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBoard();
                canPlay = !canPlay;
                click1.start();
            }
        });

        Intent serviceIntent = new Intent(this, SensorsService.class);
        bindService(serviceIntent,serviceConnection, Context.BIND_AUTO_CREATE);
        battle.start();

    }

    private void scoreBoard(String winLose) {
        Intent intent = new Intent(this, ScoreActivity.class);
        String[] score = game.getScore(winLose);
        intent.putExtra(Utility.WINLOSETAG, winLose);
        intent.putExtra(Utility.HITTAG, score[0]);
        intent.putExtra(Utility.MISSTAG, score[1]);
        intent.putExtra(Utility.SCORETAG, score[2]);
        intent.putExtra(Utility.DIFFICULTYTAG, difString);
        startActivity(intent);
        battle.stop();
    }

    public void updateViews() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TileAdapter) gridBoard.getAdapter()).notifyDataSetChanged();
            }
        });
    }

    private void pause() {
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void updateShips(boolean player) {
        int[] remainingShips = game.getRemainingShips(player);
        ship1.setText(remainingShips[0] + " : 1");
        ship2.setText(remainingShips[1] + " : 2");
        ship3.setText(remainingShips[2] + " : 3");
        ship4.setText(remainingShips[3] + " : 4");
    }

    private void toggleBoard() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (gridBoard.getAdapter().equals(comAdapter)) {
                    gridBoard.setAdapter(playerAdapter);
                    turn.setText(R.string.PlyrTurn);
                    updateShips(true);
                } else {
                    gridBoard.setAdapter(comAdapter);
                    turn.setText(R.string.CompTurn);
                    updateShips(false);
                }
            }
        });
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
        if(isBind) {
            unbindService(serviceConnection);
            isBind = false;
        }
        battle.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        battle.stop();
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
        battle.stop();
    }
}
