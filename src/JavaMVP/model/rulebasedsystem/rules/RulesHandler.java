package JavaMVP.model.rulebasedsystem.rules;

import JavaMVP.model.Board;
import JavaMVP.model.GameSession;
import JavaMVP.model.Move;
import JavaMVP.model.rulebasedsystem.facts.FactsHandler;

import java.util.ArrayList;
import java.util.List;

public class RulesHandler {

    private List<Rule> rules = new ArrayList<>();

    /**
     * Order of the rules:
     * 1. if the AI can end the game
     *    -> the AI makes this move
     * 2. if the player can end the game in his/her next move
     *    -> the AI blocks -if possible- that potential move
     * 3. if the AI can make a move to obtain a winning position
     *    -> the AI makes this move
     * 4. if the player could obtain in his/her next move a winning position
     *    -> the AI blocks that potential move
     * 5. the AI makes a good move -eg tries to create potential winning position(s)-
     */
    public RulesHandler (){
        rules.add (0, new RuleEndMoveAI());
        rules.add (1, new RuleBlockEndMovePlayer());
        rules.add (2, new RuleWinningPositionAI());
        rules.add (3, new RuleBlockWinningPositionPlayer());
        rules.add (4, new RuleGoodMove());
    }
    public boolean checkConditionRule (int index, FactsHandler facts) {
        return rules.get(index).conditionRule(facts);
    }
    public boolean fireActionRule(int index, FactsHandler facts, Board board, Move move, GameSession gameSession) {
        return rules.get(index).actionRule(facts, board, move, gameSession);
    }

    public int numberOfRules () {
        return rules.size();
    }
}

