import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Locale.Category;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.spec.IvParameterSpec;
import javax.swing.SingleSelectionModel;

import security.AesGcmPasswordEncryption;
import security.KeyPairManager;

import java.util.ArrayList;
import java.util.HashMap;

public class BlockChain implements Serializable {

    // Metadata
    /**
     * Category 1: for Doctors (Doctor (Name/ID) -> Registration Transactions)
     * Category 2: for Patients (Patient (Name/ID) -> Patient Transactions)
     */
    private final Map<String, Map<String, List<Transaction>>> categories;

    // private final Map<String, String> REGISTRATION_TABLE; // NOT STORED ON THE
    // BLOCK CHAIN
    private final List<Block> blockChain;

    public BlockChain() {
        categories = new HashMap<>();
        blockChain = new ArrayList<>();
    }

    /**
     * Encrypt (AES256/GCM) & Serializes a given blockchain to a specified path
     * 
     * @param path       Target path for the serialized blockchain
     * @param password   The Encryption key
     * @param blockChain The chain to be serialized
     * @return Successful serialization status
     */
    public static boolean safeFileSerialization(String path, String password, BlockChain blockChain, String salt,
            IvParameterSpec iv) {
        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(AesGcmPasswordEncryption.encrypt(blockChain, password, salt, iv));
            out.close();
            fileOut.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Loading the Blockchain from disk
     * Decrypt the blockchain before deserializing
     */
    public static BlockChain safeFileDeserialization(String path, String password, String salt, IvParameterSpec iv) {
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            BlockChain blockChain = (BlockChain) AesGcmPasswordEncryption.decrypt((SealedObject) in.readObject(),
                    password, salt, iv);
            in.close();
            fileIn.close();
            return blockChain;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public String getPreviousTransactionHash(String category, String id) {
        List<Transaction> relationalTransactions = categories.get(category).get(id);
        // Get the last transaction
        return relationalTransactions.get(relationalTransactions.size() - 1).getHash();
    }

    /**
     * Handles Creating new blocks, incase exceeding the threshold
     * overwise, just add the transaction
     */
    public void addTransaction(String category, String id, Key signatureKey, String[] data, String encryptKey) {
        String previousHash = getPreviousTransactionHash(category, id);
        Transaction transaction = new Transaction(previousHash, signatureKey, data, encryptKey);
        Block currentBlock = blockChain.get(blockChain.size() - 1);

        Map<String, List<Transaction>> relations = categories.get(category);

        // Add to the blockchain
        if (currentBlock.isFull()) {
            // Takes around 20~30s
            currentBlock.mineBlock(4);
            Block newBlock = new Block(currentBlock.getHash());
            newBlock.addTransaction(transaction);
        } else
            currentBlock.addTransaction(transaction);

        // Updating the relation Map
        if (relations.containsKey(id)) {
            // Update the Categories Map
            List<Transaction> transRelation = relations.get(id);
            transRelation.add(transaction);

        } else {
            // Update the Categories Map
            relations.put(id, new ArrayList<>());
            List<Transaction> transRelation = relations.get(id);
            transRelation.add(transaction);
        }
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
        return blockChain.size();
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
