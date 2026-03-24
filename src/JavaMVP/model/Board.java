package JavaMVP.model;

import enums.PieceColor;
import enums.TileState;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Represents the game board for the JavaMVP application.
 *
 * This class contains the logic for creating and managing the game board, handling AI and player
 * moves, and determining various game states. The board consists of a 5x5 grid, where players
 * and AI place their pieces and interact.
 *
 */
public class  Board  {
    public static final int COLUMNS = 5;
    public static final int ROWS = 5;

    private boolean finishedPlacingPieces = false;
    private List<Piece> blackPieces;
    private List<Piece> whitePieces;

    private int selectedTileColumn;
    private int selectedTileRow;

    private TileState TileState;
    private Tile[][] grid;


    /**
     * Constructor for the Board class.
     * Initializes the grid, pieces, and defaults for the game board.
     */
    public Board () {
        this.selectedTileColumn = -1;
        this.selectedTileRow = -1;
        this.createGrid();
        this.initializePieces();
    }

    public void createGrid() {
        // Create a 5x5 grid of tiles
        grid = new Tile[ROWS][COLUMNS];
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                grid[row][column] = new Tile(column, row);
            }
        }
    }

    private void initializePieces() {
        blackPieces = new ArrayList<>();
        whitePieces = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            blackPieces.add(new Piece(PieceColor.BLACK));
            whitePieces.add(new Piece(PieceColor.WHITE));
        }
    }

    /**
     * Places a piece on the board at the specified row and column.
     *
     * @param row         The row position.
     * @param col         The column position.
     * @param pieceCircle The piece representation to be placed.
     */
    public void placePiece(int row, int col, Circle pieceCircle) {
        PieceColor color = (PieceColor) pieceCircle.getUserData();

        // Find original Piece from model to not create a new one (this was causing bugs)
        Piece original = findMatchingUnplacedPiece(color);
        if (original != null) {
            grid[row][col].setPiece(original);
        }
    }

    public Piece findMatchingUnplacedPiece(PieceColor color) {
        List<Piece> pieces = (color == PieceColor.BLACK) ? blackPieces : whitePieces;
        for (Piece piece : pieces) {
            if (!isPieceOnBoard(piece)) {
                return piece; // Return the first unplaced piece found
            }
        }
        return null; // No unplaced piece found
    }

    //new method to check if a piece is on the board
    private boolean isPieceOnBoard(Piece piece) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (grid[row][col].getPiece() == piece) {
                    return true;
                }
            }
        }
        return false;
    }

    public int countPiecesOnBoard(PieceColor color) {
        int count = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Piece piece = grid[row][col].getPiece();
                if (piece != null && piece.getColor() == color) {
                    count++;
                }
            }
        }
        return count;
    }


    public void removePiece(int row, int col) {
        grid[row][col].setPiece(null);  // Remove the piece from the grid
    }

    public void countRemovedPieces(PieceColor pieceColor) {
        Random random = new Random();
        if (pieceColor == PieceColor.WHITE) {
            if (!blackPieces.isEmpty()) {
                int randomIndex = random.nextInt(blackPieces.size());
                blackPieces.remove(randomIndex);
            }
        } else if (pieceColor == PieceColor.BLACK) {
            if (!whitePieces.isEmpty()) {
                int randomIndex = random.nextInt(whitePieces.size());
                whitePieces.remove(randomIndex);
            }
        }
    }

    public boolean hasFinishedPlacingPieces(PieceColor color) {
        return countPiecesOnBoard(color) >= 12;
    }

    public void updateBoard() {
        // Update the board with the current state of the game
    }

    public boolean isTileOccupied(int row, int col) {
        return grid[row][col].getPiece() != null;
    }



    public Tile[][] getGrid() {
        return grid;
    }

    public void setGrid(Tile[][] grid) {
        this.grid = grid;
    }

    public List<Piece> getBlackPieces() {
        return blackPieces;
    }

    public List<Piece> getWhitePieces() {
        return whitePieces;
    }


    /**
     * Determines if the AI has any possible moves to end its turn by capturing player pieces.
     *
     * @param gameSession The current game session object that provides AI and game state.
     * @return True if a capturing move is possible, false otherwise.
     */
    public boolean endMoveAIPossible(GameSession gameSession) {

        if (!finishedPlacingPieces) {
            return false; // AI has finished placing pieces
        }

        PieceColor aiColor = gameSession.getComputerAi().getColor();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Piece piece = grid[row][col].getPiece();
                if (piece != null && piece.getColor() == aiColor) {
                    if (canCapture(row, col, -2, 0) || canCapture(row, col, 2, 0) ||
                            canCapture(row, col, 0, -2) || canCapture(row, col, 0, 2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * Checks if a capturing move is possible from a given tile in the specified direction.
     *
     * @param fromRow    The row of the starting piece.
     * @param fromCol    The column of the starting piece.
     * @param rowOffset  The row direction offset for the move.
     * @param colOffset  The column direction offset for the move.
     * @return True if a capture is possible, false otherwise.
     */
    private boolean canCapture(int fromRow, int fromCol, int rowOffset, int colOffset) {
        int toRow = fromRow + rowOffset;
        int toCol = fromCol + colOffset;

        if (toRow < 0 || toRow >= ROWS || toCol < 0 || toCol >= COLUMNS)
            return false;

        int midRow = fromRow + rowOffset / 2;
        int midCol = fromCol + colOffset / 2;

        Piece movingPiece = grid[fromRow][fromCol].getPiece();
        Piece middlePiece = grid[midRow][midCol].getPiece();
        Piece targetPiece = grid[toRow][toCol].getPiece();

        return middlePiece != null &&
                targetPiece == null &&
                middlePiece.getColor() != movingPiece.getColor();
    }


    /**
     * Determines a move to block a potential capturing move by the opponent.
     *
     * @param move        The move object to update with the blocking move.
     * @param gameSession The current game session object providing AI/player information.
     */
    public void determineBlockEndMove(Move move, GameSession gameSession) {
        if (!finishedPlacingPieces) {
            return;
        }

        PieceColor aiColor = gameSession.getComputerAi().getColor();
        PieceColor playerColor = (aiColor == PieceColor.BLACK) ? PieceColor.WHITE : PieceColor.BLACK;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Piece piece = grid[row][col].getPiece();
                if (piece != null && piece.getColor() == playerColor) {
                    int[][] directions = { {-2, 0}, {2, 0}, {0, -2}, {0, 2} };

                    for (int[] dir : directions) {
                        int toRow = row + dir[0];
                        int toCol = col + dir[1];

                        if (toRow < 0 || toRow >= ROWS || toCol < 0 || toCol >= COLUMNS) continue;

                        int midRow = row + dir[0] / 2;
                        int midCol = col + dir[1] / 2;

                        Piece middle = grid[midRow][midCol].getPiece();
                        Piece target = grid[toRow][toCol].getPiece();

                        if (middle != null && middle.getColor() == aiColor && target == null) {
                            // Try to block this capture by occupying the destination
                            for (int r = 0; r < ROWS; r++) {
                                for (int c = 0; c < COLUMNS; c++) {
                                    Piece aiPiece = grid[r][c].getPiece();
                                    if (aiPiece != null && aiPiece.getColor() == aiColor) {
                                        if (Math.abs(r - toRow) + Math.abs(c - toCol) == 1 && grid[toRow][toCol].getPiece() == null) { //made this to Math.abs because it gives us much better precision
                                            {
                                                move.setFromRow(r);
                                                move.setFromCol(c);
                                                move.setToRow(toRow);
                                                move.setToCol(toCol);
                                                move.setCapture(false);
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Determines a move for the AI to block the player's potential winning positions.
     *
     * This method scans the grid for any horizontal or vertical lines where the player has
     * two pieces in a row and one empty tile that could result in a winning line. If such a position
     * is found, the AI creates a move to place its piece in that empty tile to block the player
     * from winning.
     *
     * @param move        The move object to update with the AI's determined blocking position.
     * @param gameSession The current game session providing player and AI context.
     */
    public void determineBlockWinningPositionMove(Move move, GameSession gameSession) {
        PieceColor playerColor = gameSession.isUserIsBlack() ? PieceColor.BLACK : PieceColor.WHITE;
        PieceColor aiColor = gameSession.getComputerAi().getColor();

        // Check horizontal and vertical lines
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {

                // Check horizontal line
                if (col <= COLUMNS - 3) {
                    int playerCount = 0;
                    int emptyIndex = -1;

                    for (int offset = 0; offset < 3; offset++) {
                        Piece piece = grid[row][col + offset].getPiece();
                        if (piece != null && piece.getColor() == playerColor) {
                            playerCount++;
                        } else if (piece == null) {
                            emptyIndex = offset;
                        }
                    }

                    if (playerCount == 2 && emptyIndex != -1) {
                        int blockCol = col + emptyIndex;
                        move.setFromRow(-1);
                        move.setFromCol(-1);
                        move.setToRow(row);
                        move.setToCol(blockCol);
                        move.setCapture(false);
                        return;
                    }
                }

                // Check vertical line
                if (row <= ROWS - 3) {
                    int playerCount = 0;
                    int emptyIndex = -1;

                    for (int offset = 0; offset < 3; offset++) {
                        Piece piece = grid[row + offset][col].getPiece();
                        if (piece != null && piece.getColor() == playerColor) {
                            playerCount++;
                        } else if (piece == null) {
                            emptyIndex = offset;
                        }
                    }

                    if (playerCount == 2 && emptyIndex != -1) {
                        int blockRow = row + emptyIndex;
                        move.setFromRow(-1);
                        move.setFromCol(-1);
                        move.setToRow(blockRow);
                        move.setToCol(col);
                        move.setCapture(false);
                        return;
                    }
                }
            }
        }
    }


    /**
     * Determines if the AI can perform a capturing move and sets it in the provided `Move` object.
     *
     * <p>This method identifies if there is a valid capturing move on the board for the AI.
     * A capturing move occurs when the AI's piece jumps over an opponent's piece into an empty tile.
     * If such a move is found, the method updates the `Move` object with the starting and target positions
     * along with the indication that this move results in a capture.</p>
     *
     * @param move        The `Move` object to be updated with the capturing move details if a valid move is found.
     * @param gameSession The current game session that provides the AI's color and other game state information.
     */
    public void determineEndMove(Move move, GameSession gameSession) {
        if (!finishedPlacingPieces) {
            return;
        }

        PieceColor aiColor = gameSession.getComputerAi().getColor();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Piece piece = grid[row][col].getPiece();
                if (piece != null && piece.getColor() == aiColor) {
                    int[][] directions = {
                            {-2, 0}, {2, 0}, {0, -2}, {0, 2}
                    };

                    for (int[] dir : directions) {
                        int toRow = row + dir[0];
                        int toCol = col + dir[1];

                        if (toRow < 0 || toRow >= ROWS || toCol < 0 || toCol >= COLUMNS)
                            continue;

                        int midRow = row + dir[0] / 2;
                        int midCol = col + dir[1] / 2;

                        Piece midPiece = grid[midRow][midCol].getPiece();
                        Piece destPiece = grid[toRow][toCol].getPiece();

                        if (midPiece != null && midPiece.getColor() != aiColor && destPiece == null) {
                            // Set capture move!
                            move.setFromRow(row);
                            move.setFromCol(col);
                            move.setToRow(toRow);
                            move.setToCol(toCol);
                            move.setCapture(true);
                            return;
                        }
                    }
                }
            }
        }

        // If we reach here, no move was set
    }


    /**
     * Tries to determine a "good" move for the AI, where a "good" move refers to a valid non-capturing move.
     *
     * <p>The method identifies potential moves for the AI's pieces on the board that do not involve
     * capturing an opponent's piece. If a valid move is found, it updates the provided `Move` object
     * with details of the move. It uses randomization to ensure variety in the AI's moves when
     * multiple options exist.</p>
     *
     * @param move        The `Move` object to be updated with the details of the determined move if found.
     * @param gameSession The current game session providing the AI's context, such as color and game state.
     * @return `true` if a valid non-capturing move was determined and the `Move` object was updated,
     *         otherwise `false` if no valid move could be found.
     */
    public boolean determineGoodMove(Move move, GameSession gameSession) {
        if (!finishedPlacingPieces) {
            return false;
        }

        PieceColor aiColor = gameSession.getComputerAi().getColor();
        List<int[]> movablePieces = new ArrayList<>();
        Random random = new Random();

        // Collect all AI pieces on the board
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Piece piece = grid[row][col].getPiece();
                if (piece != null && piece.getColor() == aiColor) {
                    movablePieces.add(new int[]{row, col});
                }
            }
        }

        // Try to find a non-capturing move for any of them
        while (!movablePieces.isEmpty()) {
            int[] from = movablePieces.remove(random.nextInt(movablePieces.size()));
            int fromRow = from[0];
            int fromCol = from[1];

            List<int[]> options = new ArrayList<>();

            // Check adjacent tiles (non-capturing)
            if (fromRow > 0 && !isTileOccupied(fromRow - 1, fromCol))
                options.add(new int[]{fromRow - 1, fromCol});
            if (fromRow < ROWS - 1 && !isTileOccupied(fromRow + 1, fromCol))
                options.add(new int[]{fromRow + 1, fromCol});
            if (fromCol > 0 && !isTileOccupied(fromRow, fromCol - 1))
                options.add(new int[]{fromRow, fromCol - 1});
            if (fromCol < COLUMNS - 1 && !isTileOccupied(fromRow, fromCol + 1))
                options.add(new int[]{fromRow, fromCol + 1});

            if (!options.isEmpty()) {
                int[] to = options.get(random.nextInt(options.size()));

                move.setFromRow(fromRow);
                move.setFromCol(fromCol);
                move.setToRow(to[0]);
                move.setToCol(to[1]);
                move.setCapture(false);
                return true;
            }
        }

        // If no move is possible, leave move as is
        return false;
    }


    /**
     * Determines a random valid AI move, either to place a piece or move a piece already on the board.
     *
     * @param move        The move object to update with the determined move.
     * @param gameSession The current game session object providing AI information.
     */
    public void determineRandomMove(Move move, GameSession gameSession) {
        Random random = new Random();
        PieceColor aiColor = gameSession.getComputerAi().getColor();

        //we first place a piece from outside the board.
        if (!finishedPlacingPieces) {
            List<int[]> availableTiles = new ArrayList<>();

            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLUMNS; col++) {
                    if (!isTileOccupied(row, col)) {
                        availableTiles.add(new int[]{row, col});
                    }
                }
            }

            if (!availableTiles.isEmpty()) {
                int[] randomTile = availableTiles.get(random.nextInt(availableTiles.size()));
                int targetRow = randomTile[0];
                int targetCol = randomTile[1];

                move.setFromRow(-1);
                move.setFromCol(-1);
                move.setToRow(targetRow);
                move.setToCol(targetCol);
                move.setCapture(false);

                if (countPiecesOnBoard(aiColor) + 1 >= 12) {
                    finishedPlacingPieces = true;
                }
            }

            return;
        }

        //after placing all pieces on the board, the ai then begins moving pieces.
       List<int[]> aiPiecesOnBoard = new ArrayList<>();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Piece piece = grid[row][col].getPiece();
                if (piece != null && piece.getColor() == aiColor) {
                    aiPiecesOnBoard.add(new int[]{row, col});
                }
            }
        }

        if (!aiPiecesOnBoard.isEmpty()) {
            boolean moveFound = false;

            while (!moveFound && !aiPiecesOnBoard.isEmpty()) {
                int[] fromPos = aiPiecesOnBoard.get(random.nextInt(aiPiecesOnBoard.size()));
                int fromRow = fromPos[0];
                int fromCol = fromPos[1];

                List<int[]> possibleDestinations = new ArrayList<>();

                // Check UP
                if (fromRow > 0 && !isTileOccupied(fromRow - 1, fromCol)) {
                    possibleDestinations.add(new int[]{fromRow - 1, fromCol});
                }
                // Check DOWN
                if (fromRow < ROWS - 1 && !isTileOccupied(fromRow + 1, fromCol)) {
                    possibleDestinations.add(new int[]{fromRow + 1, fromCol});
                }
                // Check LEFT
                if (fromCol > 0 && !isTileOccupied(fromRow, fromCol - 1)) {
                    possibleDestinations.add(new int[]{fromRow, fromCol - 1});
                }
                // Check RIGHT
                if (fromCol < COLUMNS - 1 && !isTileOccupied(fromRow, fromCol + 1)) {
                    possibleDestinations.add(new int[]{fromRow, fromCol + 1});
                }

                if (!possibleDestinations.isEmpty()) {
                    int[] dest = possibleDestinations.get(random.nextInt(possibleDestinations.size()));
                    int toRow = dest[0];
                    int toCol = dest[1];

                    move.setFromRow(fromRow);
                    move.setFromCol(fromCol);
                    move.setToRow(toRow);
                    move.setToCol(toCol);
                    move.setCapture(false);

                    moveFound = true;
                } else {
                    aiPiecesOnBoard.remove(fromPos); // Remove the piece if no valid moves are found
                }
            }
        }
    }


    /**
     * Attempts to determine a winning move for the AI.
     *
     * <p>This method evaluates potential moves for the AI to see if a winning condition can be satisfied.
     * A "winning move" is defined as a move where the AI's piece captures an opponent's piece
     * and lands on a specific edge of the board corresponding to its color's objective zone
     * (top row for White or bottom row for Black).</p>
     *
     * @param move        The `Move` object to be updated with the details of the winning move if found.
     * @param gameSession The current game session providing the AI's context, including its color.
     * @return `true` if a winning move is determined and the `Move` object is updated, otherwise `false`.
     */
    public boolean determineWinningPositionMove(Move move, GameSession gameSession) {

        if (!finishedPlacingPieces) {
            return false;
        }
        PieceColor aiColor = gameSession.getComputerAi().getColor();

        int placedCount = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Piece piece = grid[row][col].getPiece();
                if (piece != null && piece.getColor() == aiColor) {
                    placedCount++;
                }
            }
        }

        if (placedCount < 12) {
            return false;
        }

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                Piece piece = grid[row][col].getPiece();
                if (piece != null && piece.getColor() == aiColor) {
                    int[][] directions = {
                            {-2, 0}, {2, 0}, {0, -2}, {0, 2}
                    };

                    for (int[] dir : directions) {
                        int toRow = row + dir[0];
                        int toCol = col + dir[1];

                        if (toRow < 0 || toRow >= ROWS || toCol < 0 || toCol >= COLUMNS)
                            continue;

                        int midRow = row + dir[0] / 2;
                        int midCol = col + dir[1] / 2;

                        Piece midPiece = grid[midRow][midCol].getPiece();
                        Piece destPiece = grid[toRow][toCol].getPiece();

                        boolean landingOnEdge = (aiColor == PieceColor.WHITE && toRow == 0) ||
                                (aiColor == PieceColor.BLACK && toRow == ROWS - 1);

                        if (midPiece != null && midPiece.getColor() != aiColor && destPiece == null && landingOnEdge) {
                            move.setFromRow(row);
                            move.setFromCol(col);
                            move.setToRow(toRow);
                            move.setToCol(toCol);
                            move.setCapture(true);
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }


}