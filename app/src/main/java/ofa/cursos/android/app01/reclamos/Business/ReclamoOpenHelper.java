package ofa.cursos.android.app01.reclamos.Business;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReclamoOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "reclamo.db";

    public ReclamoOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Reclamo ( _id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Descripcion TEXT, Resuelto INTEGER, Email TEXT, Imagen TEXT, Longitud TEXT, Latitud TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
