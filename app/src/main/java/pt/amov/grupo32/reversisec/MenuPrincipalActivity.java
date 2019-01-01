package pt.amov.grupo32.reversisec;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import pt.amov.grupo32.reversisec.ReversISEC.GameLogic.Peca;
import pt.amov.grupo32.reversisec.ReversISEC.SharedPreferences.Profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuPrincipalActivity extends AppCompatActivity {

    private static final String INTENT_GAME_MODE = "INTENT_GAME_MODE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
    }

    void goToMenuDefinicoes(View v){
        Intent intent = new Intent(this, MenuDefinicoesActivity.class);
        startActivity(intent);
    }

    void startVSCOM(View v){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(INTENT_GAME_MODE, 0);
        startActivity(intent);
    }

    void startVSPLAYER(View v){
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(INTENT_GAME_MODE, 1);
        startActivity(intent);
    }

    void startLAN(View v){
        final Intent intent = new Intent(this, GameActivity.class);
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle(getString(R.string.em_LAN));
        ad.setMessage(getString(R.string.lanDialog));
        ad.setPositiveButton(getString(R.string.btnCliente), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intent.putExtra(INTENT_GAME_MODE, 3);
                startActivity(intent);
            }
        });
        ad.setNegativeButton(getString(R.string.btnServidor), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intent.putExtra(INTENT_GAME_MODE, 2);
                startActivity(intent);
            }
        });
        ad.create();
        ad.show();

    }

}
