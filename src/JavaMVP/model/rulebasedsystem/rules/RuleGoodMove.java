package JavaMVP.model.rulebasedsystem.rules;

import JavaMVP.model.Board;
import JavaMVP.model.GameSession;
import JavaMVP.model.Move;
import JavaMVP.model.rulebasedsystem.facts.FactsHandler;

public class RuleGoodMove extends Rule{
    public RuleGoodMove() {
    }

    @Override
    public boolean conditionRule(FactsHandler facts) {
        return true;
    }

    @Override
    public boolean actionRule(FactsHandler facts, Board board, Move move, GameSession gameSession) {

        return board.determineGoodMove(move, gameSession); // returns true if the new move was determined, returns false if only the facts have been modified
    }

}
