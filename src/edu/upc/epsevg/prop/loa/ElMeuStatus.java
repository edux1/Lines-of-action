package edu.upc.epsevg.prop.loa;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Who
 */
public class ElMeuStatus extends GameStatus {

    private int hash;
    private static final int[][][] m = new int[8][8][2];


    public void fill_Matrix(){
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                for (int k = 0; k < 2; k++) {
                    m[i][j][k] = new Random().nextInt(Integer.MAX_VALUE);
                }
            }
        }
    }

    public void calcula_hash() {
        ArrayList<Point> fitxes = new ArrayList<>();
        Point p;

        for (int i = 0 ; i < this.getNumberOfPiecesPerColor(CellType.PLAYER1) ; i++) {
            p = this.getPiece(CellType.PLAYER1, i);
            hash += hash ^ m[p.x][p.y][1]; // Pendent

        }
        for (int i = 0 ; i < this.getNumberOfPiecesPerColor(CellType.PLAYER2) ; i++) {
            p = this.getPiece(CellType.PLAYER2, i);
            hash += hash ^ m[p.x][p.y][2];
        }
    }

    public ElMeuStatus(int [][] tauler) {
        super(tauler);
    }

    public ElMeuStatus(GameStatus gs) {
        super(gs);
    }



}
