package com.example.kadson.chatprojecao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.kadson.chatprojecao.helper.Preferencias;
import com.example.kadson.chatprojecao.config.ConfiguracaoFirebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {
    private EditText matricula;
    private EditText senha;
    private Button logar;
    private Button cadastroAtivity;
    private FirebaseAuth autenticacao;
    private DatabaseReference referenciaFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(MainActivity.this);
        referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        final DatabaseReference perfil = FirebaseDatabase.getInstance().getReference("Perfis");
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, MensagensActivity.class));
        } else {

            cadastroAtivity = findViewById(R.id.cadastrarId);
            matricula = findViewById(R.id.matriculaMainId);
            senha = findViewById(R.id.senhaMainId);
            logar = findViewById(R.id.logarId);

            logar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Preferencias preferencias = new Preferencias(MainActivity.this);
                    final String matriculaLogar = matricula.getText().toString();
                    final String senhaLogar = senha.getText().toString();
                    preferencias.salvarMatriculaUsuario(matriculaLogar);
                    DatabaseReference camposFilhos = perfil.child("Perfil " + matriculaLogar);

                    camposFilhos.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String senhaUsuario = dataSnapshot.child("Senha").getValue().toString();
                            String nomeUsuario = dataSnapshot.child("Nome").getValue().toString();
                            if (senhaLogar.equals(senhaUsuario)) {
                                Toast.makeText(MainActivity.this, "Credenciais Corretas", Toast.LENGTH_SHORT).show();
                                Toast.makeText(MainActivity.this, "Bem Vindo " + nomeUsuario, Toast.LENGTH_SHORT).show();
                                Preferencias preferencias = new Preferencias(MainActivity.this);
                                preferencias.salvarNomeUsuario(nomeUsuario,senhaUsuario);
                                startActivity(new Intent(MainActivity.this, MensagensActivity.class));
                            } else {
                                Toast.makeText(MainActivity.this, "Credenciais Incorretas", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w(TAG, "onCancelled", databaseError.toException());
                        }
                    });


                }
            });


            cadastroAtivity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, CadastroActivity.class));
                }
            });
        }

    }

}