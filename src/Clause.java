import java.util.ArrayList;

public class Clause {

    ArrayList<Predicate> predList;

    // This essentially creats this clause as a copy of another clause
    public Clause(Clause c){
        this.predList = new ArrayList<Predicate>(c.predList);
    }

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

    public ArrayList<String> getFirstPred(){
        return predList.get(0).getAsStringList();
    }

    @Override
    public String toString(){
        return this.predList.toString();
    }
}