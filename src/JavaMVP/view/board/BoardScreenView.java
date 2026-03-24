package JavaMVP.view.board;

import JavaMVP.model.Board;
import enums.PieceColor;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class BoardScreenView extends BorderPane {
    private static final Color TILE_COLOR = Color.web("#3e8dc2");
    private static final Color GRID_COLOR = Color.LIGHTGRAY;
    private static final Color BOARD_BORDER_COLOR = Color.web("#ed2846");
    private static final Color OUTER_BORDER_COLOR = Color.web("#38a659");
    private static final Color HOVER_COLOR = Color.web("#62aef5");
    private static final Color BLACK_PIECE_COLOR = Color.BLACK;
    private static final Color WHITE_PIECE_COLOR = Color.WHITE;

    private Canvas canvas;
    private Pane pieceLayer;
    private double boardSize;
    private double innerPadding;
    private double adjustedTileSize;
    private Board board;

    private VBox blueBorder;
    private Label blackCountTextLabel;
    private Label blackCountValueLabel;
    private Label whiteCountTextLabel;
    private Label whiteCountValueLabel;


    public BoardScreenView(Board board, double windowSize) {
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null!");
        }

        this.board = board;
        this.boardSize = windowSize * 0.8;
        this.initialiseNodes();
        this.layoutNodes();
        this.drawBoard();
        this.placePieces();
    }

    private void layoutNodes() {
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(canvas, pieceLayer);
        this.setCenter(stackPane);

    }

    private void initialiseNodes() {
        this.canvas = new Canvas(boardSize, boardSize);
        this.pieceLayer = new Pane();
        this.pieceLayer.setPrefSize(boardSize, boardSize);
        this.pieceLayer.setPickOnBounds(false);

        blackCountTextLabel = new Label("Black:");
        blackCountTextLabel.setTextFill(Color.BLACK);
        blackCountTextLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        blackCountValueLabel = new Label("12");
        blackCountValueLabel.setTextFill(Color.BLACK);
        blackCountValueLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        whiteCountTextLabel = new Label("White:");
        whiteCountTextLabel.setTextFill(Color.WHITE);
        whiteCountTextLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        whiteCountValueLabel = new Label("12");
        whiteCountValueLabel.setTextFill(Color.WHITE);
        whiteCountValueLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Group labels
        HBox blackBox = new HBox(5, blackCountTextLabel, blackCountValueLabel);
        HBox whiteBox = new HBox(5, whiteCountTextLabel, whiteCountValueLabel);

        HBox counterBox = new HBox(40, blackBox, whiteBox);
        counterBox.setAlignment(Pos.CENTER);
        counterBox.setStyle("-fx-padding: 10px;");

        // Green bottom bar
        blueBorder = new VBox(counterBox);
        blueBorder.setStyle("-fx-background-color: #38a659;");
        blueBorder.setAlignment(Pos.CENTER);

        this.setBottom(blueBorder);

    }

    public void updatePieceCounters() {
        blackCountValueLabel.setText(String.valueOf(board.getBlackPieces().size()));
        whiteCountValueLabel.setText(String.valueOf(board.getWhitePieces().size()));
    }


    public void drawBoard() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(OUTER_BORDER_COLOR);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        double borderPadding = boardSize * 0.2;
        double boardInnerSize = boardSize - 2 * borderPadding;
        double arcSize = boardInnerSize * 0.15;

        gc.setFill(BOARD_BORDER_COLOR);
        gc.fillRoundRect(borderPadding, borderPadding, boardInnerSize, boardInnerSize, arcSize, arcSize);

        this.innerPadding = borderPadding * 1.2;
        double innerBoardSize = boardSize - 2 * innerPadding;
        double innerArcSize = innerBoardSize * 0.12;

        gc.setFill(Color.LIGHTGRAY);
        gc.fillRoundRect(innerPadding, innerPadding, innerBoardSize, innerBoardSize, innerArcSize, innerArcSize);

        this.adjustedTileSize = innerBoardSize / Board.ROWS;
        drawTiles(gc);

        this.layout();
    }

    private void drawTiles(GraphicsContext gc) {
        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLUMNS; col++) {
                drawTile(gc, row, col, TILE_COLOR);
            }
        }
    }

    public void placePieces() {
        pieceLayer.getChildren().clear();
        double pieceRadius = adjustedTileSize * 0.35;

        for (int i = 0; i < board.getBlackPieces().size(); i++) {
            double x = innerPadding + (i % Board.COLUMNS) * adjustedTileSize + adjustedTileSize / 1.2;
            double y = innerPadding - adjustedTileSize;
            Circle pieceCircle = createPiece(x, y, pieceRadius, BLACK_PIECE_COLOR, PieceColor.BLACK);
            pieceLayer.getChildren().add(pieceCircle);
        }

        for (int i = 0; i < board.getWhitePieces().size(); i++) {
            double x = innerPadding + (i % Board.COLUMNS) * adjustedTileSize + adjustedTileSize / 1.2;
            double y = innerPadding + Board.ROWS * adjustedTileSize + 120;
            Circle pieceCircle = createPiece(x, y, pieceRadius, WHITE_PIECE_COLOR, PieceColor.WHITE);
            pieceLayer.getChildren().add(pieceCircle);
        }
        updatePieceCounters();
    }

    public Circle findPieceOnTile(int row, int col) {
        double tileSize = getTileSize();
        double padding = getBoardPadding();

        double expectedX = padding + col * tileSize + tileSize / 1.2;
        double expectedY = padding + row * tileSize + tileSize / 2;

        for (Node node : pieceLayer.getChildren()) {
            if (node instanceof Circle circle) {
                double cx = circle.getCenterX();
                double cy = circle.getCenterY();

                // Allow a little tolerance (because of pixel rounding)
                if (cx == expectedX && cy == expectedY) {
                    return circle;
                }
            }
        }
        return null;
    }


    private Circle createPiece(double x, double y, double radius, Color color, PieceColor pieceColor) {
        Circle piece = new Circle(radius);
        piece.setFill(color);
        piece.setCenterX(x);
        piece.setCenterY(y);
        piece.setUserData(pieceColor);
        return piece;
    }

    public void removePieceFromUI(int row, int col) {
        double tileSize = getTileSize();
        double padding = getBoardPadding();

        double expectedX = padding + col * tileSize + tileSize / 1.2;
        double expectedY = padding + row * tileSize + tileSize / 2;

        Circle pieceToRemove = null;

        for (Node node : pieceLayer.getChildren()) {
            if (node instanceof Circle) {
                Circle piece = (Circle) node;
                if (piece.getCenterX() == expectedX && piece.getCenterY() == expectedY) {
                    pieceToRemove = piece;
                    break;
                }
            }
        }

        if (pieceToRemove != null) {
            pieceLayer.getChildren().remove(pieceToRemove);
        }
    }

    public void highlightTile(int row, int col) {
        drawTile(canvas.getGraphicsContext2D(), row, col, HOVER_COLOR);
    }

    public void resetTileColor(int row, int col) {
        drawTile(canvas.getGraphicsContext2D(), row, col, TILE_COLOR);
    }

    private void drawTile(GraphicsContext gc, int row, int col, Color color) {
        double x = innerPadding + col * adjustedTileSize;
        double y = innerPadding + row * adjustedTileSize;
        gc.setFill(color);
        gc.fillRect(x, y, adjustedTileSize, adjustedTileSize);

        gc.setStroke(GRID_COLOR);
        gc.setLineWidth(3);
        gc.strokeRect(x, y, adjustedTileSize, adjustedTileSize);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public double getTileSize() {
        return adjustedTileSize;
    }

    public Pane getPieceLayer() {
        return pieceLayer;
    }

    public double getBoardPadding() {
        return innerPadding;
    }
}
