import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;

import security.CryptoUtils;
import util.Tuple;

public class BlockMiner implements Runnable {
    private volatile boolean running = true;

    private int prefixSize;
    private Block block;

    private static final String HASHING_ALGORITHM = "SHA-256";
    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    public void terminate() {
        running = false;
    }

    public void setMiningConfig(Tuple<Block, Integer> config) {
        this.block = config.fst;
        this.prefixSize = config.snd;
        this.running = true;
    }

    @Override
    public void run() {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASHING_ALGORITHM);
            SecureRandom swanson = new SecureRandom();

            String blockBody = block.getMainBlockBody();
            String hashPrefix = new String(new char[prefixSize]).replace('\0', '0');
            long nonce;
            Timestamp timeStamp;

            String hash;

            do {
                nonce = swanson.nextLong();
                timeStamp = new Timestamp(System.currentTimeMillis());
                String tempBody = blockBody + Long.toString(nonce) + timeStamp.toString();

                hash = CryptoUtils.hexRepresentation(messageDigest.digest(tempBody.getBytes(UTF_8)));
            } while (running && !hash.substring(0, prefixSize).equals(hashPrefix));

            block.setNonce(nonce);
            block.setTimeStamp(timeStamp);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("SOMETHING IS REALLY WRONG WITH YOUR JAVA!");
            e.printStackTrace();
            return;
        }
    }
}
