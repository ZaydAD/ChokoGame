package JavaMVP.model.rulebasedsystem.rules;

import JavaMVP.model.Board;
import JavaMVP.model.GameSession;
import JavaMVP.model.Move;
import JavaMVP.model.rulebasedsystem.facts.FactValues;
import JavaMVP.model.rulebasedsystem.facts.FactsHandler;

public class RuleWinningPositionAI extends Rule{
    public RuleWinningPositionAI() {
    }

    @Override
    public boolean conditionRule(FactsHandler facts) {
        return facts.factAvailable(FactValues.WINNINGPOSITIONAI);
    }

    @Override
    public boolean actionRule(FactsHandler facts, Board board, Move move, GameSession gameSession) {
        // Code to be added is this rule could initiate a new fact

        return board.determineWinningPositionMove(move, gameSession);
             // returns true if the new move was determined, returns false if only the facts have been modified
    }
}

