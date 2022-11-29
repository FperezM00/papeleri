package com.example.papeleria.Carrito;


import com.example.papeleria.firebase.articulo;

import java.util.Vector;


public class Pila extends articulo {


    private int tope;
    private Vector pila;

    public Pila () {
        tope = -1;
        pila = new Vector();
    }

    public void push(articulo elemento) {
        tope++;

        pila.addElement(elemento);
    }


    public articulo pop() {
        articulo aux;
        if (empty()) {
            return null;
        } else {
            aux = (articulo) pila.elementAt(tope);
            pila.removeElementAt(tope);
            tope--;
            return aux;
        }
    }

    public boolean empty() {
        return getTope() == -1;
    }

    public void clean() {
        tope = -1;
        pila.removeAllElements();
    }


    public void setTope(int tope) {
        this.tope = tope;
    }

    public Vector getPila() {
        return pila;
    }

    public void setPila(Vector pila) {
        this.pila = pila;
    }

    /**
     * @return the tope
     */
    public int getTope() {
        return tope;
    }
}
