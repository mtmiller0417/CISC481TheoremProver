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

    // Controls how a 'Binding' object converts to a string
    @Override
    public String toString(){
        return "(" + this.symb1 + " " + this.symb2 + ")";
    }
}