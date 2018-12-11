package com.example.battleships.Logic;

import java.util.Random;

/**
 * Created by User on 03/12/2017.
 */

public class Board {

    private final int dupliator = 18;
    private int numOfSunkenShips;
    private Tile[][] tiles;
    private BattleShip[] battleships;
    private boolean win;
    private int hit;
    private int miss;
    private int difMul;
    private Utility.Direction dir;
    private int[] remainingShips;


    public Board(Utility.Difficulty dif, boolean touchableByPlayer) {
        win = false;
        numOfSunkenShips = 0;
        battleships = new BattleShip[Utility.numOfShips(dif)];
        remainingShips = new int[Utility.MAXSHIPSIZE];
        difMul = (dif.ordinal()*dupliator+5);
        generateShips(dif);
        generateTiles(touchableByPlayer);
        hit = 0;
        miss = 0;
        remainingShips[0] = Utility.MAXSHIP1 - dif.ordinal();
        remainingShips[1] = Utility.MAXSHIP2 - dif.ordinal();
        remainingShips[2] = Utility.MAXSHIP3 - dif.ordinal();
        remainingShips[3] = Utility.MAXSHIP4 - dif.ordinal();

    }

    public boolean isPlayer(){
        return tiles[0][0].isPlayer();
    }

    public Tile getTile(int x, int y){
        return tiles[y][x];
    }
    //public Tile(int x, int y, boolean hasShip, boolean touchableByPlayer)

    private void generateTiles(boolean touchableByPlayer) {
        tiles = new Tile[Utility.BOARDSIZE][Utility.BOARDSIZE];
        for (int i = 0; i < Utility.BOARDSIZE; i++)
            for (int j = 0; j < Utility.BOARDSIZE; j++) {
                int k;
                boolean hasShip = false;
                for (k = 0; (k < battleships.length) && (!hasShip); k++)
                    if (battleships[k].checkXY(j, i)) {
                        hasShip = true;
                    }
                k--;
                tiles[i][j] = new Tile(j, i, hasShip, true);//touchableByPlayer);
                tiles[i][j].setDirection(battleships[k].getDir());
            }

    }

    private void generateShips(Utility.Difficulty dif) {
        int counter1 = Utility.MAXSHIP1 - dif.ordinal();//3-5
        int counter2 = Utility.MAXSHIP2 - dif.ordinal();//2-4
        int counter3 = Utility.MAXSHIP3 - dif.ordinal();//1-3
        int counter4 = Utility.MAXSHIP4 - dif.ordinal();//0-2
        int[] location;
        for (int i = 0; i < battleships.length; i++) {
            if(counter4 != 0) {
                location = generateShip(4);
                battleships[i] = new BattleShip(dir, location[0], location[1], location[2], location[3]);
                counter4--;
            }
            else if(counter3 != 0) {
                location = generateShip(3);
                battleships[i] = new BattleShip(dir, location[0], location[1], location[2], location[3]);
                counter3--;
            }
            else if(counter2 != 0) {
                location = generateShip(2);
                battleships[i] = new BattleShip(dir, location[0], location[1], location[2], location[3]);
                counter2--;
            }
            else if(counter1 != 0){
                location = generateShip(1);
                battleships[i] = new BattleShip(dir, location[0], location[1], location[2], location[3]);
                counter1--;
            }
        }
    }

