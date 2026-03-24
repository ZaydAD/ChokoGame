package JavaMVP.view.board;

import JavaMVP.model.*;
import JavaMVP.view.UISettings;
import JavaMVP.view.endscreen.EndScreenView;
import JavaMVP.view.endscreen.EndScreenPresenter;
import enums.PieceColor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The presenter for the Board screen in the JavaMVP application.
 *
 * This class handles all gameplay-related actions, interactions between the
 * model and view, and AI behaviors for the game session.
 */

 public class BoardScreenPresenter {
    private BoardScreenView view;
    private GameSession gameSession;
    private MVPModel model;
    private UISettings uiSettings;
    private Stage primaryStage;
    private int lastHoveredRow = -1;
    private int lastHoveredCol = -1;
    private Circle selectedPiece = null;

    /**
     * Constructor for the BoardScreenPresenter.
     *
     * @param view The view for the board screen.
     * @param model The model containing game data and logic.
     * @param gameSession The current game session instance.
     * @param uiSettings The UI settings of the application.
     * @param primaryStage The primary stage of the application.
     * @throws IllegalArgumentException if any parameter is null.
     */

    public BoardScreenPresenter(BoardScreenView view, MVPModel model, GameSession gameSession, UISettings uiSettings, Stage primaryStage) {
        if (view == null || model == null || gameSession == null || uiSettings == null || primaryStage == null) {
            throw new IllegalArgumentException("View, Model, GameSession, UISettings, and Stage must not be null");
        }

        this.view = view;
        this.gameSession = model.getGameSession(); // Use the game session from the model
        this.uiSettings = uiSettings;
        this.primaryStage = primaryStage;
        this.model = model;

        configureWindow();
        EventHandlers();
        updateView();
    }

    /**
     * Updates the view by redrawing the board.
     */
    private void updateView() {
        view.drawBoard();
    }

    /**
     * Configures all event handlers required for the board screen interactions.
     */
    private void EventHandlers() {
        Pane pieceLayer = view.getPieceLayer();

        view.getCanvas().setOnMouseMoved(this::handleMouseHover);
        view.getCanvas().setOnMouseClicked(this::handleTileClick);

        pieceLayer.getChildren().forEach(e -> {
            if (e instanceof Circle) {
                e.setOnMousePressed(this::handlePieceSelection);
            }
        });
    }

    private void goToEndScreen() {
        EndScreenView endScreenView = new EndScreenView();
        EndScreenPresenter endScreenPresenter = new EndScreenPresenter(endScreenView, model, uiSettings, model.getGameSession());
        view.getScene().setRoot(endScreenView);
    }


    private void handleMouseHover(MouseEvent event) {
        double tileSize = view.getTileSize();
        double padding = view.getBoardPadding();

        int col = (int) ((event.getX() - padding) / tileSize);
        int row = (int) ((event.getY() - padding) / tileSize);

        if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLUMNS) {
            if (row != lastHoveredRow || col != lastHoveredCol) {
                if (lastHoveredRow != -1 && lastHoveredCol != -1) {
                    view.resetTileColor(lastHoveredRow, lastHoveredCol);
                }
                view.highlightTile(row, col);
                lastHoveredRow = row;
                lastHoveredCol = col;
            }
        } else {
            if (lastHoveredRow != -1 && lastHoveredCol != -1) {
                view.resetTileColor(lastHoveredRow, lastHoveredCol);
                lastHoveredRow = -1;
                lastHoveredCol = -1;
            }
        }
    }

    /**
     * Handles the selection of game pieces when the player presses on a piece.
     *
     *
     */
    private void handlePieceSelection(MouseEvent event) {

        Circle clickedCircle = (Circle) event.getSource();
        PieceColor clickedColor = (PieceColor) clickedCircle.getUserData();
        PieceColor playerColor = model.getGameSession().getUserIsBlack() ? PieceColor.BLACK : PieceColor.WHITE;

        if (gameSession.getSecondPieceCapture() != null) {
            PieceColor pendingCaptureColor = model.getGameSession().getSecondPieceCapture();

            if (clickedColor == pendingCaptureColor) {
                return;
            }

            double tileSize = view.getTileSize();
            double padding = view.getBoardPadding();

            int removeRow = (int) ((clickedCircle.getCenterY() - padding) / tileSize);
            int removeCol = (int) ((clickedCircle.getCenterX() - padding) / tileSize);

            if (removeRow < 0 || removeRow >= Board.ROWS || removeCol < 0 || removeCol >= Board.COLUMNS) {
                return;
            }

            if (!model.getGameSession().getBoard().isTileOccupied(removeRow, removeCol)) {
                return;
            }

            model.getGameSession().getBoard().removePiece(removeRow, removeCol);
            view.removePieceFromUI(removeRow, removeCol);
            gameSession.setSecondPieceCapture(null);

            gameSession.getBoard().countRemovedPieces(pendingCaptureColor);
            view.updatePieceCounters();

            // check if the game ended after second removal
            model.getGameSession().checkEndGame();
            if (!model.getGameSession().isGameActive()) {
                goToEndScreen();
                return;
            }

            model.getGameSession().switchTurn();
            if (model.getGameSession().isCurrentPlayerComputer()) {
                handleAIMove();
            }
            return; // Important: exit after handling second capture
        } else {
            // Normal piece selection (first move)
            if (clickedColor != playerColor) {
                return;
            }

            if (selectedPiece != null) {
                selectedPiece.setStroke(null);
            }
            selectedPiece = clickedCircle;
            selectedPiece.setStroke(Color.RED);
        }
    }

    /**
     * Handles tile clicks for game moves or piece placements.
     * */
    private void handleTileClick(MouseEvent event) {
        if (selectedPiece == null) return;

        PieceColor pieceColor = (PieceColor) selectedPiece.getUserData();
        if (!isCurrentPlayersPiece(pieceColor)) return;

        double tileSize = view.getTileSize();
        double padding = view.getBoardPadding();
        int targetRow = (int) ((event.getY() - padding) / tileSize);
        int targetCol = (int) ((event.getX() - padding) / tileSize);

        if (!isValidTile(targetRow, targetCol)) return;

        boolean isOutsideBoard = isPieceOutsideBoard(selectedPiece, padding);
        if (handleForcedPlacement(pieceColor, isOutsideBoard)) return;

        if (isOutsideBoard) {
            if (handleOutsideBoardPlacement(targetRow, targetCol, pieceColor, tileSize, padding)) return;
        } else {
            handleBoardMove(pieceColor, targetRow, targetCol, tileSize, padding);
        }
    }


    /**
     * Makes the AI move when it's the computer's turn.
     */
    private void handleAIMove() {
        if (!model.getGameSession().isCurrentPlayerComputer()) return;
        Move move = model.getGameSession().getComputerAi().getMove(model.getGameSession());

        if (move == null) return;

        double tileSize = view.getTileSize();
        double padding = view.getBoardPadding();

        boolean isPlacement = move.getFromRow() == -1 && move.getFromCol() == -1;

        if (isPlacement) {
            Piece aiPiece = model.getGameSession().getBoard().findMatchingUnplacedPiece(model.getGameSession().getComputerAi().getColor());

            if (aiPiece != null) {
                Circle circle = findPieceInPieceLayer(aiPiece);
                if (circle != null && !model.getGameSession().getBoard().isTileOccupied(move.getToRow(), move.getToCol())) {
                    // Place in model
                    model.getGameSession().getBoard().placePiece(move.getToRow(), move.getToCol(), circle);
                    // Then draw
                    circle.setCenterX(padding + move.getToCol() * tileSize + tileSize / 1.2);
                    circle.setCenterY(padding + move.getToRow() * tileSize + tileSize / 2);
                }
            }
        } else {
            Circle circle = view.findPieceOnTile(move.getFromRow(), move.getFromCol());
            if (circle != null) {
                // Move the circle
                circle.setCenterX(padding + move.getToCol() * tileSize + tileSize / 1.2);
                circle.setCenterY(padding + move.getToRow() * tileSize + tileSize / 2);
            }

            if (move.isCapture()) {
                int midRow = (move.getFromRow() + move.getToRow()) / 2;
                int midCol = (move.getFromCol() + move.getToCol()) / 2;
                view.removePieceFromUI(midRow, midCol);
                gameSession.getBoard().countRemovedPieces(model.getGameSession().getComputerAi().getColor());
                handleAISecondCapture();
                view.updatePieceCounters();
            }
        }

        model.getGameSession().executeMove(move);
        model.getGameSession().switchTurn();
    }


    public void handleAISecondCapture() {
        Board board = model.getGameSession().getBoard();
        PieceColor aiColor = model.getGameSession().getComputerAi().getColor();
        List<int[]> enemyPieces = new ArrayList<>();

        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLUMNS; col++) {
                Piece piece = board.getGrid()[row][col].getPiece();
                if (piece != null && piece.getColor() != aiColor) {
                    enemyPieces.add(new int[]{row, col});
                }
            }
        }

        if (!enemyPieces.isEmpty()) {
            Random random = new Random();
            int[] target = enemyPieces.get(random.nextInt(enemyPieces.size()));
            int row = target[0];
            int col = target[1];

            model.getGameSession().getBoard().removePiece(row, col);
            view.removePieceFromUI(row, col);
            model.getGameSession().setSecondPieceCapture(null);

            model.getGameSession().getBoard().countRemovedPieces(model.getGameSession().getComputerAi().getColor());
            view.updatePieceCounters();

        }

        model.getGameSession().checkEndGame();
        if (!gameSession.isGameActive()) {
            goToEndScreen();
        }

    }

    private Circle findPieceInPieceLayer(Piece piece) {
        for (Node node : view.getPieceLayer().getChildren()) {
            if (node instanceof Circle circle) {
                if (circle.getUserData() == piece.getColor()) {
                    // Here, we assume that circles outside the board are unplaced
                    double tileSize = view.getTileSize();
                    double padding = view.getBoardPadding();
                    double x = circle.getCenterX();
                    double y = circle.getCenterY();

                    boolean isOffBoard = y < padding || y > padding + Board.ROWS * tileSize;

                    if (isOffBoard) {
                        return circle;
                    }
                }
            }
        }
        return null;
    }

    private boolean isCurrentPlayersPiece(PieceColor pieceColor) {
        if (pieceColor != model.getGameSession().getCurrentTurn()) {
            clearSelection();
            return false;
        }
        return true;
    }

    private boolean isValidTile(int row, int col) {
        return row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLUMNS;
    }

    private boolean isPieceOutsideBoard(Circle piece, double padding) {
        return piece.getCenterY() < padding || piece.getCenterY() > padding + Board.ROWS * view.getTileSize();
    }

    private boolean handleForcedPlacement(PieceColor pieceColor, boolean isOutsideBoard) {
        if (model.getGameSession().getIsForcedPlacementEnabled() == pieceColor && !isOutsideBoard) {
            clearSelection();
            return true;
        }
        return false;
    }

    private boolean handleOutsideBoardPlacement(int row, int col, PieceColor pieceColor, double tileSize, double padding) {
        if (!model.getGameSession().getBoard().isTileOccupied(row, col)) {
            model.getGameSession().getBoard().placePiece(row, col, selectedPiece);
            moveSelectedPieceTo(row, col, tileSize, padding);
            clearSelection();

            if (model.getGameSession().getIsForcedPlacementEnabled() == pieceColor) {
                model.getGameSession().setIsForcedPlacementEnabled(null);
            }

            if (model.getGameSession().isDropInitiativeEnabled() &&
                    model.getGameSession().getDropInitiativeHolder() == pieceColor) {
                model.getGameSession().setIsForcedPlacementEnabled(
                        pieceColor == PieceColor.BLACK ? PieceColor.WHITE : PieceColor.BLACK
                );
            }

            model.getGameSession().switchTurn();
            handleAIMove();
            return true;
        }
        return false;
    }

    private void handleBoardMove(PieceColor pieceColor, int targetRow, int targetCol, double tileSize, double padding) {
        int fromRow = (int) ((selectedPiece.getCenterY() - padding) / tileSize);
        int fromCol = (int) ((selectedPiece.getCenterX() - padding) / tileSize);

        if (!isValidTile(fromRow, fromCol)) return;

        boolean isCapture = (fromRow == targetRow - 2 || fromRow == targetRow + 2) ||
                (fromCol == targetCol - 2 || fromCol == targetCol + 2);

        if (model.getGameSession().getBoard().countPiecesOnBoard(pieceColor) < 2) {
            clearSelection();
            return;
        }

        Move move = new Move(fromRow, fromCol, targetRow, targetCol, isCapture);

        if (model.getGameSession().isMoveValid(move)) {
            Move executedMove = model.getGameSession().executeMove(move);
            if (executedMove != null && executedMove.isCapture()) {
                handleCapture(executedMove, pieceColor, tileSize, padding);
                return;
            }

            moveSelectedPieceTo(targetRow, targetCol, tileSize, padding);
            clearSelection();

            model.getGameSession().switchTurn();

            if (model.getGameSession().isDropInitiativeEnabled() &&
                    model.getGameSession().getDropInitiativeHolder() == pieceColor) {
                model.getGameSession().passDropInitiative();
            }

            handleAIMove();
        }
    }

    private void handleCapture(Move move, PieceColor pieceColor, double tileSize, double padding) {
        int midRow = (move.getFromRow() + move.getToRow()) / 2;
        int midCol = (move.getFromCol() + move.getToCol()) / 2;
        view.removePieceFromUI(midRow, midCol);

        moveSelectedPieceTo(move.getToRow(), move.getToCol(), tileSize, padding);
        clearSelection();

        model.getGameSession().setSecondPieceCapture(pieceColor);
        model.getGameSession().getBoard().countRemovedPieces(pieceColor);
        view.updatePieceCounters();
    }

    private void moveSelectedPieceTo(int row, int col, double tileSize, double padding) {
        selectedPiece.setCenterX(padding + col * tileSize + tileSize / 1.2);
        selectedPiece.setCenterY(padding + row * tileSize + tileSize / 2);
    }

    private void clearSelection() {
        if (selectedPiece != null) {
            selectedPiece.setStroke(null);
            selectedPiece = null;
        }
    }

    /**
     * Configures the window's display properties for the game board.
     */
    private void configureWindow() {
        double squareSize = uiSettings.getLowestRes() / 2;
        primaryStage.setX((uiSettings.getResX() - squareSize) / 2);
        primaryStage.setY((uiSettings.getResY() - squareSize) / 2);
        primaryStage.setHeight(squareSize);
        primaryStage.setWidth(squareSize);

        primaryStage.setResizable(false);
    }
}
