package com.example.papeleria.firebase;




    public class User {

        String correo;
        String name;
        int nivel;

        public User(String correo, String name, int status) {
            this.correo = correo;
            this.name = name;
            this.nivel = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        public User(String correo, int status) {
            this.correo = correo;
            this.nivel = status;
        }



        public User() {

        }



        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
        }

        public int getNivel() {
            return nivel;
        }

        public void setNivel(int nivel) {
            this.nivel = nivel;
        }


    }
