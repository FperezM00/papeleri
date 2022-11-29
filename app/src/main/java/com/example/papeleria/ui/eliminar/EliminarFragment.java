package com.example.papeleria.ui.eliminar;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.papeleria.R;
import com.example.papeleria.firebase.articulo;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.net.URI;
import java.util.Calendar;

public class EliminarFragment extends Fragment implements  View.OnClickListener{
    articulo c;
    private StorageReference mstorage;
    ProgressBar cargando ;
    Bitmap bitmap=null;
    FirebaseDatabase firebaseDataBase;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
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

    public static boolean flag = false;
    public static int idp;
int idr;
    private EliminarViewModel mViewModel;

    public static EliminarFragment newInstance() {
        return new EliminarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.eliminar_fragment, container, false);

        Componentes(root);
        return root;
    }
    private void Componentes(View root){
        EditTextComponentes(root);
        TexViewComponentes(root);
        ButtonComponentes(root);
        iniciaFirebase(root);
    }

        private void iniciaFirebase(View root){
            FirebaseApp.initializeApp(root.getContext());
            firebaseDataBase=FirebaseDatabase.getInstance();
            databaseReference=firebaseDataBase.getReference();
            databaseReference2=firebaseDataBase.getReference();

        }

    private void EditTextComponentes(View root){
      etId=root.findViewById(R.id.frageliminarBusqueda);
    }
    private void TexViewComponentes(View root){
        tvDescripcion =root.findViewById(R.id.frageliminarDescripcion);
        tvTipoDulce =root.findViewById(R.id.frageliminarTipoDulce);
        tvNombre=root.findViewById(R.id.frageliminarNombre);
        tvFechaC =root.findViewById(R.id.frageliminarFechaCad);
        tvGramos =root.findViewById(R.id.frageliminarGramos);
        tvPrecio =root.findViewById(R.id.frageliminarPrecio);
        tvTotal =root.findViewById(R.id.frageliminarTotal);
        tvStatus =root.findViewById(R.id.frageliminarStatus);
        ivFoto=root.findViewById(R.id.frageliminarFoto);
    }
    private void ButtonComponentes(View root){
  btnlimpiar=root.findViewById(R.id.fragelimbtnLimpiar);
  btnBuscar=root.findViewById(R.id.frageliminarbtnbuscar);
  btnElimi=root.findViewById(R.id.fragelimbtnEliminar);
  btnlimpiar.setOnClickListener(this);
  btnBuscar.setOnClickListener(this);
  btnElimi.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.fragelimbtnLimpiar:limpiar();break;
            case R.id.frageliminarbtnbuscar:
                if (etId.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Ingresar ID del dulce", Toast.LENGTH_SHORT).show();
                    flag = false;
                } else {

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
                                     c =objSnapshot.getValue(articulo.class);

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
            case R.id.fragelimbtnEliminar:
                if(!flag){
                    View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialogo_paciente, null);
                    ((TextView) dialogView.findViewById(R.id.diapac_textview)).setText("¿Qué desea hacer con el registro?");
                    ((TextView) dialogView.findViewById(R.id.textoInfo)).setText(tvNombre.getText()+"\n"+tvDescripcion.getText()+"\n"+tvTipoDulce.getText()+"\n"+tvFechaC.getText()+"\n"
                            +tvGramos.getText()+"\n"+tvPrecio.getText()+"\n"+tvTotal.getText()+"\n"+tvStatus.getText()+"\n");
                    cargarImagen(img,dialogView.findViewById(R.id.imgDialogoPaci));
                    AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
                    dialogo.setTitle("Importante");
                    dialogo.setView(dialogView);
                    dialogo.setCancelable(false);
                    dialogo.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            databaseReference2.child("candy").child(c.getId()+"").removeValue();

                            Toast.makeText(getContext(),"Registro eliminado",Toast.LENGTH_LONG).show();
                            limpiar();
                            flag=false;
                        }
                    });
                    dialogo.setNegativeButton("Dar de baja", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            c.setStatus("INACTIVO");
                            databaseReference.child("articulo").child(c.getId()+"").setValue(c);

                            tvStatus.setText("Status: INACTIVO");
                            Toast.makeText(getContext(),"Registro inactivo",Toast.LENGTH_SHORT).show();
                            flag=false;
                        }
                    });
                    dialogo.show();
                }
                break;

        }
    }
    private void cargarImagen(String path, ImageView image){
        try {
            Uri uriPhoto = Uri.parse(path);
            image.setImageURI(uriPhoto);
        } catch (Exception e){
            Toast.makeText(getContext(),"Fallo en carga de imagen",Toast.LENGTH_SHORT).show();
            Log.d("Carga de imagen listado", "Error al cargar la imagen\n" + path + e.getMessage() +
                    "Causa" + e.getCause());
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
        mViewModel = new ViewModelProvider(this).get(EliminarViewModel.class);

    }

}