import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

public class Main{

    ArrayList<Clause> kb1 = Inputs.getPremises1();
    ArrayList<Clause> kb2 = Inputs.getPremises2();
    ArrayList<Clause> kb3 = Inputs.getPremises3();
    ArrayList<Clause> kb4 = Inputs.getPremises4();
    Clause g1 = Inputs.getGoals1();
    Clause g2 = Inputs.getGoals2();
    Clause g3 = Inputs.getGoals3();
    Clause g4 = Inputs.getGoals4();

    ArrayList<String> usedList = new ArrayList<String>();
    Map<String,Integer> usedMap = new HashMap<String,Integer>();

    final int MAXITERATIONS = 300;

    public static void main(String [] args){
        Main m = new Main();
        m.run();
    }

    public void printClauseList(ArrayList<Clause> clauseList){
        for(Clause c : clauseList)
            System.out.println(c.getAsString());
    }

    // Allows me to print easier
    public void print(String str){
        System.out.println(str);
    }

    public void run(){
        // Test the binding function
        Helper.testBinding();

        //System.exit(-1);

        // Test the incrementSymbol function
        Helper.testIncrementSymbol();

        // Test the uniquify function
        Helper.testUniquify();
        
        // Test the unify function
        Helper.testUnify();

        //testUnify();
        run_bfs(g1, kb1);
        //search(kb1, g1);
    }

    // Might not call this anymore...
    public void search(ArrayList<Clause> kb, Clause goal){
        // Uniquify kb
        uniquify(goal);
        uniquifyKB(kb);
    
        // Instantiate a queue to be used for BFS
        Queue<Clause> openQueue = new LinkedList<Clause>();
        openQueue.add(goal); // Add the goal clause to the openQueue

        // Instantiate the stack to be used for DFS
        Stack<Clause> openStack = new Stack<Clause>();
        openStack.add(goal); // Add the goal clause to the openStack

        System.out.println("\nTESTING bfs");
        //bfs(openQueue, kb);



        System.exit(-1);
        System.out.println("\nTESTING dfs");
        //dfs(openStack, kb3);
        //dfSearch(openStack, kb);
        usedList.clear(); // Make sure to clear the usedList after each example
    }

    public void run_bfs(Clause goal, ArrayList<Clause> kb){
        // Create Queue of Nodes
        Queue<Node> nodeQueue = new LinkedList<Node>();
        
        // Uniquify both the goal clause and the whole knowledge base
        uniquify(goal);
        uniquifyKB(kb);

        // Beautiful console output
        System.out.println("\n***RUNNING BFS***\n");

        // Add the goal to the openQueue

        Node root = new Node(null, goal, new ArrayList<Binding>());

        // Add the root to the queue
        nodeQueue.add(root);

        // Run while the queue is not empty
        while(!nodeQueue.isEmpty()){
            // Pop the head off the nodeQueue 
            Node node = nodeQueue.poll();

            // Generate children of Node
            for(Clause kb_clause : kb){

                // Generate a new child assuming solution isnt found
                // Use new(ArrayList<...>) to create a copy of n.bindings, so its value doesnt change
                ArrayList<Binding> localBinds = unify(node.currClause.getFirstPred(), kb_clause.getFirstPred());

                /**
                 * UPDATE unify to take an ArrayList<Binding>..
                 */
                
                System.out.println(localBinds);
                System.exit(-1);
            }
        }

        // Make sure nothing escapes :)
        System.exit(-1);
    }



    public ArrayList<Clause> uniquifyKB(ArrayList<Clause> kb){
        System.out.println("\n");
        for(Clause clause : kb)
            clause = uniquify(clause);
        
        for(Clause clause : kb)
            System.out.println(clause.getAsString());

        return kb; 
    }

    public String binding(ArrayList<Binding> bindingList, String binding){
        boolean done = false;
        String currBind = binding;
        String tmp;
        while(!done){
            for(int i = 0; i < bindingList.size(); i++){
                // Check if a matching binding is found this iteration
                tmp = bindingList.get(i).getBinding(currBind);
                if(tmp != null){
                    // If so, set it to the currBind and move to the next iteration
                    currBind = tmp;
                    break;
                }
                // Check if you got to the end without finding one to unify with
                else if(i == bindingList.size() - 1)
                    done = true;
            } 
        }   
        return currBind;
    }

