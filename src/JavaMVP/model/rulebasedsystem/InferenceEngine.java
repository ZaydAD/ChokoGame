package JavaMVP.model.rulebasedsystem;

import JavaMVP.model.Board;
import JavaMVP.model.GameSession;
import JavaMVP.model.Move;
import JavaMVP.model.rulebasedsystem.facts.FactValues;
import JavaMVP.model.rulebasedsystem.facts.FactsHandler;
import JavaMVP.model.rulebasedsystem.rules.RulesHandler;

public class InferenceEngine {
    private FactsHandler currentFacts;
    private RulesHandler currentRules;
    private boolean ruleFired;
    public InferenceEngine() {
        currentFacts = new FactsHandler();
        currentRules = new RulesHandler();
        ruleFired = false;
    }
    public void determineFacts (Board board, GameSession gameSession) {
        currentFacts.resetFacts();
        currentFacts.setFactsEvolved(false);
        // Determine which FactValues are currently present on the given board
        if (board.endMoveAIPossible(gameSession)) {
            currentFacts.addFact(FactValues.ENDMOVEAI);
        }
        // Test code - examples - to be removed :
        currentFacts.addFact(FactValues.WINNINGPOSITIONAI);
        currentFacts.addFact(FactValues.OTHERFACT);
    }

    /**
     *   rules are ordered - stops when a rule has been fired and starts re-evaluating the rules when the facts have been changed.
     */
    public void applyRules (Board board, Move move, GameSession gameSession) {
        if (currentFacts.factsObserved()) {
            ruleFired = false;
            int i;
            do {
                currentFacts.setFactsEvolved(false);
                i = 0;
                while (i < currentRules.numberOfRules() && !ruleFired && !currentFacts.factsChanged()) {
                    if (currentRules.checkConditionRule(i, currentFacts)) {
                        ruleFired = currentRules.fireActionRule(i, currentFacts, board, move, gameSession);
                    }
                    i++;
                }
            } while (i < currentRules.numberOfRules() && !ruleFired);
            if (!ruleFired) {
                board.determineRandomMove(move, gameSession);


            }
        } else {
            board.determineRandomMove(move, gameSession);
        }
    }

}