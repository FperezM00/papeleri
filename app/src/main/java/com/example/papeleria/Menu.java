package com.example.papeleria;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;



public class Menu extends AppCompatActivity implements View.OnClickListener{

    ImageButton btnpapeleria, btnoficina,btnregalos,btnotros;



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.papeplanet)
                    .setTitle("¿Desea Salir de la aplicación?")
                    .setCancelable(false)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {// un listener que al pulsar, cierre la aplicacion
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish(); //Si solo quiere mandar la aplicación a segundo plano
                        }
                    }).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        Botones();
        horarioApertura();
    }

    private void Botones() {
        btnpapeleria =findViewById(R.id.torteriabtn);
        btnoficina =findViewById(R.id.buttonoficina);
  btnotros =findViewById(R.id.ButtonOtros);
  btnregalos =findViewById(R.id.ButtonRegalos);

        btnpapeleria.setOnClickListener(this);
        btnotros.setOnClickListener(this);
                btnregalos.setOnClickListener(this);
        btnoficina.setOnClickListener(this);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)

    private void horarioApertura() {
        long ahora = System.currentTimeMillis();
        Calendar calendario = Calendar.getInstance();
        calendario.setTimeInMillis(ahora);
        int hora = calendario.get(Calendar.HOUR_OF_DAY);
        int minuto = calendario.get(Calendar.MINUTE);

       /* if (hora >=8 && hora <=18){
            btntorteria.setVisibility(View.VISIBLE);
        }
        else {
            btntorteria.setVisibility(View.GONE);
        }
*/
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.torteriabtn:
               Intent torteria= new Intent(getApplicationContext(), CarritoCompra.class);
               startActivity(torteria);
                break;
            case R.id.buttonoficina:
                Intent oficina= new Intent(getApplicationContext(), CarritoCompraO.class);
                startActivity(oficina);
                break;

            case R.id.ButtonOtros:
                Intent otros= new Intent(getApplicationContext(), CarritoCompraOtros.class);
                startActivity(otros);
                break;


            case R.id.ButtonRegalos:
                Intent regalos= new Intent(getApplicationContext(), CarritoCompraR.class);
                startActivity(regalos);
                break;

        }

    }
}
