package com.mycompany.cient;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class Client {
    
    private static final String REMOTE_HOST = "192.168.0.109"; 
    private static final int    REMOTE_PORT = 46290;
    
    public static void main(String[] args) throws IOException {
        Socket remoteServerSocket = new Socket(REMOTE_HOST, REMOTE_PORT);
        BufferedReader networkInputStream = new BufferedReader(new InputStreamReader(remoteServerSocket.getInputStream())); 
        PrintWriter networkOutputStream = new PrintWriter(remoteServerSocket.getOutputStream(), true); 
        Scanner consoleScanner = new Scanner(System.in);
        System.out.println("[REMOTE] " + networkInputStream.readLine()); 
        executeMenuLoop(networkInputStream, networkOutputStream, consoleScanner);
        
        remoteServerSocket.close(); 
        consoleScanner.close();
    }
    
    
    private static void executeMenuLoop(BufferedReader networkInputStream, PrintWriter networkOutputStream, Scanner consoleScanner) throws IOException {
        boolean sessionActive = true;
        do {
            System.out.println(" (N) New Equation");
            System.out.println(" (Q) Exit");
            System.out.print("Selection: ");
            
            String menuSelect = consoleScanner.next();
            
            if (menuSelect.equalsIgnoreCase("Q")) {
                System.out.println("[CLIENT] Terminating session.");
                sessionActive = false;
                
            } else if (menuSelect.equalsIgnoreCase("N")) {
                handleEquationSubmission(networkInputStream, networkOutputStream, consoleScanner);
                
            } else {
                System.out.println("[CLIENT] Unrecognised option - please enter N or Q.");
            }
            
        } while (sessionActive);
    }
    
    
    private static void handleEquationSubmission(BufferedReader networkInputStream, PrintWriter networkOutputStream, Scanner consoleScanner)
            throws IOException {
        
        int Coefficient1 = promptForInteger(consoleScanner, "Coefficient X");
        int Coefficient2 = promptForInteger(consoleScanner, "Coefficient Y");
        int Coefficient3 = promptForInteger(consoleScanner, "Coefficient Z");
        int rhsTarget    = promptForInteger(consoleScanner, "Target value (RHS)");
        
        String transmissionPayload = Coefficient1 + " " + Coefficient2 + " "
                                   + Coefficient3 + " " + rhsTarget;
        
        long dispatchTimestamp  = System.nanoTime();
        networkOutputStream.println(transmissionPayload);
        String serverReply      = networkInputStream.readLine();
        long receptionTimestamp = System.nanoTime();

        long elapsedMilliseconds = (receptionTimestamp - dispatchTimestamp) / 1000000;

        System.out.println("  Equation : " + renderEquationExpression(transmissionPayload));
        System.out.println("  Result   : " + serverReply);
        System.out.println("  RTT      : " + elapsedMilliseconds + " ms");
    }
    
    
    private static int promptForInteger(Scanner consoleScanner, String fieldLabel) {
        do {
            System.out.print("  Enter " + fieldLabel + " (integer): ");
            String rawEntry = consoleScanner.next();
            try {
                return Integer.parseInt(rawEntry);
            } catch (NumberFormatException parseEx) {
                System.out.println("  [!] Invalid input — " + fieldLabel + " must be a whole number.");
            }
        } while (true);
    }
    
    
    private static String renderEquationExpression(String rawPayload) {
        String[] tokenArray = rawPayload.split(" ");
        if (tokenArray.length == 4) {
            return tokenArray[0] + "*x + " + tokenArray[1] + "*y +" + tokenArray[2] + "*z = " + tokenArray[3];
        }
        return rawPayload;
    }
}