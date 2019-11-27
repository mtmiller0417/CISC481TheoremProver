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

    final int MAX_DEPTH = 300;

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
        //run_bfs(g2, kb2);

        run_dfs(g2, kb2);
    }

    public void run_bfs(Clause goal, ArrayList<Clause> kb){
        // Save the goalClause in a variable to be used later
        Clause goalClause = new Clause(goal);

        // Create Queue of Nodes
        Queue<Node> nodeQueue = new LinkedList<Node>();

        // Uniquify both the goal clause and the whole knowledge base
        //uniquify(goal);
        //uniquifyKB(kb);

        // Beautiful console output
        System.out.println("\n***RUNNING BFS***");

        // Create root node and add it to the queue to get it started
        Node root = new Node(null, goal, new ArrayList<Binding>(), 1);
        nodeQueue.add(root);

        ArrayList<ArrayList<Binding>> uniqueSolutions = new ArrayList<ArrayList<Binding>>();

        // Variables to be used
        final int MAX_ITERS = 300;
        final int NUM_SOLS = -1;
        boolean print = false;
        int iteration = 1;


        // Run while the queue is not empty
        while(!nodeQueue.isEmpty()){
            // Pop the head off the nodeQueue 
            Node node = nodeQueue.poll();
            
            if(node.depth > MAX_DEPTH)
                continue;

            if(print)
                System.out.println("\nITERATION " + iteration + " nodeClause="+node.currClause);
            //System.out.println()

            // Generate children of Node
            for(Clause kb_clause : kb){

                // Generate a new child assuming solution isnt found
                if(print){
                    System.out.println("\nAttempting to unify " + node.currClause + " + " + kb_clause);
                }
                // Use new(ArrayList<Binding>(...)) to create a copy of node.bindings, so its value doesnt change
                uniquify(kb_clause);
                System.out.println("    KB:"+kb);
                ArrayList<Binding> localBinds = unify(node.currClause.getFirstPred(), kb_clause.getFirstPred(), new ArrayList<Binding>(node.bindings));

                if(print)
                    System.out.println("    Bindings: "+localBinds);

                if(localBinds != null){
                    // Unify was successfull
                    if(print)
                        System.out.println("    Unification was SUCCESSFUL");

                    // Create new clause with predicates left over from both clauses from unify
                    Clause tmpClause = new Clause();

                    // Fill clause with left over predicate
                    ArrayList<Predicate> p1 = new ArrayList<Predicate>(node.currClause.predList.subList(1, node.currClause.predList.size()));
                    ArrayList<Predicate> p2 = new ArrayList<Predicate>(kb_clause.predList.subList(1, kb_clause.predList.size()));

                    // Order of these two 'ifs' matters, which goes first? the kb_clause? I think this is right
                    if(!p2.isEmpty())
                        tmpClause.predList.addAll(p2);
                    if(!p1.isEmpty())
                        tmpClause.predList.addAll(p1);

                    // Create a list of bindings(there are a possibly unique solution)
                    ArrayList<Binding> sol = new ArrayList<>();

                    // If both are empty, then the empty clause is created
                    if(p1.isEmpty() && p2.isEmpty()){
                        // A solution is found
                        if(print)
                            System.out.println("    We found a solution");
                        //System.out.println("    currClause: " + node.currClause.getFirstPred());
                        for(Predicate p : goalClause.predList){
                            //System.out.println(p.args);
                            for(String arg : p.args){
                                String output = binding(localBinds, arg);
                                //System.out.println(arg+"/"+output);
                                sol.add(new Binding(arg, output));
                            }
                        }

                        // If this is a unique solution, it adds it to 'uniqueSolutions' and returns true
                        if(addUniqueSolution(uniqueSolutions, sol)){
                            System.out.println("    Unique solution added");
                            for(ArrayList<Binding> b : uniqueSolutions)
                                System.out.println("    " + b);
                        }

                        if(uniqueSolutions.size() == NUM_SOLS){
                            System.out.println(NUM_SOLS+" found, exiting");
                            System.exit(-1);
                        }
                    }
                    else{
                        // Only add the node if it is not a terminal state(ie a solution)
                        if(print)
                            System.out.println("    add node to nodeQueue...");
                        nodeQueue.add(new Node(node, tmpClause, localBinds, node.depth+1));
                    }
                } 
                else {
                    if(print)
                        System.out.println("    Unify was not successful, not necessarily a problem");
                }
                if(print)
                    System.out.println("    nodeQueue: " + nodeQueue);
            } // End of for loop through KB

            if(iteration == MAX_ITERS){
                System.out.println("Terminating after " + MAX_ITERS + " iterations");
                System.exit(-1);
            }
            iteration++;
        }
        if(nodeQueue.isEmpty()){
            System.out.println("nodeQueue is empty");
        }
        // Make sure nothing escapes :)
        System.exit(-1);
        // Make sure to clear the usedList after each example
        usedList.clear(); 
    }

    public void run_dfs(Clause goal, ArrayList<Clause> kb){
        // Save the goalClause in a variable to be used later
        Clause goalClause = new Clause(goal);

        // Create Queue of Nodes
        Stack<Node> nodeStack = new Stack<Node>();
        // Uniquify both the goal clause and the whole knowledge base
        goal = uniquify2(new Clause(goal));
        //uniquifyKB(kb);

        // Beautiful console output
        System.out.println("\n***RUNNING DFS***");

        // Create root node and add it to the queue to get it started
        Node root = new Node(null, goal, new ArrayList<Binding>(),1);
        nodeStack.push(root);

        ArrayList<ArrayList<Binding>> uniqueSolutions = new ArrayList<ArrayList<Binding>>();

        // Variables to be used
        final int MAX_ITERS = 300; // 300
        final int NUM_SOLS = -1;
        boolean print = false;
        int iter = -1;

        // Run while the queue is not empty
        while(!nodeStack.isEmpty()){
            // Pop the head off the nodeStack 
            Node node = nodeStack.pop();
            
            // If this node is past the max depth continue to the next node
            if(node.depth > MAX_DEPTH)
                continue;

            if(print)
                System.out.println("\nITERATION " + iter + " nodeClause="+node.currClause);

            // Generate children of Node
            for(Clause kb_clause : kb){

                Clause kb_clause_copy = uniquify2(new Clause(kb_clause));
                
                // Generate a new child assuming solution isnt found
                if(print){
                    System.out.println("\nAttempting to unify " + node.currClause + " + " + kb_clause_copy);
                    System.out.println("    Bindings before: " + node.bindings);
                }
                // Use new(ArrayList<Binding>(...)) to create a copy of node.bindings, so its value doesnt change
                ArrayList<Binding> localBinds = unify(node.currClause.getFirstPred(), kb_clause_copy.getFirstPred(), new ArrayList<Binding>(node.bindings));

                if(print)
                    System.out.println("    Bindings: "+localBinds);

                if(localBinds != null){
                    // Unify was successfull
                    if(print)
                        System.out.println("    Unification was SUCCESSFUL");

                    // Create new clause with predicates left over from both clauses from unify
                    Clause tmpClause = new Clause();

                    // Fill clause with left over predicate
                    ArrayList<Predicate> p1 = new ArrayList<Predicate>(node.currClause.predList.subList(1, node.currClause.predList.size()));
                    ArrayList<Predicate> p2 = new ArrayList<Predicate>(kb_clause_copy.predList.subList(1, kb_clause_copy.predList.size()));

                    // Order of these two 'ifs' matters, which goes first? the kb_clause? I think this is right
                    if(!p2.isEmpty())
                        tmpClause.predList.addAll(p2);
                    if(!p1.isEmpty())
                        tmpClause.predList.addAll(p1);

                    // Create a list of bindings(there are a possibly unique solution)
                    ArrayList<Binding> sol = new ArrayList<>();

                    // If both are empty, then the empty clause is created
                    if(p1.isEmpty() && p2.isEmpty()){
                        // A solution is found
                        if(print)
                            System.out.println("    We found a solution");
                        for(Predicate p : goalClause.predList){
                            for(String arg : p.args){
                                String output = binding(localBinds, arg);
                                sol.add(new Binding(arg, output));
                            }
                        }

                        // If this is a unique solution, it adds it to 'uniqueSolutions' and returns true
                        if(addUniqueSolution(uniqueSolutions, sol)){
                            
                            /*System.out.println("Unique solution added: " + sol);
                            System.out.println("Bindings: " + localBinds);
                            System.out.println("Goal Clause: " + goalClause);
                            System.out.println("Unique Solutions:");*/
                            
                            if(print){
                                for(ArrayList<Binding> b : uniqueSolutions)
                                    System.out.println("    "+b);
                            }
                            //System.exit(-1);
                        }

                        if(uniqueSolutions.size() == NUM_SOLS){
                            System.out.println(NUM_SOLS+" found, exiting");
                            for(ArrayList<Binding> b : uniqueSolutions)
                                    System.out.println("    " + b);
                            System.exit(-1);
                        }
                    }
                    else{
                        // Only add the node if it is not a terminal state(ie a solution)
                        if(print)
                            System.out.println("    add node to nodeStack...");
                            nodeStack.push(new Node(node, tmpClause, localBinds, node.depth+1));
                    }
                } 
                else {
                    if(print)
                        System.out.println("    Unify was not successful, not necessarily a problem");
                }
                if(print)
                    System.out.println("    nodeStack: " + nodeStack);
            } // End of for loop through KB

            /*if(iter == MAX_ITERS){
                System.out.println("MAX_ITERS reached...");
                System.exit(-1);
            }
            iter++;*/
        }

        System.out.println("Solutions ("+ uniqueSolutions.size() +")");
        for(ArrayList<Binding> b : uniqueSolutions)
            System.out.println(b);
            
        // Make sure to clear the usedList after each example
        usedList.clear(); 
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

    // Make sure this uniquify works right  
    public Clause uniquify2(Clause clause){

        //System.out.println("uniquify2 called on " + clause);

        // Initiate both arraylists needed for this 
        ArrayList<String> used_vars = new ArrayList<String>();
        ArrayList<String> new_vars = new ArrayList<String>();
        
        boolean print = false;

        if(print){
            System.out.println("usedMap: " + usedMap);
            System.out.println("Clause: " + clause);
        }

        // Sort variables into either used_vars or new_vars
        for(Predicate p : clause.predList){
            for(String var : p.args){
                // If the var is a constant and not a var, dont do anything and skip to next
                if(var.charAt(0) != '?')
                    continue;

                // Strip the number from var 
                var = getVar(var);

                if(!usedMap.containsKey(var)){
                    // If it hasnt been used, add it to the new_vars
                    if(!new_vars.contains(var)){
                        new_vars.add(var);
                        if(print)
                            System.out.println(var + " added to new_vars");
                    }
                } else {
                    // If the variable was already in the hashmap
                    if(!used_vars.contains(var)){
                        used_vars.add(var);
                        if(print)
                            System.out.println(var + " added to used_vars");
                    }
                }
            }
        }
        // Add new_vars to the usedMap now after iterating
        for(String var : new_vars)
            usedMap.put(var, 0);

        if(print)
            System.out.println("usedMap: " + usedMap);

        // Update the value of each key in the used_vars list
        for(String var : used_vars){
            // Update the map for each of these as well as update them in the clause to be returned
            usedMap.put(var, usedMap.get(var)+1);
        }

        // Loop through clause and update all variables that overlap with the used_list
        for(Predicate p : clause.predList){
            for(int i = 0; i < p.args.size(); i++){
                String var = this.getVar(p.args.get(i));
                if(used_vars.contains(var)){
                    // Create new variable with correct number
                    int num = usedMap.get(var);
                    String v = p.args.get(i);
                    p.args.set(i, replaceNum(v, num));
                }
            }
        }

        if(print){
            System.out.println("usedMap: " + usedMap);
            System.out.println("clause: " + clause);
        }

        Clause c = new Clause();
        for(Predicate p : clause.predList)
            c.addPredicate(p);

        return c;
    }

    public Clause uniquify(Clause clause){
        ArrayList<Predicate> predList = clause.predList;
        // 
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
                    System.out.println("Replace " + p.args.get(i) + " with " + replaceNum(p.args.get(i), usedMap.get(p.args.get(i))));
                    p.args.set(i, replaceNum(p.args.get(i), usedMap.get(p.args.get(i))));
                }
            }
        }

        Clause c = new Clause();
        for(Predicate p : predList)
            c.addPredicate(p);
        return c;
    }

    public String getVar(String var){
        if(var.charAt(0) != '?')
            return null;

        int length = var.length();

        int index = length;
        for(int i = 0; i < length; i ++){
            if(Character.isDigit(var.charAt(i))){
                index = i; 
                break;
            }
        }
        
        return var.substring(0, index);
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

    /*#################################################################################################################################################################################*/
    /*#################################################################################################################################################################################*/
    /*#################################################################################################################################################################################*/
    
    public boolean addUniqueSolution(ArrayList<ArrayList<Binding>> uniqueSolutions, ArrayList<Binding> solution){
        // Add the solution if it isnt already in the list
        if(!uniqueSolutions.contains(solution)){
            uniqueSolutions.add(0,solution);
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
            //System.out.println("failure, return null | x: "+x+", y:"+y);
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
            //System.out.println("NULL x: "+x+", y:"+y);
            return null;
        }
    }

    public ArrayList<Binding> unifyVar(String var, ArrayList<String> x, ArrayList<Binding> bindings){
        //System.out.println(bindings);

        Binding val1 = Helper.getBindingThatStartsWith(var, bindings);
        Binding val2 = Helper.getBindingThatStartsWith(x.get(0), bindings);

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