    private int[] generateShip(int size) {
        int xi = 0;
        int yi = 0;
        int xf = 0;
        int yf = 0;
        boolean notOK = true;
        dir = Utility.Direction.NONE;

        while (notOK) {
            notOK = false;
            xi = (new Random()).nextInt(10);
            yi = (new Random()).nextInt(10);
            // flags to tell if the potential battleship can be placed in direction (north/south/east/west)
            boolean north = true, south = true, east = true, west = true;
            if(size>1) {
                if (xi + size - 1 >= Utility.BOARDSIZE)
                    east = false;
                if (xi - size + 1 < 0)
                    west = false;
                if (yi + size - 1 >= Utility.BOARDSIZE)
                    south = false;
                if (yi - size + 1 < 0)
                    north = false;
            }
            // If you can go either East/West/North/South - Check that you don't override existing BattleShips
            // this runs on all existing remainingShips
            for (int i = 0; (i < battleships.length) && (east||west||south||north)&&(battleships[i] != null); i++) {
                // this for checks all the potential battleship parts for existing ship[i]
                for (int j = 0; (j < size) && (east||west||south||north); j++) {
                    if (east && battleships[i].location(xi + j, yi))
                        east = false;
                    if (west && battleships[i].location(xi - j, yi))
                        west = false;
                    if (south && battleships[i].location(xi, yi + j))
                        south = false;
                    if (north && battleships[i].location(xi, yi - j))
                        north = false;

                }
            }
            if (east) {
                dir = Utility.Direction.EAST;
                xf = xi + size - 1;
                yf = yi;
            } else if (north) {
                dir = Utility.Direction.NORTH;
                xf = xi;
                yf = yi - size + 1;
            } else if (south) {
                dir = Utility.Direction.SOUTH;
                xf = xi;
                yf = yi + size - 1;
            } else if (west) {
                dir = Utility.Direction.WEST;
                xf = xi - size + 1;
                yf = yi;
            } else {
                notOK = true;
            }
        }
        int[] i = {xi, yi, xf, yf};
        return i;
    }

    public Utility.Result onFire(int x, int y) {
        Utility.Result result = null;
        if (tiles[y][x].onFire()) {
            if (tiles[y][x].isHasShip()) {
                result = Utility.Result.HIT;
                hit++;
                boolean found = false;
                int[][] k = null;
                for (int i = 0; (i < battleships.length) && (!found); i++) {
                    if (battleships[i].checkXY(x, y)) {
                        //check if sunken- write a function in battleship to check if all parts are hit,
                        // if yes then send here to another function that will make all tiles in ship and around (+1)
                        // as transparent
                        found = true;
                        if (battleships[i].hitXY(x, y) == Utility.ShipState.SUNKEN) {
                            k = battleships[i].getXY();
                            numOfSunkenShips++;
                            remainingShips[battleships[i].getSize() - 1]--;
                            if (numOfSunkenShips >= battleships.length)
                                win = true;
                        }
                    }
                }

                if (k != null)
                    for (int i = 0; i < k.length; i++) {
                        int yc = k[i][1], xc = k[i][0];
                        tiles[yc][xc].makeTransperent();
                        if (xc < Utility.BOARDSIZE - 1)
                            tiles[yc][xc + 1].makeTransperent();
                        if (xc > 0)
                            tiles[yc][xc - 1].makeTransperent();

                        if (yc < Utility.BOARDSIZE - 1) {
                            tiles[yc + 1][xc].makeTransperent();
                            if (xc < Utility.BOARDSIZE - 1)
                                tiles[yc + 1][xc + 1].makeTransperent();
                            if (xc > 0)
                                tiles[yc + 1][xc - 1].makeTransperent();
                        }

                        if (yc > 0) {
                            tiles[yc - 1][xc].makeTransperent();
                            if (xc < Utility.BOARDSIZE - 1)
                                tiles[yc - 1][xc + 1].makeTransperent();
                            if (xc > 0)
                                tiles[yc - 1][xc - 1].makeTransperent();
                        }
                    }


            }else {
                result = Utility.Result.MISS;
                miss++;
            }
        }
        if(result == null)
            result = Utility.Result.UNTOUCHABLE;
        return result;


    }

    public int[] getHitLocation(){
        int k[] = null;
        for (int i = 0; (i < battleships.length)&&(k==null); i++){
            if(battleships[i].getState() == Utility.ShipState.HIT){
                k = battleships[i].getHitLocation();
            }
        }
        return k;
    }

    public boolean isWin() {
        return win;
    }

    public Utility.TileState getTileState(int x,int y){
        if((x>=Utility.BOARDSIZE)||
                (y>=Utility.BOARDSIZE)||
                (x<0)||
                (y<0))
            return null;
        return tiles[y][x].getState();
    }

    public int[] getRemainingShips() {
        return remainingShips;
    }

