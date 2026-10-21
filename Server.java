package com.mycompany.cient;

import java.io.*;
import java.net.*;


public class Server {
    
    private static final int BINDING_PORT = 46290;
    
    public static void main(String[] args) throws IOException {
        ServerSocket listenerSocket = new ServerSocket(BINDING_PORT);
        System.out.println("[SERVER] System online and awaiting requests on port " + BINDING_PORT + "...");
        
        do {
            Socket sessionSocket = listenerSocket.accept();
            System.out.println("[SERVER] Incoming session established — remote host: " + sessionSocket.getInetAddress());
            new Thread(new SessionWorker(sessionSocket)).start();
        } while (true);
        
    }
    
}


class SessionWorker implements Runnable {
    
    private static final int MAX_SEARCH_BOUND = 100;
    private final Socket activeSession;
    
    public SessionWorker(Socket activeSession) {
        this.activeSession = activeSession;
    }
    
    @Override
    public void run() {
        try {
            BufferedReader inboundStream  = new BufferedReader(
                    new InputStreamReader(activeSession.getInputStream()));
            PrintWriter    outboundStream = new PrintWriter(activeSession.getOutputStream(), true);
            
            outboundStream.println("Awaiting input — provide four integers: [A] [B] [C] [Target]");
            processClientRequests(inboundStream, outboundStream);
            activeSession.close(); 
        } catch (IOException ioEx) {
            System.out.println("[SERVER] Session terminated unexpectedly due to an I/O fault.");
        }
        
    }
    
    
    private void processClientRequests(BufferedReader inboundStream,
    PrintWriter    outboundStream) throws IOException {
        
        String incomingDataString;
        while ((incomingDataString = inboundStream.readLine()) != null) { 
            String[] rawTokens = incomingDataString.trim().split("\\s+"); 
            
            int alphaCoef   = Integer.parseInt(rawTokens[0]);
            int betaCoef    = Integer.parseInt(rawTokens[1]);
            int gammaCoef   = Integer.parseInt(rawTokens[2]); 
            int targetValue = Integer.parseInt(rawTokens[3]);
            
            String resultPayload = computeIntegerSolution(alphaCoef, betaCoef, gammaCoef, targetValue);
            outboundStream.println(resultPayload);
        }
        
    }
    
    
    private String computeIntegerSolution(int alphaCoef, int betaCoef,
    int gammaCoef, int targetValue) {
        
        for (int xVal = 0; xVal <= MAX_SEARCH_BOUND; xVal++) {
            
            for (int yVal = 0; yVal <= MAX_SEARCH_BOUND; yVal++) {
                
                for (int zVal = 0; zVal <= MAX_SEARCH_BOUND; zVal++) {
                    
                    int evaluatedSum = alphaCoef * xVal + betaCoef * yVal + gammaCoef * zVal;
                    if (evaluatedSum == targetValue)
                        return "Solution: x=" + xVal + ", y=" + yVal + ", z=" + zVal;
                }
            }
        }
        return "No Answer";
    }
    
}






