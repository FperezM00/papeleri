package com.example.papeleria.firebase;

public class articulo {
    int id;
    String nombre;
    String descripcion;
    String precio;
    String categoria;
    String fecha;
    String cantidad;
    String peso;
    String photoUri;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status;
    public articulo(int id, String nombre, String descripcion, String precio, String tipoD, String fechaCaducidad, String total, String gramos, String photoUri, String status) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = tipoD;
        this.fecha = fechaCaducidad;
        this.cantidad = total;
        this.peso = gramos;
        this.photoUri = photoUri;
        this.status = status;
    }


    public articulo() {
    }



    public articulo(int id, String nombre, String descripcion, String precio, String tipoD, String fechaCaducidad, String total, String gramos, String photoUri) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = tipoD;
        this.fecha = fechaCaducidad;
        this.cantidad = total;
        this.peso = gramos;
        this.photoUri = photoUri;
    }

    public articulo(String nombre, String descripcion, String precio, String tipoD, String fechaCaducidad, String total, String gramos, String photoUri) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = tipoD;
        this.fecha = fechaCaducidad;
        this.cantidad = total;
        this.peso = gramos;
        this.photoUri = photoUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String despliega() {
        String s = "";
        s += "ID: " + getId()+"\n";
        s += "Nombre: " + getNombre()+"\n";
        s += "Descripcion: " + getDescripcion()+"\n";
        s += "Fecha: " + getFecha()+"\n";
        s += "Peso : " + getPeso()+"\n";
        s += "Categoria " + getCategoria()+"\n";
        s += "Precio: " + getPrecio()+"\n";
        s += "Cantidad: " + getCantidad()+"\n";
        s += "Status: " + getStatus()+"\n";


        return s;

    }


    public String desplegar()
    {String s= "";

        s+=getNombre()+"   ";
        s+="$"+getPrecio()+"\n";
        return s;
    }

    public String desplegarto()
    {String s= "";
        s+=getNombre()+"      ";
        s+="$"+getPrecio()+"\n" +"\n";
        s+=""+getDescripcion()+"\n";
        return s;
    }



    public String desplegarConImagen()
    {String s= "";
        s+="Imagen: "+getPhotoUri()+"\n";
        s+=""+getNombre()+"\n";
        s+="la descripcion es: "+getDescripcion()+"\n";

        s+="el precio es: $"+getPrecio()+"\n";
        return s;
    }

    public String desplegarcarrito()
    {String s= "";

        s+=getNombre()+"   ";
        s+="$"+getPrecio()+"\n";
        return s;
    }


    public String desplegarpromo()
    {String s= "";
        if (getStatus().equals("promo")){
            s+=getNombre()+"      ";
            s+="$"+getPrecio()+ "\n";
            s+=""+getDescripcion()+"\n";
        }
        return s;
    }




}
