/**
 * This file contains various static helper functions that are to be used in Main.java
 */

import java.util.ArrayList;


public class Helper{

    // ?x => ?x1 and ?x1 => ?x2
    public static String incrementSymbol(String symbol){
        int length = symbol.length();

        // Check to see if the symbol is empty
        if(length == 0)
            return null;

        // look for index of first number
        int index = length;
        for(int i = 0; i < length; i ++){
            if(Character.isDigit(symbol.charAt(i))){
                index = i; 
                break;
            }
        }
        
        // Parse out the body and the number 
        String body = symbol.substring(0, index);
        String numStr;
        // Check if the symool had no number in it
        if(index == length)
            numStr = "0";
        else
            numStr = symbol.substring(index, length);

        // Convert string number to integer number
        int num = Integer.parseInt(numStr);

        // Combine the body and number, while incrementing the number
        return body + Integer.toString(num + 1);
    }

    public static void printClause(ArrayList<String[]> clause){
        for(String[] s : clause){
            System.out.print("( ");
            for(String str : s)
                System.out.print(str + " ");
            System.out.println(")");
        }
    }
}