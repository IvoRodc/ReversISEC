package pt.amov.grupo32.reversisec;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pt.amov.grupo32.reversisec.Adapters.ListaHistoricoAdapter;
import pt.amov.grupo32.reversisec.ReversISEC.GameLogic.HistoricList;

import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class HistoricoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        //Obter items do histórico
        HistoricList historicList = new HistoricList();

        File file = new File(getFilesDir(), "historico.dat");
        if(file.exists()){
            try{
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                historicList = (HistoricList)ois.readObject();
                ois.close();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        RecyclerView recyclerView = findViewById(R.id.listaHistorico);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ListaHistoricoAdapter adapter = new ListaHistoricoAdapter(historicList.getList(), this);
        recyclerView.setAdapter(adapter);
    }

    public void onBackButton(View v){
        finish();
    }
}
