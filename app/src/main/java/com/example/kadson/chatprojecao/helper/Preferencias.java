package com.example.kadson.chatprojecao.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Spinner;

import java.util.HashMap;

/**
 * Created by kadson on 22/02/19.
 */

public class Preferencias {
    private Context contexo;
    private SharedPreferences preferences;
    private final String NOME_ARQUIVO = "whatsappPrefencias";
    private final int MODE = 0;
    private SharedPreferences.Editor editor;
    private final String CHAVE_NOME = "nome";
    private final String CHAVE_NOME_SESSAO = "nomeSessao";
    private final String CHAVE_MATRICULA = "matricula";
    private final String CHAVE_MATRICULA_SESSAO = "matriculaSessao";
    private final String CHAVE_SENHA = "senha";
    private final String CHAVE_CURSO = "curso";
    private final String CHAVE_TELEFONE = "telefone";
    private final String CHAVE_TOKEN = "token";

    public Preferencias(Context contexoParametro) {
        contexo = contexoParametro;
        preferences = contexo.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();
    }
    public void salvarUsuarioPreferencias(String nome, String matricula, String senha, String curso, String telefone, String token){

        editor.putString(CHAVE_NOME, nome);
        editor.putString(CHAVE_MATRICULA, matricula);
        editor.putString(CHAVE_SENHA, senha);
        editor.putString(CHAVE_CURSO, String.valueOf(curso));
        editor.putString(CHAVE_TELEFONE, telefone);
        editor.putString(CHAVE_TOKEN, token);
        editor.commit();

    }

    public HashMap<String , String> getDadosUsuario(){
        HashMap<String, String> dadosUsuario = new HashMap<>();
        dadosUsuario.put(CHAVE_NOME, preferences.getString(CHAVE_NOME,null));
        dadosUsuario.put(CHAVE_MATRICULA, preferences.getString(CHAVE_MATRICULA,null));
        dadosUsuario.put(CHAVE_SENHA, preferences.getString(CHAVE_SENHA,null));
        dadosUsuario.put(CHAVE_CURSO, preferences.getString(CHAVE_CURSO,null));
        dadosUsuario.put(CHAVE_TELEFONE, preferences.getString(CHAVE_TELEFONE,null));
        dadosUsuario.put(CHAVE_TOKEN, preferences.getString(CHAVE_TOKEN,null));

        return dadosUsuario;

    }

    public void salvarNomeUsuario(String nome, String senha){
        editor.putString(CHAVE_NOME_SESSAO, nome);
        editor.putString(CHAVE_SENHA, senha);
        editor.commit();
    }

    public void salvarMatriculaUsuario(String matricula){
        editor.putString(CHAVE_MATRICULA_SESSAO,matricula);
        editor.commit();
    }

    public HashMap<String , String> getDadosUsuario2(){
        HashMap<String, String> dadosUsuario2 = new HashMap<>();
        dadosUsuario2.put(CHAVE_NOME_SESSAO, preferences.getString(CHAVE_NOME_SESSAO,null));
        dadosUsuario2.put(CHAVE_MATRICULA_SESSAO, preferences.getString(CHAVE_MATRICULA_SESSAO,null));
        dadosUsuario2.put(CHAVE_SENHA, preferences.getString(CHAVE_SENHA,null));

        return dadosUsuario2;

    }

}
