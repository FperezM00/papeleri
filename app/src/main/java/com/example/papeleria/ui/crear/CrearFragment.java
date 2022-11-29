package com.example.papeleria.ui.crear;

import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.papeleria.R;
import com.example.papeleria.firebase.articulo;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CrearFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener,
DatePickerDialog.OnDateSetListener{
    private Button btnlimpia, btnGuardar;
    private EditText  etNombre, etFechaCaducidad, etTotal, etGramos, etPrecio,etDescripcion;
    private Spinner spnTipoDulce;
    ImageView ivFoto;
    static String URLfoto ;
    FirebaseDatabase firebaseDataBase;
    DatabaseReference databaseReference;
    Bitmap bitmap=null;
    private static  final int File = 1 ;

    private ImageButton btnCalendario;
    private DatePickerDialog dpd;
    private Calendar calendar;
    private static int anio, mes, dia;
    public static String currentPhotoPath, img="/storage/emulated0/Pictures/Twitter/", tipoD;
    private StorageReference mstorage;




    private CrearViewModel mViewModel;

    public static CrearFragment newInstance() {
        return new CrearFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.crear_fragment, container, false);
        Componentes(root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CrearViewModel.class);
        // TODO: Use the ViewModel
    }

    private void Componentes(View root){

        EditTextComponent(root);
        ButtonComponent(root);
        SpinnerComponent(root);
        iniciaFirebase(root);
    }

    private void iniciaFirebase(View root){
        FirebaseApp.initializeApp(root.getContext());
        firebaseDataBase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDataBase.getReference();
        mstorage = FirebaseStorage.getInstance().getReference();
    }

    private void EditTextComponent(View root){

        etNombre = root.findViewById(R.id.fragcrearNombre);
        etFechaCaducidad = root.findViewById(R.id.fragcrear_fechaC);
        etGramos = root.findViewById(R.id.frag_crear_gramos);
        etPrecio = root.findViewById(R.id.fragcrear_precio);
        etTotal = root.findViewById(R.id.fragcrear_total);
        etDescripcion= root.findViewById(R.id.fragcrearDescripcion);
    }

    private void ButtonComponent(View root){
        ivFoto = root.findViewById(R.id.fragcrearFoto);
        btnCalendario = root.findViewById(R.id.frag_crear_ibtn);
        btnlimpia = root.findViewById(R.id.fragcrearbtnLimpiar);
        btnGuardar = root.findViewById(R.id.fragcrearbtnGuardar);

        btnlimpia.setOnClickListener(this);
        btnCalendario.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);
        ivFoto.setOnClickListener(this);

    }

    private void SpinnerComponent(View root){
        ArrayAdapter<CharSequence>  genAdapter;
        genAdapter = ArrayAdapter.createFromResource(getContext(), R.array.TipoDulce,
                android.R.layout.simple_spinner_item);


        spnTipoDulce = root.findViewById(R.id.fragcrear_tipoD);
        spnTipoDulce.setAdapter(genAdapter);
        spnTipoDulce.setOnItemSelectedListener(this);

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {

            case R.id.fragcrear_tipoD:
                if (position != 0){
                    tipoD = adapterView.getItemAtPosition(position).toString();
                    tipoD = adapterView.getSelectedItem().toString();
                }
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragcrearbtnGuardar:
                if ( etNombre.getText().equals("")
                || etFechaCaducidad.getText().equals("") || etTotal.getText().equals("")
                || etGramos.getText().equals("") || etPrecio.getText().equals("")
                ){
                    Toast.makeText(getContext(),"Llenar los campos", Toast.LENGTH_SHORT).show();
                } else {
                    String nombre = etNombre.getText().toString().toUpperCase();
                    tipoD = tipoD.toUpperCase();
                    String fechaCaducidad = etFechaCaducidad.getText().toString();
                    String total = etTotal.getText().toString();
                    String gramos = etGramos.getText().toString();
                    String precio = etPrecio.getText().toString();
                    String descripcion=etDescripcion.getText().toString();

                    int id=(int)(Math.random()*1000);
                    articulo c = new articulo(id,nombre,descripcion, precio, tipoD, fechaCaducidad, total, gramos, URLfoto,"ACTIVO");
                        databaseReference.child("articulo").child(c.getId()+"").setValue(c);
                    limpiar();
                }
                break;
            case R.id.fragcrearbtnLimpiar:
                limpiar();
                break;
            case R.id.frag_crear_ibtn:
                calendar = Calendar.getInstance();
                anio = calendar.get(Calendar.YEAR);
                mes = calendar.get(Calendar.MONTH);
                dia = calendar.get(Calendar.DAY_OF_MONTH);
                dpd = new DatePickerDialog(getContext(), this, anio, mes, dia);
                dpd.show();
                break;
            case R.id.fragcrearFoto:
                Toast.makeText(getContext(),"Tomar foto", Toast.LENGTH_SHORT).show();
                takePicture();
                break;
        }
    }

    private void limpiar(){

        etGramos.setText("");
        etNombre.setText("");
        etFechaCaducidad.setText("");
        etTotal.setText("");
        etPrecio.setText("");
        etDescripcion.setText("");
        ivFoto.setImageResource(R.drawable.ic_menu_camera);
        ArrayAdapter<CharSequence>  dulceAdapter;
        dulceAdapter = ArrayAdapter.createFromResource(getContext(),R.array.TipoDulce, android.R.layout.simple_spinner_item);
        tipoD ="";
        spnTipoDulce.setAdapter(dulceAdapter);
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        etFechaCaducidad.setText(day+"/"+ (month+1) + "/" + year);
    }


    private void takePicture(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,File);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == File){

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG"+timeStamp+"...";

            Uri FileUri = data.getData();
            //Uri FileUri= CropImage.getPickImageResultUri(getContext(),data);

            StorageReference Folder = FirebaseStorage.getInstance().getReference().child("articulo");

            final StorageReference file_name = Folder.child(imageFileName+FileUri.getLastPathSegment());


            file_name.putFile(FileUri).addOnSuccessListener(taskSnapshot -> file_name.getDownloadUrl().addOnSuccessListener(uri -> {

                   /* HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("link", String.valueOf(uri));*/


                Toast.makeText(getContext(), "Se subió correctamente", Toast.LENGTH_SHORT).show();


                Glide.with(getContext())
                        .load(String.valueOf(uri))
                        .fitCenter()
                        .centerCrop()
                        .into(ivFoto);

                URLfoto=String.valueOf(uri);
            }));




        }

    }




}