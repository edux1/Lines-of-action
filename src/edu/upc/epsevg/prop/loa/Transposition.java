package edu.upc.epsevg.prop.loa;

import java.awt.*;
import java.util.Map;

/**
 * Classe que guarda informació útil per a l'implementació del minmax Zobrist
 */
public class Transposition {
    private Map.Entry<Point, Point> millorMoviment;
    private int profunditat;
    private int heuristica;

    /**
     * Constructor principal
     * @param millorMoviment El millor moviment amb informacio de la fitxa origen i desti
     * @param profunditat La profunditat de l'estat
     * @param heuristica L'heuristica de l'estat
     */
    public Transposition(Map.Entry<Point, Point> millorMoviment, int profunditat, int heuristica) {
        this.millorMoviment = millorMoviment;
        this.profunditat = profunditat;
        this.heuristica = heuristica;
    }

    /**
     * Retorna el millorMoviment
     * @return Punt origen i punt destí
     */
    public Map.Entry<Point, Point> getMillorMoviment() {
        return millorMoviment;
    }

    /**
     * Estableix un nou millor moviment.
     * @param millorMoviment Punt origen i punt destí
     */
    public void setMillorMoviment(Map.Entry<Point, Point> millorMoviment) {
        this.millorMoviment = millorMoviment;
    }

    /**
     * Retorna la profunditat.
     * @return Valor de la profunditat.
     */
    public int getProfunditat() {
        return profunditat;
    }

    /**
     * Estableix una profunditat.
     * @param profunditat Valor de la profunditat.
     */
    public void setProfunditat(int profunditat) {
        this.profunditat = profunditat;
    }

    /**
     * Retorna una heuristica.
     * @return Una heuristica.
     */
    public int getHeuristica() {
        return heuristica;
    }

    /**
     * Estableix una heuristica.
     * @param heuristica Una heuristica.
     */
    public void setHeuristica(int heuristica) {
        this.heuristica = heuristica;
    }
}
