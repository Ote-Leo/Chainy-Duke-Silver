package security;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DigitalSignatureRSA {

    public static byte[] encrypt(byte[] message, String password)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {

        Cipher encryptCipher = Cipher.getInstance("RSA");
        SecretKey privatekey = stringToKey(password);
        encryptCipher.init(Cipher.ENCRYPT_MODE, privatekey);

        String encodedMessage = Base64.getEncoder().encodeToString(message);
        byte[] secretMessageBytes = encodedMessage.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);
        // String encodedMessage =
        // Base64.getEncoder().encodeToString(encryptedMessageBytes);
        return encryptedMessageBytes;
    }

    public static String decrypt(String cipherText, String publicKey) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        SecretKey publicKey1 = stringToKey(publicKey);
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, publicKey1);
        byte[] secretMessageBytes = cipherText.getBytes(StandardCharsets.UTF_8);
        byte[] decryptedMessageBytes = decryptCipher.doFinal(secretMessageBytes);
        String decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);
        return decryptedMessage;
    }

    public static void encryptFile(String fromFile, String toFile, String password)
            throws IOException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        // Read normal text file
        byte[] fileContent = Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(fromFile).toURI()));

        // Encrypt using the password
        byte[] encryptedContent = encrypt(fileContent, password);

        // Save the file
        Files.write(Paths.get(toFile), encryptedContent);
    }

    public static byte[] decryptFile(String path, String password) throws IOException, InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        // Read the file
        byte[] fileContent = Files.readAllBytes(Paths.get(path));
        String filecontent = Base64.getEncoder().encodeToString(fileContent);
        byte[] decryptfileContent = decrypt(filecontent, password).getBytes(StandardCharsets.UTF_8);

        return decryptfileContent;
    }

    public static SecretKey stringToKey(String key) {
        byte[] decodedKey = Base64.getDecoder().decode(key);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "RSA");
    }

}
