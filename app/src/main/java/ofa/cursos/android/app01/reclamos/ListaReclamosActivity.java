package ofa.cursos.android.app01.reclamos;


import ofa.cursos.android.app01.reclamos.Adapters.ReclamoAdapter;
import ofa.cursos.android.app01.reclamos.Dao.ReclamoDaoSql;
import ofa.cursos.android.app01.reclamos.Model.Reclamo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;

public class ListaReclamosActivity extends AppCompatActivity {
    protected ReclamoDaoSql reclamoDao;
    protected Reclamo reclamo;
    protected View volver;
    private ReclamoAdapter adaptadorReclamos;
    private ListView listaReclamos;
    private List<Reclamo> reclamos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_reclamos);

        reclamoDao = new ReclamoDaoSql(this);
        reclamos = reclamoDao.listarTodos();

        listaReclamos = findViewById(R.id.listaReclamos);
        volver = findViewById(R.id.volver);
        adaptadorReclamos = new ReclamoAdapter(ListaReclamosActivity.this, reclamos);
        listaReclamos.setAdapter(adaptadorReclamos);

        listaReclamos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                reclamo = (Reclamo) listaReclamos.getItemAtPosition(position);
                TextView resuelto = (TextView) view.findViewById(R.id.textResuelto);
                if(reclamo.getResuelto()){
                    reclamo.setResuelto(false);
                    reclamoDao.actualizar(reclamo);
                    resuelto.setText("No Resuelto");
                    resuelto.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorNoResuelto));
                    adaptadorReclamos.notifyDataSetChanged();

                } else {
                    reclamo.setResuelto(true);
                    reclamoDao.actualizar(reclamo);
                    resuelto.setText("Resuelto");
                    resuelto.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    adaptadorReclamos.notifyDataSetChanged();
                }
                return false;
            }
        });

/*        listaReclamos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                reclamo = (Reclamo) listaReclamos.getItemAtPosition(position);
                reclamoDao.eliminar(reclamo);
                reclamos.remove(position);
                adaptadorReclamos.notifyDataSetChanged();
            }
        });*/

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent(ListaReclamosActivity.this, MapsActivity.class);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }
}
