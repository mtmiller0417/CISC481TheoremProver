/**
 * This file contains various static helper functions that are to be used in Main.java
 */

import java.util.ArrayList;
import java.util.Arrays;


public class Helper{

    // ?x => ?x1 and ?x1 => ?x2
    public static String incrementSymbol(String symbol){
        int length = symbol.length();

        // Check to see if the symbol is empty or not a variable
        if(length == 0 || symbol.charAt(0)!= '?')
            return symbol;

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

    public static Binding getBindingThatStartsWith(String str, ArrayList<Binding> bindings){
        if(bindings == null)
            return null;
        if(bindings.isEmpty())
            return null;

        // Why is this like this....
        for(Binding b : bindings)
            if(b.symb1.equals(str))
                return b;
        return null;
    }

    public static Binding getBindingThatEndsWith(String str, ArrayList<ArrayList<Binding>> bindings){
        if(bindings == null)
            return null;
        if(bindings.isEmpty())
            return null;
        if(bindings.get(0) == null){
            return null;
        }
        for(ArrayList<Binding> b : bindings){
            if(b.get(0).symb2.equals(str))
                return b.get(0);
        }
        return null;
    }

    public static String chaseBack(ArrayList<Binding> bindings, String initial){
        boolean done = false;
        String currBind = initial;
        String tmp;
        //System.out.println("input to chaseBack = "+initial);
        while(!done){
            for(int i = 0; i < bindings.size(); i++){
                // Check if a matching binding is found this iteration
                tmp = bindings.get(i).getBackBinding(currBind);
                if(tmp != null){
                    // If so, set it to the currBind and move to the next iteration
                    //System.out.println("tmp: "+tmp);
                    currBind = tmp;
                    break;
                }
                // Check if you got to the end without finding one to unify with
                if(i == bindings.size() - 1)
                    done = true;
            } 
        }   
        return currBind;
    }

    public static String bindingList(ArrayList<ArrayList<Binding>> bindings, String initial){
        boolean done = false;
        String currBind = initial;
        String tmp;
        while(!done){
            for(int i = 0; i < bindings.size(); i++){
                // Check if a matching binding is found this iteration
                tmp = bindings.get(i).get(0).getBinding(currBind);
                if(tmp != null){
                    // If so, set it to the currBind and move to the next iteration
                    currBind = tmp;
                    break;
                }
                // Check if you got to the end without finding one to unify with
                if(i == bindings.size() - 1)
                    done = true;
            } 
        }   
        return currBind;
    }

    // Test functions below

    public static void testBinding(){
        Main m = new Main();
        System.out.println("\nTESTING Binding()");

        ArrayList<Binding> bindList = new ArrayList<Binding>();
        bindList.add(new Binding("?x", "?y"));
        bindList.add(new Binding("?y", "?z"));
        bindList.add(new Binding("?w", "?q"));
        bindList.add(new Binding("?z", "?w"));

        String answer = m.binding(bindList, "?x");
        System.out.println(answer);

        if(!answer.equals("?q")){
            System.out.println("ERROR: test failed");
            System.exit(-1);
        }
    }

    public static void testIncrementSymbol(){
        System.out.println("\nTESTING incrementSymbol()");
        String str;
        str = incrementSymbol("?x");
        System.out.println(str);
        if(!str.equals("?x1")){
            System.out.println("ERROR: test failed");
            System.exit(-1);
        }
        str = incrementSymbol("?x5");
        System.out.println(str);
        if(!str.equals("?x6")){
            System.out.println("ERROR: test failed");
            System.exit(-1);
        }
        str = incrementSymbol("?x11");
        System.out.println(str);
        if(!str.equals("?x12")){
            System.out.println("ERROR: test failed");
            System.exit(-1);
        }
    }

    public static void testUniquify(){
        Main m = new Main();
        System.out.println("\nTESTING uniquify()");

        ArrayList<Clause> clauseList = new ArrayList<Clause>();
        Clause c = new Clause();
        c.addPredicate(new Predicate(new ArrayList<String>(Arrays.asList("aunt","?x","?y"))));

        Clause c2 = new Clause();
        c2.addPredicate(new Predicate(new ArrayList<String>(Arrays.asList("aunt","?x","?y"))));

        Clause result = m.uniquify(c);
        
        if(!result.predList.equals(c2.predList))
            System.exit(-1);
        
        Clause answer = new Clause();
        answer.addPredicate(new Predicate(new ArrayList<String>(Arrays.asList("aunt","?x","?y"))));
        m.usedMap.clear();
    }

    public static void testUnify(){
        System.out.println("\nTESTING unify()");
        Main m = new Main();

        ArrayList<Binding> b = new ArrayList<Binding>();
        b.add(new Binding("?x", "?w"));
        
        ArrayList<Binding> answer = m.unify(new ArrayList<String>(Arrays.asList("p","?x","?y")), new ArrayList<String>(Arrays.asList("p","?z","?w")), b);
        System.out.println(answer);
        
        System.out.println(m.unify(new ArrayList<String>(Arrays.asList("p","?x")), new ArrayList<String>(Arrays.asList("p","?y"))));
        ArrayList<Binding> b_list = new ArrayList<Binding>();
        b_list.add(new Binding("?x","?w"));
        b_list.add(new Binding("?w","?z"));
        b_list.add(new Binding("?y","?z"));

        if(!answer.equals(b_list)){
            System.out.println("ERROR: test failed");
            System.exit(-1);
        }
    }
}