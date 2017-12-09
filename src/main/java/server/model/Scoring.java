/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model;

/**
 *
 * @author Relax2954
 */
public class Scoring {

    int score1;
    int score2;

    public int scoreincrement1() {
        score1 = score1+1;
        return score1;
    }

    public int scoreincrement2() {
        score2 = score2 + 1;
        return score2;
    }

    public Scoring() {
        score1 = 0;
        score2 = 0;
    }
}
