package com.example.battleships.Logic;

import android.widget.Button;

/**
 * Created by User on 01/12/2017.
 */

public class Utility {
    public static final String DBNAME = "SCOREDATA";
    public static final int DBVER = 1;
    public static final int BOARDSIZE = 10;
    public static final int TABLENUMOFROWS = 10;
    public static final int SCORETEXTLENGTH = 12;
    public static final String GAMETAG="GAME";
    public static final String DIFFICULTYTAG="DIFFICULTY";
    public static final int MAXSHIPS = 14;
    public static final int DELTASHIP = 4;
    public static final int MAXSHIP1 = 5;
    public static final int MAXSHIP2 = 4;
    public static final int MAXSHIP3 = 3;
    public static final int MAXSHIP4 = 2;
    public static final int MAXSHIPSIZE = 4;
    public static final String WINLOSETAG = "WINLOSE";
    public static final String LOSE = "LOSE";
    public static final String WIN = "WIN";
    public static final String HITTAG = "HIT";
    public static final String MISSTAG = "MISS";
    public static final String SCORETAG = "SCORE";
    public static final int MAXCOMPTRYS = 5;

    public enum Direction{
        NORTH, EAST, SOUTH, WEST, NONE
    }
    public static Direction reverseDir(Direction dir){
        switch(dir.ordinal()){
            case 0:
                return Direction.SOUTH;
            case 1:
                return Direction.WEST;
            case 2:
                return Direction.NORTH;
            case 3:
                return Direction.EAST;
            default:
                return Direction.NONE;
        }
    }
    public enum TileState{
        NONE, HIT, MISS, CLOSE
    }

    public enum Result{
        HIT, MISS, UNTOUCHABLE
    }

    public enum Difficulty{
        EASY,MEDIUM,HARD
    }

    public enum ShipState{
        LIVE, HIT, SUNKEN
    }

    public static int numOfShips(Difficulty dif){
        return (MAXSHIPS - dif.ordinal()*DELTASHIP);
    }

    public static int difOrdinal(String str){
        int i =  Difficulty.EASY.ordinal();
        if(str.equals(Difficulty.MEDIUM.toString()))
            i = Difficulty.MEDIUM.ordinal();
        else if(str.equals(Difficulty.HARD.toString()))
            i =  Difficulty.HARD.ordinal();
        return i;

    }




}
