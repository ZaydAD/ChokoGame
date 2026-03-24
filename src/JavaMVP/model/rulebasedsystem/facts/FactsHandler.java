package JavaMVP.model.rulebasedsystem.facts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class FactsHandler {

    private List<FactValues> facts = new ArrayList<>();
    private boolean factsEvolved = false;

    public FactsHandler () {
    }
    public void addFact (FactValues fact) {
        facts.add (fact);
        factsEvolved = true;
    }
    public void removeFact (FactValues fact) {
        facts.remove(fact);
        factsEvolved = true;
    }
    public boolean factsObserved () {
        return !facts.isEmpty();
    }
    public boolean factAvailable (FactValues fact) {
        return facts.contains(fact);
    }
    public void resetFacts () {
        for (Iterator<FactValues> it = facts.iterator(); it.hasNext();) {
            this.removeFact(it.next());
        }
        factsEvolved = true;
    }
    public boolean factsChanged () {
        return factsEvolved;
    }
    public void setFactsEvolved (Boolean newValue) {
        factsEvolved = newValue;
    }
}

