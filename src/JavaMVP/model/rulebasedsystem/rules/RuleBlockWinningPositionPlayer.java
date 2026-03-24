package JavaMVP.model.rulebasedsystem.rules;

import JavaMVP.model.Board;
import JavaMVP.model.GameSession;
import JavaMVP.model.Move;
import JavaMVP.model.rulebasedsystem.facts.FactValues;
import JavaMVP.model.rulebasedsystem.facts.FactsHandler;

public class RuleBlockWinningPositionPlayer extends Rule{
    public RuleBlockWinningPositionPlayer() {
    }

    @Override
    public boolean conditionRule(FactsHandler facts) {
        return facts.factAvailable(FactValues.WINNINGPOSITIONPLAYER);
    }
    @Override
    public boolean actionRule(FactsHandler facts, Board board, Move move, GameSession gameSession) {
        // Code to be added is this rule could initiate a new fact

        if (!board.hasFinishedPlacingPieces(gameSession.getComputerAi().getColor())) {
            return false;
        }

        board.determineBlockWinningPositionMove(move, gameSession);
        return move.getFromRow() != 0 || move.getFromCol() != 0 || move.getToRow() != 0 || move.getToCol() != 0;
        // returns true if the new move was determined, returns false if only the facts have been modified
    }
}

