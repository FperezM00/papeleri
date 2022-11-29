package com.example.papeleria.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQL extends SQLiteOpenHelper {
    private static final String database = "dulces.db";
    private static final int VERSION = 1;
    private final String tableDulces = "CREATE TABLE DULCES(" +
            "                ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "                NOMBRE TEXT NOT NULL," +
            "                DESCRIPCION TEXT NOT NULL," +
            "                PRECIO TEXT NOT NULL," +
            "                TIPODULCE TEXT NOT NULL," +
            "                FECHACADUCIDAD TEXT NOT NULL," +
            "                TOTAL TEXT NOT NULL," +
            "                GRAMOS TEXT NOT NULL," +
            "                STATUS TEXT NOT NULL," +
            "                IMAGEN TEXT NOT NULL)";

    public SQL(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SQL(Context context){
        super(context, database, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(tableDulces);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS DULCES");
            onCreate(sqLiteDatabase);
        }
    }
}
