package com.example.battleships.Logic;

/**
 * Created by User on 03/12/2017.
 */

public class BattleShipPart {

    private Utility.Direction dir;
    private int x;
    private int y;
    private boolean side;// for image
    private boolean hit;

    public BattleShipPart(Utility.Direction dir, int x, int y, boolean side){
        this.dir = dir;
        this.x = x;
        this.y = y;
        this.side = side;
        hit = false;

    }

    public int[] getXY(){
        int[] i ={x,y};
        return i;
    }
    
    public int[] getYX(){
        int[] i ={y,x};
        return i;
    }

    public Utility.Direction getDir() {
        return dir;
    }

    // When placing a new battleship, you should check in each x,y that 1 meter near the x,y in each direction
    // there is no existing battleship Parts
    public boolean location(int x,int y) {
        boolean location;
        if(     (this.x==x)&&(this.y==y)||
                (this.x+1==x)&&(this.y==y)||
                (this.x-1==x)&&(this.y==y)||
                (this.x==x)&&(this.y+1==y)||
                (this.x+1==x)&&(this.y+1==y)||
                (this.x-1==x)&&(this.y-1==y)||
                (this.x+1==x)&&(this.y-1==y)||
                (this.x-1==x)&&(this.y+1==y)||
                (this.x==x)&&(this.y-1==y))
            location = true;
        else
            location = false;

        return location;
    }

    public void hit() {
        this.hit = true;
    }

    public boolean isHit() {
        return hit;
    }



}
