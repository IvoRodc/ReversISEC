package pt.amov.grupo32.reversisec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.IntentCompat;
import pt.amov.grupo32.reversisec.ReversISEC.GlobalProfile;
import pt.amov.grupo32.reversisec.ReversISEC.Profile;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity implements NewUserDialog.NewUserDialogListener {

    private static final int ANIMATION_APPLOGO_DURATION = 1000;
    private static final int ANIMATION_ISECLOGO_DURATION = 500;
    ImageView appLogo, isecLogo;
    GlobalProfile globalProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Verificar as SharedPreferences
        globalProfile = (GlobalProfile) getApplicationContext();

        appLogo = findViewById(R.id.logo_app);
        isecLogo = findViewById(R.id.logo_isec);

        ///region Animação logo APP
        //Obter posição final do logo da app
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int modifierY = -((displayMetrics.heightPixels)-appLogo.getHeight()/2);

        //configurar a animação de mover o logo
        ObjectAnimator moverLogo = ObjectAnimator.ofFloat(appLogo, "translationY", modifierY/4);
        moverLogo.setDuration(ANIMATION_APPLOGO_DURATION);
        moverLogo.setInterpolator(new AccelerateDecelerateInterpolator());

        //configurar animação para mudar o alpha
        ObjectAnimator fadeInAPP = ObjectAnimator.ofFloat(appLogo, "alpha", 0f, 1f);
        fadeInAPP.setDuration(ANIMATION_APPLOGO_DURATION);

        //configurar animação para mudar o tamanho do logo
        ObjectAnimator resizeX = ObjectAnimator.ofFloat(appLogo, "scaleX", 5f);
        resizeX.setDuration(ANIMATION_APPLOGO_DURATION);
        resizeX.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator resizeY = ObjectAnimator.ofFloat(appLogo, "scaleY", 5f);
        resizeY.setDuration(ANIMATION_APPLOGO_DURATION);
        resizeY.setInterpolator(new AccelerateDecelerateInterpolator());
        ///endregion

        ///region Animação logo ISEC
        ObjectAnimator fadeInISEC = ObjectAnimator.ofFloat(isecLogo, "alpha", 0f, 1f);
        fadeInISEC.setDuration(ANIMATION_ISECLOGO_DURATION);
        fadeInISEC.setStartDelay(ANIMATION_APPLOGO_DURATION);
        ///endregion


        //region AnimatorListner para transição de atividades

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if(globalProfile.getProfile().getNickname()==null) {
                    openDialog();
                } else {
                    Intent intent = new Intent(SplashActivity.this, MenuPrincipalActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    finish();
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        //endregion

        animatorSet.play(moverLogo).with(fadeInAPP).with(resizeX).with(resizeY).with(fadeInISEC);
        animatorSet.start();
    }


    private void openDialog(){
        NewUserDialog dialog = new NewUserDialog();
        dialog.show(getSupportFragmentManager(), "new user dialog");

    }

    @Override
    public void applyNickname(String nick) {
        String nickname = nick;
        if(nickname == null || nickname.isEmpty()) {
            reopenDialog();
        } else {
            globalProfile.saveNickname(nickname);
            Intent intent = new Intent(SplashActivity.this, MenuPrincipalActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            finish();
        }
    }

    @Override
    public void reopenDialog() {
        openDialog();
    }
}
