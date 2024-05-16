package collection;
import java.security.*;

public class HashPassword {
	
	public static String hashPassword(String password){
        MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
		
        md.update(password.getBytes());
        byte[] hashedPassword = md.digest();

        StringBuilder sb = new StringBuilder();
        for (byte b : hashedPassword) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();}
        catch (NoSuchAlgorithmException e) {
			return null;
		}
    }

}
