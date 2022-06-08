import java.util.Optional;

/**
 * NON-MODIFIABLE
 * The data structure to hold the transaction
 */
public final class Transaction {
    private final String PREVIOUS_HASH;
    private final String SIGNATURE;
    private final Optional<String> keyPair;
    private final String[] DATA;

    public Transaction(String previoushash, String signatrue, String[] data) {
        PREVIOUS_HASH = previoushash;
        DATA = data;
        SIGNATURE = signatrue;
        // In haskell -> MONAD
        keyPair = Optional.empty();
    }

    public Transaction(String previoushash, String signatrue, String[] data, Optional<String> pair) {
        PREVIOUS_HASH = previoushash;
        DATA = data;
        SIGNATURE = signatrue;
        keyPair = pair;
    }

    public String[] getData() {
        return DATA;
    }

    public String getPreviousHash() {
        return PREVIOUS_HASH;
    }

    /**
     * Only encrypt the data\
     * Do not encrypt the signatures
     * 
     * @return
     */
    public String encrypt() {
        return null;
    }

    public String decrypt() {
        return null;
    }

    public String toString() {
        return String.format("TRANSACTION [%s]");
    }
}