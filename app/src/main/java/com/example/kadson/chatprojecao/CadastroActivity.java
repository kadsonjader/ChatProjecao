package com.example.kadson.chatprojecao;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kadson.chatprojecao.helper.Permissao;
import com.example.kadson.chatprojecao.helper.Preferencias;
import com.example.kadson.chatprojecao.model.Usuario;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.common.SignInButton;

import java.util.HashMap;
import java.util.Random;

public class CadastroActivity extends Activity {
    private EditText nome;
    private EditText matricula;
    private EditText senha;
    private EditText confirmarSenha;
    private EditText curso;
    private Button cadastrar;
    private EditText cdpais;
    private EditText ddd;
    private EditText telefone;
    private Usuario usuario;
  private String[] permissoesNecessarias = new String[]{
              Manifest.permission.SEND_SMS,
                Manifest.permission.INTERNET
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

      Permissao.validaPermissoes(1,this,permissoesNecessarias);

        nome = findViewById(R.id.nomeId);
        matricula = findViewById(R.id.matriculaId);
        senha = findViewById(R.id.senhaId);
        confirmarSenha = findViewById(R.id.confirmarSenhaId);
        curso = findViewById(R.id.cursoId);
        cadastrar = findViewById(R.id.botaoCadastrarId);
        cdpais = findViewById(R.id.cdPaisId);
        ddd = findViewById(R.id.dddId);
        telefone = findViewById(R.id.telefoneId);

        //definir mascaras
        SimpleMaskFormatter simpleMaskTelefone  = new SimpleMaskFormatter(" NNNNN-NNNN");
        SimpleMaskFormatter simpleMaskcdpais  = new SimpleMaskFormatter(" +NN");
        SimpleMaskFormatter simpleMaskddd  = new SimpleMaskFormatter(" NN");
        MaskTextWatcher maskTelefone  = new MaskTextWatcher(telefone ,simpleMaskTelefone);
        MaskTextWatcher maskcdpais  = new MaskTextWatcher(cdpais ,simpleMaskcdpais);
        MaskTextWatcher maskddd  = new MaskTextWatcher(ddd ,simpleMaskddd);

        telefone.addTextChangedListener(maskTelefone);
        cdpais.addTextChangedListener(maskcdpais);
        ddd.addTextChangedListener(maskddd);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario = new Usuario();
                usuario.setNome("");

                //pegar o texto das variaveis e converte para String
                String nomeUsuario = nome.getText().toString();
                String matriculaUsuario = matricula.getText().toString();
                String senhaUsuario = senha.getText().toString();
                String confirmarSenhaUsuario = confirmarSenha.getText().toString();
                String cursoUsuario = curso.getText().toString();
                String telefoneCompleto =

                        cdpais.getText().toString() +
                        ddd.getText().toString() +
                        telefone.getText().toString();

                usuario.setNome(nomeUsuario);
                usuario.setMatricula(matriculaUsuario);
                usuario.setSenha(senhaUsuario);
                usuario.setCurso(cursoUsuario);

                String telefoneSemFormatacao = telefoneCompleto.replace("+","");
                telefoneSemFormatacao = telefoneSemFormatacao.replace("-","");

                //Gerar Token
                Random random = new Random();
                int numeroRandom = random.nextInt(9999 - 1000) + 1000;

                String token =  String.valueOf(numeroRandom);
                String mensagemSms= "ChatProjeção Código de Confirmação " + token;

                //Log.i("TOKEN","T:" + token);

                Preferencias preferencias = new Preferencias(CadastroActivity.this);
                preferencias.salvarUsuarioPreferencias(nomeUsuario,matriculaUsuario,senhaUsuario,cursoUsuario,telefoneSemFormatacao,token);

                /*
                HashMap <String, String> usuario = preferencias.getDadosUsuario();

                Log.i("TOKEN", "T: " + usuario.get("token"));
                    */

                //Envio do SMS
             // telefoneSemFormatacao = "15555218135";
                boolean enviadoSms = enviaSMS("+" + telefoneSemFormatacao, mensagemSms);

                if(enviadoSms){
                    Intent intent = new Intent(CadastroActivity.this, ActivityValidador.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(CadastroActivity.this, "Erro ao enviar a mensagem, Tente novamente",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

  private boolean enviaSMS(String telefone , String mensagem){
        try {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefone,null, mensagem, null, null);

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;

        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        for (int resultado : grantResults){
            if(resultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }

    public void alertaValidacaoPermissao(){

        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para utilizar o app, é necessario aceitar as permissões");
        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
