package security;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import util.Tuple;

public class KeyPairManager {

  /**
   * Get a tuple of the public & private keys
   * 
   * @return (Private key, Public key)
   * @throws NoSuchAlgorithmException
   */
  public static Tuple<PrivateKey, PublicKey> generateKeyPair() throws NoSuchAlgorithmException {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

    keyPairGenerator.initialize(2048);
    KeyPair pair = keyPairGenerator.generateKeyPair();

    return new Tuple<PrivateKey, PublicKey>(pair.getPrivate(), pair.getPublic());
  }

  /**
   * Encrypt using RSA
   * 
   * @param message Message to encrypt
   * @param key
   * @return Encrypted Base64 encoding
   * @throws NoSuchAlgorithmException
   * @throws NoSuchPaddingException
   * @throws InvalidKeyException
   * @throws IllegalBlockSizeException
   * @throws BadPaddingException
   */
  public static String encrypt(String message, Key key)
      throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
      BadPaddingException {
    Cipher cipher = Cipher.getInstance(key.getAlgorithm());
    cipher.init(Cipher.ENCRYPT_MODE, key);
    return new String(Base64.getEncoder().encode(cipher.doFinal(message.getBytes())));
  }

  public static String decrypt(String encryptedMessage, Key key)
      throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
      BadPaddingException {
    Cipher cipher = Cipher.getInstance(key.getAlgorithm());
    cipher.init(Cipher.DECRYPT_MODE, key);

    byte[] encBytes = Base64.getDecoder().decode(encryptedMessage);
    return new String(cipher.doFinal(encBytes), StandardCharsets.UTF_8);
  }

}