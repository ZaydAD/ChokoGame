package JavaMVP.model;

import enums.GameStatus;
import enums.PieceColor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GameSession {
    
    private ComputerPlayer computer;
    private Player player;
    private Board board;
    private GameStatus gameStatus;
    private Leaderboard leaderboard;
    private boolean isGameActive;
    private PieceColor currentTurn;
    private boolean userIsBlack;
    private MVPModel model;

    //check who has drop iniative
    private boolean dropInitiativeEnabled;
    private PieceColor dropInitiativeHolder;

    private PieceColor isForcedPlacementEnabled = null;

    private PieceColor secondPieceCapture = null;

    //15/04/2025
    private long startTime;
    private long endTime;

    private final Map<PieceColor, Integer> moveCounts = new HashMap<>();
    private final Map<PieceColor, Long> totalTurnTimes = new HashMap<>();
    private long currentTurnStartTime;


    public GameSession() {
        this.board = new Board();
    }

    public GameSession(MVPModel model) {
        this.model = model;
        this.board = new Board();
        this.leaderboard = new Leaderboard(new JDBC(), this);
    }

    public void play() {
        if (this.board == null) {
            this.board = new Board(); // Reset board state
        }
        this.isGameActive = true; // Set game as active
        this.currentTurn = userIsBlack ? PieceColor.BLACK : PieceColor.WHITE; // Set current turn based on user color
        //=============================================
        this.startTime = System.currentTimeMillis(); // Start time of the game

        this.moveCounts.put(PieceColor.BLACK, 0);
        this.moveCounts.put(PieceColor.WHITE, 0);
        this.totalTurnTimes.put(PieceColor.BLACK, 0L);
        this.totalTurnTimes.put(PieceColor.WHITE, 0L);
        this.currentTurnStartTime = System.currentTimeMillis();
    }

    public void setGameData() {
        int i_currentTurn = 0;
        if (currentTurn == PieceColor.BLACK) {
            i_currentTurn = 1;
        } else if (currentTurn == PieceColor.WHITE) {
            i_currentTurn = 2;
        }
        int gameId = leaderboard.insertGame(model.getCurrentPlayer().getUsername(), i_currentTurn, isDropInitiativeEnabled(), startTime, endTime);
        leaderboard.insertTurn(gameId, getUserMoves(), getAvgTurnDuration());
    }

    public void checkEndGame() {
        if (board.getBlackPieces().isEmpty()) {
            gameStatus = userIsBlack ? GameStatus.LOST : GameStatus.WON;
            this.isGameActive = false;
            this.endTime = System.currentTimeMillis();
            if (!Objects.equals(model.getCurrentPlayer().getUsername(), "Guest") && model.getCurrentPlayer() != null) {
                leaderboard.updateWinLoss(model.getCurrentPlayer().getUsername());
                setGameData();
            }
        } else if (board.getWhitePieces().isEmpty()) {
            gameStatus = userIsBlack ? GameStatus.WON : GameStatus.LOST;
            this.isGameActive = false;
            this.endTime = System.currentTimeMillis();
            if (!Objects.equals(model.getCurrentPlayer().getUsername(), "Guest") && model.getCurrentPlayer() != null) {
                leaderboard.updateWinLoss(model.getCurrentPlayer().getUsername());
                setGameData();
            }
        }
    }

    public long getGameDuration() {
        return endTime - startTime; // Calculate game duration
    }

    public void switchTurn() {
        long now = System.currentTimeMillis();
        long turnDuration = now - currentTurnStartTime;

        // Save the player who just finished their turn
        PieceColor previousPlayer = currentTurn;

        // Switch the turn
        if (currentTurn == PieceColor.BLACK) {
            currentTurn = PieceColor.WHITE;
        } else {
            currentTurn = PieceColor.BLACK;
        }

        // Record time for the player who just finished their turn
        totalTurnTimes.put(
                previousPlayer,
                totalTurnTimes.getOrDefault(previousPlayer, 0L) + turnDuration
        );

        // Reset start time for next turn
        this.currentTurnStartTime = System.currentTimeMillis();
    }

    public boolean isGameActive() {
        return isGameActive;
    }

    public boolean isCurrentPlayerComputer() {
        if (computer == null) {
            return false;
        }
        return (userIsBlack && currentTurn == PieceColor.WHITE) ||
                (!userIsBlack && currentTurn == PieceColor.BLACK);
    }

    public boolean isMoveValid(Move move) {
        if (move.getToRow() < 0 || move.getToRow() >= Board.ROWS ||
                move.getToCol() < 0 || move.getToCol() >= Board.COLUMNS) {
            return false; // Move is out of bounds of the playingfield
        }

        if (board.isTileOccupied(move.getToRow(), move.getToCol())) {
            return false; // tile is occupied
        }

        int rowDiff = Math.abs(move.getFromRow() - move.getToRow());
        int colDiff = Math.abs(move.getFromCol() - move.getToCol());

        // Restrict normal movement to strictly one tile horizontally or vertically
        if (rowDiff + colDiff == 1) {
            return true; // Valid normal move left up right down
        }

        // Allow captures so basically jump over a piece
        if (rowDiff == 2 && colDiff == 0 || rowDiff == 0 && colDiff == 2) {
            int middleRow = (move.getFromRow() + move.getToRow()) / 2;
            int middleCol = (move.getFromCol() + move.getToCol()) / 2;

            Piece middlePiece = board.getGrid()[middleRow][middleCol].getPiece();
            Piece movingPiece = board.getGrid()[move.getFromRow()][move.getFromCol()].getPiece();

            if (middlePiece != null && movingPiece != null && middlePiece.getColor() != movingPiece.getColor()) {
                //make sure all pieces are placed before attempting captures

                return true; // Valid capture move
            }
        }

        return false; //  Invalid move if its not one step or a valid capture
    }

    public Move executeMove(Move move) {
        if (!isMoveValid(move)) return null;

        Piece movingPiece = board.getGrid()[move.getFromRow()][move.getFromCol()].getPiece();
        board.removePiece(move.getFromRow(), move.getFromCol());

        if (move.isCapture()) {
            int middleRow = (move.getFromRow() + move.getToRow()) / 2;
            int middleCol = (move.getFromCol() + move.getToCol()) / 2;
            Piece capturedPiece = board.getGrid()[middleRow][middleCol].getPiece();

            if (capturedPiece != null) {
                board.removePiece(middleRow, middleCol);
            }
        }

        board.getGrid()[move.getToRow()][move.getToCol()].setPiece(movingPiece);

        //Increment move count for this player
        PieceColor moverColor = movingPiece.getColor();
        moveCounts.put(moverColor, moveCounts.getOrDefault(moverColor, 0) + 1);

        return move;
    }

    public int getScore() {
        if (!Objects.equals(model.getCurrentPlayer().getUsername(), "Guest") && model.getCurrentPlayer() != null) {
            long gameDuration = getGameDuration();
            int score;
            if (gameDuration <= 2 * 60 * 1000) {
                score = 100;
                leaderboard.updatePlayerScore(model.getCurrentPlayer().getUsername(), score);
                return score;
            } else if (gameDuration <= 4 * 60 * 1000) {
                score = 75;
                leaderboard.updatePlayerScore(model.getCurrentPlayer().getUsername(), score);
                return score;
            } else {
                score = 50;
                leaderboard.updatePlayerScore(model.getCurrentPlayer().getUsername(), score);
                return score;
            }
        }   else return 0;
    }

    public PieceColor getUserColor() {
        return getUserIsBlack() ? PieceColor.BLACK : PieceColor.WHITE;
    }

    public int getUserMoves() {
        return getTotalMoves(getUserColor());
    }

    public double getAvgTurnDuration() {
        long userTime = getTotalTurnTime(getUserColor());
        return (double) userTime / getUserMoves() / 1000;
    }

    public String getAvgTurnDurationAsString(double userAvgTurn) {
        return String.format("%.2f", userAvgTurn);
    }


    public PieceColor getSecondPieceCapture() {
        return secondPieceCapture;
    }

    public void setSecondPieceCapture(PieceColor secondPieceCapture) {
        this.secondPieceCapture = secondPieceCapture;
    }

    public PieceColor getIsForcedPlacementEnabled() {
        return isForcedPlacementEnabled;
    }

    public void setIsForcedPlacementEnabled(PieceColor isForcedPlacementEnabled) { this.isForcedPlacementEnabled = isForcedPlacementEnabled; }

    public PieceColor getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(PieceColor currentTurn) {
        this.currentTurn = currentTurn;
    }

    public void setUserIsBlack(boolean userIsBlack) {
        this.userIsBlack = userIsBlack;
    }

    public boolean getUserIsBlack() { return userIsBlack; }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public Player getPlayer() {
        return player;
    }

    public ComputerPlayer getComputerAi() {
        return computer;
    }

    public void setPlayer(HumanPlayer player) {
        this.player = player;
    }

    public void setComputerAi(ComputerPlayer computerAi) {
        this.computer = computerAi;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int getTotalMoves(PieceColor color) {
        return moveCounts.getOrDefault(color, 0);
    }

    public long getTotalTurnTime(PieceColor color) {
        return totalTurnTimes.getOrDefault(color, 0L);
    }

    public Board getBoard () {
        return board;
    }

    public boolean isUserIsBlack() {
        return userIsBlack;
    }

    public void setDropInitiativeEnabled(boolean enabled) {
        this.dropInitiativeEnabled = enabled;
    }

    public boolean isDropInitiativeEnabled() {
        return this.dropInitiativeEnabled;
    }

    public void setDropInitiativeHolder(PieceColor color) {
        this.dropInitiativeHolder = color;
    }

    public PieceColor getDropInitiativeHolder() {
        return this.dropInitiativeHolder;
    }

    public void passDropInitiative() {
        if (dropInitiativeHolder == PieceColor.BLACK) {
            dropInitiativeHolder = PieceColor.WHITE;
        } else {
            dropInitiativeHolder = PieceColor.BLACK;
        }
    }

}