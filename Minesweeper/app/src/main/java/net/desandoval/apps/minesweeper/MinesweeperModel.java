package net.desandoval.apps.minesweeper;

import android.util.Log;

import java.util.Random;

/**
 * Created by Daniel on 4/10/2014.
 */
public class MinesweeperModel {

    private static MinesweeperModel instance = null;
    private MinesweeperModel () {
    }
    public static MinesweeperModel getInstance() {
        if (instance == null) {
            instance = new MinesweeperModel();
        }
        return instance;
    }

    public static int gridViewSize; // determines number of squares in the NxN grid
    public static int numBombs;     // determines the number of bombs in the field

    public static final short EMPTY = 0;
    public static final short FLAGGED = 1;
    public static final short BOMB = 2;
    public static final short CLICKED = 3;

    Random random = new Random();
    private short[][] boardModel;   // NxN model of board, used to determine user input
    private short[][] bombModel;    // NxN model of board, used to determine bomb locations

    /*
    Resets the game board to the desired size and number of bombs
     */
    public void resetModel(int setGridSize, int setBombNumber) {
        gridViewSize = setGridSize;
        numBombs = setBombNumber;
        boardModel = new short[gridViewSize + 1][gridViewSize + 1];
        bombModel = new short[gridViewSize + 1][gridViewSize + 1];

        for (int i = 0; i < gridViewSize; i++) {
            for (int j = 0; j < gridViewSize; j++) {
                bombModel[i][j] = EMPTY;
            }
        }

        int counter = 0;
        while(counter < numBombs){
            int randRow = random.nextInt(gridViewSize - 1);
            int randColumn = random.nextInt(gridViewSize - 1);
            if (bombModel[randRow][randColumn] == EMPTY) {
                bombModel[randRow][randColumn] = BOMB;
                Log.d("Alert", "adding bomb to " + randRow + ":" + randColumn);
                ++ counter;
            }
        }

        for (int i = 0; i < gridViewSize; i++) {
            for (int j = 0; j < gridViewSize; j++) {
                boardModel[i][j] = EMPTY;
            }
        }

    }
    /*
    Returns clicked/flagged/empty content of the desired x,y board coordinate
     */
    public short getBoardContent(int x, int y) {
        return boardModel[x][y];
    }

    /*
    Returns the empty/bomb content of the desired x,y bomb coordinate
     */
    public short getBombContent(int x, int y) {
        return bombModel[x][y];
    }

    /*
    Sets clicked/flagged/empty content of the desired x,y board coordinate
     */
    public short setBoardContent(int x, int y, short content) {
        return boardModel[x][y] = content;
    }

    /*
    Returns the number (int) of bombs near the desired x,y board coordinate
     */
    public int getBombsNear(int x, int y){
        int counter = 0;
        for (int xOffset = -1; xOffset < 2; xOffset++){
            for (int yOffset = -1; yOffset < 2; yOffset++){
                if (x + xOffset > -1 && x + xOffset < gridViewSize
                        && y + yOffset > -1 && y + yOffset < gridViewSize){
                    if (bombModel[x+xOffset][y+yOffset] == BOMB){
                        counter++;
                    }
                }
            }
        }
        return counter;
    }

    /*
    Decides whether or not the player has won by verifying that all boxes have been clicked.
    If an empty box is flagged or left un-clicked, return 0. Winner = 1
     */
    public int getWinner() {
        for (int i = 0; i < gridViewSize; i++) {
            for (int j = 0; j < gridViewSize; j++) {
                if(boardModel[i][j] == FLAGGED && bombModel[i][j] == EMPTY){
                    return 0;
                }else if (boardModel[i][j] == EMPTY){
                    return 0;
                }
            }
        }
        return 1;
    }
}
