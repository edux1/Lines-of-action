package edu.upc.epsevg.prop.loa;

import java.awt.*;
import java.util.Map;

public class Transposition {
    private Map.Entry<Point, Point> millorMoviment;
    private int profunditat;
    private int heuristica;

    public Transposition(Map.Entry<Point, Point> millorMoviment, int profunditat, int heuristica) {
        this.millorMoviment = millorMoviment;
        this.profunditat = profunditat;
        this.heuristica = heuristica;
    }

    public Map.Entry<Point, Point> getMillorMoviment() {
        return millorMoviment;
    }

    public void setMillorMoviment(Map.Entry<Point, Point> millorMoviment) {
        this.millorMoviment = millorMoviment;
    }

    public int getProfunditat() {
        return profunditat;
    }

    public void setProfunditat(int profunditat) {
        this.profunditat = profunditat;
    }

    public int getHeuristica() {
        return heuristica;
    }

    public void setHeuristica(int heuristica) {
        this.heuristica = heuristica;
    }
}
