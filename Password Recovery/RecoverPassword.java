/*
 * University of Central Florida
 * CIS 33l60 - Fall 2017
 * Author: John Hacker
 */
 
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class RecoverPassword {
    //Member variables
    private static String fileName;
    private static String hashValue;
    private static int inputHash = 0;
    private static int saltCount = 0;
    private static int[] saltedPassword = new int[15];
    private static boolean found = false;
    private static String foundPassword;
    private static int foundASCII;
    private static int foundSalt;
    private static int combosTested;
    
    public static void main(String args[]) {
        //Save the command arguments
        fileName = args[0];
        hashValue = args[1];
        
        //Convert hashValue to int
        for (int j = 0, k = hashValue.length() - 1; j < hashValue.length(); j++, k--)
            {
                inputHash += (((int)hashValue.toCharArray()[j]) - '0') * Math.pow(10.0, (double) k);
            }
        
        //Print header information
        for (int i = 0; i < 40; i++)
        {
            System.out.print("-");
        }
        System.out.println("\n\nCIS3360 Password Recovery by John Hacker\n");
        System.out.println("   Directory file name        : " + fileName);
        System.out.println("   Salted password hash value : " + hashValue);
        System.out.println("\nIndex   Word   Unsalted ASCII equivalent\n");
        
        //Compute ASCII values for a password
        try 
        {
            int wordCount = 1;
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String word;
            while ((word = bufferedReader.readLine()) != null) 
            {
                if (wordCount / 10 == 0)
                {
                    System.out.print("   ");
                }
                else if (wordCount / 10 < 10)
                {
                    System.out.print("  ");
                }
                else if (wordCount / 10 < 100)
                {
                    System.out.print(" ");
                }
                System.out.print(wordCount + " :  " + word + " ");
                for (int i = 0; i < word.toString().length(); i++)
                {
                    System.out.print((int) word.toCharArray()[i]);
                }
                checkSalt(word);
                System.out.println("");
                wordCount++;
                bufferedReader.readLine();
            }
            reader.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        if (!found)
        {
            System.out.println("\nPassword not found in dictionary");
            System.out.println("\nCombinations tested: " + saltCount);
        }
        else
        {
            System.out.println("\nPassword recovered:");
            System.out.println("   Password: " + foundPassword);
            System.out.println("   ASCII value: " + foundASCII);
            System.out.println("   Salt value: " + foundSalt);
            System.out.println("   Combinations tested: " + combosTested);
        }
    }
    
    private static void checkSalt(String password)
    {
        int left = 0, right = 0;
        int testHash = 0;
        System.out.print("   Test: ");
        for (int i = 3, j = 0; i < saltedPassword.length; i = i + 2, j++)
        {
            saltedPassword[i] = (int) password.toCharArray()[j] / 10;
            saltedPassword[i + 1] = (int) password.toCharArray()[j] % 10;
        }
        for (int i = 0; i < 1000; i++)
        {
            saltCount++;
            left = 0;
            right = 0;
            if (i / 10 == 0)
            {
                saltedPassword[0] = 0;
                saltedPassword[1] = 0;
                saltedPassword[2] = i;
            }
            else if (i / 10 < 10)
            {
                saltedPassword[0] = 0;
                saltedPassword[1] = i / 10;
                saltedPassword[2] = i % 10;
            }
            else
            {
                saltedPassword[0] = i / 100;
                saltedPassword[1] = i / 10 % 10;
                saltedPassword[2] = i % 10;
            }
            for (int j = 0, k = 6; j < 7; j++, k--)
            {
                left += saltedPassword[j] * Math.pow(10.0, (double) k);
            }
            for (int j = 7, k = 7; j < 15; j++, k--)
            {
                right += saltedPassword[j] * Math.pow(10.0, (double) k);
            }
            testHash = ((243 * left) + right) % 85767489;
            if (testHash == inputHash)
            {
                found = true;
                foundPassword = password;
                foundASCII = 0;
                for (int z = 0, x = 11; z < 6; z++, x = x-2)
                {
                    foundASCII += ((int) password.toCharArray()[z]) * Math.pow(10.0, (double) x);
                }
                foundSalt = saltedPassword[0] * 100 + saltedPassword[1] * 10 + saltedPassword[2];
                combosTested = saltCount;
            }
        }
        System.out.print("   left=" + left + "   right=" + right);
    }
}