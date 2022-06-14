package csen1002.main.task1;
import org.javatuples.Pair;  
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Optional;
import java.util.Scanner;

public class DSA {

 	public static Pair<String,String> generatekeypair() throws NoSuchAlgorithmException {
 		 KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA");
 	      
 	      //Initializing the key pair generator
 	      keyPairGen.initialize(2048);
 	      Pair<PrivateKey, PublicKey> pair=new Pair<PrivateKey,PublicKey>();
 	      //Generate the pair of keys
 	      KeyPair pair = keyPairGen.generateKeyPair();
 	      
 	      //Getting the private key from the key pair
 	      PrivateKey privKey = pair.getPrivate();
 	      PublicKey publicKey= pair.getPublic();
 	      pair.with(privKey,publicKey );  
          return pair; 	      
 	}
	
	
   public static String digitalsignature( String msg, PrivateKey privKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
      //Creating KeyPair generator object
     
      
      //Creating a Signature object
      Signature sign = Signature.getInstance("SHA256withDSA");
      
      //Initialize the signature
      sign.initSign(privKey);
      byte[] bytes = msg.getBytes();
      
      //Adding data to the signature
      sign.update(bytes);
      
      //Calculating the signature
      byte[] signature = sign.sign();
      
      return new String(signature, "UTF8");
      //Printing the signature
     // System.out.println("Digital signature for given text: "+new String(signature, "UTF8"));
   }
   
   public static void main(String[]args) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException, UnsupportedEncodingException {
	   System.out.println((digitalsignature("Ziad Amr")));
   }
}