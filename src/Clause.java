import java.util.ArrayList;

public class Clause {

    ArrayList<Predicate> predList;

    public Clause(){
        this.predList = new ArrayList<Predicate>();
    }

    public void addPredicate(Predicate p){
        predList.add(p);
    }

    public ArrayList<ArrayList<String>> getAsString(){
        ArrayList<ArrayList<String>> a = new ArrayList<ArrayList<String>>();
        for(Predicate p : this.predList){
            a.add(p.getAsStringList());
        }
        return a;
    }
}