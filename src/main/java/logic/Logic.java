package logic;

import java.util.Random;

public class Logic {
    private static Logic INSTANCE;


    private enum CellStatus {EMPTY, YOU, BOT}


    public static Logic getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Logic();
        return INSTANCE;
    }

    private String playerName;
    private Notation notation;
    private final Random random;
    private TurnStatus turnStatus;
    private ResultStatus resultStatus;

    private final CellStatus[][] grids;

    public Logic() {
        this.random = new Random();
        grids = new CellStatus[3][3];
        emptyGrid();
    }

    public void setResultStatus(ResultStatus resultStatus) {
        this.resultStatus = resultStatus;
    }

    private void emptyGrid() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grids[i][j] = CellStatus.EMPTY;
            }
        }
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Notation getNotation() {
        return notation;
    }

    public void startGame() {
        notation = random.nextInt() % 2 == 0 ? Notation.LINE : Notation.CIRCLE;
        turnStatus = random.nextInt() % 2 == 0 ? TurnStatus.YOU : TurnStatus.BOT;
        resultStatus = ResultStatus.GAMING;
    }

    public int step(int row, int col) {
        if (grids[row][col] == CellStatus.EMPTY) {
            grids[row][col] = CellStatus.valueOf(turnStatus.name());
            changeTurnStatus();
            resultStatus = isFinished();
            return Integer.parseInt(row + "" + col);
        }
        return -1;
    }

    public int step() {
        if (turnStatus == TurnStatus.BOT) {
            while (true) {
                int row = random.nextInt(3);
                int col = random.nextInt(3);
                if (grids[row][col] == CellStatus.EMPTY) {
                    grids[row][col] = CellStatus.valueOf(turnStatus.name());
                    changeTurnStatus();
                    resultStatus = isFinished();
                    return Integer.parseInt(row + "" + col);
                }
            }
        }
        return -1;
    }

    public TurnStatus getTurnStatus() {
        return turnStatus;
    }

    private void changeTurnStatus() {
        if (turnStatus == TurnStatus.YOU) turnStatus = TurnStatus.BOT;
        else turnStatus = TurnStatus.YOU;
    }

    public ResultStatus isFinished() {
        // Rows
        for (int i = 0; i < 3; i++) {
            if (grids[i][0] != CellStatus.EMPTY &&
                    grids[i][0] == grids[i][1] && grids[i][1] == grids[i][2]) {
                return grids[i][0] == CellStatus.YOU ? ResultStatus.WIN : ResultStatus.LOSE;
            }
        }

        // Columns
        for (int j = 0; j < 3; j++) {
            if (grids[0][j] != CellStatus.EMPTY &&
                    grids[0][j] == grids[1][j] && grids[1][j] == grids[2][j]) {
                return grids[0][j] == CellStatus.YOU ? ResultStatus.WIN : ResultStatus.LOSE;
            }
        }

        // Main diagonal
        if (grids[0][0] != CellStatus.EMPTY &&
                grids[0][0] == grids[1][1] && grids[1][1] == grids[2][2]) {
            return grids[0][0] == CellStatus.YOU ? ResultStatus.WIN : ResultStatus.LOSE;
        }

        // Secondary diagonal
        if (grids[0][2] != CellStatus.EMPTY &&
                grids[0][2] == grids[1][1] && grids[1][1] == grids[2][0]) {
            return grids[0][2] == CellStatus.YOU ? ResultStatus.WIN : ResultStatus.LOSE;
        }

        // Break or continue
        boolean isEmptyExist = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grids[i][j] == CellStatus.EMPTY) {
                    isEmptyExist = true;
                    break;
                }
            }
        }

        return isEmptyExist ? ResultStatus.GAMING : ResultStatus.NO_RESULT;
    }

    public ResultStatus getResultStatus() {
        return resultStatus;
    }

    public void restToDefault() {
        resultStatus = ResultStatus.GAMING;
        emptyGrid();
    }
}
