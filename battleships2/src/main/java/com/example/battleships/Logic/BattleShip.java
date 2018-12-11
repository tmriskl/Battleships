package com.example.battleships.Logic;

import java.util.Random;

/**
 * Created by User on 03/12/2017.
 */

public class BattleShip {

    private BattleShipPart[] parts;
    private Utility.Direction dir;
    private Utility.ShipState state;

    public BattleShip(Utility.Direction dir, int xi, int yi, int xf, int yf) {
        this.dir = dir;
        state = Utility.ShipState.LIVE;
        build( xi, yi, xf, yf);
    }

    // builds battleship parts - params are accurate and checked before this method is called
    private void build( int xi, int yi, int xf, int yf){
        if(xi>xf){
            int temp = xi;
            xi = xf;
            xf = temp;
            dir = Utility.reverseDir(dir);
        }
        if(yi>yf){
            int temp = yi;
            yi = yf;
            yf = temp;
            dir = Utility.reverseDir(dir);
        }
        if(xi<xf){
            parts = new BattleShipPart[xf-xi+1];
            for(int i = 0; i<parts.length; i++){
                if(i==0)
                    parts[i] = new BattleShipPart(Utility.reverseDir(dir),xi+i, yi,true);
                else if(i==parts.length-1)
                    parts[i] = new BattleShipPart(dir,xi+i, yi,true);
                else
                    parts[i] = new BattleShipPart(dir,xi+i, yi,false);
            }
        }
        else{
            parts = new BattleShipPart[yf-yi+1];
            for(int i = 0; i<parts.length; i++)
                if(i==0)
                    parts[i] = new BattleShipPart(Utility.reverseDir(dir),xi, yi+i,true);
                else if(i==parts.length-1)
                    parts[i] = new BattleShipPart(dir,xi, yi+i,true);
                else
                    parts[i] = new BattleShipPart(dir,xi, yi+i,false);
        }
    }

    public boolean location(int x,int y){
        boolean location = false;
        for(int i = 0;(i<parts.length)&&(!location);i++){
            if(parts[i].location(x,y)){
                location = true;
            }
        }
        return location;

    }


    public Utility.ShipState hitXY(int x,int y){
        for(int i = 0;i<parts.length;i++){
            if((parts[i].getXY()[0] == x)&&(parts[i].getXY()[1] == y)){
                parts[i].hit();
                state = Utility.ShipState.SUNKEN;
                for(i = 0;i<parts.length;i++)
                    if(!parts[i].isHit())
                        state = Utility.ShipState.HIT;
            }
        }
        return state;
    }
    public Utility.ShipState getState(){
        return state;
    }


    public int[] getHitLocation(){
        int k[] = null;
        for (int i = 0 ; (i<parts.length)&&(k==null);i++){
            if(parts[i].isHit())
                k = parts[i].getXY();
        }
        return k;
    }

    public Utility.Direction getDir() {
        return dir;
    }

    public int getSize(){
        return parts.length;
    }

    public int[][] getXY() {
        int location[][] = new int[parts.length][2];
        for (int i = 0; i < parts.length; i++)
            location[i] = parts[i].getXY();
        return location;
    }


    public boolean checkXYHits(int x, int y) {
        for(int i = 0;i<parts.length;i++){
            if((parts[i].getXY()[0] == x)&&(parts[i].getXY()[1] == y)&&(parts[i].isHit())){
                return true;
            }
        }
        return false;
    }

    public boolean checkXY(int x,int y){
        for(int i = 0;i<parts.length;i++){
            if((parts[i].getXY()[0] == x)&&(parts[i].getXY()[1] == y)){
                return true;
            }
        }
        return false;
    }

    public int[] getALivePart() {
        int stop  = (new Random()).nextInt(parts.length);
        boolean first = true;
        for(int i = stop ; (i<stop+parts.length)||first ; i++){
            first = false;
            if(!parts[i%parts.length].isHit()){
                return parts[i%parts.length].getXY();
            }
        }
        return null;
    }

}
