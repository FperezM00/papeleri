package com.example.papeleria.ui.busqueda;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.papeleria.R;

import android.app.DatePickerDialog;
import android.database.Cursor;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.papeleria.bd.SQLite;
import com.example.papeleria.firebase.articulo;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;
import java.util.Calendar;

public class BuscarFragment extends Fragment implements View.OnClickListener {

    private Button btnlimpiar, btnElimi, btnBuscar;
    private EditText etId;
    private TextView tvId, tvNombre, tvGramos, tvPrecio, tvTotal, tvStatus,tvIdR, tvDescripcion, tvTipoDulce, tvFechaC;
    ImageView ivFoto;
    private ImageButton btnCalendario;
    private DatePickerDialog dpd;
    private Calendar calendar;
    Cursor cursor;
    private static int anio, mes, dia;
    public static String currentPhotoPath, img="/storage/emulated0/Pictures/Twitter/Prueba.jpg", a, d, sex;
    private URI photoUri;
    private SQLite sqlite;
    public static boolean flag = false;
    public static int idp;
    int idr;
    FirebaseDatabase firebaseDataBase;
    DatabaseReference databaseReference;

    private BuscarViewModel mViewModel;

    public static BuscarFragment newInstance() {
        return new BuscarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       View root= inflater.inflate(R.layout.buscar_fragment, container, false);
        sqlite=new SQLite(getContext());
        Componentes(root);
        return root;
    }
    private void Componentes(View root){
        EditTextComponentes(root);
        TexViewComponentes(root);
        iniciaFirebase(root);
        ButtonComponentes(root);
    }
    private void EditTextComponentes(View root){
        etId=root.findViewById(R.id.fragbuscarBusqueda);
    }
    private void TexViewComponentes(View root){
        tvDescripcion =root.findViewById(R.id.fragbuscarDescripcion);
        tvTipoDulce =root.findViewById(R.id.fragbuscarTipoDulce);
        tvNombre=root.findViewById(R.id.fragbuscarNombre);
        tvFechaC =root.findViewById(R.id.fragbuscarFechaCad);
        tvGramos =root.findViewById(R.id.fragbuscarGramos);
        tvPrecio =root.findViewById(R.id.fragbuscarPrecio);
        tvTotal =root.findViewById(R.id.fragbuscarTotal);
        tvStatus =root.findViewById(R.id.fragbuscarStatus);
        ivFoto=root.findViewById(R.id.fragbuscarFoto);
    }

    private void iniciaFirebase(View root){
        FirebaseApp.initializeApp(root.getContext());
        firebaseDataBase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDataBase.getReference();


    }
    private void ButtonComponentes(View root){

        btnBuscar=root.findViewById(R.id.fragbuscarbtnbuscar);
        btnBuscar.setOnClickListener(this);

    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.fragbuscarbtnbuscar:
                if (etId.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Ingresar ID del dulce", Toast.LENGTH_LONG).show();

                } else {
                    sqlite.abrir();
                    idp = Integer.parseInt(etId.getText().toString());
                    Query query=databaseReference.child("articulo").orderByChild("id").equalTo(idp);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int cont=0;
                            for (DataSnapshot objSnapshot: snapshot.getChildren()) {
                                cont++;
                            }
                            if (cont==1){
                                for (DataSnapshot objSnapshot: snapshot.getChildren()){
                                    articulo c =objSnapshot.getValue(articulo.class);

                                    Glide.with(getContext())
                                            .load(c.getPhotoUri())
                                            .fitCenter()
                                            .centerCrop()
                                            .into(ivFoto);

                                    tvDescripcion.setText(c.getDescripcion());
                                    tvStatus.setText(c.getStatus());
                                    tvNombre.setText(c.getNombre());
                                    tvTipoDulce.setText( c.getCategoria());
                                    tvFechaC.setText(c.getFecha());
                                    tvTotal.setText(c.getCantidad());
                                    tvGramos.setText(c.getPeso());
                                    tvPrecio.setText(c.getPrecio());
                                    img= c.getPhotoUri();

                                }


                            }





                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {


                        }
                    });
                }
                break;

        }
    }

    public static int obtenerPosicion(Spinner spinner, String item){
        int position=0;
        for(int i=0;i<spinner.getCount();i++)
        {
            if(spinner.getItemAtPosition(i).toString().equals(item)){
                position=i;

            }
        }
        return position;
    }
    private void limpiar(){
        etId.setText("");
        tvTotal.setText("");
        tvNombre.setText("");
        tvGramos.setText("");
        tvPrecio.setText("");
        tvStatus.setText("");
        ivFoto.setImageResource(R.drawable.dulce);
        tvFechaC.setText("");
        tvTipoDulce.setText("");
        tvDescripcion.setText("");
        flag=false;

    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BuscarViewModel.class);
        // TODO: Use the ViewModel
    }

}