package security;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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
 * the output consist of iv, encrypted content, and auth tag in the following
 * format:
 * output = byte[] {i i i c c c c c c ...}
 * <p>
 * i = IV bytes
 * c = content bytes (encrypted content, auth tag)
 */
public class AesGcmEncryption {
    private static final String ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;
    // private static final int AES_KEY_BIT = 256;

    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    // ASE-GCM needs GCMParameterSpec
    public static byte[] encrypt(byte[] message, SecretKey secretKey, byte[] iv)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, InvalidAlgorithmParameterException {

        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
        return cipher.doFinal(message);
    }

    // Prefix IV length + IV bytes to cipher text
    public static byte[] encryptWithPrefixIV(byte[] message, SecretKey secretKey, byte[] iv)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
            BadPaddingException, InvalidAlgorithmParameterException {

        byte[] cipherText = encrypt(message, secretKey, iv);
        return ByteBuffer.allocate(iv.length + cipherText.length).put(iv).put(cipherText).array();
    }

    public static String decrypt(byte[] cipherText, SecretKey secretKey, byte[] iv)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

        return new String(cipher.doFinal(cipherText), UTF_8);
    }

    public static String decryptWithPrefixIV(byte[] cipherText, SecretKey secretKey)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(cipherText);

        byte[] iv = new byte[IV_LENGTH_BYTE];
        byteBuffer.get(iv);
        byte[] cipherMessage = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherMessage);

        return decrypt(cipherMessage, secretKey, iv);
    }

    /**
     * RUNNING EXAMPLES:
     * 
     * 
     * --- AES GCM ENCRYPTION ---
     * Input (Plain Text) : This is the original message from Ote Leo
     * Key (hex) : c747e425f0caf5943f60f901c700a18465ed4081defcf3fa4f07e588edf8eb7d
     * IV (hex) : 2eb979e1e62fdaab3458a824
     * Encrypted (hex) :
     * 2eb979e1e62fdaab3458a824b7dbd120b2955674f08622e2c57e370f7af97f335730a6bc31d8589b642f78a5953a3acf13da4eeeb66189c593e49b9fe994ba864738399478
     * Encrypted (hex) (block = 16) : [2eb979e1e62fdaab3458a824b7dbd120,
     * b2955674f08622e2c57e370f7af97f33, 5730a6bc31d8589b642f78a5953a3acf,
     * 13da4eeeb66189c593e49b9fe994ba86, 4738399478]
     * 
     * --- AES GCM DECRYPTION ---
     * Input (hex) :
     * 2eb979e1e62fdaab3458a824b7dbd120b2955674f08622e2c57e370f7af97f335730a6bc31d8589b642f78a5953a3acf13da4eeeb66189c593e49b9fe994ba864738399478
     * Input (hex) (block = 16) : [2eb979e1e62fdaab3458a824b7dbd120,
     * b2955674f08622e2c57e370f7af97f33, 5730a6bc31d8589b642f78a5953a3acf,
     * 13da4eeeb66189c593e49b9fe994ba86, 4738399478]
     * Key (hex) : c747e425f0caf5943f60f901c700a18465ed4081defcf3fa4f07e588edf8eb7d
     * Decrypted Message (Plain Text): This is the original message from Ote Leo
     */
}