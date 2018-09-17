package ofa.cursos.android.app01.reclamos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ofa.cursos.android.app01.reclamos.Dao.ReclamoDaoSql;
import ofa.cursos.android.app01.reclamos.Model.Reclamo;

public class ReclamoActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    protected ViewHolder holder;
    protected Reclamo reclamo;
    protected ReclamoDaoSql reclamoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reclamo);

        holder = new ViewHolder();
        reclamo = new Reclamo();
        reclamoDao = new ReclamoDaoSql(this);

        holder.reclamo = findViewById(R.id.editReclamo);
        holder.mail = findViewById(R.id.editMail);
        holder.imagen = findViewById(R.id.imageView);
        holder.cargarImagen = findViewById(R.id.buttonImagen);
        holder.hacerReclamo = findViewById(R.id.buttonReclamo);
        holder.cancelar = findViewById(R.id.buttonCancelar);

        Intent coordenada = getIntent();
        String longitud = coordenada.getStringExtra("longitud");
        String latitud = coordenada.getStringExtra("latitud");
        LatLng ubicacion = new LatLng(Double.valueOf(latitud),Double.valueOf(longitud));
        reclamo.setUbicacion(ubicacion);

        holder.cargarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        holder.hacerReclamo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                reclamo.setDescripcion(holder.reclamo.getText().toString());
                reclamo.setMailContacto(holder.mail.getText().toString());
                reclamo.setResuelto(false);

                reclamoDao.agregar(reclamo);

                Intent returnIntent = new Intent(ReclamoActivity.this,MapsActivity.class);
                String dato = reclamo.toJson().toString();
                returnIntent.putExtra("result", dato);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            holder.imagen.setImageBitmap(imageBitmap);
            holder.imagen.setVisibility(View.VISIBLE);
            reclamo.setPathImagen( this.saveToInternalStorage(imageBitmap));
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        File directory = getDir("imageDir", Context.MODE_PRIVATE);
        File mypath=new File(directory,"profile.jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public static class ViewHolder {
        EditText reclamo;
        EditText mail;
        ImageView imagen;
        View cargarImagen;
        View hacerReclamo;
        View cancelar;
    }
}
