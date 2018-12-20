package pt.amov.grupo32.reversisec;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatDialogFragment;
import pt.amov.grupo32.reversisec.R;

public class NewUserDialog extends AppCompatDialogFragment {

    private TextInputEditText edNickname;
    private NewUserDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_new_user, null);

        builder.setView(view)
                .setTitle(getString(R.string.title_newUser))
                .setPositiveButton(getString(R.string.btnGuardar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nick = edNickname.getText().toString();
                        listener.applyNickname(nick);
                    }
                });

        edNickname = view.findViewById(R.id.nicknameText);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener = (NewUserDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
            "Não foi implementado o listner NewUserDialog");
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        listener.reopenDialog();
    }

    public interface NewUserDialogListener{
        void applyNickname(String nick);
        void reopenDialog();
    }
}
