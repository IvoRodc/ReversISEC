package pt.amov.grupo32.reversisec.ReversISEC.GameLogic;

import java.io.Serializable;
import java.util.ArrayList;

public class HistoricList implements Serializable {
    static final long serialVersionUID = 42L;
    private ArrayList<HistoricEntry> listaJogos;

    public HistoricList(){
        listaJogos = new ArrayList<>();
    }

    public void addEntry(HistoricEntry entry){
        listaJogos.add(entry);
    }

    public ArrayList<HistoricEntry> getList(){
        return new ArrayList<>(listaJogos);
    }

    public void setList(ArrayList<HistoricEntry> list){
        listaJogos = new ArrayList<>(list);
    }
}
