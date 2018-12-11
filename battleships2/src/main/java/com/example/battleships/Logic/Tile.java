package com.example.battleships.Logic;

import android.widget.Button;

/**
 * Created by User on 03/12/2017.
 */


public class Tile{

    private Utility.TileState state;
    private int x;
    private int y;
    private boolean touchableByPlayer;
    private boolean transperent;
    private boolean hasShip;
    private boolean touched;
    private boolean first;

    private Utility.Direction direction;

    public Tile(int x, int y, boolean hasShip, boolean touchableByPlayer) {
        this.x = x;
        this.y = y;
        this.hasShip = hasShip;
        this.touchableByPlayer = touchableByPlayer;
        transperent = !touchableByPlayer;
        state = Utility.TileState.NONE;
        touched = false;
        first = true;
    }

    public boolean isPlayer() {
        return touchableByPlayer;
    }

    public boolean onFire(){
        if(state == Utility.TileState.NONE){
            touched = true;
            if(hasShip)
                state = Utility.TileState.HIT;
            else
                state = Utility.TileState.MISS;
            return true;
        }
        return false;
    }

    public void makeTransperent(){
        transperent=true;
        if(state == Utility.TileState.NONE){
            state = Utility.TileState.CLOSE;
        }
    }

    public boolean isTransperent() {
        return transperent;
    }

    public boolean isHasShip() {
        return hasShip;
    }


    public void setDirection(Utility.Direction direction) {
        this.direction = direction;
    }

    public Utility.Direction getDirection() {
        return direction;
    }

    public Utility.TileState getState() {
        return state;
    }

    public void setState(Utility.TileState state) {
        this.state = state;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    public boolean isTouched() {
        return touched;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }
}
