package com.example.papeleria.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class SQLite {
    private SQL sql;
    private SQLiteDatabase db;

    public SQLite(Context context) {
        sql = new SQL(context);
    }

    public void abrir() {
        Log.i("SQLite", "Se abre conexion a la bdd" + sql.getDatabaseName());
        db = sql.getWritableDatabase();
    }

    public void cerrar() {
        Log.i("SQLite", "Se cierra conexion a bdd" + sql.getDatabaseName());
        sql.close();
    }

    public boolean agregarDulce(String nombre,String descripcion,String precio,String tipodulce,String fechacaducidad,String total,String gramos,String imagen) {
        ContentValues values = new ContentValues();
        values.put("NOMBRE", nombre);
        values.put("DESCRIPCION", descripcion);
        values.put("PRECIO", precio);
        values.put("TIPODULCE", tipodulce);
        values.put("FECHACADUCIDAD", fechacaducidad);
        values.put("TOTAL", total);
        values.put("GRAMOS", gramos);
        values.put("STATUS", "ACTIVO");
        values.put("IMAGEN", imagen);

        return (db.insert("DULCES", null, values) != 1) ? true : false;
    }

    public Cursor getCursorDulcesActivos(String status) {
        return db.rawQuery("SELECT * FROM DULCES WHERE STATUS='"+status+"'", null);
    }

    public ArrayList<String> getRegistroDulces(Cursor cursor) {
        ArrayList<String> listData = new ArrayList<>();
        String item = "";
        if (cursor.moveToFirst()) {
            do {
                item += "ID:[" + cursor.getString(0) + "]\t";
                item += "NOMBRE:[" + cursor.getString(1) + "]\n";
                item += "DESCRIPCION:[" + cursor.getString(2) + "]\n";
                item += "PRECIO:[" + cursor.getString(3) + "]\n";
                item += "TIPO DE DULCE:[" + cursor.getString(4) + "]\n";
                item += "FECHA DE CADUCIDAD:[" + cursor.getString(5) + "]\n";
                item += "TOTAL:[" + cursor.getString(6) + "]\t";
                item += "GRAMOS:[" + cursor.getString(7) + "]\t";
                item += "STATUS:[" + cursor.getString(8) + "]";
                listData.add(item);
                item = "";
            } while (cursor.moveToNext());
        }
        return listData;
    }

    public ArrayList<String> getRegistroImagen(Cursor cursor) {
        ArrayList<String> listData = new ArrayList<>();
        if (cursor.moveToFirst()){
            do{
                listData.add("IMAGEN:[" + cursor.getString(9) + "]");
            }while(cursor.moveToNext());
        }
        return listData;
    }
    public ArrayList<String> getRegistroID(Cursor cursor) {
        ArrayList<String> listData = new ArrayList<>();
        if (cursor.moveToFirst()){
            do{
                listData.add(cursor.getString(0));
            }while(cursor.moveToNext());
        }
        return listData;
    }



    public String updateDulce(int id,String nombre,String descripcion,String precio,String tipodulce,String fechacaducidad,String total,String gramos,String status,String imagen) {
        ContentValues values = new ContentValues();
        values.put("ID", id);
        values.put("NOMBRE", nombre);
        values.put("DESCRIPCION", descripcion);
        values.put("PRECIO", precio);
        values.put("TIPODULCE", tipodulce);
        values.put("FECHACADUCIDAD", fechacaducidad);
        values.put("TOTAL", total);
        values.put("GRAMOS", gramos);
        values.put("STATUS", status);
        values.put("IMAGEN", imagen);

        String WHERE = "ID ="+id;

        int valor = db.update("DULCES", values, WHERE, null);
        return valor==1?"Dulce actualizado":"Error de actulizacion";
    }

    public String updateStatus(int id){
        ContentValues values = new ContentValues();
        values.put("STATUS","INACTIVO");
        String WHERE = "ID ="+id;
        int valor = db.update("DULCES", values, WHERE, null);
        return valor==1?"Dulce inactivo":"Error de actulizacion";
    }
    public String updateStatus(int id,String status){
        ContentValues values = new ContentValues();
        values.put("STATUS",status);
        String WHERE = "ID ="+id;
        int valor = db.update("DULCES", values, WHERE, null);
        return valor==1?"Dulce "+status:"Error de actulizacion";
    }


    public Cursor getValor(int id){
        return db.rawQuery("SELECT * FROM DULCES WHERE ID = " + id,null);
    }

    public int eliminar(String id){
        return db.delete("DULCES", "ID="+id,null);
    }


}
