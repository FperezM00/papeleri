package com.example.papeleria;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.papeleria.Carrito.Pila;
import com.example.papeleria.firebase.Pedido;
import com.example.papeleria.firebase.articulo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CarritoCompraR extends AppCompatActivity {



    ListView lProductoT;
    private Button boton;
    private Button botonComprar;
    ListView listView;
    Pila carrito = new Pila();

    private List<articulo> ListaProductosT = new ArrayList<articulo>();

    articulo ProductoSelected;
    TextView num;
    TextView pedido;
    ArrayList<String> arrayList=new ArrayList<>();
    ArrayAdapter<String> arrayAdapterProductoT;

    FirebaseDatabase firebaseDataBase;
    DatabaseReference databaseReference;

    DatabaseReference databaseReference2;

    static String idasignado="";
    String nombreusu="kike";

    Dialog customDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carrito_compra);

        //ImageView Foto = (ImageView) findViewById(R.id.imagenproducto);
        num = findViewById(R.id.id_cantidad);
        num.setText("0");

        preciototal();

        databaseReference=FirebaseDatabase.getInstance().getReference("articulo");
        listView=(ListView)findViewById(R.id.productos);
        arrayAdapterProductoT =new ArrayAdapter<String>(getApplicationContext(), R.layout.color_text,arrayList);
        listView.setAdapter(arrayAdapterProductoT);

        databaseReference.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getValue(articulo.class).getStatus().equals("ACTIVO")&&snapshot.getValue(articulo.class).getCategoria().equals("REGALOS")){
                    String value = snapshot.getValue(articulo.class).desplegar();
                    arrayList.add(value);
                    arrayAdapterProductoT.notifyDataSetChanged();}
                if (snapshot.getValue(articulo.class).getStatus().equals("PROMO")){
                    promo(snapshot.getValue(articulo.class));}
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) { }


        });


        iniciaFirebase();




        lProductoT = findViewById(R.id.productos);
        lProductoT.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_producto,null);
                String prducto  = (String) parent.getItemAtPosition(position);

                String[] parts = prducto.split("  ");
                String productoname = parts[0];



                Query query=databaseReference.child("articulo").orderByChild("nombre").equalTo(productoname).limitToFirst(1);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int cont=0;
                        for (DataSnapshot objSnapshot: snapshot.getChildren()) {
                            cont++;
                        }

                        if (cont==1){
                            for (DataSnapshot objSnapshot: snapshot.getChildren()){
                                articulo p=objSnapshot.getValue(articulo.class);

                                mostrar( p);




                            }

                        }else {
                            Toast.makeText(CarritoCompraR.this,"El articulo '"+ productoname +"' No existe", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {


                    }
                });


            }

        });

        boton = findViewById(R.id.btnVerCarro);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarDialogoPersonalizado();
            }

        });
        botonComprar = findViewById(R.id.btnComprar);
        botonComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprar();
            }

        });

    }

    private void iniciaFirebase(){
        firebaseDataBase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDataBase.getReference();
        databaseReference2 =firebaseDataBase.getReference();

    }








