package com.example.whatsapp.helper;

import android.util.Base64;

public class Base64Custom {

    public static String codificarBase64(String texto){ //Para codificar a chave que vai acessar os dados no BD , NO CASO DESSE APP , O EMAIL
        return Base64.encodeToString(texto.getBytes(),Base64.DEFAULT)
                .replaceAll("(\\n|\\r)",""); //replaceAll troca onde codificar os caracteres escolhidos por espaços em branco

    }

    public static String decodificarBase64(String textoCodificado){
        return new String ( Base64.decode(textoCodificado,Base64.DEFAULT));

    }

}
