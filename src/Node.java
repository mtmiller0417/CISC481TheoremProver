import java.util.ArrayList;

public class Node{

    // Properties of Node
    boolean root;
    Node parent;
    Clause currClause;
    ArrayList<Bindings> bindings;

    // Whats in the definition of a Node?
    public Node(Node parent, Clause currClause, ArrayList<Bindings> bindings){
        // Set passedin in parameters
        this.parent = parent;
        this.currClause = currClause;
        this.bindings = bindings;
        // If the Node doesnt have a parent it is the root
        if(this.parent == null){
            this.root = true;
        } else {
            this.root = false;
        }
    }

}