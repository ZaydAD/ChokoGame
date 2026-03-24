package JavaMVP.model;

public class Tile {
    private int x, y;
    private Piece piece; // Holds a piece on this tile

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.piece = null; // Initially no piece on the tile
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}
