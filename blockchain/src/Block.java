import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import security.DukeHash;
import util.Tuple;

/**
 * Each Block on the can only hold up to 3 transaction
 */
public class Block {
    private static final int TRANSACTIONAL_CAPACITY = 3;

    private static final String NULL_MINE_TIMESTAMP_ERR = "BLOCK (UUID: `%s', MEM: `%s') HAVE NOT BEEN MINED YET!";
    private static final String FULL_BLOCK_ERR = "BLOCK (UUID: `%s', MEM: `%s') IS FULL (BLOCK CAPACITY IS `%d')";

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
            throw new IllegalStateException(String.format(Block.NULL_MINE_TIMESTAMP_ERR, uuid.toString(), this));
        return miningTimestamp;
    }

    public long getNonce() {
        return this.nonce;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getHash() throws IllegalStateException {
        if (miningTimestamp == null)
            throw new IllegalStateException(String.format(NULL_MINE_TIMESTAMP_ERR, uuid.toString(), this));

        String blockBody = transactions.toString() + previousHash + Long.toString(nonce) + miningTimestamp.toString();
        return DukeHash.hash(blockBody);
    }

    public String getMainBlockBody() {
        String blockBody = transactions.toString() + previousHash;
        return blockBody;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((miningTimestamp == null) ? 0 : miningTimestamp.hashCode());
        result = prime * result + (int) (nonce ^ (nonce >>> 32));
        result = prime * result + ((previousHash == null) ? 0 : previousHash.hashCode());
        result = prime * result + ((transactions == null) ? 0 : transactions.hashCode());
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
        Block other = (Block) obj;
        if (miningTimestamp == null) {
            if (other.miningTimestamp != null)
                return false;
        } else if (!miningTimestamp.equals(other.miningTimestamp))
            return false;
        if (nonce != other.nonce)
            return false;
        if (previousHash == null) {
            if (other.previousHash != null)
                return false;
        } else if (!previousHash.equals(other.previousHash))
            return false;
        if (transactions == null) {
            if (other.transactions != null)
                return false;
        } else if (!transactions.equals(other.transactions))
            return false;
        if (uuid == null) {
            if (other.uuid != null)
                return false;
        } else if (!uuid.equals(other.uuid))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("BLOCK (%s) [MINING TIMESTAMP = %s, NONCE = %s, PREVIOUS HASH = %s, TRANSACTIONS = %s]",
                uuid.toString(), (miningTimestamp == null ? "null" : miningTimestamp.toString()), Long.toString(nonce),
                previousHash, transactions.toString());
    }

    /**
     * Add new transaction to the block, without checking for data integrity.
     * 
     * @param transaction The transaction to be added
     * @return `true` if the transaction have been added successfully
     * @throws IllegalStateException In case the block is full
     */
    public boolean addTransaction(Transaction transaction) throws IllegalStateException {
        if (isFull())
            throw new IllegalStateException(String.format(Block.FULL_BLOCK_ERR, uuid.toString(), this));

        transactions.add(transaction);
        return true;
    }

    public void setNonce(long nonce) {
        this.nonce = nonce;
    }

    /**
     * A non-threaded mining function, the function manipulates the block nonce and
     * mining timestamp
     * 
     * @param prefixSize Number of zeros in the hash prefix
     * @return A tuple containing the mine hash and it's timestamp
     */
    public Tuple<String, Timestamp> mineBlock(int prefixSize) {
        SecureRandom swanson = new SecureRandom();
        String blockBody = getMainBlockBody();
        String hashPrefix = new String(new char[prefixSize]).replace('\0', '0');

        long nonce;
        String hash;
        Timestamp timeStamp;

        do {
            nonce = swanson.nextLong();
            timeStamp = new Timestamp(System.currentTimeMillis());
            String tempBody = blockBody + Long.toString(nonce) + timeStamp.toString();
            hash = DukeHash.hash(tempBody);
        } while (!hash.substring(0, prefixSize).equals(hashPrefix));

        setNonce(nonce);
        setTimeStamp(timeStamp);

        return new Tuple<String, Timestamp>(hash, timeStamp);
    }

    /**
     * A function that attempts to mine the block, in the case of successful mining,
     * the function manipulates the block by adding the new nonce and time stamp
     * 
     * @param swanson    A random number generator
     * @param prefixSize Number of zeros in the mined hash
     * @return `true` in case of a successful mining
     */
    public boolean stepMineBlock(SecureRandom swanson, int prefixSize) {
        String blockBody = getMainBlockBody();
        String hashPrefix = new String(new char[prefixSize]).replace('\0', '0');

        long nonce = swanson.nextLong();
        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
        String hash = DukeHash.hash(blockBody + Long.toString(nonce) + timeStamp);

        if (!hash.substring(0, prefixSize).equals(hashPrefix))
            return false;

        setNonce(nonce);
        setTimeStamp(timeStamp);
        return true;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.miningTimestamp = timeStamp;
    }

    // private String encryptTransaction() {
    // return null;
    // }

    // private String decryptTransaction(String password) {
    // return null;
    // }
}
