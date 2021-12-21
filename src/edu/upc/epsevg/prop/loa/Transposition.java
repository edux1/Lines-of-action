package edu.upc.epsevg.prop.loa;

import java.awt.*;
import java.util.Map;

public class Transposition {
    private Map.Entry<Point, Point> millorMoviment;
    private int profunditat;
    private int heuristica;
    private int alfa;
    private int beta;

    public Transposition(Map.Entry<Point, Point> millorMoviment, int profunditat, int heuristica, int alfa, int beta) {
        this.millorMoviment = millorMoviment;
        this.profunditat = profunditat;
        this.heuristica = heuristica;
        this.alfa = alfa;
        this.beta = beta;
    }

    public int getAlfa() {
        return alfa;
    }

    public void setAlfa(int alfa) {
        this.alfa = alfa;
    }

    public int getBeta() {
        return beta;
    }

    public void setBeta(int beta) {
        this.beta = beta;
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
