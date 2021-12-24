package edu.upc.epsevg.prop.loa;

import java.awt.*;
import java.util.Hashtable;
import java.util.Map;

/**
 *
 * @author Who
 */
public class ElMeuStatus extends GameStatus {

    private long hash;
    private int[][][] zobristTable = new int[8][8][2];
    private Hashtable<Long, Transposition> transpositionHashtable;

    // Constructor per als tests
    public ElMeuStatus(int [][] tauler) {
        super(tauler);
    }

    // Constructor pare de GameStatus
    public ElMeuStatus(GameStatus gameStatus) {
        super(gameStatus);
    }

    // Constructor pare de GameStatus mes implementacio amb Zobrist Hashing
    public ElMeuStatus(GameStatus gs, int[][][] zobristTable) {
        super(gs);
        this.zobristTable = zobristTable;
        this.transpositionHashtable = new Hashtable<>();

        calcula_hash();

        System.out.println("Este estado tiene hash: " + hash);
    }

    // Constructor por copia
    public ElMeuStatus(ElMeuStatus estat) {
        super(estat);
        this.hash = estat.hash;
        this.zobristTable = estat.zobristTable;
        this.transpositionHashtable = estat.transpositionHashtable;
    }

    public ElMeuStatus(int[][] ints, long hash, int[][][] zobristTable, Hashtable<Long, Transposition> transpositionHashtable) {
        super(ints);
        this.hash = hash;
        this.zobristTable = zobristTable;
        this.transpositionHashtable = transpositionHashtable;
    }

    public ElMeuStatus(GameStatus gameStatus, long hash, int[][][] zobristTable, Hashtable<Long, Transposition> transpositionHashtable) {
        super(gameStatus);
        this.hash = hash;
        this.zobristTable = zobristTable;
        this.transpositionHashtable = transpositionHashtable;
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

    public void put_transposicio(Map.Entry<Point, Point> millorMoviment, int profunditat, int heuristica) {
        this.transpositionHashtable.put(hash, new Transposition(millorMoviment, profunditat, heuristica));
    }

    public Transposition get_transposicio() {
        return this.transpositionHashtable.get(hash);
    }

    public long getHash() {
        return hash;
    }
}
