import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

public class RSAencryption
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
        } while (!e.gcd(phi).equals(BigInteger.valueOf(1)));

        //Creating d - the inverse of e: 1/e // then modulo phi
        BigInteger d = e.modPow(BigInteger.valueOf(-1), phi);

        //destroying p and q and phi, and variables derived from those variables
        //good news: since these variables are on the ram, they will be overwritten anyway, or wiped when the computer is turned off
        p = null;
        q = null;
        pMinusOne = null;
        qMinusOne = null;
        phi = null;

        // Showing the behavior of the encryption with a simple terminal.
        RSA_terminal(n, e, d);
    }

    private static BigInteger[] encryptString(String message, BigInteger n, BigInteger e) {
        BigInteger[] output = new BigInteger[message.length()];
        for (int c = 0; c < message.length(); ++c) {
            output[c] = encrypt(message.charAt(c), n, e); // implicit type casting char to int.
        }
        return output;
    }

    private static String decryptString(BigInteger[] cypherText, BigInteger d, BigInteger n) {
        StringBuilder output = new StringBuilder();
        for (BigInteger bigInteger : cypherText) {
            char character = (char) decrypt(bigInteger, d, n);
            output.append(character);
        }
        return output.toString();
    }

    private static BigInteger encrypt(int input, BigInteger n, BigInteger e)
    {
        BigInteger message = BigInteger.valueOf(input);
        BigInteger encrypted = message.modPow(e, n);

        return encrypted;
    }

    private static int decrypt(BigInteger message, BigInteger d, BigInteger n)
    {
        BigInteger decrypted = message.modPow(d, n);
        int c = decrypted.intValue();

        return c;
    }

    private static void RSA_terminal(BigInteger n, BigInteger e, BigInteger d) {
        // Scanner for receiving user input
        Scanner input = new Scanner(System.in);
        String quitSentinel = "quit";
        System.out.println("Put in a string, and watch it get encrypted (type \"" + quitSentinel + "\" to quit): ");

        while (true) {
            String str = input.nextLine();
            if (str.equalsIgnoreCase(quitSentinel)) {
                return;
            }
            System.out.println("Your input encrypted as a bunch of 1024 bit integers.");
            BigInteger[] encrypted = encryptString(str, n, e);
            for (BigInteger i : encrypted){
                System.out.println(i); // just dump everything out to the terminal.
            }

            System.out.println("Your original message decrypted from that mess:");
            System.out.println(decryptString(encrypted, d, n));
            System.out.print("Next string: ");
        }
    }
}
