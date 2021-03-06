/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConnectFour.client;

import javax.swing.JOptionPane;

/**
 * A client window of the connect four game.
 */
public class GameClient {

    /**
     * Start a client window of the connect four game.
     */
    public GameClient() {

        System.out.println("Client: Started.");

        // Display dialogue for selecting a server ip.
        String serverIP = (String) JOptionPane.showInputDialog(null, "What is your server ip?", "The game is starting soon!!", JOptionPane.QUESTION_MESSAGE, null, null, "127.0.0.1");

        // Check if server ip is valid.
        if ((serverIP != null) && (serverIP.length() > 6)) {

            System.out.println("Client: User selected IP " + serverIP);

            // Start new game window with selected server ip.
            NetworkingClient client = new NetworkingClient(serverIP);
            GameWindow window = new GameWindow();
            ClientGameLogic gameLogic = new ClientGameLogic(client, window);

            gameLogic.setWindow(window);
            gameLogic.setClient(client);

            window.setGameLogic(gameLogic);

            client.setGameLogic(gameLogic);
            client.setWindow(window);
        }

    }
}
