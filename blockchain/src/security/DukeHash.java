package security;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The Hashing class of the blockchain; hashing algorithm is set to SHA256
 */
public class DukeHash {
    private static final String HASHING_ALGORITHM = "SHA-256";
    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    public static String hash(String message) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASHING_ALGORITHM);
            return CryptoUtils.hexRepresentation(messageDigest.digest(message.getBytes(UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("SOMETHING IS REALLY WRONG WITH YOUR JAVA!");
            e.printStackTrace();
            return null;
        }
    }
}
