import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import security.DukeHash;

/**
 * Each Block on the can only hold up to 3 transaction
 */
public class Block {
    private static final int TRANSACTIONAL_CAPACITY = 3;

    private static final String NULL_MINE_TIMESTAMP_ERR = "BLOCK `%s' HAVE NOT BEEN MINED YET!";
    private static final String FULL_BLOCK_ERR = "BLOCK `%s' IS FULL (BLOCK CAPACITY IS `%d')";

    private final List<Transaction> transactions;
    private final String previousHash;
    private final UUID uuid;

    private Timestamp miningTimestamp;
    private long nonce;

    public Block(String previousHash) {
        this.transactions = new ArrayList<>(Block.TRANSACTIONAL_CAPACITY);
        this.previousHash = previousHash;

        this.uuid = UUID.randomUUID();
    }

    public boolean isFull() {
        return transactions.size() >= Block.TRANSACTIONAL_CAPACITY;
    }

    public String getPreviousHash() {
        return this.previousHash;
    }

    public Timestamp getMiningTimeStamp() throws IllegalStateException {
        if (miningTimestamp == null)
            throw new IllegalStateException(String.format(Block.NULL_MINE_TIMESTAMP_ERR, this));
        return miningTimestamp;
    }

    public long getNonce() {
        return this.nonce;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getHash() {
        String blockBody = transactions.toString() + previousHash
                + (miningTimestamp == null ? "" : miningTimestamp.toString()) + Long.toString(nonce);
        return DukeHash.hash(blockBody);
    }

    // public String getPreviousHash() {
    // return PREVIOUS_HASH;
    // }

    // public void mine() {

    // }

    // public long getNonce() {
    // return nonce;
    // }

    // private String encryptTransaction() {
    // return null;
    // }

    // private String decryptTransaction(String password) {
    // return null;
    // }

    // public String toString() {
    // return String.format("BLOCK: [PREVIOUS_HASH=%s, TRANSACTIONS=%s]");
    // }
}
