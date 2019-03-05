package com.example.kadson.chatprojecao;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.kadson.chatprojecao.config.ConfiguracaoFirebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends Activity {
    private EditText matricula;
    private EditText Senha;
    private Button logar;
    private Button cadastroAtivity;
    private DatabaseReference referenciaFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(MainActivity.this);

        referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("pontos").setValue("800");

         cadastroAtivity = findViewById(R.id.cadastrarId);

         cadastroAtivity.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                   startActivity(new Intent(MainActivity.this,CadastroActivity.class));
             }
         });
    }

}
