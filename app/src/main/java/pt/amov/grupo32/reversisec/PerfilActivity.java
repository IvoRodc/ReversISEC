package pt.amov.grupo32.reversisec;

import androidx.appcompat.app.AppCompatActivity;
import pt.amov.grupo32.reversisec.ReversISEC.SharedPreferences.GlobalProfile;
import pt.amov.grupo32.reversisec.ReversISEC.SharedPreferences.Profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class PerfilActivity extends AppCompatActivity {

    TextInputLayout nicknameIL;
    TextInputEditText nicknameET;
    ImageView ivFotografia;
    private GlobalProfile globalProfile;
    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        nicknameIL = findViewById(R.id.nicknameLayout);
        nicknameET = findViewById(R.id.nicknameText);
        ivFotografia = findViewById(R.id.fotografia);
        globalProfile = (GlobalProfile)getApplicationContext();
        Log.w("Nick PerfilCreate xpto", globalProfile.getProfile().getNickname());
        if(globalProfile.getProfile().getFotografia()!=null)
            Log.w("Foto PerfilCreate xpto", globalProfile.getProfile().getFotografia().toString());
        profile = globalProfile.getProfile();


        //carregar dados da SharedPreferences
        nicknameET.setText(profile.getNickname());
        if(profile.getFotografia()!=null) {
            setPicture(profile.getFotografia());
        }
    }

    void onBackButton(View v){
        finish();
    }

    void goToCamera(View v){
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    void saveNickname(View v){

        boolean valido = true;
        if(nicknameET.getText().toString().isEmpty()){
            nicknameIL.setError(getString(R.string.emptyFieldNick));
            valido = false;
        } else if(nicknameET.getText().toString().equalsIgnoreCase("COM")){
            nicknameIL.setError(getString(R.string.nickInvalido));
            valido = false;
        }

        nicknameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nicknameIL.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if(valido) {
            try {
                String name = nicknameET.getText().toString();
                globalProfile.saveNickname(name);
                Toast.makeText(this, getString(R.string.guadarSucesso), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, getString(R.string.guadarFail), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setPicture(byte[] foto){
        if(foto!=null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(foto, 0, foto.length);
            BitmapDrawable image = new BitmapDrawable(ivFotografia.getResources(), bmp);

            ivFotografia.setImageBitmap(image.getBitmap());
        } else {
            ivFotografia.setImageDrawable(getDrawable(R.drawable.ic_account));
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        profile = globalProfile.getProfile();
        if(profile.getFotografia()!=null) {
            setPicture(profile.getFotografia());
        }
        nicknameET.setText(profile.getNickname());
        Log.w("Nick PerfilResume xpto", globalProfile.getProfile().getNickname());
        if(globalProfile.getProfile().getFotografia()!=null)
            Log.w("Foto PerfilResume xpto", globalProfile.getProfile().getFotografia().toString());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(profile.getFotografia()!=null){
            setPicture(profile.getFotografia());
        }
    }
}
