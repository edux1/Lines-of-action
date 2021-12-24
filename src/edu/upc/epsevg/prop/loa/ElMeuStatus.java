package edu.upc.epsevg.prop.loa;

import java.awt.*;
import java.util.Hashtable;
import java.util.Map;

/**
 * Benvolguts al Nostre Status! :D
 * @author Eduardo Pinto && Oriol Fernández
 */
public class ElMeuStatus extends GameStatus {

    private long hash;
    private int[][][] zobristTable = new int[8][8][2];
    private Hashtable<Long, Transposition> transpositionHashtable;

    /**
     * Constructor per als tests
     * @param tauler Tauler on es jugarà.
     */
    public ElMeuStatus(int [][] tauler) {
        super(tauler);
    }

    /**
     * Constructor pare de GameStatus
     * @param gameStatus Status a copiar.
     */
    public ElMeuStatus(GameStatus gameStatus) {
        super(gameStatus);
    }

    /**
     * Constructor pare de GameStatus més implementació amb Zobrist Hashing
     * @param gs Status a copiar.
     * @param zobristTable Taula per fer la millora Zobrist.
     */
    public ElMeuStatus(GameStatus gs, int[][][] zobristTable) {
        super(gs);
        this.zobristTable = zobristTable;
        this.transpositionHashtable = new Hashtable<>();

        calcula_hash();

        System.out.println("Este estado tiene hash: " + hash);
    }

    /**
     * Constructor por copia
     * @param estat Estat del tauler.
     */
    public ElMeuStatus(ElMeuStatus estat) {
        super(estat);
        this.hash = estat.hash;
        this.zobristTable = estat.zobristTable;
        this.transpositionHashtable = estat.transpositionHashtable;
    }

    /**
     * Constructor de ElMeuStatus
     * @param ints Matriu
     * @param hash Valor del hash
     * @param zobristTable Taula Zobrist
     * @param transpositionHashtable Taula Hash Transposicions
     */
    public ElMeuStatus(int[][] ints, long hash, int[][][] zobristTable, Hashtable<Long, Transposition> transpositionHashtable) {
        super(ints);
        this.hash = hash;
        this.zobristTable = zobristTable;
        this.transpositionHashtable = transpositionHashtable;
    }

    /**
     * Constructor de ElMeuStatus
     * @param gameStatus Status a copiar
     * @param hash Valor del hash
     * @param zobristTable Taula Zobrist
     * @param transpositionHashtable Taula Hash Transposicions
     */
    public ElMeuStatus(GameStatus gameStatus, long hash, int[][][] zobristTable, Hashtable<Long, Transposition> transpositionHashtable) {
        super(gameStatus);
        this.hash = hash;
        this.zobristTable = zobristTable;
        this.transpositionHashtable = transpositionHashtable;
    }

    /**
     * Calcula el valor del hash.
     */
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

    /**
     * Guarda el la profunditat, l'heuristica i el millor moviment a la taula de transposició.
     * @param millorMoviment Punt origen i punt destí.
     * @param profunditat Profunditat explorada.
     * @param heuristica Heuristica que s'utilitzará.
     */
    public void put_transposicio(Map.Entry<Point, Point> millorMoviment, int profunditat, int heuristica) {
        this.transpositionHashtable.put(hash, new Transposition(millorMoviment, profunditat, heuristica));
    }

    /**
     * Retorna una taula de transposició.
     * @param profunditat profunditat explorada.
     * @return Taula de transposició.
     */
    public Transposition get_transposicio(int profunditat) {
        Transposition t = this.transpositionHashtable.get(hash);
        if (t.getProfunditat() < profunditat)
            return t;


        return null;
    }

    /**
     * Retorna el valor del hash
     * @return valor del hash
     */
    public long getHash() {
        return hash;
    }
}
