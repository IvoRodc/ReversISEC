package pt.amov.grupo32.reversisec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import pt.amov.grupo32.reversisec.ReversISEC.GlobalProfile;
import pt.amov.grupo32.reversisec.ReversISEC.Profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class PerfilActivity extends AppCompatActivity {

    TextInputEditText nicknameView;
    ImageView ivFotografia;
    private GlobalProfile globalProfile;
    Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        nicknameView = findViewById(R.id.nicknameText);
        ivFotografia = findViewById(R.id.fotografia);
        globalProfile = (GlobalProfile)getApplicationContext();
        Log.w("Nick PerfilCreate xpto", globalProfile.getProfile().getNickname());
        if(globalProfile.getProfile().getFotografia()!=null)
            Log.w("Foto PerfilCreate xpto", globalProfile.getProfile().getFotografia().toString());
        profile = globalProfile.getProfile();


        //carregar dados da SharedPreferences
        nicknameView.setText(profile.getNickname());
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
        String name = nicknameView.getText().toString();
        globalProfile.saveNickname(name);
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
        nicknameView.setText(profile.getNickname());
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
