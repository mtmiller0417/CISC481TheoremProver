import java.util.ArrayList;

public class Predicate {

    String predicate;
    ArrayList<String> args;

    public Predicate(ArrayList<String> stringList){

        if(stringList.size() < 2){
            System.out.println("ERROR: StringList not large enough");
            System.exit(-1);
        }

        this.predicate = stringList.get(0);
        this.args = new ArrayList<String>(stringList.subList(1, stringList.size()));
        //System.out.println(this.args);
    }

    // This is how you make a copy
    public Predicate(Predicate p){
        this.predicate = new String(p.predicate);
        this.args = new ArrayList<String>(p.args);
    }

    public ArrayList<String> getAsStringList(){
        ArrayList<String> a = new ArrayList<String>(this.args);
        a.add(0, this.predicate);
        return a;
    }

    @Override
    public String toString(){
        String s = predicate;
        for(String sym : args)
            s+= " " + sym;
        return s;
    }
}