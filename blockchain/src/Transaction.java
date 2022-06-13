import java.util.Arrays;
import java.util.Optional;

/**
 * NON-MODIFIABLE
 * The data structure to hold the transaction
 */
public final class Transaction {
    private final String[] data;
    private final String signature;
    private final String previousHash;
    private final Optional<String> keyPair;

    public Transaction(String previousHash, String signature, String[] data) {
        this.previousHash = previousHash;
        this.signature = signature;
        this.data = data;
        this.keyPair = Optional.empty();
    }

    public Transaction(String previousHash, String signature, String[] data, Optional<String> keyPair) {
        this.previousHash = previousHash;
        this.signature = signature;
        this.keyPair = keyPair;
        this.data = data;
    }

    public String[] getData() {
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(data);
        result = prime * result + ((keyPair == null) ? 0 : keyPair.hashCode());
        result = prime * result + ((previousHash == null) ? 0 : previousHash.hashCode());
        result = prime * result + ((signature == null) ? 0 : signature.hashCode());
        return result;
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
        if (!Arrays.equals(data, other.data))
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
        return true;
    }

    @Override
    public String toString() {
        return String.format("TRANSACTION [DATA = %s, KEYPAIR = %s, PREVIOUS HASH = %s, SIGNATURE = %s]",
                Arrays.toString(data), keyPair, previousHash, signature);
    }
}