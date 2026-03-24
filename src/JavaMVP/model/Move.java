package JavaMVP.model;

public class Move {
    private int fromRow, fromCol;
    private int toRow, toCol;
    private boolean isCapture;

    public Move(int fromRow, int fromCol, int toRow, int toCol, boolean isCapture) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.isCapture = isCapture;
    }

    public Move() {
        this.fromRow = -1;
        this.fromCol = -1;
        this.toRow = -1;
        this.toCol = -1;
        this.isCapture = false;
    }

    public int getFromRow() { return fromRow; }
    public int getFromCol() { return fromCol; }
    public int getToRow() { return toRow; }
    public int getToCol() { return toCol; }
    public boolean isCapture() { return isCapture; }
    public void setFromRow(int fromRow) { this.fromRow = fromRow; }
    public void setFromCol(int fromCol) { this.fromCol = fromCol; }
    public void setToRow(int toRow) { this.toRow = toRow; }
    public void setToCol(int toCol) { this.toCol = toCol; }
    public void setCapture(boolean isCapture) { this.isCapture = isCapture; }
}
