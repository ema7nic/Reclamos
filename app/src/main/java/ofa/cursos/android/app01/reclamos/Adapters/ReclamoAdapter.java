package ofa.cursos.android.app01.reclamos.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ImageButton;
import android.widget.TextView;
import java.util.List;

import ofa.cursos.android.app01.reclamos.Dao.ReclamoDaoSql;
import ofa.cursos.android.app01.reclamos.Model.Reclamo;
import ofa.cursos.android.app01.reclamos.R;

public class ReclamoAdapter extends ArrayAdapter {
    private LayoutInflater inflater;
    private Context context;
    private List<Reclamo> listaReclamos;
    private  ViewHolder holder;
    protected ReclamoDaoSql reclamoDao;

    public ReclamoAdapter(@NonNull Context context, List<Reclamo> lista) {
        super(context, 0, lista);
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.listaReclamos = lista;
        holder = new ViewHolder();
        reclamoDao = new ReclamoDaoSql(context);
    }

    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View fila = convertView;

        if (fila == null) {
            fila = inflater.inflate(R.layout.fila_reclamo, parent, false);
            holder.descripcion = fila.findViewById(R.id.textDescripcion);
            holder.email = fila.findViewById(R.id.textEmail);
            holder.resuelto = fila.findViewById(R.id.textResuelto);
        }

        Reclamo reclamo = this.listaReclamos.get(position);

        holder.descripcion.setText(reclamo.getDescripcion());
        holder.resuelto.setText(reclamo.getResuelto()? "Resuelto" : "No Resuelto");
        holder.resuelto.setTextColor(reclamo.getResuelto()? ContextCompat.getColor(context, R.color.colorPrimary) : ContextCompat.getColor(context, R.color.colorNoResuelto ));
        holder.email.setText(reclamo.getMailContacto());

        return fila;
    }

    private class ViewHolder {
        TextView descripcion;
        TextView email;
        TextView resuelto;
        ImageButton eliminar;
    }
}


