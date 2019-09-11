/*
 * University of Central Florida
 * CIS 3360 - Fall 2017
 * Author: John Hacker
 */

public class CrcIntegrity {
    
    //Member variables
    private static String modeOfOperation; 
    private static String stringToProcess;
    private static char[] charArrToProcess;
    private static String stringInBinary = "";
    private static int[] binaryInput;
    private static int[] binaryPolynomial;
    private static int[] toDivide;
    private static int[] observedCrc = new int[12];
    private static int[] computedCrc = new int[12];
    private static char[] computedCrcHex = new char[3];
    private static boolean match = true;
    
    public static void main(String args[]) {
        
        //Read in the command line arguments
        modeOfOperation = args[0];
        stringToProcess = args[1];
        
        //Print the header information
        for(int c = 0; c < 62; c++)
        {
            System.out.print("-");
        }
        System.out.println("\n\nCIS3360 Fall 2017 Integrity Checking Using CRC");
        System.out.println("Author: John Hacker\n");
        System.out.println("The input string (hex): " + stringToProcess);
        
        //Convert the input string to binary from hexadecimal
        charArrToProcess = stringToProcess.toCharArray();
        binaryInput = new int[4 * stringToProcess.length()];
        for (int i = 0; i < stringToProcess.length(); i++)
        {
            switch (charArrToProcess[i]) {
                case '0':
                    stringInBinary = stringInBinary + "0000 ";
                    binaryInput[4*i] = 0;
                    binaryInput[4*i+1] = 0;
                    binaryInput[4*i+2] = 0;
                    binaryInput[4*i+3] = 0;
                    break;
                case '1':
                    stringInBinary = stringInBinary + "0001 ";
                    binaryInput[4*i] = 0;
                    binaryInput[4*i+1] = 0;
                    binaryInput[4*i+2] = 0;
                    binaryInput[4*i+3] = 1;
                    break;
                case '2':
                    stringInBinary = stringInBinary + "0010 ";
                    binaryInput[4*i] = 0;
                    binaryInput[4*i+1] = 0;
                    binaryInput[4*i+2] = 1;
                    binaryInput[4*i+3] = 0;
                    break;
                case '3':
                    stringInBinary = stringInBinary + "0011 ";
                    binaryInput[4*i] = 0;
                    binaryInput[4*i+1] = 0;
                    binaryInput[4*i+2] = 1;
                    binaryInput[4*i+3] = 1;
                    break;
                case '4':
                    stringInBinary = stringInBinary + "0100 ";
                    binaryInput[4*i] = 0;
                    binaryInput[4*i+1] = 1;
                    binaryInput[4*i+2] = 0;
                    binaryInput[4*i+3] = 0;
                    break;
                case '5':
                    stringInBinary = stringInBinary + "0101 ";
                    binaryInput[4*i] = 0;
                    binaryInput[4*i+1] = 1;
                    binaryInput[4*i+2] = 0;
                    binaryInput[4*i+3] = 1;
                    break;
                case '6':
                    stringInBinary = stringInBinary + "0110 ";
                    binaryInput[4*i] = 0;
                    binaryInput[4*i+1] = 1;
                    binaryInput[4*i+2] = 1;
                    binaryInput[4*i+3] = 0;
                    break;
                case '7':
                    stringInBinary = stringInBinary + "0111 ";
                    binaryInput[4*i] = 0;
                    binaryInput[4*i+1] = 1;
                    binaryInput[4*i+2] = 1;
                    binaryInput[4*i+3] = 1;
                    break;
                case '8':
                    stringInBinary = stringInBinary + "1000 ";
                    binaryInput[4*i] = 1;
                    binaryInput[4*i+1] = 0;
                    binaryInput[4*i+2] = 0;
                    binaryInput[4*i+3] = 0;
                    break;
                case '9':
                    stringInBinary = stringInBinary + "1001 ";
                    binaryInput[4*i] = 1;
                    binaryInput[4*i+1] = 0;
                    binaryInput[4*i+2] = 0;
                    binaryInput[4*i+3] = 1;
                    break;
                case 'A':
                    stringInBinary = stringInBinary + "1010 ";
                    binaryInput[4*i] = 1;
                    binaryInput[4*i+1] = 0;
                    binaryInput[4*i+2] = 1;
                    binaryInput[4*i+3] = 0;
                    break;
                case 'B':
                    stringInBinary = stringInBinary + "1011 ";
                    binaryInput[4*i] = 1;
                    binaryInput[4*i+1] = 0;
                    binaryInput[4*i+2] = 1;
                    binaryInput[4*i+3] = 1;
                    break;
                case 'C':
                    stringInBinary = stringInBinary + "1100 ";
                    binaryInput[4*i] = 1;
                    binaryInput[4*i+1] = 1;
                    binaryInput[4*i+2] = 0;
                    binaryInput[4*i+3] = 0;
                    break;
                case 'D':
                    stringInBinary = stringInBinary + "1101 ";
                    binaryInput[4*i] = 1;
                    binaryInput[4*i+1] = 1;
                    binaryInput[4*i+2] = 0;
                    binaryInput[4*i+3] = 1;
                    break;
                case 'E':
                    stringInBinary = stringInBinary + "1110 ";
                    binaryInput[4*i] = 1;
                    binaryInput[4*i+1] = 1;
                    binaryInput[4*i+2] = 1;
                    binaryInput[4*i+3] = 0;
                    break;
                case 'F':
                    stringInBinary = stringInBinary + "1111 ";
                    binaryInput[4*i] = 1;
                    binaryInput[4*i+1] = 1;
                    binaryInput[4*i+2] = 1;
                    binaryInput[4*i+3] = 1;
                    break;
                default:
                    break;
            }
        }
        System.out.println("The input string (bin): " + stringInBinary);
        
        //Establish the CRC polynomial
        binaryPolynomial = new int[] {1,1,0,0, 1,1,0,1, 1,0,1,0, 1};
        System.out.println("\nThe polynomial used (binary bit string): 1100 1101 1010 1\n");
        
        //Check if the user wants to calculate or validate
        if (modeOfOperation.equals("c"))
        {
            //Enter calculate mode of operation
            calculate();
        }
        else
        {
            //Enter verify mode of operation
            verify();
        }
    }
    
