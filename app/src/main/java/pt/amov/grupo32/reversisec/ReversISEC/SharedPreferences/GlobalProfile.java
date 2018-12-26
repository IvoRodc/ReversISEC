package pt.amov.grupo32.reversisec.ReversISEC.SharedPreferences;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class GlobalProfile extends Application {

    private static final String PREFS_PROFILE_NICKNAME = "PREFS_PROFILE_NICKNAME";
    private static final String PREFS_PROFILE_FOTOGRAFIA = "PREFS_PROFILE_FOTOGRAFIA";

    private Profile profile;

    public Profile getProfile() {
        return profile;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        this.profile = readProfileFromSharedPreferences();
    }

    public Profile readProfileFromSharedPreferences(){
        Profile p = new Profile();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String name = prefs.getString(PREFS_PROFILE_NICKNAME, null);
        p.setNickname(name);
        String foto = prefs.getString(PREFS_PROFILE_FOTOGRAFIA, null);
        if(foto!=null){
            byte[] decodedByte = Base64.decode(foto, Base64.DEFAULT);
            p.setFotografia(decodedByte);
        }
        return p;
    }

    public void saveFotografia(byte[] foto){
        if(foto != null){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();
            Bitmap bitmapImage = BitmapFactory.decodeByteArray(foto, 0, foto.length, null);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
            byte[] b = outputStream.toByteArray();
            profile.setFotografia(b);
            String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
            editor.putString(PREFS_PROFILE_FOTOGRAFIA, imageEncoded);
            editor.apply();
        }

    }

    public void saveNickname(String name){
        if(name.isEmpty()){
            name = "Jogador 1";
        }
        profile.setNickname(name);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREFS_PROFILE_NICKNAME, profile.getNickname());
        editor.apply();
    }
}
