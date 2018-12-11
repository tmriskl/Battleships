package com.example.battleships.Logic;

/**
 * Created by User on 03/12/2017.
 */

public class Game {

    private Board comBattlefiedBoard;//Where player plays
    private Board playerBattlefiedBoard;//Where computer plays
    private boolean playerTurn;

    public Game(Utility.Difficulty dif){
        playerBattlefiedBoard = new Board(dif, true);
        comBattlefiedBoard = new Board(Utility.Difficulty.MEDIUM, false);
        playerTurn = true;
    }

    public boolean playTurn(int x, int y){
        if(playerTurn){
            if(playerBattlefiedBoard.onFire(x,y)==Utility.Result.MISS)
                playerTurn = false;
            return playerBattlefiedBoard.isWin();
        }
        if(comBattlefiedBoard.onFire(x,y)==Utility.Result.MISS)
            playerTurn = true;
        return comBattlefiedBoard.isWin();
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public Board getPlayerBattlefiedBoard() {
        return playerBattlefiedBoard;
    }

    public Board getComBattlefiedBoard() {
        return comBattlefiedBoard;
    }

    public int[] getRemainingShips(boolean player){
        if(player)
            return playerBattlefiedBoard.getRemainingShips();
        else
            return comBattlefiedBoard.getRemainingShips();
    }

    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }

    public String[] getScore(String winLose) {
        return playerBattlefiedBoard.getScore(winLose);
    }
}
