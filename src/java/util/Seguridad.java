
package util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Base64;

public class Seguridad {
    private static final String ALGORITMO = "AES";
    private static final String LLAVE = "1Hbfh667adfDEJ78"; // Debe ser de 16 caracteres

    public static String cifrar(String valor) throws Exception {
        Key llave = generarLlave();
        Cipher cifrador = Cipher.getInstance(ALGORITMO);
        cifrador.init(Cipher.ENCRYPT_MODE, llave);
        byte[] valorCifrado = cifrador.doFinal(valor.getBytes("utf-8"));
        return Base64.getEncoder().encodeToString(valorCifrado);
    }

    public static String descifrar(String valor) throws Exception {
        Key llave = generarLlave();
        Cipher cifrador = Cipher.getInstance(ALGORITMO);
        cifrador.init(Cipher.DECRYPT_MODE, llave);
        byte[] valorDescifrado64 = Base64.getDecoder().decode(valor);
        byte[] valorDescifrado = cifrador.doFinal(valorDescifrado64);
        return new String(valorDescifrado, "utf-8");
    }

    public static String hash(String valor) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(valor.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(hash);
    }

    private static Key generarLlave() {
        return new SecretKeySpec(LLAVE.getBytes(), ALGORITMO);
    }
}
