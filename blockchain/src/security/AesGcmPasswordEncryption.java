package security;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
import javax.crypto.spec.GCMParameterSpec;

/**
 * AES-GCM inputs - 12 bytes IV, need the same IV and secret keys for encryption
 * and decryption
 * <p>
 * The output consist of iv, password's salt, encrypted content and auth tag, in
 * the following format:
 * output = byte[] {i i i s s s c c c c c c ...}
 * <p>
 * i = IV bytes
 * s = Salt bytes
 * c = Content bytes (encrypted content)
 */
public class AesGcmPasswordEncryption {
    private static final String ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128; // values must be one of {128, 120, 112, 104, 96}
    private static final int IV_LENGTH_BYTE = 12;
    private static final int SALT_LENGTH_BYTE = 16;
    // private static final int AES_KEY_BIT = 256;

    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    /**
     * @param message  Targeted message to be encrypted
     * @param password The password used to generate the secret key
     * @return A base64 encoded AES encrypted text
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] encrypt(byte[] message, String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

        byte[] iv = CryptoUtils.getRandomNonce(IV_LENGTH_BYTE);
        byte[] salt = CryptoUtils.getRandomNonce(SALT_LENGTH_BYTE);
        SecretKey secretKey = CryptoUtils.getAESKeyFromPassword(password.toCharArray(), salt);
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

        byte[] cipherText = cipher.doFinal(message);

        // prefix IV and Salt to cipher text
        byte[] cipherTextWithIVSalt = ByteBuffer.allocate(iv.length + salt.length + cipherText.length).put(iv).put(salt)
                .put(cipherText).array();

        // return Base64.getEncoder().encodeToString(cipherTextWithIVSalt);
        return cipherTextWithIVSalt;
    }

    /**
     * 
     * @param fromFile Target file to encrypt
     * @param toFile   The destination of the encrypted copy
     * @param password Encryption password
     * @throws IOException
     * @throws URISyntaxException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws NoSuchPaddingException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
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

    /**
     * 
     * @param cipherText
     * @param password
     * @return Hopefully the original message, fingers crossed
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String decrypt(String cipherText, String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] decode = Base64.getDecoder().decode(cipherText.getBytes(UTF_8));

        // extracting the salt & iv from the ciphered text
        ByteBuffer byteBuffer = ByteBuffer.wrap(decode);
        byte[] iv = new byte[IV_LENGTH_BYTE];
        byteBuffer.get(iv);

        byte[] salt = new byte[SALT_LENGTH_BYTE];
        byteBuffer.get(salt);

        byte[] cipherMessage = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherMessage);

        // Get the same AES key from the same password & salt
        SecretKey secretKey = CryptoUtils.getAESKeyFromPassword(password.toCharArray(), salt);
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

        return new String(cipher.doFinal(cipherMessage), UTF_8);
    }

    public static byte[] decryptFile(String path, String password) throws IOException {
        // Read the file
        byte[] fileContent = Files.readAllBytes(Paths.get(path));
        return decrypt(fileContent, password);
    }
}
