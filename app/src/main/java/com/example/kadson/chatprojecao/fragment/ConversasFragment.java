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
import com.example.kadson.chatprojecao.adapter.ConversasAdapter;
import com.example.kadson.chatprojecao.config.ConfiguracaoFirebase;
import com.example.kadson.chatprojecao.helper.Preferencias;
import com.example.kadson.chatprojecao.model.Conversa;
import com.example.kadson.chatprojecao.model.Mensagem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment{

    private ListView listView;
    private ArrayAdapter<Conversa> adapter;
    private ArrayList<Conversa> conversas;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerConversas;
    private Conversa conversa;
    private String conversaString;

    public ConversasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        listView = view.findViewById(R.id.lv_conversas);

        conversas = new ArrayList<>();
        adapter = new ConversasAdapter(getActivity(),conversas);

        listView.setAdapter(adapter);

        Preferencias preferencias = new Preferencias(getActivity());
        HashMap<String, String> usuario = preferencias.getDadosUsuario2();
        String nomeUsuarioLogado = usuario.get("nomeSessao");

        firebase = ConfiguracaoFirebase.getFirebase()
                .child("Conversas")
                .child(nomeUsuarioLogado);


        valueEventListenerConversas = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Limpar mensagens
                conversas.clear();

                // Recupera mensagens
                for ( DataSnapshot dados: dataSnapshot.getChildren() ){
                    conversa = dados.getValue(Conversa.class);
                    conversas.add(conversa);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Conversa conversa = conversas.get(i);
                Intent intent = new Intent(getActivity(), ConversaActivity.class);

                intent.putExtra("nome",conversa.getUsuario());

                startActivity(intent);
            }
        });

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListenerConversas);
    }


    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerConversas);
    }


}
