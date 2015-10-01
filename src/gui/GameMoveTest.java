package gui;

import org.junit.Test;

/**
 * Eduardo Fernandes
 * Filipe Eiras
 */
public class GameMoveTest {

    @Test
    public void test01validateInitialSetup() throws Exception {
        //noinspection ConstantConditions,PointlessBooleanExpression
        if (GameBoard.verticalSize < 5 || GameBoard.horizontalSize < 5) {
            throw new Exception("Board too small!");
        }

        System.out.println("Testing GameMove Class.");
        System.out.println("Board size: " + Integer.toString(GameBoard.verticalSize) + "x" + Integer.toString(GameBoard.horizontalSize));
    }

    @Test
    public void test02validMoveValuesTest() throws Exception {
        GameMove testMove;

        int testMaxX = GameBoard.horizontalSize;
        int testMaxY = GameBoard.verticalSize;

        for (int directionN = 0; directionN < 4; directionN++) {
            char direction;
            switch (directionN) {
                case 0:
                    direction = GameMove.MoveUp;
                    break;

                case 1:
                    direction = GameMove.MoveDown;
                    break;

                case 2:
                    direction = GameMove.MoveLeft;
                    break;

                case 3:
                    direction = GameMove.MoveRight;
                    break;

                default:
                    throw new Exception("There is an error with the unit tests.");
            }

            for (int x = 0; x < testMaxX; x++) {
                for (int y = 0; y < testMaxY; y++) {
                    testMove = new GameMove(x, y, direction);
                    if (!checkValues(testMove, x, y, direction)) {
                        throw new Exception("Failed at valid value test with " + Integer.toString(x) + ", " + Integer.toString(y) + ", " + direction + ".");
                    }
                }
            }
        }

        System.out.println("All possible move objects are ok.");
    }

    private boolean checkValues(GameMove testMove, int x, int y, char d) {
        int moveX = testMove.getPieceX();
        int moveY = testMove.getPieceY();
        char moveD = testMove.getDirection();

        return (moveX == x) && (moveY == y) && (d == moveD);
    }


}
