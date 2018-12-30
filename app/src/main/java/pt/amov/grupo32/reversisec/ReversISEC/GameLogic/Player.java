package pt.amov.grupo32.reversisec.ReversISEC.GameLogic;

import android.graphics.Color;
import android.graphics.Point;

import java.util.List;

import pt.amov.grupo32.reversisec.ReversISEC.SharedPreferences.Profile;

public class Player {

    private Peca corJogador;
    private Profile perfil;
    private boolean computerControlled;

    private boolean jogarNovamente;
    private boolean passarVez;

    private int pontuacao;

    //FALTA ADAPTAR PARA JOGAR EM LAN


    public Player(Profile perfil, Peca p, boolean isCOM){
        this.perfil = perfil;
        this.corJogador = p;
        this.computerControlled = isCOM;

        this.jogarNovamente = true;
        this.passarVez = true;

        this.pontuacao = 0;
    }

    public void setProfile(Profile profile){
        this.perfil = profile;
    }

    public String getNickname(){
        return this.perfil.getNickname();
    }

    public byte[] getFotografia(){
        return this.perfil.getFotografia();
    }

    public boolean isComputerControlled(){
        return this.computerControlled;
    }

    public void setComputerControlled(boolean isCOM){
        this.computerControlled = isCOM;
    }

    public void usouPassarVez(){
        this.passarVez = false;
    }

    public boolean podePassarVez(){
        return this.passarVez;
    }

    public void usouJogarNovamente(){
        this.jogarNovamente = false;
    }

    public boolean podeJogarNovamente() {
        return this.jogarNovamente;
    }

    public void setPontuacao(int pontos){
        this.pontuacao = pontos;
    }

    public int getPontuacao(){
        return this.pontuacao;
    }

    public Peca getCorJogador(){
        return this.corJogador;
    }
}
