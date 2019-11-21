import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Inputs{

    public static Predicate getPred(List<String> list){
        return new Predicate(new ArrayList<String>(list));
    } 

    public static ArrayList<Clause> getPremises1(){
        ArrayList<Clause> clauseList = new ArrayList<Clause>();
        Clause c = new Clause();
        c.addPredicate(getPred(Arrays.asList("aunt","?x","?y")));
        c.addPredicate(getPred(Arrays.asList("sister","?x","?z")));
        c.addPredicate(getPred(Arrays.asList("mother","?z","?y")));
        clauseList.add(c);
        c = new Clause();
        c.addPredicate(getPred(Arrays.asList("aunt","?x","?y")));
        c.addPredicate(getPred(Arrays.asList("sister","?x","?z")));
        c.addPredicate(getPred(Arrays.asList("father","?z","?y")));
        clauseList.add(c);
        c = new Clause();
        c.addPredicate(getPred(Arrays.asList("sister","Mary","Sue")));
        clauseList.add(c);
        c = new Clause();
        c.addPredicate(getPred(Arrays.asList("sister","Mary","Doug")));
        clauseList.add(c);
        c = new Clause();
        c.addPredicate(getPred(Arrays.asList("father","Doug","John")));
        clauseList.add(c);
        c = new Clause();
        c.addPredicate(getPred(Arrays.asList("mother","Sue","Paul")));
        clauseList.add(c);

        /*for(Clause clause : clauseList){
            System.out.println(clause.getAsString());
        }*/

        return clauseList;
    }

    public static Clause getGoals1(){
        Clause c = new Clause();
        c.addPredicate(getPred(Arrays.asList("aunt","?x","?y")));
        return c;
    }

    public static ArrayList<Clause> getPremises2(){
        ArrayList<Clause> clauseList = new ArrayList<Clause>();
        Clause c = new Clause();
        c.addPredicate(getPred(Arrays.asList("simple_sentence","?x","?y","?z","?u","?v")));
        c.addPredicate(getPred(Arrays.asList("noun_phrase","?x","?y")));
        c.addPredicate(getPred(Arrays.asList("verb_phrase","?z","?u","?v")));
        clauseList.add(c);
        c = new Clause();
        c.addPredicate(getPred(Arrays.asList("noun_phrase","?x","?y")));
        c.addPredicate(getPred(Arrays.asList("article","?x")));
        c.addPredicate(getPred(Arrays.asList("noun","?y")));
        clauseList.add(c);
        c = new Clause();
        c.addPredicate(getPred(Arrays.asList("verb_phrase","?x","?y","?z")));
        c.addPredicate(getPred(Arrays.asList("verb","?x")));
        c.addPredicate(getPred(Arrays.asList("noun_phrase","?y","?z")));
        clauseList.add(c);
        c = new Clause();
        c.addPredicate(getPred(Arrays.asList("article","a")));
        clauseList.add(c);
        c = new Clause();
        c.addPredicate(getPred(Arrays.asList("article","the")));
        clauseList.add(c);
        c = new Clause();
        c.addPredicate(getPred(Arrays.asList("noun","man")));
        clauseList.add(c);
        c = new Clause();
        c.addPredicate(getPred(Arrays.asList("noun","dog")));
        clauseList.add(c);
        c = new Clause();
        c.addPredicate(getPred(Arrays.asList("verb","likes")));
        clauseList.add(c);
        c = new Clause();
        c.addPredicate(getPred(Arrays.asList("verb","bites")));
        clauseList.add(c);

        /*for(Clause clause : clauseList){
            System.out.println(clause.getAsString());
        }*/

        return clauseList;
    }

    public static Clause getGoals2(){
        Clause c = new Clause();
        c.addPredicate(getPred(Arrays.asList("simple_sentence","?x","?y","?z","?u","?v")));
        return c;
    }

    public static ArrayList<Clause> getPremises3(){
        ArrayList<Clause> clauseList = new ArrayList<Clause>();
        Clause c = new Clause();
        c.addPredicate(getPred(Arrays.asList("ancestor","?x","?y")));
        c.addPredicate(getPred(Arrays.asList("parent","?x","?y")));
        clauseList.add(c);
        c = new Clause();
        c.addPredicate(getPred(Arrays.asList("ancestor","?x","?y")));
        c.addPredicate(getPred(Arrays.asList("ancestor","?x","?z")));
        c.addPredicate(getPred(Arrays.asList("parent","?z","?y")));
        clauseList.add(c);
        c = new Clause();
        c.addPredicate(getPred(Arrays.asList("parent","Mary","Paul")));
        clauseList.add(c);
        c = new Clause();
        c.addPredicate(getPred(Arrays.asList("parent","Sue","Mary")));
        clauseList.add(c);


        /*for(Clause clause : clauseList){
            System.out.println(clause.getAsString());
        }*/

        return clauseList;
    }

    public static Clause getGoals3(){
        Clause c = new Clause();
        c.addPredicate(getPred(Arrays.asList("ancestor","?x","?y")));
        return c;
    }

    public static ArrayList<Clause> getPremises4(){
        ArrayList<Clause> clauseList = new ArrayList<Clause>();
        Clause c = new Clause();
        c.addPredicate(getPred(Arrays.asList("ancestor","?x","?y")));
        c.addPredicate(getPred(Arrays.asList("ancestor","?x","?z")));
        c.addPredicate(getPred(Arrays.asList("parent","?z","?y")));
        clauseList.add(c);
        c = new Clause();
        c.addPredicate(getPred(Arrays.asList("ancestor","?x","?y")));
        c.addPredicate(getPred(Arrays.asList("parent","?x","?y")));
        clauseList.add(c);
        c = new Clause();
        c.addPredicate(getPred(Arrays.asList("parent","Mary","Paul")));
        clauseList.add(c);
        c = new Clause();
        c.addPredicate(getPred(Arrays.asList("parent","Sue","Mary")));
        clauseList.add(c);

        /*for(Clause clause : clauseList){
            System.out.println(clause.getAsString());
        }*/

        return clauseList;
    }

    public static Clause getGoals4(){
        Clause c = new Clause();
        c.addPredicate(getPred(Arrays.asList("ancestor","?x","?y")));
        return c;
    }

}