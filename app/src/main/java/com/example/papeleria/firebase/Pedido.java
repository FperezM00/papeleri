package com.example.papeleria.firebase;

public class Pedido {

    public Pedido() {
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    int id;
    String listaProductos;
    int total;

    public Pedido(int id, String nombre, int total, int status) {
        this.id = id;
        this.listaProductos = nombre;
        this.total = total;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(String listaProductos) {
        this.listaProductos = listaProductos;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    int status;

    public String despliega() {
        String s = "";
        s += "articulos: " + getListaProductos()+"\n";
        s += "Total: " + getTotal()+"\n";
        s += "Estado: " + getStatus()+"\n";


        return s;

    }






}
