package JavaMVP.model;

import JavaMVP.model.rulebasedsystem.InferenceEngine;
import enums.PieceColor;


public class ComputerPlayer extends Player{
    private PieceColor color;

    public ComputerPlayer() {
        super();
        //...
    }

    public PieceColor getColor() {
        return color;
    }

    public void setColor(PieceColor color) {
        this.color = color;
    }


//    public Move getMove(GameSession gameSession) {
//        Board board = gameSession.getBoard();
//
//        // 1. Check if AI still has pieces not yet placed
//        List<Piece> aiPieces = (this.color == PieceColor.BLACK) ? board.getBlackPieces() : board.getWhitePieces();
//        boolean hasUnplacedPieces = false;
//        for (Piece piece : aiPieces) {
//            boolean placed = false;
//            for (int row = 0; row < Board.ROWS; row++) {
//                for (int col = 0; col < Board.COLUMNS; col++) {
//                    Piece boardPiece = board.getGrid()[row][col].getPiece();
//                    if (boardPiece == piece) {
//                        placed = true;
//                        break;
//                    }
//                }
//                if (placed) break;
//            }
//            if (!placed) {
//                hasUnplacedPieces = true;
//                break;
//            }
//        }
//
//        // 2. If AI has unplaced pieces -> find random empty tile and place
//        if (hasUnplacedPieces) {
//            for (int row = 0; row < Board.ROWS; row++) {
//                for (int col = 0; col < Board.COLUMNS; col++) {
//                    if (!board.isTileOccupied(row, col)) {
//                        Move move = new Move(-1, -1, row, col, false); // fromRow/fromCol = -1 means "from outside"
//                        return move;
//                    }
//                }
//            }
//        }
//
//        // 3. Otherwise use Inference Engine (normal gameplay)
//        InferenceEngine engine = new InferenceEngine();
//        Move move = new Move();
//        engine.determineFacts(board);
//        engine.applyRules(board, move, gameSession);
//        return move;
//    }
//

    //Original
    public Move getMove (GameSession gameSession){ // method to be completed
        InferenceEngine engine = new InferenceEngine();
        Move move = new Move();
        Board board = gameSession.getBoard();
        engine.determineFacts(board, gameSession); // added gameSession
        engine.applyRules(board, move, gameSession);

        return move;
    }
}
