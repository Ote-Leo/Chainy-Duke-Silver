import java.util.ArrayList;
import java.util.List;

// import java.util.Random;

public class Block {
    private final List<Transaction> TRANSACTIONS;
    private final String PREVIOUS_HASH;
    private final long nonce;

    public Block(String previousHash) {
        TRANSACTIONS = new ArrayList<>();
        PREVIOUS_HASH = previousHash;
        nonce = 0l;
    }

    public String getPreviousHash() {
        return PREVIOUS_HASH;
    }

    public void mine() {

    }

    public long getNonce() {
        return nonce;
    }

    private String encryptTransaction() {
        return null;
    }

    private String decryptTransaction(String password) {
        return null;
    }

    public String toString() {
        return String.format("BLOCK: [PREVIOUS_HASH=%s, TRANSACTIONS=%s]");
    }
}
