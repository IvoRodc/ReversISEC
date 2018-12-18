package pt.amov.grupo32.reversisec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuPrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
    }

    void goToMenuDefinicoes(View v){
        Intent intent = new Intent(this, MenuDefinicoesActivity.class);
        startActivity(intent);
    }

}
