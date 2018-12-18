package pt.amov.grupo32.reversisec;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class PerfilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        //carregar dados da SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String nickname = prefs.getString("PREFS_NICKNAME", null);
        ((TextInputEditText)findViewById(R.id.nicknameText)).setText(nickname);
    }

    void onBackButton(View v){
        finish();
    }

    void goToCamera(View v){
        Toast.makeText(this, "Falta fazer atividade para tirar a foto", Toast.LENGTH_SHORT).show();
    }

    void saveSharedPreferences(View v){
        TextInputEditText nicknameView = findViewById(R.id.nicknameText);
        if(nicknameView.getText().toString().isEmpty()) {
            nicknameView.setText(getString(R.string.preNickname));
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();

        editor.putString("PREFS_NICKNAME", nicknameView.getText().toString());
        editor.commit();

        Toast.makeText(this, getString(R.string.toastSaveNickname), Toast.LENGTH_SHORT).show();
    }
}
