package pt.amov.grupo32.reversisec;

import androidx.appcompat.app.AppCompatActivity;
import pt.amov.grupo32.reversisec.ReversISEC.GameLogic.HistoricEntry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuDefinicoesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_definicoes);
    }

    void onBackButton(View v){
        finish();
    }

    void goToPerfil(View v){
        Intent intent = new Intent(this, PerfilActivity.class);
        startActivity(intent);
    }

    void goToHistorico(View v){
        Intent intent = new Intent(this, HistoricoActivity.class);
        startActivity(intent);
    }

    void goToCreditos(View v){
        Intent intent = new Intent(this, CreditosActivity.class);
        startActivity(intent);
    }
}
