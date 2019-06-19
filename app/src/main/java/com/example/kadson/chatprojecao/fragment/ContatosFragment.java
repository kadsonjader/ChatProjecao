package com.example.kadson.chatprojecao.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.kadson.chatprojecao.ConversaActivity;
import com.example.kadson.chatprojecao.R;
import com.example.kadson.chatprojecao.model.Conversa;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {

    private ListView listView;
    DatabaseReference perfil = FirebaseDatabase.getInstance().getReference("Perfis");

    public ContatosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);
        listView =  view.findViewById(R.id.lv_contatos);

        perfil.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> coordenadores = new ArrayList<>();
                String array[];

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                        boolean nomeUsuariobolean = snapshot.child("Direção").exists();
                        if(nomeUsuariobolean == true) {
                            String nomeDirecao = snapshot.child("Nome").getValue().toString();
                            coordenadores.add(nomeDirecao);
                        }else{

                        }

                }
                ArrayAdapter adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, coordenadores);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedFromList = (String) listView.getItemAtPosition(i);
                Intent intent = new Intent(getActivity(), ConversaActivity.class);

                intent.putExtra("nome",selectedFromList);

                startActivity(intent);
            }
        });


        return view;
    }
}