package com.example.kadson.chatprojecao;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.BoringLayout;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kadson.chatprojecao.adapter.MensagemAdapter;
import com.example.kadson.chatprojecao.config.ConfiguracaoFirebase;
import com.example.kadson.chatprojecao.helper.Preferencias;
import com.example.kadson.chatprojecao.model.Conversa;
import com.example.kadson.chatprojecao.model.Mensagem;
import com.example.kadson.chatprojecao.model.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.UUID;

public class ConversaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText edMensagem;
    private ImageButton btMensagem;
    private ImageButton btDocumento;
    private ImageButton btUpload;
    private String nomeUsuarioDestinatario;
    private String nomeUsuarioRementente;
    private String filename;
    private File file;
    private DatabaseReference firebase;
    private ListView listView;
    private ArrayList<Mensagem> mensagens;
    private ArrayAdapter<Mensagem> adapter;
    private ValueEventListener valueEventListenerMensagem;
    private Uri mSelectUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        toolbar = findViewById(R.id.tb_conversa);
        edMensagem = findViewById(R.id.ed_mensagem);
        btMensagem = findViewById(R.id.bt_enviar);
        btDocumento = findViewById(R.id.bt_documento);
        listView = findViewById(R.id.lv_conversas);
        btUpload = findViewById(R.id.bt_upload);

        Preferencias preferencias = new Preferencias(ConversaActivity.this);
        HashMap<String, String> usuario = preferencias.getDadosUsuario2();
        nomeUsuarioRementente = usuario.get("nomeSessao");
        Bundle extra = getIntent().getExtras();

        if(extra != null){
            nomeUsuarioDestinatario = extra.getString("nome");
        }

        toolbar.setTitle(nomeUsuarioDestinatario);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        mensagens = new ArrayList<>();
        adapter = new MensagemAdapter(ConversaActivity.this,mensagens);
        /*adapter = new ArrayAdapter(
           ConversaActivity.this,
           android.R.layout.simple_list_item_1,
                mensagens
        );*/
        listView.setAdapter(adapter);
        listView.setSelection(listView.getCount() - 1);

        firebase = ConfiguracaoFirebase.getFirebase()
                .child("Mensagens")
                .child(nomeUsuarioRementente)
                .child(nomeUsuarioDestinatario);

        valueEventListenerMensagem = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Limpar mensagens
                mensagens.clear();

                // Recupera mensagens
                for ( DataSnapshot dados: dataSnapshot.getChildren() ){
                    Mensagem mensagem = dados.getValue(Mensagem.class);
                    mensagens.add(mensagem);
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        firebase.addValueEventListener(valueEventListenerMensagem);

        btMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textoMensagem = edMensagem.getText().toString();

                if(textoMensagem.isEmpty()){
                    Toast.makeText(ConversaActivity.this, "Digite a mensagem para enviar", Toast.LENGTH_SHORT).show();
                }else{
                    Mensagem mensagem = new Mensagem();
                    mensagem.setUsuario(nomeUsuarioRementente);
                    mensagem.setMensagem(textoMensagem);
                    Boolean retornoMensagemRemetente
                            = salvarMensagem(nomeUsuarioRementente,nomeUsuarioDestinatario,mensagem);

                    if(!retornoMensagemRemetente){
                        Toast.makeText(ConversaActivity.this, "Problema em enviar a mensagem",
                                Toast.LENGTH_SHORT).show();

                    }else{
                        Boolean retornoMensagemDestinatario =
                                salvarMensagem(nomeUsuarioDestinatario,nomeUsuarioRementente,mensagem);

                        if(!retornoMensagemDestinatario){
                            Toast.makeText(ConversaActivity.this, "Problema em enviar a mensagem",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    Conversa conversa = new Conversa();
                    conversa.setUsuario(nomeUsuarioDestinatario);
                    conversa.setMensagem(textoMensagem);
                    boolean retornoConversaRemetente = salvarConversa(nomeUsuarioRementente,nomeUsuarioDestinatario,conversa);
                    if(!retornoConversaRemetente){
                        Toast.makeText(ConversaActivity.this, "Problema em enviar a mensagem",
                                Toast.LENGTH_SHORT).show();

                    }else{
                        conversa = new Conversa();
                        conversa.setUsuario(nomeUsuarioRementente);
                        conversa.setMensagem(textoMensagem);
                        boolean retornoConversaDestinatario = salvarConversa(nomeUsuarioDestinatario,nomeUsuarioRementente,conversa);
                        if(!retornoConversaDestinatario){
                            Toast.makeText(ConversaActivity.this, "Problema em enviar a mensagem",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }

                    edMensagem.setText("");
                }
                System.out.println(nomeUsuarioDestinatario);
                System.out.println(nomeUsuarioRementente);
                System.out.println(textoMensagem);
            }
        });

        btDocumento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent,"Selecione o Documento do tipo PDF"),86);
                Toast.makeText(ConversaActivity.this, "Seleciona documento tipo PDF para Upload ", Toast.LENGTH_SHORT).show();
            }
        });

        btUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSelectUrl != null){
                Toast.makeText(ConversaActivity.this, "Aguarde o link do documento ser gerado", Toast.LENGTH_SHORT).show();
                final StorageReference ref = FirebaseStorage.getInstance().getReference("docs/" + filename);
                ref.putFile(mSelectUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                edMensagem.setText(uri.toString());
                            }
                        });
                    }
                });
              }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 86 && resultCode == RESULT_OK && data != null && data.getData() != null){
            mSelectUrl = data.getData();
        }
    }

    private boolean salvarMensagem(String nomeUsuarioRementente, String destinatario, Mensagem mensagem){
        try {
            firebase = ConfiguracaoFirebase.getFirebase().child("Mensagens");

            firebase.child(nomeUsuarioRementente)
                    .child(destinatario)
                    .push()
                    .setValue(mensagem);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private boolean salvarConversa(String nomeUsuarioRementente, String destinatario, Conversa conversa){
        try{
            firebase = ConfiguracaoFirebase.getFirebase().child("Conversas");
            firebase.child(nomeUsuarioRementente)
            .child(destinatario)
            .setValue(conversa);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerMensagem);
    }
}
