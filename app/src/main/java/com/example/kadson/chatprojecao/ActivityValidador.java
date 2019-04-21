package com.example.kadson.chatprojecao;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kadson.chatprojecao.config.ConfiguracaoFirebase;
import com.example.kadson.chatprojecao.helper.Preferencias;
import com.example.kadson.chatprojecao.CadastroActivity;
import com.example.kadson.chatprojecao.model.Usuario;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

public class ActivityValidador extends Activity {
    private EditText codigoId;
    private Button botaoValidarId;
    private DatabaseReference referenciaFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validador);

        codigoId = findViewById(R.id.codigoId);
        botaoValidarId = findViewById(R.id.botarValidarId);

        SimpleMaskFormatter simpleMaskCodigoValidacao = new SimpleMaskFormatter("NNNN");
        MaskTextWatcher mascaraCodigovalidacao = new MaskTextWatcher(codigoId,simpleMaskCodigoValidacao);

        codigoId.addTextChangedListener(mascaraCodigovalidacao);

        botaoValidarId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pegar os dados da classe preferencias
                Preferencias preferencias = new Preferencias(ActivityValidador.this);
                HashMap<String , String> usuario = preferencias.getDadosUsuario();

                String tokenGerado = usuario.get("token");
                String nomeUsuario = usuario.get("nome");
                String matriculaUsuario = usuario.get("matricula");
                String senhaUsuario = usuario.get("senha");
                String cursoUsuario = usuario.get("curso");
                String tokenDigitado = codigoId.getText().toString();

                if(tokenDigitado.equals(tokenGerado)){
                    referenciaFirebase = ConfiguracaoFirebase.getFirebase();
                    DatabaseReference perfil = referenciaFirebase.child("Perfis");
                    referenciaFirebase.child("Perfis").child("Perfil " + matriculaUsuario).child("Nome").setValue(nomeUsuario);
                    referenciaFirebase.child("Perfis").child("Perfil " + matriculaUsuario).child("Matricula").setValue(matriculaUsuario);
                    referenciaFirebase.child("Perfis").child("Perfil " + matriculaUsuario).child("Senha").setValue(senhaUsuario);
                    referenciaFirebase.child("Perfis").child("Perfil " + matriculaUsuario).child("Curso").setValue(cursoUsuario);
                    Toast.makeText(ActivityValidador.this,"Token Validado com Sucesso!",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ActivityValidador.this,MainActivity.class));
                }else{
                    Toast.makeText(ActivityValidador.this,"Token não validado",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}