package pt.amov.grupo32.reversisec.ReversISEC.GameLogic;


public class GameRules {

    private Peca[][] tabuleiroPecas;
    public Peca currentPlayer;
    int gameMode;
    int moveCount;

    public GameRules(Peca[][] tabPecas, int mode){
        this.tabuleiroPecas = tabPecas;
        this.currentPlayer = Peca.WHITE;
        this.gameMode = mode;
        moveCount = 0;
    }

    public void clearBoard(){
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                tabuleiroPecas[i][j] = Peca.EMPTY;
            }
        }
        //Colocar as peças iniciais
        tabuleiroPecas[3][3] = Peca.WHITE;
        tabuleiroPecas[3][4] = Peca.BLACK;
        tabuleiroPecas[4][3] = Peca.BLACK;
        tabuleiroPecas[4][4] = Peca.WHITE;
    }

    private void swapSides(){
        assert(currentPlayer != Peca.EMPTY);
        currentPlayer = (currentPlayer == Peca.WHITE)? Peca.BLACK : Peca.WHITE;
    }

    public int move(int x, int y, boolean move){
        if(tabuleiroPecas[x][y] != Peca.EMPTY){
            return 0;
        }

        int dx, dy;
        int total = 0;

        //Explora as 8 células em torno da posição
        for(dx=-1; dx<=1; dx++){
            for(dy=-1; dy<=1; dy++){
                //salta a própria posição
                if(dx==0 && dy==0){continue; }

                //Explorar as direções à procura de validação
                for (int passos=1; passos<8; passos++){
                    int direcaoX = x + (dx*passos);
                    int direcaoY = y + (dy*passos);

                    //verificar se a direção já saiu dos limites
                    if(direcaoX<0 || direcaoX>=8 || direcaoY<0 || direcaoY>=8){break;}

                    Peca pecaAtual = tabuleiroPecas[direcaoX][direcaoY];

                    //verificar se atingiu uma célula vazia
                    if(pecaAtual == Peca.EMPTY){ break; }

                    //se atingirmos uma peça da nossa cor captura-se a sequencia
                    if(pecaAtual == currentPlayer){
                        if(passos>1){
                            //tem de se dar pelo menos 1 passo para capturar
                            total += passos - 1;
                            //se for para efetuar a jogada executa-se a captura
                            if(move){
                                while (passos-- >0)
                                    tabuleiroPecas[x+(dx*passos)][y+(dy*passos)] = currentPlayer;
                            }
                        }
                        break;
                    }
                }
            }
        }
        return total;
    }

    public void nextTurn(boolean invalid){
        swapSides();

        if(!hasMoves()){
            //se o método for chamado duas vezes seguidas significa que não há mais jogadas
            if(invalid){
                //Não há mai jogadas
                //gameOver()
            } else {
                //passa a jogada e verifica se tem jogadas
                moveCount++;
                nextTurn(true);
            }
        }
    }

    private boolean hasMoves(){
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                int numCaptured = move(i, j, false);
                if(numCaptured > 0){
                    return true;
                }
            }
        }
        return false;
    }

    public void moveCOM(){
        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                int moves = move(i, j, true);
                if(moves > 0) {
                    i=9;
                    j=9;
                }
            }
        }
    }
}