    //Mode of operation that calculates the CRC
    public static void calculate() {
        
        //Identify that the mode of operation
        System.out.println("Mode of operation: calculation\n");
        
        //Required padding statement
        System.out.println("Number of zeroes that will be appended to the binary input: 12\n");
        
        //Create the binary to divide
        toDivide = new int[binaryInput.length + 12];
        for (int i = 0; i < toDivide.length; i++)
        {
            try
            {
                toDivide[i] = binaryInput[i];
            }
            catch (Exception e)
            {
                toDivide[i] = 0;
            }
        }
        
        //Display the intermediate results of modulo division
        division();
        
        //Show the computed CRC
        System.out.print("\nThe CRC computed from the input: ");
        for (int i = 0, c = toDivide.length - 12; i < computedCrc.length; i++, c++)
        {
            computedCrc[i] = toDivide[c];
            System.out.print(computedCrc[i]);
            if(i % 4 == 3)
            {
                System.out.print(" ");
            }
        }
        System.out.print("(bin) = ");
        CrcToHex();
        for (int i = 0; i < 3; i++)
        {
            System.out.print(computedCrcHex[i]);
        }
        System.out.println(" (hex)");
    }
    
    //Mode of operation that verifies the CRC
    public static void verify() {
        
        //Identify that the mode of operation
        System.out.println("Mode of operation: verification\n");
        
        //Identify what the CRC at the end of the input
        System.out.print("The CRC observed at the end of the input: ");
        for (int i = binaryInput.length - 12, c = 0; i < binaryInput.length; i++, c++)
        {
            System.out.print(binaryInput[i]);
            observedCrc[c] = binaryInput[i];
            if(i % 4 == 3)
            {
                System.out.print(" ");
            }
        }
        System.out.print("(bin) = ");
        for (int i = charArrToProcess.length - 3; i < charArrToProcess.length; i++)
        {
            System.out.print(charArrToProcess[i]);
        }
        System.out.println(" (hex)\n");
        
        //Create the binary to divide
        toDivide = binaryInput;
        
        //Displayt the intermediate steps of modulo division
        division();
        
        //Show the computed CRC
        System.out.print("\nThe CRC computed from the input: ");
        for (int i = 0, c = toDivide.length - 12; i < computedCrc.length; i++, c++)
        {
            computedCrc[i] = toDivide[c] ^ observedCrc[i];
            System.out.print(computedCrc[i]);
            if(computedCrc[i] != observedCrc[i])
            {
                match = false;
            }
            if(i % 4 == 3)
            {
                System.out.print(" ");
            }
        }
        System.out.print("(bin) = ");
        CrcToHex();
        for (int i = 0; i < 3; i++)
        {
            System.out.print(computedCrcHex[i]);
        }
        System.out.println(" (hex)\n");
        
        //Identify if the computed CRC matches the observed CRC
        System.out.print("Did the CRC check pass? (Yes or No): ");
        if (match)
        {
            System.out.println("Yes");
        }
        else
        {
            System.out.println("No");
        }
    }
    
