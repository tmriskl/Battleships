package com.example.battleships.Services;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.battleships.BoardActivity;
import com.example.battleships.Logic.Game;

/**
 * Created by User on 12/01/2018.
 */

public class SensorsService extends Service{
    private final IBinder binder = new LocalService();
    private Sensors sensors;
    private Game game;
    private BoardActivity boardActivity;
//    private boolean sensor1 = false;
//    private boolean sensor2 = false;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setBoardActivity(BoardActivity boardActivity) {
        this.boardActivity = boardActivity;
    }

    public void setSensor(SensorManager sensorManager){
        sensors = new Sensors(sensorManager);
    }


    public class LocalService extends Binder{
        public SensorsService getService(){
            return SensorsService.this;
        }
    }

    public class Sensors implements SensorEventListener {

        private SensorManager sensorManager;
        private Sensor sensor;
        private long count = 0;
        private int countTo = 10;
        private int lengthVal = 3;
        private float[] avg;
        private float[][] lastValues;
        private float[] stay = new float[lengthVal];
        private int times;
        private boolean first = true;
        private int moveShipCount = 100;


        public Sensors(SensorManager sensorManager){
            this.sensorManager = sensorManager;
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            sensorManager.registerListener(Sensors.this,sensor,SensorManager.SENSOR_DELAY_NORMAL);

            avg = new float[lengthVal];
            lastValues = new float[countTo][lengthVal];
            for(float[] ff: lastValues)
                for (float f: ff) {
                    f = 0;
                }
            for (float f: avg) {
                f = 0;
            }
            times = 0;

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if(first){
                first = false;
                for(int i = 0;i<lastValues.length;i++)
                    for(int j = 0;j<lastValues[0].length;j++)
                        lastValues[i][j] = sensorEvent.values[j];
            }
            for(int i = 0;i<lastValues[0].length;i++)
                lastValues[(int)(count % countTo)][i] = sensorEvent.values[i];
//                if (count >= 2*countTo) {

                for (int j = 0; j < lastValues[0].length; j++) {
                    for (int i = 0; i < lastValues.length; i++) {
                        if (i == 0)
                            avg[j] = 0 + lastValues[i][j];
                        else
                            avg[j] += lastValues[i][j];
                    }
                    avg[j] /= countTo;
                    if(count == 2*countTo){
                        for (int i = 0; i < stay.length; i++)
                            stay[i] = avg[i];
                    }
                    if (!Check(stay, avg)) {
                        //sensor2 = true;
                        //if(sensor1) {
                           // Toast.makeText(getApplicationContext(), "stay != avg", Toast.LENGTH_LONG).show();
                            times++;
                            if (times % moveShipCount == 0) {
                                game.getPlayerBattlefiedBoard().moveShip();

                                boardActivity.updateViews();
                            }
                            if (times >= moveShipCount * 10) {
                                times = 0;
                                if (!game.getComBattlefiedBoard().LastShip()) {
                                    int[] location = game.getComBattlefiedBoard().getALivePart();
                                    game.getComBattlefiedBoard().onFire(location[0], location[1]);
                                }
                            }
                            boardActivity.updateViews();
                       // }
                    } else {
                        //sensor2 = false;
                        //Toast.makeText(getApplicationContext(),"stay = avg", Toast.LENGTH_LONG).show();
                        times = 0;
                    }
                }
                count++;
//            }
        }

        public boolean Check(float[] f1,float[] f2){
            for(int i = 0; i< f1.length; i++){
                float check = f1[i] - f2[i];
                if(check<0)
                    check = -check;
                if(check>20)
                    return false;
            }
            return true;
        }
    }

    private void change(){

    }

}
