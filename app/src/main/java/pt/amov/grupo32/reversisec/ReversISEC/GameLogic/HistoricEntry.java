package pt.amov.grupo32.reversisec.ReversISEC.GameLogic;

import java.io.Serializable;
import java.util.Date;

public class HistoricEntry implements Serializable {
    static final long serialVersionUID = 42L;
    private String nickP1, nickP2;
    private int pontosP1, pontosP2;
    private Date data;

    public HistoricEntry(String p1, int pontos1, String p2, int pontos2){
        this.data = new Date();
        this.nickP1 = p1;
        this.pontosP1 = pontos1;
        this.nickP2 = p2;
        this.pontosP2 = pontos2;
    }

    public String getNickP1() {
        return nickP1;
    }

    public String getNickP2() {
        return nickP2;
    }

    public int getPontosP1() {
        return pontosP1;
    }

    public int getPontosP2() {
        return pontosP2;
    }

    public Date getData() {
        return data;
    }
}
