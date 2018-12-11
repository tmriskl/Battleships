package com.example.battleships.Logic;

import java.util.Random;

/**
 * Created by User on 08/12/2017.
 */

public class ComputerPlayer {

    private Game game;

    public ComputerPlayer(Game game) {
        this.game = game;
    }

    public boolean playTurn() {
        Board b = game.getComBattlefiedBoard();
        int[] hitLocation = b.getHitLocation();
        // Enter this if only when you have no "hit" to start from
       if (hitLocation == null) {
            hitLocation = new int[2];
            int count = 0;
            boolean found = false;
            do {
                hitLocation[0] = (new Random()).nextInt(Utility.BOARDSIZE);
                hitLocation[1] = (new Random()).nextInt(Utility.BOARDSIZE);
                count++;
            } while ((b.getTileState(hitLocation[0], hitLocation[1]) != Utility.TileState.NONE)&&count<Utility.MAXCOMPTRYS);
            if(count>=Utility.MAXCOMPTRYS){
                for(int i = 0; (i < Utility.BOARDSIZE)&&(!found);i++)
                    for(int j = 0; j < Utility.BOARDSIZE&&(!found);j++)
                        if(b.getTileState(i, j) == Utility.TileState.NONE){
                            found = true;
                            hitLocation[0] = i;
                            hitLocation[1] = j;
                        }
                }
        }else {
           boolean direction = false;
           int i;
           i = 1;
           while (b.getTileState(hitLocation[0] + i, hitLocation[1]) == Utility.TileState.HIT) {
               i++;
           }
           if (b.getTileState(hitLocation[0] + i, hitLocation[1]) == Utility.TileState.NONE) {
               direction = true;
               hitLocation[0] += i;
           }

           if (!direction) {
               i = 1;
               while (b.getTileState(hitLocation[0], hitLocation[1] - i) == Utility.TileState.HIT) {
                   i++;
               }
               if (b.getTileState(hitLocation[0], hitLocation[1] - i) == Utility.TileState.NONE) {
                   direction = true;
                   hitLocation[1] -= i;
               }
           }
           if (!direction) {
               i = 1;
               while (b.getTileState(hitLocation[0], hitLocation[1] + i) == Utility.TileState.HIT) {
                   i++;
               }
               if (b.getTileState(hitLocation[0], hitLocation[1] + i) == Utility.TileState.NONE) {
                   direction = true;
                   hitLocation[1] += i;
               }
           }
           if (!direction) {
               i = 1;
               while (b.getTileState(hitLocation[0] - i, hitLocation[1]) == Utility.TileState.HIT) {
                   i++;
               }
               if (b.getTileState(hitLocation[0] - i, hitLocation[1]) == Utility.TileState.NONE) {
                   hitLocation[0] -= i;
               }
           }
       }
        return game.playTurn(hitLocation[0], hitLocation[1]);
    }

    private int getMax(int eastCount, int westCount, int northCount, int southCount) {
        return Math.max(Math.max(eastCount,westCount),Math.max(northCount,southCount));
    }

}
