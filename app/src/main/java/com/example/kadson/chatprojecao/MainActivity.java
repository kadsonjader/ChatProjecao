package com.example.kadson.chatprojecao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.kadson.chatprojecao.helper.Preferencias;
import com.example.kadson.chatprojecao.config.ConfiguracaoFirebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class MainActivity extends Activity {
    private EditText matricula;
    private EditText senha;
    private Button logar;
    private Button cadastroAtivity;
    private DatabaseReference referenciaFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(MainActivity.this);
        referenciaFirebase = ConfiguracaoFirebase.getFirebase();

         cadastroAtivity = findViewById(R.id.cadastrarId);
         matricula = findViewById(R.id.matriculaMainId);
         senha = findViewById(R.id.senhaMainId);
         logar = findViewById(R.id.logarId);

         logar.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                String matriculaLogar = matricula.getText().toString();
                String senhaLogar = senha.getText().toString();

                Query queryMatricula = referenciaFirebase.child("Perfis").orderByChild("Matricula").equalTo(matriculaLogar);
                Query querySenha =  referenciaFirebase.child("Perfis").orderByChild("Senha").equalTo(senhaLogar);
if(queryMatricula.equals(matriculaLogar) && querySenha.equals(senhaLogar)){
                    Toast.makeText(MainActivity.this,"Logado com Sucesso!",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this,"Matricula ou senha incorretos!",Toast.LENGTH_LONG).show();
                }
                 System.out.println(matriculaLogar);
                 System.out.println(senhaLogar);
                 System.out.println(queryMatricula);
                 System.out.println(querySenha);
             }
         });


         cadastroAtivity.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                   startActivity(new Intent(MainActivity.this,CadastroActivity.class));
             }
         });
    }

}
