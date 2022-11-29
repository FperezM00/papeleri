package com.example.papeleria.ui.listar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.papeleria.R;
import com.example.papeleria.firebase.articulo;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ListarFragment extends Fragment {

    ListView Producto;
    ListView listView;

    private List<articulo> listaProductosR = new ArrayList<articulo>();
    ArrayAdapter<articulo> arrayAdapterProducto;
    articulo productosRSelected;


    ArrayList<String> arrayList=new ArrayList<>();
    ArrayAdapter<String> arrayAdapterRegal;

    FirebaseDatabase firebaseDataBase;
    DatabaseReference databaseReference;


    private ListarViewModel listarViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        listarViewModel =
                ViewModelProviders.of(this).get(ListarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_listar, container, false);
        final TextView textView = root.findViewById(R.id.text_vi);

        listarViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {


            }
        });
        databaseReference=FirebaseDatabase.getInstance().getReference("articulo");
        listView=(ListView)root.findViewById(R.id.ListaPacientes);
        arrayAdapterRegal =new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapterRegal);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                articulo aux = snapshot.getValue(articulo.class);
                if (!aux.getStatus().equals("articulo")) {
                    arrayList.add(aux.despliega());
                    arrayAdapterRegal.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });


        iniciaFirebase(root);





        return root;
    }

    private void iniciaFirebase(View root){
        FirebaseApp.initializeApp(root.getContext());
        firebaseDataBase= FirebaseDatabase.getInstance();
        databaseReference=firebaseDataBase.getReference();

    }

}
