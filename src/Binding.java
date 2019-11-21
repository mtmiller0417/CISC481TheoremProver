import java.util.Arrays;
import java.util.ArrayList;;

public class Binding{

    String symb1, symb2;

    public Binding(String symb1, String symb2){
        this.symb1 = symb1;
        this.symb2 = symb2;
    }

    public String getBinding(String s){
        if(s.equals(this.symb1))
            return this.symb2;
        else 
            return null;
    }

    public String getBackBinding(String s){
        if(s.equals(this.symb2))
            return this.symb1;
        else 
            return null;
    }

    // Controls how a 'Binding' object converts to a string
    @Override
    public String toString(){
        return "(" + this.symb1 + " " + this.symb2 + ")";
    }

    public ArrayList<String> getAsStrArrayList(){
        return new ArrayList<String>(Arrays.asList(this.symb1, this.symb2));
    }

    // Overrides the equals method
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Binding){
            return this.symb1.equals(((Binding)obj).symb1) && this.symb2.equals(((Binding)obj).symb2);
        }
        return false;
    }
}