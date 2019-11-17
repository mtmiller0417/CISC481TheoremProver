import java.util.ArrayList;

public class Main{

    ArrayList<Binding> bindingList;
    final int MAXITERATIONS = 300;

    public Main(){
        // Instantiate bindingList
        bindingList = new ArrayList<Binding>();
    }

    public static void main(String [] args){
        Main m = new Main();
        m.run();
    }

    // Allows me to print easier
    public void print(String str){
        System.out.println(str);
    }

    public void run(){
        // Test the binding function
        print("\nTESTING Binding()");
        ArrayList<Binding> bindList = new ArrayList<Binding>();
        bindList.add(new Binding("?x", "?y"));
        bindList.add(new Binding("?y", "?z"));
        bindList.add(new Binding("?w", "?q"));
        bindList.add(new Binding("?z", "?w"));

        String answer = binding(bindList, "?x");
        print(answer);

        print("\nTESTING incrementSymbol()");
        print(Helper.incrementSymbol("?x"));
        print(Helper.incrementSymbol("?x5"));
        print(Helper.incrementSymbol("?x11"));
        print(Helper.incrementSymbol("m"));
        print(Helper.incrementSymbol("matt2"));

        print("\nTESTING uniquify()");

        ArrayList<String[]> clause = new ArrayList<String[]>();  
        clause.add(new String[]{"p", "?x", "?y"});
        clause.add(new String[]{"a", "?x", "?z"});
        clause.add(new String[]{"a", "?z", "?y"});
        
        uniquify(clause);
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
                    continue;
                }
                // Check if you got to the end without finding one to unify with
                if(i == bindingList.size() - 1)
                    done = true;
            } 
        }   
        return currBind;
    }

    public ArrayList<String[]> uniquify(ArrayList<String[]> clause){
        ArrayList<String> variableList = new ArrayList<String>();
        for(String[] statement : clause){
            for(int i = 1; i < statement.length; i++){
                // Check if the variable is in the variable list, if so replace it with a new symbol
                if(variableList.contains(statement[i]))
                    statement[i] = Helper.incrementSymbol(statement[i]);
                else // Other wise keep it the same and add it to the variable list
                    variableList.add(statement[i]);
            }
        }
        Helper.printClause(clause);
        return clause;
    }
}

