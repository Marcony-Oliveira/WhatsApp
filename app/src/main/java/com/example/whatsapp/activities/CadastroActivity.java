package com.example.whatsapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.example.whatsapp.config.ConfiguracaoFirebase;
import com.example.whatsapp.helper.Base64Custom;
import com.example.whatsapp.helper.UsuarioFirebase;
import com.example.whatsapp.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail,campoSenha;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoNome = findViewById(R.id.editPerfilNome);
        campoEmail = findViewById(R.id.editLoginEmail);
        campoSenha = findViewById(R.id.editLoginSenha);

    }

    //------------------------------------------------------------------------------------//
    //BLOCO VALIDAR E CADASTRAR USUÁRIOS

    public void validarCadastroUsuario(View view){

        //Recuperar textos para validar os campos
        String textoNome = campoNome.getText().toString();
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();

        if(!textoNome.isEmpty()){//verifica nome
            if(!textoEmail.isEmpty()){//verifica Email
                if(!textoSenha.isEmpty()){//verifica Senha

                    Usuario usuario = new Usuario();
                    usuario.setNome(textoNome);
                    usuario.setEmail(textoEmail);
                    usuario.setSenha(textoSenha);

                    cadastrarUsuario(usuario);

                }else{
                    Toast.makeText(CadastroActivity.this,"Campo 'Senha' não foi preenchido!",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(CadastroActivity.this,"Campo 'Email' não foi preenchido!",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(CadastroActivity.this,"Campo 'Nome' não foi preenchido!",Toast.LENGTH_LONG).show();
        }
    }

    public void cadastrarUsuario(final Usuario usuario){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){ //verifica se foi possível cadastrar usuario

                    try{

                        String identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmail()); //Codificando para Base64 o id
                        usuario.setId(identificadorUsuario);
                        usuario.salvar();

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroActivity.this,"Usuário cadastrado com sucesso!",Toast.LENGTH_LONG).show();
                    UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());

                    finish();//para finalizar a activity
                }else{
                    String excecao = "";
                    try{
                        throw task.getException();
                    }catch(FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte.";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao="Por favor, digite um e-mail válido.";
                    }catch (FirebaseAuthUserCollisionException e){
                        excecao = "Essa conta já está cadastrada!";
                    }catch(Exception e){
                        excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this,excecao,Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    //BLOCO VALIDAR E CADASTRAR USUARIOS
    //----------------------------------------------------------------------------------//

}
