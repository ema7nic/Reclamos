package ofa.cursos.android.app01.reclamos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ofa.cursos.android.app01.reclamos.Dao.ReclamoDaoSql;
import ofa.cursos.android.app01.reclamos.Model.Reclamo;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_CREAR_RECLAMO = 1;
    private static final int REQUEST_LISTAR_RECLAMO = 2;
    protected ReclamoDaoSql reclamoDao;
    protected List<Reclamo> listReclamos;
    Map<String, MarkerOptions> mapMarcadores;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        reclamoDao = new ReclamoDaoSql(this);
        listReclamos = reclamoDao.listarTodos();
        mapMarcadores = new HashMap<String, MarkerOptions>();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng santaFe = new LatLng(-31.6333, -60.7);
        CameraUpdate center = CameraUpdateFactory.newLatLng(santaFe);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);

        for (Reclamo reclamo : listReclamos) {
            MarkerOptions mo = new MarkerOptions().position(reclamo.getUbicacion()).title(reclamo.getMailContacto()).snippet(reclamo.getDescripcion())
                    .icon(BitmapDescriptorFactory.defaultMarker(reclamo.getResuelto() ? BitmapDescriptorFactory.HUE_BLUE : BitmapDescriptorFactory.HUE_RED));
            mapMarcadores.put(reclamo.getMailContacto(), mo);
            mMap.addMarker(mo);
        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Intent nuevoReclamoIntent = new Intent(MapsActivity.this, ReclamoActivity.class);
                nuevoReclamoIntent.putExtra("longitud", String.valueOf(latLng.longitude));
                nuevoReclamoIntent.putExtra("latitud", String.valueOf(latLng.latitude));
                startActivityForResult(nuevoReclamoIntent, REQUEST_CREAR_RECLAMO);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reclamos_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuListaReclamos:
                startActivityForResult(new Intent(MapsActivity.this, ListaReclamosActivity.class), REQUEST_LISTAR_RECLAMO);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CREAR_RECLAMO && resultCode == RESULT_OK) {
            String reclamoJson = data.getStringExtra("result");
            Reclamo r = new Reclamo();
            try {
                r.loadFromJson(new JSONObject(reclamoJson));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            float color = r.getResuelto() ? BitmapDescriptorFactory.HUE_BLUE : BitmapDescriptorFactory.HUE_RED;
            listReclamos.add(r);

            MarkerOptions mo = new MarkerOptions().position(r.getUbicacion()).title(r.getMailContacto()).snippet(r.getDescripcion());
            mapMarcadores.put(r.getMailContacto(), mo);
            mMap.addMarker(mo);
        } else {
            mMap.clear();
            listReclamos.clear();
            listReclamos = reclamoDao.listarTodos();
            for (Reclamo reclamo : listReclamos) {
                MarkerOptions mo = mapMarcadores.get(reclamo.getMailContacto());
                if (reclamo.getResuelto()) {
                    mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                } else {
                    mo.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }
                mMap.addMarker(mo);
            }
        }
    }
}
