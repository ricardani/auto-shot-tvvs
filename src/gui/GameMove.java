package gui;

/**
 * GameMove Class
 * <p/>
 * Eduardo Fernandes
 * Filipe Eiras
 */
class GameMove {
    public static final char MoveUp = 'u';
    public static final char MoveDown = 'd';
    public static final char MoveLeft = 'l';
    public static final char MoveRight = 'r';

    private int pieceX;
    private int pieceY;
    private char direction;

    GameMove(int x, int y, char d) throws Exception {
        boolean isValid;
        isValid = validateDirection(d);
        isValid &= validateX(x);
        isValid &= validateY(y);

        if (isValid) {
            pieceX = x;
            pieceY = y;
            direction = d;
        } else {
            throw new Exception("Invalid Move: x:" + Integer.toString(x) + " y:" + Integer.toString(y) + " direction:" + d);
        }
    }

    private boolean validateDirection(char d) {
        return d == MoveUp || d == MoveDown || d == MoveLeft || d == MoveRight;
    }

    private boolean validateX(int x) {
        return x < GameBoard.horizontalSize && x >= 0;
    }

    private boolean validateY(int y) {
        return y < GameBoard.verticalSize && y >= 0;
    }

    public int getPieceX() {
        return pieceX;
    }

    public int getPieceY() {
        return pieceY;
    }

    public char getDirection() {
        return direction;
    }

    private String getDirectionLiteral() {
        switch (direction) {
            case MoveUp:
                return "up";

            case MoveDown:
                return "down";

            case MoveLeft:
                return "left";

            case MoveRight:
                return "right";
        }
        return "INVALID DIRECTION";
    }

    @Override
    public String toString() {
        return "Move from" +
                " X: " + pieceX +
                " Y: " + pieceY +
                " to: " + getDirectionLiteral();
    }

    @Override
    public boolean equals( Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameMove gameMove = (GameMove) o;

        return !((direction != gameMove.direction) || (pieceX != gameMove.pieceX) || (pieceY != gameMove.pieceY));
    }

    @Override
    public int hashCode() {
        int result = pieceX;
        result = 31 * result + pieceY;
        result = 31 * result + (int) direction;
        return result;
    }
}
