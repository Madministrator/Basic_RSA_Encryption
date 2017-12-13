//import java.lang.Math;
import java.math.BigInteger;
import java.util.Random;
public class RSA_Encryption 
{
	public static void main(String[] args) 
	{
		Random rnd = new Random();
		rnd.setSeed(System.currentTimeMillis());
		// Generate 1024 bit primes P and Q
		BigInteger p = new BigInteger(1024, 256, rnd);
		BigInteger q = new BigInteger(1024, 256, rnd);
		
		//Multiplying p and q to get n, part of the public key
		BigInteger n =  p.multiply(q);
		
		//Creating phi, the generator of the private key
		BigInteger pMinusOne = p.subtract(BigInteger.valueOf(1));
		BigInteger qMinusOne = q.subtract(BigInteger.valueOf(1));
		BigInteger phi = pMinusOne.multiply(qMinusOne);
		
		//Generating the 'e', the other part of the public key
		BigInteger e;
		do //Verify that the greatest common denominator of e and phi is 1 and only 1
		{
			do // e is a BigInteger between 2 and phi
			{
				//Giving a new seed every time we run through the loop
				rnd = new Random();
				rnd.setSeed(System.currentTimeMillis());
				
				e = new BigInteger(phi.bitLength(), rnd);
			} while ((e.compareTo(phi) >= 0) || (e.compareTo(BigInteger.valueOf(2)) <= 0));
		} while (e.gcd(phi) != BigInteger.valueOf(1));
		
		//Creating d - the inverse of e: 1/e // then modulo phi
		BigInteger d = e.modPow(BigInteger.valueOf(-1), phi);
		
		//destroying p and q and phi, and variables derived from those variables
		//good news: since these variables are on the ram, they will be overwritten anyway, or wiped when the computer is turned off
		p = null;
		q = null;
		pMinusOne = null;
		qMinusOne = null;
		phi = null;
		
		//Testing the encryption
		final int MESSAGE = 900;
		BigInteger hardToRead = encrypt(MESSAGE, n, e);
		int recieved = decrypt(hardToRead, d, n);
		System.out.println(hardToRead);
		System.out.println(recieved);
		
	}
	
	static BigInteger encrypt(int input, BigInteger n, BigInteger e)
	{
		BigInteger message = BigInteger.valueOf(input);
		BigInteger encrypted = message.modPow(e, n);
		
		return encrypted;
	}
	
	static int decrypt(BigInteger message, BigInteger d, BigInteger n)
	{
		BigInteger decrypted = message.modPow(d, n);
		int c = decrypted.intValue();
		
		return c;
	}
}