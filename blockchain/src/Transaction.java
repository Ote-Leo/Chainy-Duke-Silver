import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

/**
 * The main data unit in the blockchain.
 * Transaction are not modifiable.
 */
public final class Transaction {
    private final UUID uuid;
    private final String[] data;
    private final String signature;
    private final String previousHash;
    private final Optional<String> keyPair;

    private final Timestamp timestamp;

    public Transaction(String previousHash, String signature, String[] data) {
        this.previousHash = previousHash;
        this.signature = signature;
        this.data = data;
        this.keyPair = Optional.empty();

        this.uuid = UUID.randomUUID();
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public Transaction(String previousHash, String signature, String[] data, Optional<String> keyPair) {
        this.previousHash = previousHash;
        this.signature = signature;
        this.keyPair = keyPair;
        this.data = data;

        this.uuid = UUID.randomUUID();
        this.timestamp = new Timestamp(System.currentTimeMillis());
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(data);
        result = prime * result + ((keyPair == null) ? 0 : keyPair.hashCode());
        result = prime * result + ((previousHash == null) ? 0 : previousHash.hashCode());
        result = prime * result + ((signature == null) ? 0 : signature.hashCode());
        result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return String.format(
                "TRANSACTION(%s) [DATA = %s, KEYPAIR = %s, PREVIOUS HASH = s, SIGNATURE = %s, TIMESTAMP = %s]",
                uuid.toString(), Arrays.toString(data), keyPair, previousHash, signature, timestamp.toString());
    }
}