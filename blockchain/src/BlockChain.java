import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.crypto.SealedObject;
import javax.crypto.spec.IvParameterSpec;
import javax.swing.plaf.synth.SynthOptionPaneUI;

import security.AesGcmPasswordEncryption;
import security.KeyPairManager;

import util.Tuple;

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
        if (!categories.containsKey(category)) {
            Map<String, List<Transaction>> relations = new HashMap<>();
            relations.put(id, new ArrayList<>());
            categories.put(category, relations);
            return "";
        }
        Map<String, List<Transaction>> relations = new HashMap<>();
        if (!relations.containsKey(id)) {
            relations.put(id, new ArrayList<>());
            return "";
        }

        List<Transaction> relationalTransactions = categories.get(category).get(id);
        // Get the last transaction
        return relationalTransactions.get(relationalTransactions.size() - 1).getHash();
    }

    /**
     * Handles Creating new blocks, incase exceeding the threshold
     * overwise, just add the transaction
     */
    public void addTransaction(String category, String id, Key signatureKey, String[] data, String encryptKey) {
        if (blockChain.isEmpty()) {
            // Create a block
            Block block = new Block("");
            blockChain.add(block);
        }
        String previousHash;
        try {
            previousHash = getPreviousTransactionHash(category, id);
        } catch (NullPointerException e) {
            previousHash = "";
        }

        Transaction transaction = new Transaction(previousHash, signatureKey, data, encryptKey);
        Block currentBlock = blockChain.get(blockChain.size() - 1);

        Map<String, List<Transaction>> relations = categories.get(category);

        // Add to the blockchain
        if (currentBlock.isFull()) {
            // Takes around 20~30s
            System.out.println("\n\n\nMining ...");
            util.Tuple<String, Timestamp> t = currentBlock.mineBlock(4);
            System.out.println(t + "\n\n\n");
            Block newBlock = new Block(currentBlock.getHash());
            newBlock.addTransaction(transaction);
            blockChain.add(newBlock);
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
        for (int i = getSize() - 1; i > 0; i--) {
            Block lastblock = blockChain.get(i);
            Stack<String> checkhash = new Stack<String>();
            checkhash.add(lastblock.getPreviousHash());
            Block checkblock = blockChain.get(i - 1);
            if (checkblock.getHash().equals(checkhash.peek())) {
                checkhash.pop();
            } else {
                return false;
            }
        }
        return true;
    }

    public int getSize() {
        return blockChain.size();
    }

    public String readAuthDate(String password) {
        return null;
    }

    @Override
    public String toString() {
        return "BlockChain [blockChain=" + blockChain + ", categories=" + categories + "]";
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String PASSWORD = "This is a strong and complicated password, Please god make this work";
        Tuple<PrivateKey, PublicKey> keyPair = KeyPairManager.generateKeyPair();

        BlockChain blockChain = new BlockChain();

        blockChain.addTransaction("Doctor", "Ote", keyPair.snd, new String[] { "Here", "is", "some", "data" },
                PASSWORD);
        blockChain.addTransaction("Doctor", "Ote", keyPair.snd, new String[] { "Here", "is", "some", "data" },
                PASSWORD);
        blockChain.addTransaction("Doctor", "Ote", keyPair.snd, new String[] { "Here", "is", "some", "data" },
                PASSWORD);

        blockChain.addTransaction("Doctor", "Ote", keyPair.snd, new String[] { "Here", "is", "some", "data" },
                PASSWORD);
        System.out.println(blockChain);
        System.out.println(blockChain.getSize());
        System.out.println(blockChain.crossValidate());
    }
}
