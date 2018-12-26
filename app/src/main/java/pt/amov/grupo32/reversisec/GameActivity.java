package pt.amov.grupo32.reversisec;

import androidx.appcompat.app.AppCompatActivity;
import pt.amov.grupo32.reversisec.ReversISEC.GameLogic.Peca;
import pt.amov.grupo32.reversisec.ReversISEC.GameLogic.Player;
import pt.amov.grupo32.reversisec.ReversISEC.GameLogic.GameRules;
import pt.amov.grupo32.reversisec.ReversISEC.SharedPreferences.GlobalProfile;
import pt.amov.grupo32.reversisec.ReversISEC.SharedPreferences.Profile;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity{

    private static final String INTENT_GAME_MODE = "INTENT_GAME_MODE";

    LinearLayout LLFrame;
    LinearLayout LLTabuleiro;
    TextView tvPontuacao;

    LinearLayout whiteSide;
    LinearLayout blackSide;

    private GlobalProfile globalProfile;
    private int gameMode;

    private ImageView[][] celulasTabuleiro;
    private Peca[][] pecasTabuleiro;

    //players[0] -> Player 1
    //players[1] -> Player 2
    List<Player> players;

    GameRules game;
    Peca myTurn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        globalProfile = (GlobalProfile)getApplicationContext();

        LLFrame = findViewById(R.id.linearLayout_BOARD);
        LLTabuleiro = findViewById(R.id.board);
        celulasTabuleiro = new ImageView[8][8];
        pecasTabuleiro = new Peca[8][8];
        loadIVSTabuleiro();
        game = new GameRules(pecasTabuleiro, gameMode);

        tvPontuacao = findViewById(R.id.tvPontuacao);
        whiteSide = findViewById(R.id.whiteSide);
        blackSide = findViewById(R.id.blackSide);

        Intent intent = getIntent();
        //Obter dados sobre o modo de joga escolhido
        //gamemode:
        //  -> 0 - single player (default)
        //  -> 1 - multiplayer (same device)
        //  -> 2 - multiplayer (dif. device)
        gameMode = intent.getIntExtra(INTENT_GAME_MODE, 0);
        switch (gameMode) {
            case 0:     //SINGLE PLAYER
                setUpOneDevices();
                break;
            case 1:
                setUpOneDevices();
                break;
            case 2:
                //MULTIPLAYER 2 DEVICES
                break;
        }


        game.clearBoard();
        drawBoard();
        calcPontuacao();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //adaptar o tabuleiro ao espaço do ecrã
        setUpTabuleiro(LLFrame, LLTabuleiro);
    }

    public void onBackButton(View v){
        finish();
    }

    private void setUpOneDevices(){
        myTurn = Peca.WHITE;
        //Player 1
        players = new ArrayList<>();
        players.add(new Player(globalProfile.getProfile(), Peca.WHITE, false));
        //COM
        players.add(new Player(new Profile("COM"), Peca.BLACK, true));
        //clearBoard();
        game.clearBoard();
        calcPontuacao();
        whiteSide.setBackgroundColor(getResources().getColor(R.color.playSide));
    }

    private void setUpTabuleiro(LinearLayout llframe, LinearLayout llBoard){
        int max_size = 0;
        max_size = Math.min(llframe.getHeight(), llframe.getWidth()) - llframe.getPaddingBottom();

        ViewGroup.LayoutParams lp = llBoard.getLayoutParams();
        lp.height = max_size;
        lp.width = max_size;

        llBoard.setLayoutParams(lp);
        llBoard.requestLayout();
    }

    private void loadIVSTabuleiro(){

        //region Carregar views para a array
        //Linha A (1ª)
        celulasTabuleiro[0][0] = findViewById(R.id.a1);
        celulasTabuleiro[0][1] = findViewById(R.id.a2);
        celulasTabuleiro[0][2] = findViewById(R.id.a3);
        celulasTabuleiro[0][3] = findViewById(R.id.a4);
        celulasTabuleiro[0][4] = findViewById(R.id.a5);
        celulasTabuleiro[0][5] = findViewById(R.id.a6);
        celulasTabuleiro[0][6] = findViewById(R.id.a7);
        celulasTabuleiro[0][7] = findViewById(R.id.a8);

        //Linha B (2ª)
        celulasTabuleiro[1][0] = findViewById(R.id.b1);
        celulasTabuleiro[1][1] = findViewById(R.id.b2);
        celulasTabuleiro[1][2] = findViewById(R.id.b3);
        celulasTabuleiro[1][3] = findViewById(R.id.b4);
        celulasTabuleiro[1][4] = findViewById(R.id.b5);
        celulasTabuleiro[1][5] = findViewById(R.id.b6);
        celulasTabuleiro[1][6] = findViewById(R.id.b7);
        celulasTabuleiro[1][7] = findViewById(R.id.b8);

        //Linha C (3ª)
        celulasTabuleiro[2][0] = findViewById(R.id.c1);
        celulasTabuleiro[2][1] = findViewById(R.id.c2);
        celulasTabuleiro[2][2] = findViewById(R.id.c3);
        celulasTabuleiro[2][3] = findViewById(R.id.c4);
        celulasTabuleiro[2][4] = findViewById(R.id.c5);
        celulasTabuleiro[2][5] = findViewById(R.id.c6);
        celulasTabuleiro[2][6] = findViewById(R.id.c7);
        celulasTabuleiro[2][7] = findViewById(R.id.c8);

        //Linha D (4ª)
        celulasTabuleiro[3][0] = findViewById(R.id.d1);
        celulasTabuleiro[3][1] = findViewById(R.id.d2);
        celulasTabuleiro[3][2] = findViewById(R.id.d3);
        celulasTabuleiro[3][3] = findViewById(R.id.d4);
        celulasTabuleiro[3][4] = findViewById(R.id.d5);
        celulasTabuleiro[3][5] = findViewById(R.id.d6);
        celulasTabuleiro[3][6] = findViewById(R.id.d7);
        celulasTabuleiro[3][7] = findViewById(R.id.d8);

        //Linha E (5ª)
        celulasTabuleiro[4][0] = findViewById(R.id.e1);
        celulasTabuleiro[4][1] = findViewById(R.id.e2);
        celulasTabuleiro[4][2] = findViewById(R.id.e3);
        celulasTabuleiro[4][3] = findViewById(R.id.e4);
        celulasTabuleiro[4][4] = findViewById(R.id.e5);
        celulasTabuleiro[4][5] = findViewById(R.id.e6);
        celulasTabuleiro[4][6] = findViewById(R.id.e7);
        celulasTabuleiro[4][7] = findViewById(R.id.e8);

        //Linha F (6ª)
        celulasTabuleiro[5][0] = findViewById(R.id.f1);
        celulasTabuleiro[5][1] = findViewById(R.id.f2);
        celulasTabuleiro[5][2] = findViewById(R.id.f3);
        celulasTabuleiro[5][3] = findViewById(R.id.f4);
        celulasTabuleiro[5][4] = findViewById(R.id.f5);
        celulasTabuleiro[5][5] = findViewById(R.id.f6);
        celulasTabuleiro[5][6] = findViewById(R.id.f7);
        celulasTabuleiro[5][7] = findViewById(R.id.f8);

        //Linha G (7ª)
        celulasTabuleiro[6][0] = findViewById(R.id.g1);
        celulasTabuleiro[6][1] = findViewById(R.id.g2);
        celulasTabuleiro[6][2] = findViewById(R.id.g3);
        celulasTabuleiro[6][3] = findViewById(R.id.g4);
        celulasTabuleiro[6][4] = findViewById(R.id.g5);
        celulasTabuleiro[6][5] = findViewById(R.id.g6);
        celulasTabuleiro[6][6] = findViewById(R.id.g7);
        celulasTabuleiro[6][7] = findViewById(R.id.g8);

        //Linha H (8ª)
        celulasTabuleiro[7][0] = findViewById(R.id.h1);
        celulasTabuleiro[7][1] = findViewById(R.id.h2);
        celulasTabuleiro[7][2] = findViewById(R.id.h3);
        celulasTabuleiro[7][3] = findViewById(R.id.h4);
        celulasTabuleiro[7][4] = findViewById(R.id.h5);
        celulasTabuleiro[7][5] = findViewById(R.id.h6);
        celulasTabuleiro[7][6] = findViewById(R.id.h7);
        celulasTabuleiro[7][7] = findViewById(R.id.h8);

        //endregion

        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                celulasTabuleiro[i][j].setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                celulasTabuleiro[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id = getResources().getResourceName(v.getId());
                        int l = id.charAt(id.length() - 2) - 'a';
                        int c = Character.getNumericValue(id.charAt(id.length() - 1 )) - 1;
                        handleMove(l, c);
                        calcPontuacao();
                        drawBoard();
                    }
                });
            }
        }
    }

    private void drawBoard(){
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(pecasTabuleiro[i][j] == Peca.WHITE){
                    celulasTabuleiro[i][j].setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    celulasTabuleiro[i][j].setImageDrawable(getDrawable(R.drawable.peca_white));
                    celulasTabuleiro[i][j].setColorFilter(Color.WHITE);
                } else if(pecasTabuleiro[i][j] == Peca.BLACK){
                    celulasTabuleiro[i][j].setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    celulasTabuleiro[i][j].setImageDrawable(getDrawable(R.drawable.peca_white));
                    celulasTabuleiro[i][j].setColorFilter(Color.BLACK);
                }
            }
        }
    }

    private void calcPontuacao(){
        int pPlayer1 = 0;
        int pPlayer2 = 0;
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(pecasTabuleiro[i][j] == Peca.WHITE){
                    pPlayer1++;
                } else if(pecasTabuleiro[i][j] == Peca.BLACK){
                    pPlayer2++;
                }
            }
        }
        players.get(0).setPontuacao(pPlayer1);
        players.get(1).setPontuacao(pPlayer2);
        tvPontuacao.setText(getString(R.string.pontuacao, pPlayer1, pPlayer2));
    }

    private void handleMove(int x, int y){
        //Verificar se a jogada é válida
        int capturado = game.move(x, y, true);
        if(capturado == 0){
            Toast.makeText(this, R.string.jogadaInvalida, Toast.LENGTH_SHORT).show();
            return;
        }
        game.nextTurn(false);

        //CORRIGIR JOGADA DO PC
        if(gameMode == 0){
            game.moveCOM();
            game.nextTurn(false);
        }
        myTurn = game.currentPlayer;

        if(myTurn == Peca.WHITE){
            whiteSide.setBackgroundColor(getResources().getColor(R.color.playSide));
            blackSide.setBackgroundColor(0);
        }else if(myTurn == Peca.BLACK){
            blackSide.setBackgroundColor(getResources().getColor(R.color.playSide));
            whiteSide.setBackgroundColor(0);
        }
    }


}
