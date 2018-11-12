package ConnectFour;


/**
 * Write a description of class GameServer here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class ConnectFourGame
{
     
    private GameClient client;
    private GameWindow window;
    
    private String[][] playingField = new String[7][7];
    private boolean gameStarted;
    private Player playerOne;
    private Player playerTwo;
    private Player currentPlayer;
    private int numberOfPlayers;
    private int numberOfMarks;
    private String gameResult;    
    

    public ConnectFourGame()
    {
        gameStarted = false;
        numberOfPlayers = 0;
        numberOfMarks = 0;
        playerOne = new Player("Spieler 1");
        playerTwo = new Player("Spieler 2");

        for(int column = 0; column < 7; column++){
            for(int row = 0; row < 7; row++){
                playingField[column][row] = " ";
            } 
        }

    }
    
    
    public void startGame(){
        gameStarted = true;
        currentPlayer = playerOne;
    }
    
    /**
     * Sets a mark on a specific position on the playing field.
     * This mark is stored in the string array with a specific 
     * character for each player.
     * @param pColumn The column where the mark should be set.
     * @param pRow The row where the mark should be set.
     */
    public void setMark(int pColumn, int pRow){
        if(playingField[pColumn][pRow].equals(" ")){
            if(currentPlayer == playerOne){
                playingField[pColumn][pRow] = "X";
                numberOfMarks++;
                this.switchPlayers();
            }
            else if(currentPlayer == playerTwo){ 
                playingField[pColumn][pRow] = "O";
                numberOfMarks++;
                this.switchPlayers();
            }
            else{
                System.err.println("Fehler bei Zeichensetzen: kein aktueller Spieler");
            }
        }
        else{
            System.err.println("Fehler bei Zeichensetzen: Feld schon markiert");
        }

    }

    public void switchPlayers(){
        if(currentPlayer == playerOne){
            currentPlayer = playerTwo;
        }
        else if(currentPlayer == playerTwo){ 
            currentPlayer = playerOne;
        }
        else{
            System.err.println("Error while switching palyers: No current player!");            
        }
    }

    public void checkForGameEnd(){
        boolean won = false;

        for(int row = 0; row < 7; row++){
            for(int position = 0; position < 4; position++){

                String mark1 = playingField[position][row];
                String mark2 = playingField[position + 1][row];
                String mark3 = playingField[position + 2][row];
                String mark4 = playingField[position + 3][row];

                if(mark1.equals(mark2) && mark2.equals(mark3) && mark3.equals(mark4)){
                    won = true;
                    break;
                }
            }
        }

        if(won == false){
            for(int column = 0; column < 7; column++){
                for(int position = 0; position < 4; position++){

                    String mark1 = playingField[column][position];
                    String mark2 = playingField[column][position + 1];
                    String mark3 = playingField[column][position + 2];
                    String mark4 = playingField[column][position + 3];

                    if(mark1.equals(mark2) && mark2.equals(mark3) && mark3.equals(mark4)){
                        won = true;
                        break;
                    }
                }
            }
        }

        if(won == false){
            for(int column = 0; column < 7; column++){
                for(int row = 0; row < 7; row++){
                    if(column + 3 < 7 && row + 3 < 7){
                        String mark1 = playingField[column][row];
                        String mark2 = playingField[column+1][row + 1];
                        String mark3 = playingField[column+2][row + 2];
                        String mark4 = playingField[column+3][row + 3];

                        if(mark1.equals(mark2) && mark2.equals(mark3) && mark3.equals(mark4)){
                            won = true;
                            break;
                        }
                    }
                }
            }

        }
        
        if(won == false){
            for(int column = 6; column >= 0; column--){
                for(int row = 6; row >= 0; row--){
                    if(column - 3 >= 0 && row  - 3 >= 0){
                        String mark1 = playingField[column][row];
                        String mark2 = playingField[column-1][row - 1];
                        String mark3 = playingField[column-2][row - 2];
                        String mark4 = playingField[column-3][row - 3];

                        if(mark1.equals(mark2) && mark2.equals(mark3) && mark3.equals(mark4)){
                            won = true;
                            break;
                        }
                    }
                }
            }

        }     
        this.gameEnd(won);       
    }

    
    public void gameEnd(boolean pWon){
        gameResult = "The game is still running!";
        if(pWon){
            gameStarted = false;
            gameResult = "The game is over! Winner is: "+ currentPlayer.getName(); 
        }
        else{
            if(numberOfMarks == 49){
                gameResult = "The game is over! Draw, there is no winner!"; 
            }

        }
    }

    public String getPlayerOne(){
        return playerOne.getName();
    }

    public String getPlayerTwo(){
        return playerTwo.getName();
    }

    public void addPlayer(String pName){
        if(numberOfPlayers == 0){
            playerOne.setName(pName);
        }
        else if(numberOfPlayers == 1){
            playerTwo.setName(pName);
        }
        else{
            System.err.println("Error adding player: Game is full!");
        }

    }
    
    /**
     * Event listener to be called by the networking client
     * when the server reports that a new enemy has
     * joined the game.
     * @param pName The name of the new enemy.
     */
    public void onNewEnemy(String pName) {
        this.addPlayer(pName);
    }
    
    /**
     * Pass the network client to this class.
     * @param pClient The network game client.
     */
    public void setClient(GameClient pClient) {
        client = pClient;
    }
    
    /**
     * Pass the window to this class.
     * @param pClient The window object.
     */
    public void setWindow(GameWindow pWindow) {
        window = pWindow;
    }

   
}