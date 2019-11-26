import java.util.ArrayList;

public class Node{

    // Properties of Node
    boolean root;
    Node parent;
    Clause currClause;
    ArrayList<Binding> bindings;
    int depth;

    // Whats in the definition of a Node?
    public Node(Node parent, Clause currClause, ArrayList<Binding> bindings, int depth){
        // Set passedin in parameters
        this.parent = parent;
        this.currClause = currClause;
        this.bindings = bindings;
        this.depth = depth;
        // If the Node doesnt have a parent it is the root
        if(this.parent == null){
            this.root = true;
        } else {
            this.root = false;
        }

        // Initiate children arraylist
        //this.children = new ArrayList<Node>();
    }

    @Override
    public String toString(){
        return this.currClause.toString();
    }
}