public  void comprar(){
    int id=(int)(Math.random()*1000);
    Pedido p = new Pedido();
    p.setId(id);
    p.setListaProductos(desplegararticulos("")+"");
    p.setTotal(desplegarcarritototal(0)+0);
    p.setStatus(1);
    if (p.getTotal()>0) {
        databaseReference.child("pedido").child(p.getId() + "").setValue(p);
        pedido = findViewById(R.id.txtPedido);
        pedido.setText("su numero de pedido es: " + p.getId());
        deshabilitar();
    }

    else{

        Toast.makeText(getApplicationContext(),"No  hay productos en el carrito",Toast.LENGTH_SHORT).show();

    }
}



    public void eliminarcarrito(int id){
        Pila aux = new Pila();
        Pila aux2 = new Pila();
        aux = carrito;

        articulo obj ;
        while (!aux.empty()){
            obj= carrito.pop();
            if (obj.getId()!=id)
                aux2.push(obj);
        }
        carrito = aux2;

    }

    public int desplegarcarritototal( int s ){
        Pila aux = new Pila() ;
        Pila aux2 = new Pila() ;
        articulo obj ;
        aux = carrito;
        while (!aux.empty()){
            obj=aux.pop();
            s+=Integer.parseInt(obj.getPrecio());
            aux2.push(obj);
        }

        carrito=ordenar_pila(aux2);

        return s;}

    public String desplegararticulos( String s ){
        Pila aux = new Pila() ;
        Pila aux2 = new Pila() ;
        articulo obj ;
        aux = carrito;
        while (!aux.empty()){
            obj=aux.pop();
            s+=obj.getNombre()+" \n";
            aux2.push(obj);
        }

        carrito=ordenar_pila(aux2);

        return s;}

    public String desplegarcarrito(String s ){
        Pila aux = new Pila() ;
        Pila aux2 = new Pila() ;
        articulo obj ;
        aux = carrito;
        while (!aux.empty()){
            obj=aux.pop();
            s+= obj.desplegarcarrito()+"\n";
            aux2.push(obj);
        }
        carrito=ordenar_pila(aux2);
        return s;}


    public void numeroproductos(){
        int contador = carrito.getTope()+1;
        num.setText((""+contador));
    }

    public Pila ordenar_pila(Pila va){
        Pila aux = new Pila() ;
        Pila aux2 = new Pila() ;
        aux = va;
        while (!aux.empty()){
            aux2.push(aux.pop());
        }
        va= aux2;

        return va;}


    public void vaciarcarrito(){
        carrito.clean();
        preciototal();
        numeroproductos();
    }



    public void preciototal(){

        TextView nump = findViewById(R.id.ID_precio);

        nump.setText("$"+(desplegarcarritototal(0)));
    }



    private void mostrarDialogoPersonalizado(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CarritoCompraR.this);

        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_carrito,null);

        builder.setView(view);


        final AlertDialog dialog = builder.create();
        dialog.show();

        TextView txt = view.findViewById(R.id.text_dialogdetalle);
        txt.setText(desplegarcarrito("")+"     El total es: "+desplegarcarritototal(0));

        Button btnComprar = view.findViewById(R.id.btnReintentar);
        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           comprar();
                dialog.dismiss();

            }
        });
        Button btnvaciar = view.findViewById(R.id.btnCancel);
        btnvaciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaciarcarrito();
                Toast.makeText(getApplicationContext(),"Carrito vacio",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }




    public String desplegarpedidos(String s ){
        Pila aux = new Pila() ;
        Pila aux2 = new Pila() ;
        articulo obj ;
        aux = carrito;
        while (!aux.empty()){
            obj=aux.pop();
            s+= obj.getNombre()+"\n";
            aux2.push(obj);
        }
        carrito=ordenar_pila(aux2);
        return s;}




    public void mostrar(  articulo pc)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(CarritoCompraR.this);

        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_detalle,null);

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        ImageView productofoto =  view.findViewById(R.id.ImagenProducto);
        TextView txt1 = view.findViewById(R.id.detalleproductotext);
        txt1.setText(pc.desplegarto()+"");
        Glide.with(getApplicationContext())
                .load(pc.getPhotoUri())
                .fitCenter()
                .into((productofoto));

        Button agregar = view.findViewById(R.id.btnAgregarCarrito);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carrito.push(pc);
                numeroproductos();
                preciototal();
                Toast.makeText(getApplicationContext(),"agregado al carrito",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


    }


    public void promo(  articulo pc)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(CarritoCompraR.this);

        LayoutInflater inflater = getLayoutInflater();

        View view = inflater.inflate(R.layout.dialogo_promocion,null);

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        ImageView productofoto =  view.findViewById(R.id.Imagen_promo);
        TextView txt1 = view.findViewById(R.id.id_promocion);
        txt1.setText(pc.desplegarpromo()+"");
        Glide.with(getApplicationContext())
                .load(pc.getPhotoUri())
                .fitCenter()
                .into((productofoto));

        Button agregar = view.findViewById(R.id.btnagrega_carrito);
        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carrito.push(pc);
                numeroproductos();
                preciototal();
                Toast.makeText(getApplicationContext(),"agregado al carrito",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


    }

    public void deshabilitar(){
        Button boton = (Button) findViewById(R.id.btnComprar);
        boton.setVisibility(View.INVISIBLE);
        Button boton2 = (Button) findViewById(R.id.btnVerCarro);
        boton2.setVisibility(View.INVISIBLE);
        lProductoT = findViewById(R.id.productos);
        lProductoT.setAdapter(null);


    }


}