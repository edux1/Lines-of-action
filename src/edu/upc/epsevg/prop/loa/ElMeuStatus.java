package edu.upc.epsevg.prop.loa;

import java.awt.*;
import java.util.Hashtable;

/**
 *
 * @author Who
 */
public class ElMeuStatus extends GameStatus {

    private long hash;
    private int[][][] zobristTable = new int[8][8][2];
    Hashtable<Long, Transposition> transpositionHashtable;

    // Constructor per als tests
    public ElMeuStatus(int [][] tauler) {
        super(tauler);
    }

    // Constructor per al override de GameStatus
    public ElMeuStatus(ElMeuStatus estat) {
        super(estat);
    }

    // Constructor per al override de GameStatus amb Zobrist Hashing
    public ElMeuStatus(GameStatus gs, int[][][] zobristTable) {
        super(gs);
        this.zobristTable = zobristTable;
        transpositionHashtable = new Hashtable<>();

        calcula_hash();
        System.out.println("Este estado tiene hash: " + hash);
    }

    public void calcula_hash() {
        Point p;
        hash = 0;

        for (int i = 0 ; i < this.getNumberOfPiecesPerColor(CellType.PLAYER1) ; i++) {
            p = this.getPiece(CellType.PLAYER1, i);
            hash += hash ^ zobristTable[p.x][p.y][0]; // XOR
        }
        for (int i = 0 ; i < this.getNumberOfPiecesPerColor(CellType.PLAYER2) ; i++) {
            p = this.getPiece(CellType.PLAYER2, i);
            hash += hash ^ zobristTable[p.x][p.y][1]; // XOR
        }
    }

//    public void guarda_hash(Map.Entry<Point, Point> millorMoviment, int profunditat, int heuristica) {
//        transpositionHashtable.put(hash, new Transposition(millorMoviment, profunditat, heuristica));
//    }

    public long getHash() {
        return hash;
    }
}
