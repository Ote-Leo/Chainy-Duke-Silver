import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import java.util.ArrayList;
import java.util.HashMap;

public class BlockChain implements Serializable {
    /**
     * TODO:
     * 1. Boiler palte
     */

    // Metadata
    private final Map<String, List<Transaction>> TRANS_VALIDATION; // Transaction are stored on the blockchain

    // private final Map<String, String> REGISTRATION_TABLE; // NOT STORED ON THE
    // BLOCK CHAIN
    private final List<Block> BLOCK_CHAIN;

    public BlockChain() {
        TRANS_VALIDATION = new HashMap<>();
        BLOCK_CHAIN = new ArrayList<>();
    }

    /**
     * Encrypt (AES256/GCM) & Serializes a given blockchain to a specified path
     * 
     * @param path       Target path for the serialized blockchain
     * @param password   The Encryption key
     * @param blockChain The chain to be serialized
     * @return Successful serialization status
     */
    public static boolean safeFileSerialization(String path, String password, BlockChain blockChain) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(blockChain);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Loading the Blockchain from disk
     * Decrypt the blockchain before deserializing
     */
    public static BlockChain safeFileDeserialization(String path, String password) {
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            BlockChain blockChain = (BlockChain) ois.readObject();
            ois.close();
            return blockChain;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
        //
        // BLOCK_CHAIN.ad
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
        // return BLOCK_CHAIN.toString();
        return "Wow, this is a blockchain";
    }

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
