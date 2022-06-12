package security;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DigitalSignatureRSA {
    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    public static byte[] encrypt(byte[] message, String password)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {

        Cipher encryptCipher = Cipher.getInstance("RSA");
        SecretKey privatekey = stringToKey(password);
        encryptCipher.init(Cipher.ENCRYPT_MODE, privatekey);

        String encodedMessage = Base64.getEncoder().encodeToString(message);
        byte[] secretMessageBytes = encodedMessage.getBytes(UTF_8);
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
        byte[] secretMessageBytes = cipherText.getBytes(UTF_8);

        return new String(decryptCipher.doFinal(secretMessageBytes), UTF_8);
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

        byte[] fileContent = Files.readAllBytes(Paths.get(path));
        return decrypt(Base64.getEncoder().encodeToString(fileContent), password).getBytes(StandardCharsets.UTF_8);
    }

    public static SecretKey stringToKey(String key) {
        byte[] decodedKey = Base64.getDecoder().decode(key);
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "RSA");
    }
}
