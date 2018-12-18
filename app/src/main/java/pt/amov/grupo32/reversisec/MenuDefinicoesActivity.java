package pt.amov.grupo32.reversisec;

import androidx.appcompat.app.AppCompatActivity;

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
}
