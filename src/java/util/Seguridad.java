package util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Base64;

public class Seguridad {

    private static final String ALGORITMO = "AES";
    private static final String LLAVE = "1Hbfh667adfDEJ78"; // Debe ser de 16 caracteres → AES-128

    /**
     * Este método cifra una cadena de texto utilizando el algoritmo AES y una
     * llave específica.
     *
     * @param valor La cadena de texto a cifrar.
     * @return La cadena cifrada y codificada en Base64.
     * @throws Exception Si ocurre un error durante el proceso de cifrado.
     */
    public static String cifrar(String valor) throws Exception {
        Key llave = generarLlave();
        Cipher cifrador = Cipher.getInstance(ALGORITMO);
        cifrador.init(Cipher.ENCRYPT_MODE, llave);
        byte[] valorCifrado = cifrador.doFinal(valor.getBytes("utf-8"));
        return Base64.getEncoder().encodeToString(valorCifrado);
    }

    /**
     * Este método descifra una cadena de texto cifrada (y codificada en Base64)
     * utilizando el algoritmo AES y una llave específica.
     *
     * @param valor La cadena cifrada y codificada en Base64 a descifrar.
     * @return La cadena de texto descifrada.
     * @throws Exception Si ocurre un error durante el proceso de descifrado.
     */
    public static String descifrar(String valor) throws Exception {
        Key llave = generarLlave();
        Cipher cifrador = Cipher.getInstance(ALGORITMO);
        cifrador.init(Cipher.DECRYPT_MODE, llave);
        byte[] valorDescifrado64 = Base64.getDecoder().decode(valor);
        byte[] valorDescifrado = cifrador.doFinal(valorDescifrado64);
        return new String(valorDescifrado, "utf-8");
    }

    /**
     * Este método aplica un hash SHA-256 a una cadena de texto y luego codifica
     * el hash en Base64.
     *
     * @param valor La cadena de texto a la que se le aplicará el hash.
     * @return El hash de la cadena codificado en Base64.
     * @throws Exception Si ocurre un error durante el proceso de hashing.
     */
    public static String hash(String valor) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(valor.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Este método genera una llave para el cifrado/descifrado utilizando el
     * algoritmo AES y una llave específica.
     *
     * @return La llave generada.
     */
    private static Key generarLlave() {
        return new SecretKeySpec(LLAVE.getBytes(), ALGORITMO);
    }
}
