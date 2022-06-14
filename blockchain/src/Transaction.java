import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import security.AesGcmPasswordEncryption;
import security.DukeHash;
import security.KeyPairManager;

/**
 * The main data unit in the blockchain.
 * Transaction are not modifiable.
 */
public final class Transaction {
    private final UUID uuid;
    private final String data;
    private final String signature;
    private final String previousHash;
    private final Optional<String> keyPair;

    private final Timestamp timestamp;

    public Transaction(String previousHash, Key key, String[] data, String encryptKey) {
        this.previousHash = previousHash;
        this.signature = assignSignature(key);
        this.data = encryptData(data, encryptKey);
        this.keyPair = Optional.empty();

        this.uuid = UUID.randomUUID();
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public Transaction(String previousHash, Key key, String[] data, Optional<String> keyPair,
            String encryptKey) {
        this.previousHash = previousHash;
        this.signature = assignSignature(key);
        this.keyPair = keyPair;
        this.data = encryptData(data, encryptKey);

        this.uuid = UUID.randomUUID();
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public String assignSignature(Key key) {
        try {
            return KeyPairManager.decrypt(getHash(), key);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
                | BadPaddingException e) {
            System.out.println("It's way better to commit suicide");
            e.printStackTrace();
            return null;
        }
    }

    public String getData() {
        return this.data;
    }

    public String getSignature() {
        return signature;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public Optional<String> getKeyPair() {
        return keyPair;
    }

    public Timestamp gTimestamp() {
        return this.timestamp;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Transaction other = (Transaction) obj;
        if (!data.equals(other.data))
            return false;
        if (keyPair == null) {
            if (other.keyPair != null)
                return false;
        } else if (!keyPair.equals(other.keyPair))
            return false;
        if (previousHash == null) {
            if (other.previousHash != null)
                return false;
        } else if (!previousHash.equals(other.previousHash))
            return false;
        if (signature == null) {
            if (other.signature != null)
                return false;
        } else if (!signature.equals(other.signature))
            return false;
        if (timestamp == null) {
            if (other.timestamp != null)
                return false;
        } else if (!timestamp.equals(other.timestamp))
            return false;
        if (uuid == null) {
            if (other.uuid != null)
                return false;
        } else if (!uuid.equals(other.uuid))
            return false;
        return true;
    }

    public String getHash() {
        String transactionBody = data + keyPair.toString() + previousHash + timestamp.toString() + uuid + signature;
        return DukeHash.hash(transactionBody);
    }

    /**
     * A function for encrypting the transaction's body (data)
     * 
     * @param data     An array that will be held by the transaction
     * @param password The password of encryption
     * @return The encrypted data, otherwise expect anything else
     */
    public String encryptData(String[] data, String password) {
        try {
            return new String(Base64.getEncoder()
                    .encode(AesGcmPasswordEncryption.encrypt(Arrays.toString(data).getBytes(), password)));
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
                | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Decrypt the transaction data, provided the encryption key as a password
     * 
     * @param password The encryption key
     * @return Hopefully the encrypted data
     */
    public String decryptData(String password) {
        try {
            return AesGcmPasswordEncryption.decrypt(data, password);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException
                | NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException
                | BadPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + data.hashCode();
        result = prime * result + ((keyPair == null) ? 0 : keyPair.hashCode());
        result = prime * result + ((previousHash == null) ? 0 : previousHash.hashCode());
        result = prime * result + ((signature == null) ? 0 : signature.hashCode());
        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return String.format(
                "TRANSACTION(%s) [DATA = %s, KEYPAIR = %s, PREVIOUS HASH = %s, SIGNATURE = %s, TIMESTAMP = %s]",
                uuid.toString(), data, keyPair, previousHash, signature, timestamp.toString());
    }
}