    public Clause uniquify(Clause clause){
        ArrayList<Predicate> predList = clause.predList;
        ArrayList<String> localList = new ArrayList<String>();
        ArrayList<Predicate> updatePredList = new ArrayList<Predicate>();
        ArrayList<String> firstList = new ArrayList<String>();
        ///System.out.println("\n New Clause");
        for(Predicate p : predList){
            //System.out.println("\n Predicate: " + p.getAsStringList());
            for(int i = 0; i < p.args.size(); i++){
                if(p.args.get(i).charAt(0) != '?'){
                    // Not a variable, dont do anything
                    //System.out.println(p.args.get(i) + " is not a variable");
                }
                else if(localList.contains(p.args.get(i))){ 
                    // We've already accounted for this local symbol
                    //System.out.println(p.args.get(i) + " has already been accounted for in this clause");
                }
                else if(usedMap.containsKey(p.args.get(i)) && !firstList.contains(p.args.get(i))){
                    // If the symbol is in the map but not yet in this clause
                    //System.out.println(p.args.get(i) + " first time seen in this clause, already in map");
                    localList.add(p.args.get(i));
                    updatePredList.add(p);
                }
                else{
                    // This is the first time the symbol has been seen in the clause or usedMap
                    //System.out.println(p.args.get(i) + " not seeen in map or clause yet");
                    firstList.add(p.args.get(i));
                    usedMap.put(p.args.get(i), getNumFromVar(p.args.get(i)));
                }
            }
        }

        //System.out.println("Symbols to be updated this clause: " + localList);
        // Update map
        for(String var : localList){
            if(usedMap.containsKey(var)){
                int newVal = usedMap.get(var)+1;
                usedMap.put(var, newVal);
            }
        }
        for(Predicate p : predList){
            for(int i = 0; i < p.args.size(); i++){
                if(localList.contains(p.args.get(i))){
                    //System.out.println("Replace " + p.args.get(i) + " with " + replaceNum(p.args.get(i), usedMap.get(p.args.get(i))));
                    p.args.set(i, replaceNum(p.args.get(i), usedMap.get(p.args.get(i))));
                }
            }
        }

        Clause c = new Clause();
        for(Predicate p : predList)
            c.addPredicate(p);
        return c;
    }

    public String replaceNum(String symbol, int val){
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

        return body + Integer.toString(val);           
    }

    public int getNumFromVar(String var){
        if(var.charAt(0) != '?')
            return 0;
        int index = var.length();
        for(int i = 0; i < var.length(); i ++){
            if(Character.isDigit(var.charAt(i))){
                index = i; 
                break;
            }
        }
        if(index == var.length())
            return 0;
        else
            return Integer.parseInt(var.substring(index, var.length()));
    }

    // This is outdated
    public ArrayList<String[]> uniquify(ArrayList<String[]> clause){
        //ArrayList<String> variableList = new ArrayList<String>();
        for(String[] statement : clause){
            for(int i = 1; i < statement.length; i++){
                // Check if the variable is in the variable list, if so replace it with a new symbol
                if(usedList.contains(statement[i]))
                    statement[i] = Helper.incrementSymbol(statement[i]);
                else // Other wise keep it the same and add it to the variable list
                    usedList.add(statement[i]);
            }
        }
        Helper.printClause(clause);
        return clause;
    }

    /*#################################################################################################################################################################################*/
    /*#################################################################################################################################################################################*/
    /*#################################################################################################################################################################################*/
    
    public boolean addUniqueSolution(ArrayList<ArrayList<Binding>> uniqueSolutions, ArrayList<Binding> solution){
        // Add the solution if it isnt already in the list
        if(!uniqueSolutions.contains(solution)){
            uniqueSolutions.add(solution);
            return true;
        }
        return false;
    }

