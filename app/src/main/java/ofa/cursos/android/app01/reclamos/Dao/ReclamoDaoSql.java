package ofa.cursos.android.app01.reclamos.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import ofa.cursos.android.app01.reclamos.Business.DatabaseManager;
import ofa.cursos.android.app01.reclamos.Business.ReclamoOpenHelper;
import ofa.cursos.android.app01.reclamos.Model.Reclamo;

public class ReclamoDaoSql {

    public ReclamoDaoSql(Context ctx) {
        DatabaseManager.initializeInstance(new ReclamoOpenHelper(ctx));
    }

    public void agregar(Reclamo reclamo) {
        SQLiteDatabase dbConn = DatabaseManager.getInstance().openDatabase();
        try {
            dbConn.beginTransaction();
            ContentValues cvPedido = new ContentValues();

            cvPedido.put("Descripcion", reclamo.getDescripcion());
            cvPedido.put("Resuelto",reclamo.getResuelto());
            cvPedido.put("Email",reclamo.getMailContacto());
            cvPedido.put("Imagen",reclamo.getPathImagen());
            cvPedido.put("Longitud",String.valueOf(reclamo.getUbicacion().longitude));
            cvPedido.put("Latitud",String.valueOf(reclamo.getUbicacion().latitude));
            dbConn.insert("Reclamo","_id",cvPedido);
            dbConn.setTransactionSuccessful();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            dbConn.endTransaction();
        }
    }

    public void eliminar(Reclamo reclamo) {
        SQLiteDatabase dbConn = DatabaseManager.getInstance().openDatabase();
        try {
            dbConn.beginTransaction();
            long idReclamoInsertado = dbConn.delete("Reclamo","_id= ?", new String[]{reclamo.getId().toString()});
            dbConn.setTransactionSuccessful();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            dbConn.endTransaction();
        }
    }

    public ArrayList<Reclamo> listarTodos() {
        ArrayList<Reclamo> resultado = new ArrayList<>();
        SQLiteDatabase dbConn = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = dbConn.rawQuery("SELECT _ID, Descripcion, Resuelto, Email, Imagen, Longitud, Latitud from Reclamo ",null);
        while (cursor.moveToNext()){
            Reclamo aux = new Reclamo();
            aux.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            aux.setDescripcion(cursor.getString(cursor.getColumnIndex("Descripcion")));
            aux.setMailContacto(cursor.getString(cursor.getColumnIndex("Email")));
            aux.setPathImagen(cursor.getString(cursor.getColumnIndex("Imagen")));
            aux.setResuelto(cursor.getInt(cursor.getColumnIndex("Resuelto"))>0);
            LatLng ubicacion = new LatLng(Double.valueOf(cursor.getString(cursor.getColumnIndex("Latitud"))),Double.valueOf(cursor.getString(cursor.getColumnIndex("Longitud"))));
            aux.setUbicacion(ubicacion);
            resultado.add(aux);
        }
        DatabaseManager.getInstance().closeDatabase();
        return resultado;
    }

    public void actualizar(Reclamo nw) {
        SQLiteDatabase dbConn = DatabaseManager.getInstance().openDatabase();
        try {
            dbConn.beginTransaction();
            ContentValues reclamo = new ContentValues();
            reclamo.put("Descripcion",nw.getDescripcion());
            reclamo.put("Email",nw.getMailContacto());
            reclamo.put("Resuelto",nw.getResuelto()?1:0);
            reclamo.put("Imagen",nw.getPathImagen());
            reclamo.put("Longitud",String.valueOf(nw.getUbicacion().longitude));
            reclamo.put("Latitud",String.valueOf(nw.getUbicacion().latitude));
            dbConn.update("Reclamo", reclamo,"_id=?", new String[]{nw.getId().toString()});
            dbConn.setTransactionSuccessful();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            dbConn.endTransaction();
        }
    }
}
