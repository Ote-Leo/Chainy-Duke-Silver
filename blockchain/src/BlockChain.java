import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class BlockChain implements Serializable {
    private final Map<String, List<Transaction>> TRANS_VALIDATION;
    private final List<Block> BLOCK_CHAIN;

    public BlockChain() {
        TRANS_VALIDATION = new HashMap<>();
        BLOCK_CHAIN = new ArrayList<>();
    }

    /**
     * Saving the blockchain to the disk
     */
    public static void save(String path, BlockChain blockChain) throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(blockChain);
        oos.close();
    }

    /**
     * Loading the Blockchain from disk
     */
    public static BlockChain load(String path) {
        return null;
    }

    /**
     * Handles Creating new blocks, incase exceeding the threshold
     * overwise, just add the transaction
     */
    public void addTransaction() {

    }

    /**
     * Checks the validity of the block and its transactions
     * 
     * @return TRUE if the chain is valid
     */
    public boolean crossValidate() {
        return false;
    }

    public int getSize() {
        return BLOCK_CHAIN.size();
    }

    public String readAuthDate(String password) {
        return null;
    }

    public String toString() {
        return BLOCK_CHAIN.toString();
    }
}