    //Perform modulo long division and show intermediate results
    public static void division() {
        
        //Declare intermediate results
        System.out.println("The binary string difference after each XOR step of the CRC calculation:\n");
        
        //Print the initial input with padding
        for (int i = 0; i < toDivide.length; i++)
        {
            System.out.print(toDivide[i]);
            if (i % 4 == 3)
            {
                System.out.print(" ");
            }
        }
        
        //Modulo divide the binary input by the binary polynomial
        boolean done = false;
        boolean found;
        division:
        while (!done)
        {
            found = false;
            for (int i = 0; i < toDivide.length; i++)
            {
                if (i == toDivide.length - binaryPolynomial.length && !found)
                {
                    done = true;
                    found = true;
                    if (toDivide[i] == 0)
                    {
                        break division;
                    }
                    else
                    {
                        
                        for (int c = i, k = 0; c < i + 13; c++, k++)
                        {
                            toDivide[c] = toDivide[c] ^ binaryPolynomial[k];
                        }
                        i += binaryPolynomial.length;
                    }
                }
                else
                {
                    if (toDivide[i] == 1 && !found)
                    {
                        found = true;
                        for (int c = i, k = 0; c < i + 13; c++, k++)
                        {
                            toDivide[c] = toDivide[c] ^ binaryPolynomial[k];
                        }
                        i += 12;
                    }
                }
            }
            System.out.println();
            for (int i = 0; i < toDivide.length; i++)
            {
                System.out.print(toDivide[i]);
                if (i % 4 == 3)
                {
                    System.out.print (" ");
                }
            }
        }
        System.out.println();
    }
    
    //Convert a CRC from binary to hexadecimal
    public static void CrcToHex() {
        for (int i = 0; i < 3; i++)
        {
            if (computedCrc[4 * i] == 0)
            {
                if (computedCrc[4 * i + 1] == 0)
                {
                    if (computedCrc[4 * i + 2] == 0)
                    {
                        if (computedCrc[4 * i + 3] == 0)
                        {
                            computedCrcHex[i] = '0';
                        }
                        else
                        {
                            computedCrcHex[i] = '1';
                        }
                    }
                    else
                    {
                        if (computedCrc[4 * i + 3] == 0)
                        {
                            computedCrcHex[i] = '2';
                        }
                        else
                        {
                            computedCrcHex[i] = '3';
                        }
                    }
                }
                else
                {
                    if (computedCrc[4 * i + 2] == 0)
                    {
                        if (computedCrc[4 * i + 3] == 0)
                        {
                            computedCrcHex[i] = '4';
                        }
                        else
                        {
                            computedCrcHex[i] = '5';
                        }
                    }
                    else
                    {
                        if (computedCrc[4 * i + 3] == 0)
                        {
                            computedCrcHex[i] = '6';
                        }
                        else
                        {
                            computedCrcHex[i] = '7';
                        }
                    }
                }
            }
            else
            {
                if (computedCrc[4 * i + 1] == 0)
                {
                    if (computedCrc[4 * i + 2] == 0)
                    {
                        if (computedCrc[4 * i + 3] == 0)
                        {
                            computedCrcHex[i] = '8';
                        }
                        else
                        {
                            computedCrcHex[i] = '9';
                        }
                    }
                    else
                    {
                        if (computedCrc[4 * i + 3] == 0)
                        {
                            computedCrcHex[i] = 'A';
                        }
                        else
                        {
                            computedCrcHex[i] = 'B';
                        }
                    }
                }
                else
                {
                    if (computedCrc[4 * i + 2] == 0)
                    {
                        if (computedCrc[4 * i + 3] == 0)
                        {
                            computedCrcHex[i] = 'C';
                        }
                        else
                        {
                            computedCrcHex[i] = 'D';
                        }
                    }
                    else
                    {
                        if (computedCrc[4 * i + 3] == 0)
                        {
                            computedCrcHex[i] = 'E';
                        }
                        else
                        {
                            computedCrcHex[i] = 'F';
                        }
                    }
                }
            }
        }
    }
}
