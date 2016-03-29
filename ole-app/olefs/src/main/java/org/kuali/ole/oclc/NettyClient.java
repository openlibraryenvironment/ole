package org.kuali.ole.oclc;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by SheikS on 2/24/2016.
 */
public class NettyClient {

    public String sendRequestToNettyServer(String requestMessage, String host, int port) {
        String response = "";
        try {
            Socket clientSocket = new Socket(host, port);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeBytes(requestMessage);
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            response = inFromServer.readLine();
            System.out.println("FROM SERVER: " + response);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
