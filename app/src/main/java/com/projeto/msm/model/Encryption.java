package com.projeto.msm.model;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class Encryption {

    private static String saltStr = "MSM2021";
    private static int key = 27;

    /**
     * Encripta password
     * Source: https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
     * @param aToEncrypt
     * @return
     */
    public static String encrypt(String aToEncrypt){
        int iterations = 1000;
        char[] chars = aToEncrypt.toCharArray();
        byte[] salt = saltStr.getBytes();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = null;
        try {
            skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return iterations + ":" + toHex(salt) + ":" + toHex(hash);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * Altera string para valores hexadecimais.
     * Source: https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
     * @param array
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }

    /**
     * Altera de hexadecimal para string.
     * Source: https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
     * @param hex
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException
    {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    /**
     * Valida uma string com outra string encriptada.
     * Source: https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
     * @param originalPassword
     * @param storedPassword
     * @return
     */
    public static boolean validatePassword(String originalPassword, String storedPassword)
    {
        try {
            String[] parts = storedPassword.split(":");
            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = fromHex(parts[1]);
            byte[] hash = fromHex(parts[2]);

            PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] testHash = new byte[0];

            testHash = skf.generateSecret(spec).getEncoded();
            int diff = hash.length ^ testHash.length;
            for(int i = 0; i < hash.length && i < testHash.length; i++)
            {
                diff |= hash[i] ^ testHash[i];
            }
            return diff == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Utilizado para encriptar os valores do ficheiro de propriedades da BD.
     * @param aToEncrypt
     * @return
     */
    public static String encryptDBProperties(String aToEncrypt){
        char[] stringToChar = aToEncrypt.toCharArray();
        for(int i=0; i<stringToChar.length; i++){
            stringToChar[i] += key;
        }
        return new String(stringToChar);
    }

    /**
     * Utilizado para desincriptar os valores do ficheiro de propriedades da BD.
     * @param aToDecrypt
     * @return
     */
    public static String decryptDBProperties(String aToDecrypt){
        char[] stringToChar = aToDecrypt.toCharArray();
        for(int i=0; i<stringToChar.length; i++){
            stringToChar[i] -= key;
        }
        return new String(stringToChar);
    }
}
