package security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.util.List;
import java.util.ArrayList;

public class CryptoUtils {
    public static byte[] getRandomNonce(int numByte) {
        byte[] nonce = new byte[numByte];
        new SecureRandom().nextBytes(nonce);
        return nonce;
    }

    public static SecretKey getAESKey(int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(keySize, SecureRandom.getInstanceStrong());
        return keyGenerator.generateKey();
    }

    public static SecretKey getAESKeyFromPassword(char[] password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        // iterationCount = 65_536
        // keyLength = 256
        KeySpec keySpec = new PBEKeySpec(password, salt, 65_536, 256);
        SecretKey secretKey = new SecretKeySpec(secretKeyFactory.generateSecret(keySpec).getEncoded(), "AES");
        return secretKey;
    }

    public static String hexRepresentation(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes)
            stringBuilder.append(String.format("%02x", b));
        return stringBuilder.toString();
    }

    public static String hexWithBlockSize(byte[] bytes, int blockSize) {
        String hexRepresentation = hexRepresentation(bytes);

        // one hex = 2 chars
        blockSize *= 2;

        // TODO: Use your brain for a better idea
        List<String> result = new ArrayList<>();
        int index = 0;
        while (index < hexRepresentation.length()) {
            result.add(hexRepresentation.substring(index, Math.min(index + blockSize, hexRepresentation.length())));
            index += blockSize;
        }
        return result.toString();
    }
}