    public String[] getScore(String winLose) {
        String[] str = new String[3];
        double mul;
        if(winLose.equals(Utility.WIN))
            mul = 2;
        else
            mul = 0.5;
        str[0] = (hit)+"";
        str[1] = (miss)+"";
        str[2] = ((int)((hit+10)*mul*difMul - 3.0*dupliator*miss/difMul))+"";
        //dupliator = 10
//1*3 +2*2 +3*1 +4*0=10  20*2*30  - 3*10*90/30  ~ 1200 - 90
//1*4 +2*3 +3*2 +4*1=10  30*2*20  - 3*10*80/20  ~ 1200 - 120
//1*5 +2*4 +3*3 +4*2=10  40*2*10  - 3*10*70/10  ~ 800  - 210
        //dupliator = 15
//1*3 +2*2 +3*1 +4*0=10  20*2*40  - 3*15*90/40  ~ 1600 - 101.25
//1*4 +2*3 +3*2 +4*1=10  30*2*25  - 3*15*80/25  ~ 1500 - 144
//1*5 +2*4 +3*3 +4*2=10  40*2*10  - 3*15*70/10  ~ 800  - 315
        //dupliator = 18
//1*3 +2*2 +3*1 +4*0=10  20*2*46  - 3*18*90/46  ~ 1840 - 105.65
//1*4 +2*3 +3*2 +4*1=10  30*2*28  - 3*18*80/28  ~ 1680 - 154.29
//1*5 +2*4 +3*3 +4*2=10  40*2*10  - 3*18*70/10  ~ 800  - 378
        //dupliator = 20
//1*3 +2*2 +3*1 +4*0=10  20*2*50  - 3*20*90/50  ~ 2000 - 118
//1*4 +2*3 +3*2 +4*1=10  30*2*30  - 3*20*80/30  ~ 1800 - 160
//1*5 +2*4 +3*3 +4*2=10  40*2*10  - 3*20*70/10  ~ 800  - 420
        //dupliator = 40
//1*3 +2*2 +3*1 +4*0=10  20*2*110 - 3*40*90/110 ~ 4400 - 98.98
//1*4 +2*3 +3*2 +4*1=10  30*2*50  - 3*40*80/50  ~ 3000 - 192
//1*5 +2*4 +3*3 +4*2=10  40*2*10  - 3*40*70/10  ~ 800  - 840

        return str;
    }

    private void reGenerateTiles() {
        Tile[][] tiles2 = new Tile[Utility.BOARDSIZE][Utility.BOARDSIZE];
        for (int i = 0; i < Utility.BOARDSIZE; i++)
            for (int j = 0; j < Utility.BOARDSIZE; j++) {
                Utility.TileState tileState = tiles[i][j].getState();
                int k;
                boolean hasShip = false;
                for (k = 0; (k < battleships.length) && (!hasShip); k++)
                    if (battleships[k].checkXY(j, i)) {
                        hasShip = true;
                        if (!battleships[k].checkXYHits(j, i))
                            tileState = Utility.TileState.NONE;
                    }
                k--;
                tiles2[i][j] = new Tile(j, i, hasShip, true);
                tiles2[i][j].setDirection(battleships[k].getDir());
                tiles2[i][j].setState(tileState);
                if(battleships[k].getState()==Utility.ShipState.SUNKEN)
                    tiles2[i][j].makeTransperent();
            }
        tiles = tiles2;
    }

    public boolean moveShip(){
        int shipNum = (new Random()).nextInt(battleships.length);
        boolean first = true;
        boolean found = false;
        int[] location;
        for(int i = shipNum;(first||i!=shipNum)&&(!found);i++,i = (i == battleships.length)? 0 : i)
            if(battleships[i].getState() == Utility.ShipState.LIVE) {
                location = generateShip(battleships[i].getSize());
                battleships[i] = new BattleShip(dir, location[0], location[1], location[2], location[3]);
                found = true;
            }
        reGenerateTiles();
        return found;
    }
    public int[] getALivePart(){
        int stop  = (new Random()).nextInt(battleships.length);
        boolean first = true;
        int[] location = null;
        for(int i = stop ; (i<stop+battleships.length)||first ; i++){
            first = false;
            location = battleships[i%battleships.length].getALivePart();
            int temp = i;
            i=stop+battleships.length-1;
            if(location == null)
                i = temp;

        }
        return location;
    }

    public boolean LastShip() {
        if(numOfSunkenShips == battleships.length-1)
            return true;
        return false;
    }
}
