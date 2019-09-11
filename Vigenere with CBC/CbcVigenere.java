//------------------------------------------------------------------
// University of Central Florida
// CIS3360 - Fall 2017
// Program Author: John Hacker
//------------------------------------------------------------------

//Imports
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class CbcVigenere {
    
    //Member variables
    private static String inputFileName;
    private static String vigenereKey;
    private static String initializationVector;
    private static String readMessage;
    private static char[] printReadMessage;
    private static String buffer;
    private static String encryptedMessage;
    private static char[] printEncryptedMessage;
    private static int blockSize;
    private static int pad;
    private static int numBlocks;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //Identify who the assignment is written by
        System.out.println("\nCBC Vigenere by John Hacker");
        
        //Read the file name in the command line first argument
        inputFileName = args[0];
        System.out.println("Plaintext file name: " + inputFileName);
        
        //Read the vigenere key in the command line second argument
        vigenereKey = args[1];
        System.out.println("Vigenere keyword: " + vigenereKey);
        
        //Read the init vector in the comman line thrid argument
        initializationVector = args[2];
        System.out.println("Initialization vector: " + initializationVector + "\n");
        
        //Open the file by the name in the first argument
        File file = new File(inputFileName);
        
        //Label that the plain text read message
        System.out.println("Clean Plaintext:\n");
        
        //Try block for obtaining the plaintext read message
        try 
        {
            //Read the file
            BufferedReader br = new BufferedReader(new FileReader(file));
            
            //Save the contents of the txt file
            while ((buffer = br.readLine()) != null)
            {   
                //Check if the message read in already has content
                if(readMessage != null)
                {
                    //Add the newly read message to the message that has already been read
                    readMessage = readMessage + buffer;
                }
                else
                {
                    //Save the beginning of the message to be read in
                    readMessage = buffer;
                }
            }
            
            //Replace all the non-letters with empty strings
            readMessage = readMessage.replaceAll("[^a-zA-Z]", "");
            
            //Make all the letters lower case
            readMessage = readMessage.toLowerCase();
            
            //Make a char array of the read message for printing purposes
            printReadMessage = readMessage.toCharArray();
            
            //Loop through all the char's in the read message
            for (int i = 0; i < printReadMessage.length; i++)
            {
                //Print the current character in the read message
                System.out.print(printReadMessage[i]);
                
                //Check if the current index is a multiple of 80, if starting from 1 instead of 0
                if ((i+1) % 80 == 0 && i != 0)
                {
                    //Print a new line
                    System.out.println();
                }
            }
        }
        catch (Exception e)
        {
            //Print the existing exception
            e.printStackTrace();
        }
        
        //Print the label for the Vigenere encrpyted text
        System.out.println("\n\nCiphertext:\n");
        
        //Try block for encrypting the read message
        try
        {
            //The block size is simply the number of characters in the Vigenere keyword
            blockSize = vigenereKey.length();
            
            //Find the pad size of the last block
            pad = blockSize - printReadMessage.length % blockSize;
            
            //Determine the maount of blocks
            numBlocks = printReadMessage.length / blockSize;
            
            //Determine if the amount of blocks is correct
            if (numBlocks * blockSize != printReadMessage.length)
            {
                //Add 1 in the case that integer division resulted in truncation
                numBlocks += 1;
            }
            
            //Array of blocks of characters
            char[][] blockedMessage = new char[numBlocks+1][blockSize];
            int count = 0;
            char[] charArrIV = initializationVector.toCharArray();
            
            //Make the IV a char array
            System.arraycopy(charArrIV, 0, blockedMessage[0], 0, blockSize);
            
            //Loop through all the blocks
            for (int r = 1; r <= numBlocks; r++)
            {
                //Make sure its not the last block
                if (r != numBlocks)
                {
                    //Loop through all the spaces in the block
                    for (int c = 0; c < blockSize; c++)
                    {
                        //Add the next letter to the current spot in the block
                        blockedMessage[r][c] = printReadMessage[count++];
                    }
                }
                //Case of the last block
                else
                {
                    //Loop though all the block spots
                    for (int c = 0; c < blockSize; c++)
                    {
                        //Check if the spot needs to be padded
                        if (c >= blockSize - pad)
                        {
                            //Pad the spot with the letter x
                            blockedMessage[r][c] = 'x';
                        }
                        //Case of letter(s) left from message to put into block
                        else
                        {
                            //Add the next letter to the current spot in the block
                            blockedMessage[r][c] = printReadMessage[count++];
                        }   
                    }
                }
            }      
            
            //Initialize the encrypted message to an empty string
            encryptedMessage = "";
            
            //Loop through all the blocks without the IV to perform the CBC cipher with Vigenere encryption
            for (int i = 1; i <= numBlocks; i++)
            {
                //Loop through all of the letters in the current block
                for (int j = 0, k = 0; j < blockSize; j++)
                {
                    //Implement XOR on each letter in the current block with the letters from the previous block
                    blockedMessage[i][j] = (char)('a' + (blockedMessage[i][j] - 'a' + blockedMessage[i - 1][j] - 'a') % 26);
                    
                    //Implement Vigenere step
                    blockedMessage[i][j] = (char)('a' + (blockedMessage[i][j] - 'a' + vigenereKey.charAt(k) - 'a') % 26);
                    k = ++k % vigenereKey.length();
                }
                
                //Add the encrypted block to the encrypted message
                encryptedMessage += new String(blockedMessage[i]);
            }
            
            //Make a char array of the encrypted message for printing purposes
            printEncryptedMessage = encryptedMessage.toCharArray();

            //Loop through all the char's in the ecnrpyted message
            for (int i = 0; i < printEncryptedMessage.length; i++)
            {
                //Print the current character in the encrypted message
                System.out.print(printEncryptedMessage[i]);

                //Check if the current index is a multiple of 80, if starting from 1 instead of 0
                if ((i+1) % 80 == 0 && i != 0)
                {
                    //Print a new line
                    System.out.println();
                }
            }
            
        }
        catch (Exception e)
        {
            //Print the existing exception
            e.printStackTrace();
        }
        
        //Print the how many char's were in the clean read message
        System.out.println("\n\nNumber of characters in clean plaintext file: " + printReadMessage.length);
        
        //Print the block size
        System.out.println("Block size = " + blockSize);
        
        //Print the number of characters padded to the last block
        System.out.println("Number of pad characters added: " + pad);
    }
 
}
