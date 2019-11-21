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

        search(kb1, g1);
    }

    // Run the
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
        bfs(openQueue, kb);



        System.exit(-1);
        System.out.println("\nTESTING dfs");
        //dfs(openStack, kb3);
        //dfSearch(openStack, kb);
        usedList.clear(); // Make sure to clear the usedList after each example
    }


    public ArrayList<Clause> uniquifyKB(ArrayList<Clause> kb){
        System.out.println("\n");
        for(Clause clause : kb)
            clause = uniquify(clause);
        
        for(Clause clause : kb)
            System.out.println(clause.getAsString());

        return kb; 
    }

    /*public void dfs(Stack<Clause> openStack, ArrayList<Clause> kb){
        ArrayList<ArrayList<Binding>> empty = new ArrayList<ArrayList<Binding>>();
        empty.add(null);
        ArrayList<Binding> bindingList = new ArrayList<Binding>();

        System.out.println();

        while(!openStack.isEmpty()){
            Clause clause = openStack.pop();
            for(Clause c : kb){
                Clause newClause = new Clause();
                ArrayList<ArrayList<Binding>> bindings = new ArrayList<ArrayList<Binding>>();
                
                System.out.println("\nAttempting to unify " + clause.getAsString() + " and " + c.getAsString());

                bindings = unify(clause.predList.get(0).getAsStringList(), c.predList.get(0).getAsStringList());
                System.out.println("bindings: " + bindings);
                if(bindings != null && (bindings.equals(empty) || bindings.equals(new ArrayList<ArrayList<Binding>>()))){
                    System.out.println("Unified and got empty clause");
                    
                    // Add whats left over to newClause
                    newClause.predList.addAll(c.predList.subList(1, c.predList.size()));
                    newClause.predList.addAll(clause.predList.subList(1, clause.predList.size()));

                    // Add new clause to openList
                    openStack.add(newClause);

                    System.out.print("openStack after unification: ");
                    for(Clause cl : openStack){
                        System.out.print(cl.getAsString() + " ");
                    }
                    System.out.println();
                }
                else{
                    //System.exit(-1);
                    //bindingList.addAll(bindings.get(0));
                }
                //System.exit(-1);
            }
        }

        System.exit(-1);
    }*/

    

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
        ArrayList<ArrayList<Binding>> bindResult = new ArrayList<ArrayList<Binding>>();
        ArrayList<ArrayList<Binding>> answer = unify(new ArrayList<String>(Arrays.asList("parent","?x","?y")), new ArrayList<String>(Arrays.asList("parent","?x1","?y1")));
        System.out.println(answer);
    }

    public void bfs(Queue<Clause> openQueue,ArrayList<Clause> kb ){
        ArrayList<ArrayList<Binding>> bindingList = new ArrayList<ArrayList<Binding>>();
        final int NUMSOLUTIONS = 2;
        int index = 1;
        int num_iters = 1;
        boolean print = true;
        int solutions = 0;
        ArrayList<ArrayList<Binding>> uniqueSolutions = new ArrayList<ArrayList<Binding>>();
        while(!openQueue.isEmpty()){
            // Pop off the head of the openQueue
            Clause queueClause = openQueue.poll();
            
            // Start by saving what the bindingList is right now
            ArrayList<ArrayList<Binding>> currBindings = new ArrayList<ArrayList<Binding>>(bindingList); // Initiate it with bindingList


            // Generate all successors
            // Unify with all of the knowledge base
            if(print)
                System.out.println("\nITERATION " + index + "\n");
            for(Clause kbClause : kb){
                // Generate 'successors' for this clause
                if(print)
                    System.out.println("Unifying "+queueClause.predList.toString()+" and "+kbClause.predList.toString());
                ArrayList<ArrayList<Binding>> bindings = unify(queueClause.predList.get(0).getAsStringList(), kbClause.predList.get(0).getAsStringList(), currBindings); // Add bindingList?
                // If unification didnt fail
                if(print)
                    System.out.println("    BindingList: " + bindingList);
                if(bindings != null){
                    if(print)
                        System.out.println("    Unification succeeded");
    
                    // Add rest of each clause to the openQueue
                    Clause newClause = new Clause();
                    ArrayList<Predicate> predList1 =  new ArrayList<Predicate>();
                    ArrayList<Predicate> predList2 =  new ArrayList<Predicate>();
                    predList2.addAll(queueClause.predList.subList(1, queueClause.predList.size()));
                    predList1.addAll(kbClause.predList.subList(1, kbClause.predList.size()));
                    if(print){
                        System.out.println("    Predicate1: " + predList1.toString());
                        System.out.println("    Predicate2: " + predList2.toString());
                    }

                    // If one of the predLists is didnt have anything dont add it
                    if(!predList1.equals(new ArrayList<Predicate>()))
                        newClause.predList.addAll(predList1);
                    if(!predList2.equals(new ArrayList<Predicate>()))
                        newClause.predList.addAll(predList2);
                    
                    // If no new predicates were added, dont add empty clause(a solution was found)
                    if(newClause.predList.equals(new ArrayList<Predicate>())){
                        // Chase all bindings
                        // Hold all bindings in a simple arraylist of bindings
                        solutions++;
                        ArrayList<Binding> b_list = new ArrayList<Binding>(); 
                        for(ArrayList<Binding> bin : bindings)
                            b_list.add(bin.get(0));
                        if(print)
                            System.out.println("    b_list: "+b_list);
                        // Make a list to hold all the answers, this will be added to uniqueSolutions list
                        ArrayList<Binding> sol_bin_list = new ArrayList<Binding>();
                        // For each binding in the 'bindings'
                        int arglen = kbClause.predList.get(0).args.size();

                        for(int i = arglen; i > 0; i--){
                            Binding tmpBind = bindings.get(bindings.size()-i).get(0);
                            //System.out.println(tmpBind);
                            String chased = Helper.chaseBack(b_list, tmpBind.symb1);
                            //System.out.println(chased);
                            sol_bin_list.add(new Binding(chased, tmpBind.symb2));
                        }
                        if(addUniqueSolution(uniqueSolutions, sol_bin_list)){
                            if(print)
                                System.out.println("    Unique solution found!");
                        }
                        if(uniqueSolutions.size() == NUMSOLUTIONS){
                            System.out.println("\n"+uniqueSolutions.size()+" unique solutions found\n");
                            System.out.println(uniqueSolutions);
                            System.exit(-1);
                        }
                        //EXIT
                        /*
                        for(String str : kbClause.predList.get(0).args){
                            // Chase str back to its original binding
                            String init = str; 
                            str = Helper.chaseBack(b_list, init); // Could loop forever if circular...
                            System.out.println("    "+init+" chased back to "+str);
                            sol_bin_list.add(new Binding(str, init));
                        }
                        System.out.println("    "+sol_bin_list);
                        //System.exit(-1);
                        solutions++;
                        if(addUniqueSolution(uniqueSolutions,sol_bin_list)){
                            System.out.println("    Found a unique solution...");
                            //System.out.println(bindings);
                        }
                        //if(solutions == 2)
                        //    System.exit(-1);
                        //bindingList.addAll(bindings);
                        //solutions++;
                        // Quits when 2 unique solutions are found
                        */
                    } 
                    else{ // If you dont find a solution...
                        openQueue.add(newClause);
                    }
                } 
                else{
                    // Unification failed
                    if(print)
                        System.out.println("    Unification failed(thats fine...)");
                }
                // At the end of each iteration of the kb for loop
                if(print){
                    System.out.print("    openQueue: ");
                    for(Clause cl : openQueue)
                        System.out.print(cl.getAsString() + " ");
                    System.out.println();
                }
            }
            if(index == num_iters){
                System.out.println("Hit max num_iters");
                break;
            }
            index++;
            if(print)
                System.out.println("    Start next pass through the kb(uniqueSolutions found: " + uniqueSolutions.size()+")");
        }
        System.out.println(uniqueSolutions.size() + " solution(s) found");
        //System.out.println(uniqueSolutions);
        for(ArrayList<Binding> b: uniqueSolutions)
            System.out.println(b);
        System.exit(-1);
    }

    // Override parameter list to handle when the bindingList is omitted
    public ArrayList<ArrayList<Binding>> unify(ArrayList<String> x, ArrayList<String> y){
        ArrayList<ArrayList<Binding>> b = new ArrayList<ArrayList<Binding>>();
        //b.add(null); // Dont think i want this
        return unify(x,y,new ArrayList<ArrayList<Binding>>());
    }

    public ArrayList<ArrayList<Binding>> unify(ArrayList<String> x, ArrayList<String> y, ArrayList<ArrayList<Binding>> bindings){
        
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

    public ArrayList<ArrayList<Binding>> unifyVar(String var, ArrayList<String> x, ArrayList<ArrayList<Binding>> bindings){
        //System.out.println(bindings);

        Binding val1 = Helper.getBindingThatStartsWith(var, bindings);
        Binding val2 = Helper.getBindingThatStartsWith(x.get(0), bindings);

        //System.out.println("bindings: "+bindings);
        System.out.println("val1: "+val1);
        System.out.println("val2: "+val2);
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
            ArrayList<Binding> bin = new ArrayList<Binding>();
            bin.add(new Binding(var, x.get(0)));
            bindings.add(bin);
            return bindings;
        }
    }
}