    public void testUnify(){
        ArrayList<Binding> answer = unify(new ArrayList<String>(Arrays.asList("parent","?x","?y")), new ArrayList<String>(Arrays.asList("parent","?x1","?y1")));
        System.out.println(answer);
    }

    // Override parameter list to handle when the bindingList is omitted
    public ArrayList<Binding> unify(ArrayList<String> x, ArrayList<String> y){
        //ArrayList<Binding> b = new ArrayList<Binding>();
        //b.add(null); // Dont think i want this
        return unify(x,y,new ArrayList<Binding>());
    }

    public ArrayList<Binding> unify(ArrayList<String> x, ArrayList<String> y, ArrayList<Binding> bindings){
        
        if(bindings == null){ // Check for failure
            //System.out.println("\nfailure, return null");
            return null;
        }
        else if(x.equals(y)){
            //System.out.println("\nx equals y");
            //System.out.println(x + " == " + y);
            return bindings;
        }
        else if(x.size() == 1 && x.get(0).charAt(0) == '?'){ // Check if x is a variable
            // x is a variable
            //System.out.println("\nx is a variable");
            //System.out.println("unifyVar("+x.get(0)+", "+y+", "+bindings);
            return unifyVar(x.get(0),y,bindings);
        }
        else if(y.size() == 1 && y.get(0).charAt(0) == '?'){ // Check if y is a variable
            // y is a variable
            //System.out.println("\ny is a variable");
            //System.out.println("unifyVar("+y.get(0)+", "+x+", "+bindings);
            return unifyVar(y.get(0),x,bindings);
        }
        else if(x.size() > 1 && y.size() > 1){ // Check if they are both lists
            // Get the first element of each of x and y
            //System.out.println("\nx and y are both lists");
            ArrayList<String> x_first =  new ArrayList<String>(Arrays.asList(x.get(0)));
            ArrayList<String> y_first =  new ArrayList<String>(Arrays.asList(y.get(0)));

            // Get all but the first element of x in an arraylist called x_rest
            ArrayList<String> x_rest = new ArrayList<String>();
            for(String str : x)
                x_rest.add(str);
            x_rest.remove(0);

            // Get all but the first element of y in an arraylist called y_rest
            ArrayList<String> y_rest = new ArrayList<String>();
            for(String str : y)
                y_rest.add(str);
            y_rest.remove(0);

            //System.out.println("x_first: " + x_first);
            //System.out.println("y_first: " + y_first);
            //System.out.println("unify("+x_rest+", "+y_rest+", unify("+x_first+", "+y_first+", "+bindings+"))");
            return unify(x_rest, y_rest, unify(x_first, y_first, bindings));
        }
        else{
            return null;
        }
    }

    public ArrayList<Binding> unifyVar(String var, ArrayList<String> x, ArrayList<Binding> bindings){
        //System.out.println(bindings);

        Binding val1 = Helper.getBindingThatStartsWith(var, bindings);
        Binding val2 = Helper.getBindingThatStartsWith(x.get(0), bindings);

        //System.out.println("bindings: "+bindings);
        //System.out.println("val1: "+val1);
        //System.out.println("val2: "+val2);
        //System.exit(-1);

        if(val1 != null){ // Check if var is already bound
            //System.out.println("\nval1 isnt null");
            //System.out.println("unify("+new ArrayList<String>(Arrays.asList(val1.symb2))+", "+x+", "+bindings);
            return unify(new ArrayList<String>(Arrays.asList(val1.symb2)), x, bindings);
        }
        else if(val2 != null){ // Check if x is bound variable
            //System.out.println("\nval2 isnt null");
            //System.out.println("unify("+new ArrayList<String>(Arrays.asList(var))+", "+new ArrayList<String>(Arrays.asList(val2.symb2))+", "+bindings);
            return unify(new ArrayList<String>(Arrays.asList(var)),new ArrayList<String>(Arrays.asList(val2.symb2)), bindings);
        }
        else{ // Add to bindings list
            //System.out.println("Add bindings");
            //System.out.println("binding before: " + bindings);

            // Create new binding
            bindings.add(new Binding(var, x.get(0)));
            return bindings;
        }
    }
}

