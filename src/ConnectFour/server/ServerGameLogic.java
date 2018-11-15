/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConnectFour.server;

import ConnectFour.server.PlayerStore.Player;
import ConnectFour.server.PlayerStore.PlayerStore;
import java.util.List;

/**
 *
 * @author tmst
 */
public class ServerGameLogic {

    private GameServer gameServer;
    private PlayerStore playerStore = new PlayerStore();
    private int currentPlayerNumber = 0;
    private Player currentPlayer;

    private int numberOfColumns = 7;
    private int numberOfRows = 7;
    private String[][] playingField = new String[numberOfColumns][7];
    private int numberOfChipsInColumn[] = {0, 0, 0, 0, 0, 0, 0};
    private boolean gameStarted;
    private int numberOfMarks;
    private String gameResult;

    public ServerGameLogic(GameServer pGameServer) {
        gameServer = pGameServer;
    }

    /**
     * Add a new player to the game.
     *
     * @param pName
     * @param pIpAddress
     * @param pPort
     * @return The player.
     */
    public Player addPlayer(String pName, String pIpAddress, int pPort) {
        Player p = playerStore.addPlayer(pName, pName, pPort);
        return p;
    }

    public PlayerStore getPlayerStore() {
        return playerStore;
    }

    public Player getActivePlayer() {
        return currentPlayer;
    }

    public List<Player> getInactivePlayers() {
        return playerStore.getAllExcept(currentPlayer);
    }

    /**
     * Checks if the game has ended and then returns the winner
     *
     * @return Boolean indicating whether the game has ended.
     */
    public boolean hasGameEnded() {
        boolean ended = false;

        for (int row = 0; row < 7; row++) {
            for (int position = 0; position < 4; position++) {

                String mark1 = playingField[position][row];
                String mark2 = playingField[position + 1][row];
                String mark3 = playingField[position + 2][row];
                String mark4 = playingField[position + 3][row];

                if (mark1.equals(mark2) && mark2.equals(mark3) && mark3.equals(mark4)) {
                    ended = true;
                    break;
                }
            }
        }

        if (ended == false) {
            for (int column = 0; column < 7; column++) {
                for (int position = 0; position < 4; position++) {

                    String mark1 = playingField[column][position];
                    String mark2 = playingField[column][position + 1];
                    String mark3 = playingField[column][position + 2];
                    String mark4 = playingField[column][position + 3];

                    if (mark1.equals(mark2) && mark2.equals(mark3) && mark3.equals(mark4)) {
                        ended = true;
                        break;
                    }
                }
            }
        }

        if (ended == false) {
            for (int column = 0; column < 7; column++) {
                for (int row = 0; row < 7; row++) {
                    if (column + 3 < 7 && row + 3 < 7) {
                        String mark1 = playingField[column][row];
                        String mark2 = playingField[column + 1][row + 1];
                        String mark3 = playingField[column + 2][row + 2];
                        String mark4 = playingField[column + 3][row + 3];

                        if (mark1.equals(mark2) && mark2.equals(mark3) && mark3.equals(mark4)) {
                            ended = true;
                            break;
                        }
                    }
                }
            }

        }

        if (ended == false) {
            for (int column = 6; column >= 0; column--) {
                for (int row = 6; row >= 0; row--) {
                    if (column - 3 >= 0 && row - 3 >= 0) {
                        String mark1 = playingField[column][row];
                        String mark2 = playingField[column - 1][row - 1];
                        String mark3 = playingField[column - 2][row - 2];
                        String mark4 = playingField[column - 3][row - 3];

                        if (mark1.equals(mark2) && mark2.equals(mark3) && mark3.equals(mark4)) {
                            ended = true;
                            break;
                        }
                    }
                }
            }

        }

        return ended;
    }

    /**
     * Sets a mark on a specific position on the playing field. This mark is
     * stored in the string array with a specific character for each player.
     *
     * @param pPlayerNumber The number of the player for whom the mark should be
     * set. The number 0 is for player one and the number 1 is for player two.
     * @param pColumn The column where the mark should be set.
     * @param pRow The row where the mark should be set.
     */
    public void setMark(String playerId, int pColumn, int pRow) {
        if (playingField[pColumn][pRow].equals(" ")) {

            // Using a seperate marking character for each player.
            playingField[pColumn][pRow] = playerId;

            numberOfMarks++;
        } else {
            System.err.println("Fehler bei Zeichensetzen: Feld schon markiert");
        }

    }

    /**
     * Puts a chip on the top position in a column.
     *
     * @param pPlayerNumber The number of the player for whom the chip should be
     * dropped (0 or 1).
     * @param pColumn The number of the column in which the chip should be
     * dropped.
     */
    public void drop(String pPlayerId, int pColumn) {
        if (!isColumnFull(pColumn)) {

            // Calculate the position of the dropped chip.
            int row = numberOfRows - 1 - numberOfChipsInColumn[pColumn];

            // Change the fill color at that position.
            this.setMark(pPlayerId, pColumn, row);

            // Increase the number of chips in that column.
            numberOfChipsInColumn[pColumn]++;

            // Notify game server of successful drop
            gameServer.handlePlayerDrop(pPlayerId, pColumn, row);
        }
    }

    public void gameEnd(boolean pWon) {
        gameResult = "The game is still running!";
        if (pWon) {
            gameStarted = false;
            gameResult = "The game is over! Winner is: " + currentPlayer.getName();
        } else {
            if (numberOfMarks == 49) {
                gameResult = "The game is over! Draw, there is no winner!";
            }

        }
    }

    /**
     * Check if a number is describing a column on the playing field.
     *
     * @param pColumn The column number to be checked.
     * @return Boolean indicating whether the column number is valid.
     */
    public boolean isValidColumnNumber(int pColumn) {
        return pColumn < (numberOfColumns - 1) && pColumn >= 0;
    }

    /**
     * Checks if a column is completely filled with chips.
     *
     * @param pColumn The column to be checked.
     * @return Boolean indicating whether the column is full.
     */
    public boolean isColumnFull(int pColumn) {
        return numberOfChipsInColumn[pColumn] >= numberOfRows;
    }

    public void nextPlayer() {
        currentPlayerNumber++;
        currentPlayer = playerStore.getPlayerByNumber(this.currentPlayerNumber);
    }

}
