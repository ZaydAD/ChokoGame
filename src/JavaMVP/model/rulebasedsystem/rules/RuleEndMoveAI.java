package JavaMVP.model.rulebasedsystem.rules;

import JavaMVP.model.Board;
import JavaMVP.model.GameSession;
import JavaMVP.model.Move;
import JavaMVP.model.rulebasedsystem.facts.FactValues;
import JavaMVP.model.rulebasedsystem.facts.FactsHandler;

public class RuleEndMoveAI extends Rule{
    public RuleEndMoveAI() {
    }

    @Override
    public boolean conditionRule(FactsHandler facts) {
//        return !facts.factAvailable(FactValues.OTHERFACT)
//                || facts.factAvailable(FactValues.ENDMOVEAI);
        return true;
    }

    @Override
    public boolean actionRule(FactsHandler facts, Board board, Move move, GameSession gameSession) {
        // Code to be added is this rule could initiate a new fact
        // example:
        if (!facts.factAvailable(FactValues.OTHERFACT)) {
            facts.addFact(FactValues.OTHERFACT);
            return false; // returns true if the new move was determined, returns false if only the facts have been modified
        }

        int beforeRow = move.getFromRow();
        board.determineEndMove(move, gameSession);

        // Only return true if move was actually set
        if (move.getFromRow() != beforeRow) {
            return true;
        }

        return false; // returns true if the new move was determined, returns false if only the facts have been modified
    }
}


