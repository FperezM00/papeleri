package com.example.papeleria.ui.editar;

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
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditarFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener,
        DatePickerDialog.OnDateSetListener {

    private Button btnlimpiar, btnGuardar, btnBuscar;
    private EditText etId, etNombre, etFechaCad, etTotal, etGramos, etPrecio,etIdR,etDescripcion;
    private Spinner spnTipoDulce, spnStatus;
    ImageView ivFoto;
    private ImageButton btnCalendario;
    private DatePickerDialog dpd;
    private Calendar calendar;
    private Uri photoURI;
    private static  final int File = 1 ;
    private StorageReference mstorage;
    ProgressBar cargando ;
    Bitmap bitmap=null;
    FirebaseDatabase firebaseDataBase;
    DatabaseReference databaseReference;
    String id, fec, nombre, mo, suc, mar, cat, ti, URLFoto,pro,stat;


    static int bnd = 0, idb;
    private static int anio, mes, dia;
    public static String currentPhotoPath, img="/storage/emulated0/Pictures/Twitter/Prueba.jpg", status, tipoDulce;
    private Uri photoUri;

    public static boolean flag = false;
    public static int idp;

    private EditarViewModel mViewModel;

    public static EditarFragment newInstance() {
        return new EditarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.editar_fragment, container, false);
        componentes(root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EditarViewModel.class);
        // TODO: Use the ViewModel
    }
    private void iniciaFirebase(View root){
        FirebaseApp.initializeApp(root.getContext());
        firebaseDataBase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDataBase.getReference();
        mstorage = FirebaseStorage.getInstance().getReference();
    }
    private void componentes(View root){
        iniciaFirebase(root);
        EditTextComponent(root);
        ButtonComponent(root);
        SpinnerComponent(root);
    }

    private void EditTextComponent(View root){
        etIdR=root.findViewById(R.id.frageditarBusqueda);
        etNombre = root.findViewById(R.id.frageditarNombre);
        etFechaCad = root.findViewById(R.id.frageditarFechaCaducidad);
        etGramos = root.findViewById(R.id.frageditargramos);
        etPrecio = root.findViewById(R.id.frageditarPrecio);
        etTotal = root.findViewById(R.id.frag_editar_total);
        etDescripcion=root.findViewById(R.id.frageditarDescripcion);
    }

    private void ButtonComponent(View root){
        ivFoto = root.findViewById(R.id.fragEditarFoto);
        btnCalendario = root.findViewById(R.id.frag_editar_ibtn);
        btnlimpiar = root.findViewById(R.id.frageditarbtnLimpiar);
        btnGuardar = root.findViewById(R.id.frageditarbtnGuardar);
        btnBuscar = root.findViewById(R.id.frag_editar_btn_buscar);

        btnlimpiar.setOnClickListener(this);
        btnCalendario.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);
        ivFoto.setOnClickListener(this);
        btnBuscar.setOnClickListener(this);

    }

    private void SpinnerComponent(View root){
        ArrayAdapter<CharSequence> statusAdapter, drAdapter, genAdapter;
        statusAdapter = ArrayAdapter.createFromResource(getContext(), R.array.status,
                android.R.layout.simple_spinner_item);
        genAdapter = ArrayAdapter.createFromResource(getContext(), R.array.TipoDulce,
                android.R.layout.simple_spinner_item);

        spnStatus = root.findViewById(R.id.frag_editar_status);
        spnStatus.setAdapter(statusAdapter);

        spnTipoDulce = root.findViewById(R.id.frag_editar_tipoDulce);
        spnTipoDulce.setAdapter(genAdapter);


        spnStatus.setOnItemSelectedListener(this);
        spnTipoDulce.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {

            case R.id.frag_editar_tipoDulce:
                if (position != 0){
                    tipoDulce = adapterView.getItemAtPosition(position).toString();
                    tipoDulce = adapterView.getSelectedItem().toString();
                }else{
                    tipoDulce="";
                }
                break;
            case R.id.frag_editar_status:
                if (position != 0){
                    status = adapterView.getItemAtPosition(position).toString();
                    status = adapterView.getSelectedItem().toString();
                }else{
                    status = "";
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
            case R.id.frag_editar_ibtn:
                if (flag){
                    calendar = Calendar.getInstance();
                    anio = calendar.get(Calendar.YEAR);
                    mes = calendar.get(Calendar.MONTH);
                    dia = calendar.get(Calendar.DAY_OF_MONTH);
                    dpd = new DatePickerDialog(getContext(), this, anio, mes, dia);
                    dpd.show();
                    break;
                }else{
                    Toast.makeText(getContext(),"Ingresar una fecha", Toast.LENGTH_SHORT).show();
                    flag = false;
                }
                break;
            case R.id.frageditarbtnLimpiar:
                limpiar();
                break;
            case R.id.frageditarbtnGuardar:
                if (flag) {
                    guardar();
                } else {
                    flag = false;
                    Toast.makeText(getContext(),"Ingresar ID del artculo", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.frag_editar_btn_buscar:
                if (etIdR.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Ingresar ID del artculo", Toast.LENGTH_SHORT).show();
                    flag = false;
                } else {

                    idp = Integer.parseInt(etIdR.getText().toString());

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
                                    articulo pr=objSnapshot.getValue(articulo.class);

                                    Glide.with(getContext())
                                            .load(pr.getPhotoUri())
                                            .fitCenter()
                                            .centerCrop()
                                            .into(ivFoto);

                                etDescripcion.setText(pr.getDescripcion());
                                status =pr.getStatus();
                                etNombre.setText(pr.getNombre());
                                tipoDulce = pr.getCategoria();
                                etFechaCad.setText(pr.getFecha());
                                etTotal.setText(pr.getCantidad());
                                etGramos.setText(pr.getPeso());
                                etPrecio.setText(pr.getPrecio());
                                img=pr.getPhotoUri();

                    }

                                flag=true;
                            }





                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {


                    }
                });
        }
        break;
            case R.id.fragEditarFoto:


                    selecionar();
                    break;


        }
    }


    public static int obtenerPosicion(Spinner spinner, String item){
        int position=0;
        for(int i=0;i<spinner.getCount();i++)
        {
            if(spinner.getItemAtPosition(i).toString().toUpperCase().equals(item.toUpperCase())){
                position=i;

            }
        }
        return position;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        etFechaCad.setText(day+"/"+ (month+1) + "/" + year);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == File){

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG"+timeStamp+"...";

            Uri FileUri = data.getData();
            //Uri FileUri= CropImage.getPickImageResultUri(getContext(),data);

            StorageReference Folder = FirebaseStorage.getInstance().getReference().child("articulo");

            final StorageReference file_name = Folder.child(imageFileName+FileUri.getLastPathSegment());


            file_name.putFile(FileUri).addOnSuccessListener(taskSnapshot -> file_name.getDownloadUrl().addOnSuccessListener(uri -> {

                Toast.makeText(getContext(), "Subido", Toast.LENGTH_SHORT).show();
                URLFoto=String.valueOf(uri);
            }));




        }
    }

    public void selecionar(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,File);
    }

    private void limpiar(){
        etIdR.setText("");
        etGramos.setText("");
        etNombre.setText("");
        etFechaCad.setText("");
        etTotal.setText("");
        etPrecio.setText("");
        ivFoto.setImageResource(R.drawable.dulce);
        ArrayAdapter<CharSequence> statusAdapter, medicoAdapter, dulceAdapter;
        dulceAdapter = ArrayAdapter.createFromResource(getContext(),R.array.TipoDulce, android.R.layout.simple_spinner_item);
        statusAdapter = ArrayAdapter.createFromResource(getContext(),R.array.status, android.R.layout.simple_spinner_item);


        status ="";
        tipoDulce ="";
        spnStatus.setAdapter(statusAdapter);
        spnTipoDulce.setAdapter(dulceAdapter);
    }

    private void guardar(){
        if (etIdR.getText().equals("") || etNombre.getText().equals("")
                || etFechaCad.getText().equals("") || etTotal.getText().equals("")
                || etGramos.getText().equals("") || etPrecio.getText().equals("")
                ||status.equals("")||tipoDulce.equals("")||etDescripcion.getText().equals("")){
            Toast.makeText(getContext(),"Llenar los campos", Toast.LENGTH_SHORT).show();
        } else {
            int id = Integer.parseInt(etIdR.getText().toString());
            status = status.toUpperCase();
            String nombre = etNombre.getText().toString().toUpperCase();
            String fechaCad = etFechaCad.getText().toString();
            String total = etTotal.getText().toString();
            String gramos= etGramos.getText().toString();
            String descripcion=etDescripcion.getText().toString();
            String precio = etPrecio.getText().toString();
            articulo pr = new articulo(id, nombre, descripcion, precio, tipoDulce, fechaCad, total, gramos, img,status);
            databaseReference.child("articulo").child(pr.getId()+"").setValue(pr);
            limpiar();
            Toast.makeText(getContext(), "modicado ", Toast.LENGTH_SHORT).show();


        }


    }
}