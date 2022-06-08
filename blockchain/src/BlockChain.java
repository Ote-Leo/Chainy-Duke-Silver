import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class BlockChain implements Serializable {
    // Metadata
    private final Map<String, List<Transaction>> TRANS_VALIDATION; // Transsaction are stored on the blockchain
    private final Map<String, String> REGISTRATION_TABLE; // NOT STORED ON THE BLOCK CHAIN
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
        fos.close();
        oos.close();
    }

    /**
     * Loading the Blockchain from disk
     */
    public static BlockChain load(String path) throws FileNotFoundException, ClassNotFoundException, IOException {
        FileInputStream fis = new FileInputStream(path);
        ObjectInputStream ois = new ObjectInputStream(fis);
        BlockChain blockChain = (BlockChain) ois.readObject();
        fis.close();
        ois.close();
        return blockChain;
    }

    /**
     * Handles Creating new blocks, incase exceeding the threshold
     * overwise, just add the transaction
     */
    public void addTransaction(Transaction data) {

        // Checking the threshold
        // Mine the block
        //
        // Just Add
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

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}