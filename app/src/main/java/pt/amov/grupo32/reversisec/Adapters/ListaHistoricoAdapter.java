package pt.amov.grupo32.reversisec.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pt.amov.grupo32.reversisec.R;
import pt.amov.grupo32.reversisec.ReversISEC.GameLogic.HistoricEntry;

public class ListaHistoricoAdapter extends RecyclerView.Adapter {


    ArrayList<HistoricEntry> listaHistorico;
    Context context;

    public ListaHistoricoAdapter(ArrayList<HistoricEntry> lista, Context context){
        try {
            listaHistorico = new ArrayList<>(lista);
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.historic_entry_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HistoricEntry entry = listaHistorico.get(position);

        String outputPatterm = "dd MMM yyyy";
        SimpleDateFormat outputFormat =  new SimpleDateFormat(outputPatterm);
        Date d = entry.getData();
        String data = outputFormat.format(d);
        ((ViewHolder)holder).data.setText(data);

        ((ViewHolder)holder).jogador1.setText(context.getString(R.string.itemPlayer1, entry.getNickP1(), entry.getPontosP1()));
        ((ViewHolder)holder).jogador2.setText(context.getString(R.string.itemPlayer2, entry.getPontosP2(), entry.getNickP2()));
        if(entry.getPontosP1() > entry.getPontosP2()){
            ((ViewHolder)holder).jogador1.setPaintFlags(((ViewHolder)holder).jogador1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else if(entry.getPontosP1() < entry.getPontosP2()){
            ((ViewHolder)holder).jogador2.setPaintFlags(((ViewHolder)holder).jogador2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    }

    @Override
    public int getItemCount() {
        return listaHistorico.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView data, jogador1, jogador2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            data = itemView.findViewById(R.id.data);
            jogador1 = itemView.findViewById(R.id.jogador1);
            jogador2 = itemView.findViewById(R.id.jogador2);
        }
    }
}
