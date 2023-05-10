package com.opencode.minikeyvault.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * class: Encrypt. <br/>
 *
 * @author Henry Navarro <br/><br/>
 * <u>Cambios</u>:<br/>
 * <ul>
 * <li> Creación del proyecto.</li>
 * </ul>
 * @version 1.0
 */
public class Encrypt {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Authenticate {

        @JsonProperty("auth.username")
        private String username;

        @JsonProperty("auth.password")
        private String password;

    }

    private static final String ALGORITHM = "AES";

    /**
     * Método que devuelve una nueva secuencia aleatoria de 16 bytes, la cual debería ser
     * usada para el proceso de encriptación y desencriptación.
     *
     * @return arreglo de bytes.
     */
    public static byte[] getSecureRandom() throws NoSuchAlgorithmException {

        byte[] bytes = new byte[16];

        SecureRandom.getInstanceStrong().nextBytes(bytes);

        return bytes;
    }

    /**
     * Método que devuelve una cadena codificada en base64 con la representación JSON del modelo
     * Encrypt.Authenticate, el cual contiene las credenciales (ya encriptadas) que se recibieron.
     *
     * @param secureRandom llave para la encriptación.
     * @param username usuario para la aplicación.
     * @param password clave para la aplicación.
     * @return cadena de texto.
     * @throws Exception excepción en caso de error.
     */
    public static String encryptCredentials(byte[] secureRandom, String username, String password) throws Exception {

        String encodedCredentials;

        try {
            String jsonAuth = Utils.getAsJsonString(
                    Authenticate.builder()
                            .username(doEncrypt(secureRandom, username))
                            .password(doEncrypt(secureRandom, password))
                            .build());

            encodedCredentials = Base64.getEncoder().encodeToString(
                    jsonAuth.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException
                 | IllegalBlockSizeException | BadPaddingException e) {
            throw new Exception("Ocurrió un error al encriptar las credenciales: " + e.getMessage());
        } catch (JsonProcessingException e) {
            throw new Exception("Ocurrió un error al convertir el modelo con las credenciales " +
                    "a su representación JSON: " + e.getMessage());
        }

        return encodedCredentials;
    }

    /**
     * Método que devuelve una instancia del modelo Encrypt.Authenticate, el cual contendrá las credenciales
     * de autenticación que se recuperaron de la cadena codificada en base64 que se recibió.
     *
     * @param secureRandom llave para la desencriptación (debe ser la misma que se usó para la encriptación).
     * @param base64Auth cadena en base64 con las credenciales de autenticación encriptadas.
     * @return instancia del objeto con las credenciales de autenticación.
     * @throws Exception excepción en caso de error.
     */
    public static Authenticate decryptCredentials(byte[] secureRandom, String base64Auth) throws Exception {

        Authenticate authenticate;

        try {
            byte[] array = Base64.getDecoder().decode(base64Auth);
            String jsonAuth = new String(array, StandardCharsets.UTF_8);

            authenticate = Utils.mapJsonStringToObject(jsonAuth, Authenticate.class);

            authenticate.setUsername(doDecrypt(secureRandom, authenticate.getUsername()));
            authenticate.setPassword(doDecrypt(secureRandom, authenticate.getPassword()));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException
                 | IllegalBlockSizeException | BadPaddingException e) {
            throw new Exception("Ocurrió un error al encriptar las credenciales: " + e.getMessage());
        } catch (JsonProcessingException e) {
            throw new Exception("Ocurrió un error al convertir el modelo con las credenciales " +
                    "a su representación JSON: " + e.getMessage());
        }

        return authenticate;
    }

    /**
     * Método que lleva a cabo el proceso de encriptación del valor recibido.
     *
     * @param secureRandom llave para la encriptación.
     * @param value valor a encriptar.
     * @return valor encriptado.
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    private static String doEncrypt(byte[] secureRandom, String value) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secureRandom, ALGORITHM));

        byte[] array = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(array);
    }

    /**
     * Método que lleva a cabo el proceso de desencriptación del valor recibido.
     *
     * @param secureRandom llave para la desencriptación.
     * @param value valor a desencriptar.
     * @return valor desencriptado.
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    private static String doDecrypt(byte[] secureRandom, String value) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secureRandom, ALGORITHM));

        byte[] array = cipher.doFinal(Base64.getDecoder().decode(value));

        return new String(array, StandardCharsets.UTF_8);
    }

}
