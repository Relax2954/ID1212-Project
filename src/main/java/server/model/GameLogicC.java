/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 *
 * @author Relax2954
 */
public class GameLogicC {

    //public volatile int score1; //the total current score of player 1;
    //public volatile int score2; //the total current score of player 2;
    public volatile char[][] checkerTable;   //SHOULD IT BE VOLATILE, MAYBE ATOMIC?  check private?
    public volatile String checkerTableString;
    private Character winner = null;
    private char currentpick;
    private int currentstate;  //this defines whether the game is finished because all entries are filled!

    public GameLogicC() { //start game
        checkerTable = new char[][]{{'1', '2', '3'},
        {'4', '5', '6'},
        {'7', '8', '9'},};
        checkerTableString = Arrays
                .stream(checkerTable)
                .map(Arrays::toString)
                .collect(Collectors.joining(System.lineSeparator()));
        currentpick = 'X';
        currentstate=1;
    }

    public void gamelogic(int fieldnum) {
        
        switch (fieldnum) {
            case 1:
                if(checkerTable[0][0]=='X' ||checkerTable[0][0]=='O')
                    break;
                checkerTable[0][0] = currentpick;
                pickmark();
                break;
            case 2:
                if(checkerTable[0][1]=='X' ||checkerTable[0][1]=='O')
                    break;
                checkerTable[0][1] = currentpick;
                pickmark();
                break;
            case 3: 
                if(checkerTable[0][2]=='X' ||checkerTable[0][2]=='O')
                    break;
                checkerTable[0][2] = currentpick;
                pickmark();
                break;
            case 4:
                if(checkerTable[1][0]=='X' ||checkerTable[1][0]=='O')
                    break;
                checkerTable[1][0] = currentpick;
                pickmark();
                break;
            case 5:
                if(checkerTable[1][1]=='X' ||checkerTable[1][1]=='O')
                    break;
                checkerTable[1][1] = currentpick;
                pickmark();
                break;
            case 6:
                if(checkerTable[1][2]=='X' ||checkerTable[1][2]=='O')
                    break;
                checkerTable[1][2] = currentpick;
                pickmark();
                break;
            case 7:
                if(checkerTable[2][0]=='X' ||checkerTable[2][0]=='O')
                    break;
                checkerTable[2][0] = currentpick;
                pickmark();
                break;
            case 8:
                if(checkerTable[2][1]=='X' ||checkerTable[2][1]=='O')
                    break;
                checkerTable[2][1] = currentpick;
                pickmark();
                break;
            case 9:
                if(checkerTable[2][2]=='X' ||checkerTable[2][2]=='O')
                    break;
                checkerTable[2][2] = currentpick;
                pickmark();
                break;
        }
        checkerTableString = Arrays
                .stream(checkerTable)
                .map(Arrays::toString)
                .collect(Collectors.joining(System.lineSeparator()));
        /* if(!checkerTableString.matches(".*\\d+.*"))
        currentstate=0;*/   //please recheck why?????????
        if(!checkerTableString.contains("1") && !checkerTableString.contains("2") && !checkerTableString.contains("3")
                && !checkerTableString.contains("4") && !checkerTableString.contains("5") && !checkerTableString.contains("6")
                && !checkerTableString.contains("7") && !checkerTableString.contains("8") && !checkerTableString.contains("9")){
            currentstate=0;
        }
        
    }
    
    public char getCurrentPick(){
        return this.currentpick;
    }

    public Character getWinner() {
        return this.winner;
    }

    public boolean isWon() {
        this.checkIfWinner();
        return this.winner != null;
    }
    public int isFinished(){
        return this.currentstate;
    }

    private void checkIfWinner() {
        for (int i = 0; i < checkerTable.length; i++) {
            Character winRow = checkRow(i);
            Character winColumn = checkColumn(i);
            if (winRow != null) {
                this.winner = winRow;
                return;
            }
            if (winColumn != null) {
                this.winner = winColumn;
                return;
            }
        }
        //Check diagonal top right to bottom left
        if (this.checkerTable[0][2] != '3') {
            if (this.checkerTable[0][2] == this.checkerTable[1][1]
                    && this.checkerTable[1][1] == this.checkerTable[2][0]) {
                this.winner = this.checkerTable[0][2];
            }
        } //Check diagonal top left to bottom right
         if (this.checkerTable[0][0] != '1') {
            if (this.checkerTable[0][0] == this.checkerTable[1][1]
                    && this.checkerTable[1][1] == this.checkerTable[2][2]) {
                this.winner = this.checkerTable[0][0];
            }
        }

    }

    private Character checkRow(int row) {
       
        if (this.checkerTable[row][0] == this.checkerTable[row][1]
                && this.checkerTable[row][1] == this.checkerTable[row][2]) {
            return this.checkerTable[row][0];
        } else {
            return null;
        }
    }

    private Character checkColumn(int column) {
 
        if (this.checkerTable[0][column] == this.checkerTable[1][column]
                && this.checkerTable[1][column] == this.checkerTable[2][column]) {
            return this.checkerTable[column][0];
        } else {
            return null;
        }
    }

    private void pickmark() {
        if (currentpick == 'X') {
            currentpick = 'O';
        } else {
            currentpick = 'X';
        }
    }
}
