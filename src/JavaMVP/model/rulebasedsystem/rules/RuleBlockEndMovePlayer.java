package JavaMVP.model.rulebasedsystem.rules;

import JavaMVP.model.Board;
import JavaMVP.model.GameSession;
import JavaMVP.model.Move;
import JavaMVP.model.rulebasedsystem.facts.FactsHandler;

public class RuleBlockEndMovePlayer extends Rule{
    public RuleBlockEndMovePlayer() {
    }

    @Override
    public boolean conditionRule(FactsHandler facts) {
//        return facts.factAvailable(FactValues.ENDMOVEPLAYER);
        return true;
    }

    @Override
    public boolean actionRule(FactsHandler facts, Board board, Move move, GameSession gameSession) {


        // Code to be added is this rule could initiate a new fact

        int originalFromRow = move.getFromRow();
        int originalFromCol = move.getFromCol();
        int originalToRow = move.getToRow();
        int originalToCol = move.getToCol();

        board.determineBlockEndMove(move, gameSession);

        boolean moveWasSet = !(move.getFromRow() == originalFromRow &&
                move.getFromCol() == originalFromCol &&
                move.getToRow() == originalToRow &&
                move.getToCol() == originalToCol);

        return moveWasSet;     // returns true if the new move was determined, returns false if only the facts have been modified
    }
}
