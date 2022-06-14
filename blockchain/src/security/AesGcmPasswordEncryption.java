package security;


import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import java.io.Serializable;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

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
    private static final String OBJ_ENCRYPT_ALGO = "AES/CBC/PKCS5Padding";
    private static final int TAG_LENGTH_BIT = 128; // values must be one of {128, 120, 112, 104, 96}
    private static final int IV_LENGTH_BYTE = 12;
    private static final int SALT_LENGTH_BYTE = 16;
    // private static final int AES_KEY_BIT = 256;

    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    /**
     * Encrypt a message using AES/GCM encryption using a password as the encryption
     * key
     * 
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
     * Serializing Encrypted Objects
     * 
     * @param obj      The Object to encrypt
     * @param os       The output stream of the serialized object
     * @param password The Encryption key
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws IOException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeySpecException
     */
    public static SealedObject encrypt(Serializable obj, String password, String salt, IvParameterSpec iv)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
            InvalidAlgorithmParameterException, InvalidKeySpecException, IOException {

        SecretKey K = getKeyFromPassword(password, salt);
        Cipher cipher = Cipher.getInstance(OBJ_ENCRYPT_ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, K, iv);
        SealedObject sealedObject = new SealedObject(obj, cipher);
        return sealedObject;

    }

    // make a decryption method that takes in a sealed object and password
    // and returns the decrypted object
    public static Serializable decrypt(SealedObject sealedObject, String password, String salt, IvParameterSpec iv)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
            IOException, InvalidAlgorithmParameterException, InvalidKeySpecException, ClassNotFoundException,
            BadPaddingException {

        SecretKey K = getKeyFromPassword(password, salt);
        Cipher cipher = Cipher.getInstance(OBJ_ENCRYPT_ALGO);
        cipher.init(Cipher.DECRYPT_MODE, K, iv);
        Serializable obj = (Serializable) sealedObject.getObject(cipher);
        return obj;
    }

    public static SecretKey getKeyFromPassword(String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
                .getEncoded(), "AES");
        return secret;
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
     * Decrypting a (AES/GCM) cipher given it's password as a key
     * 
     * @param cipherText The encrypted message
     * @param password   The password (key) of the encrypted message
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

    /**
     * Deserializing encrypted objects from an inputstream
     * 
     * @param is       Encrypted object inputstream to be deserialized
     * @param password The encryption key
     * @return The deserialized object (casted to Object type)
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws ClassNotFoundException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static Object decrypt(InputStream is, String password)
            throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            ClassNotFoundException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec sks = new SecretKeySpec(password.getBytes(), ENCRYPTION_ALGORITHM);
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, sks);

        CipherInputStream cis = new CipherInputStream(is, cipher);
        ObjectInputStream ois = new ObjectInputStream(cis);

        Object result = ((SealedObject) ois.readObject()).getObject(cipher);
        ois.close();
        return result;
    }

    public static byte[] decryptFile(String path, String password)
            throws IOException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        // Read the file
        byte[] fileContent = Files.readAllBytes(Paths.get(path));
        return decrypt(Base64.getEncoder().encodeToString(fileContent), password).getBytes(UTF_8);
    }

}
