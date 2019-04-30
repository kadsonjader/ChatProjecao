package com.example.kadson.chatprojecao.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kadson.chatprojecao.R;
import com.example.kadson.chatprojecao.helper.Preferencias;
import com.example.kadson.chatprojecao.model.Mensagem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MensagemAdapter extends ArrayAdapter<Mensagem> {

    private Context context;
    private ArrayList<Mensagem> mensagens;

    public MensagemAdapter(@NonNull Context c, @NonNull ArrayList<Mensagem> objects) {
        super(c, 0, objects);
        this.context = c;
        this.mensagens = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if(mensagens != null){

            Preferencias preferencias =  new Preferencias(context);
            HashMap<String, String> usuario = preferencias.getDadosUsuario2();
            String nomeUsuarioRemetente  = usuario.get("nomeSessao");

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            Mensagem mensagem = mensagens.get(position);

            if(nomeUsuarioRemetente.equals(mensagem.getUsuario())){
                view = inflater.inflate(R.layout.item_mensagem_direita,parent,false);
            }else{
                view = inflater.inflate(R.layout.item_mensagem_esquerda,parent,false);
            }

            TextView textoMensagem = view.findViewById(R.id.tv_mensagem);
            textoMensagem.setText(mensagem.getMensagem());

        }

        return view;
    }
}
