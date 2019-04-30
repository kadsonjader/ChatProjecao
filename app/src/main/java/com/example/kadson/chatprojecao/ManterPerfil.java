package com.example.kadson.chatprojecao;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kadson.chatprojecao.config.ConfiguracaoFirebase;
import com.example.kadson.chatprojecao.helper.Preferencias;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ManterPerfil extends AppCompatActivity {

    private DatabaseReference referenciaFirebase;
    private TextView nomeManter;
    private EditText matriculaManter;
    private EditText senhaManter;
    private Button botaoManter;
    private Spinner cursoManter;
    private String nomeString;
    private String matriculaString;
    private String senhaString;
    private String cursoUsuarioString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manter_perfil);

        referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        final DatabaseReference cursofirebase = FirebaseDatabase.getInstance().getReference("Curso");

        nomeManter = findViewById(R.id.nomeManterId);
        matriculaManter = findViewById(R.id.matriculaMainManterId);
        senhaManter = findViewById(R.id.senhaMainManterId);
        botaoManter = findViewById(R.id.botaoCadastrarManterId);


        cursofirebase.child("nomeCursos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> cursosLista = new ArrayList<String>();
                cursoManter = findViewById(R.id.cursoManterId);

                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String cursoNome = areaSnapshot.getValue(String.class);
                    cursosLista.add(cursoNome);
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                        (ManterPerfil.this, android.R.layout.simple_spinner_item,cursosLista);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                cursoManter.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });

        Preferencias preferencias = new Preferencias(ManterPerfil.this);
        HashMap<String, String> usuario = preferencias.getDadosUsuario2();
        nomeString = usuario.get("nomeSessao");
        matriculaString = usuario.get("matriculaSessao");
        senhaString = usuario.get("senha");

        nomeManter.setText(nomeString);
        matriculaManter.setText(matriculaString);
        senhaManter.setText(senhaString);

        botaoManter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Preferencias preferencias = new Preferencias(ManterPerfil.this);
                HashMap<String, String> usuario = preferencias.getDadosUsuario2();
                nomeString = usuario.get("nomeSessao");
                matriculaString = usuario.get("matriculaSessao");
                senhaString = usuario.get("senha");

                final String nomeString2 = nomeManter.getText().toString();
                String matriculaString2 = matriculaManter.getText().toString();
                String senhaString2 = senhaManter.getText().toString();
                cursoUsuarioString = cursoManter.getSelectedItem().toString();

                if(nomeString2.equals(nomeString) && matriculaString2.equals(matriculaString) && senhaString2.equals(senhaString2)){
                    Toast.makeText(ManterPerfil.this, "Seus Dados continuam os mesmos!", Toast.LENGTH_LONG).show();
                }else {
                    referenciaFirebase = ConfiguracaoFirebase.getFirebase();
                    DatabaseReference perfil = referenciaFirebase.child("Perfis");
                    referenciaFirebase.child("Perfis").child("Perfil " + matriculaString).child("Matricula").setValue(matriculaString2);
                    referenciaFirebase.child("Perfis").child("Perfil " + matriculaString).child("Senha").setValue(senhaString2);
                    referenciaFirebase.child("Perfis").child("Perfil " + matriculaString).child("Curso").setValue(cursoUsuarioString);
                    Toast.makeText(ManterPerfil.this, "Dados atualizados com sucesso!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ManterPerfil.this, ConversaActivity.class));

                    //referenciaFirebase.child("Mensagens").child(nomeString).setValue(nomeString2);
                    //referenciaFirebase.child("Conversas").child(nomeString).setValue(nomeString2);

                    //DatabaseReference atualizacaoMensagem  = FirebaseDatabase.getInstance().getReference("Mensagens").child(nomeString);
                    //DatabaseReference atualizacaoConversa = FirebaseDatabase.getInstance().getReference("Conversas").child(nomeString);

                  /*  atualizacaoMensagem.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                referenciaFirebase.child("Mensagens").child(nomeString).setValue(nomeString2);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    atualizacaoConversa.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                referenciaFirebase.child("Conversas").child(nomeString).setValue(nomeString2);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/

                }
            }
        });
    }

}
