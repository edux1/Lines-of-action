package edu.upc.epsevg.prop.loa;

import java.awt.*;
import java.util.Map;

public class CustomInfo {
    private Map.Entry<Point, Point> millorMoviment;
    private int profunditat;
    private int nodes_explorats;
    private int nodes_explorats_total;

    public CustomInfo(Map.Entry<Point, Point> millorMoviment, int profunditat, int nodes_explorats, int nodes_explorats_total) {
        this.millorMoviment = millorMoviment;
        this.profunditat = profunditat;
        this.nodes_explorats = nodes_explorats;
        this.nodes_explorats_total = nodes_explorats_total;
    }

    public CustomInfo() {}

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

    public int getNodes_explorats() {
        return nodes_explorats;
    }

    public void setNodes_explorats(int nodes_explorats) {
        this.nodes_explorats = nodes_explorats;
    }

    public int getNodes_explorats_total() {
        return nodes_explorats_total;
    }

    public void setNodes_explorats_total(int nodes_explorats_total) {
        this.nodes_explorats_total = nodes_explorats_total;
    }
}
