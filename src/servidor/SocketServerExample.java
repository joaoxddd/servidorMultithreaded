package servidor;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerExample {
    
    public static int[][] produtoMatrizes(int[][] a, int[][] b) throws Exception {
        int aLinhas = a.length;
        int aColunas = a[0].length;
        int bLinhas = b.length;
        int bColunas = b[0].length;
        if (aColunas != bLinhas) {
            throw new Exception("As matrizes não podem ser multiplicadas!");
        }
        int[][] resultado = new int[aLinhas][bColunas];
        for (int i = 0; i < aLinhas; i++) {
            for (int j = 0; j < bColunas; j++) {
                for (int k = 0; k < aColunas; k++) {
                    resultado[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return resultado;
    }
    
    public static void main(String[] args)
    {
    	System.out.println("Aguardando Cliente");
        ServerSocket server = null;
  
        try {
  
            // server is listening on port 9876
            server = new ServerSocket(9876);
            server.setReuseAddress(true);
  
            // running infinite loop for getting
            // client request
            while (true) {
  
                // socket object to receive incoming client
                // requests
                Socket client = server.accept();
  
                // Displaying that new client is connected
                // to server
                System.out.println("New client connected"
                                   + client.getInetAddress()
                                         .getHostAddress());
  
                // create a new thread object
                ClientHandler clientSock
                    = new ClientHandler(client);
  
                // This thread will handle the client
                // separately
                new Thread(clientSock).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (server != null) {
                try {
                    server.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
  
    // ClientHandler class
    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;
  
        // Constructor
        public ClientHandler(Socket socket)
        {
            this.clientSocket = socket;
        }
  
        public void run()
        {
            PrintWriter out = null;
            BufferedReader in = null;
            try {
                    
                  // get the outputstream of client
                out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
  
                  // get the inputstream of client
                in = new BufferedReader(
                    new InputStreamReader(
                        clientSocket.getInputStream()));
  
                String line;

                while ((line = in.readLine()) != null) {
                	
                    String[] substrings = line.split(" ");

                    //[[1,2],[1,2]] [[3,4],[3,4]]
                    //[[1,2],[1,2]]
                    String substring1 = substrings[0];
                    //[[3,4],[3,4]]
                    String substring2 = substrings[1];
                    
                 // Remova os caracteres "[" e "]"
                    substring1 = substring1.replaceAll("\\[|\\]", "");
                    substring2 = substring2.replaceAll("\\[|\\]", "");

                    // Quebre a string em uma matriz de strings
                    String[] elementosA = substring1.split(",");
                    String[] elementosB = substring2.split(",");

                    // Determine o tamanho da matriz (2x2)
                    int linhasA = 2;
                    int colunasA = 2;
                    
                    int linhasB = 2;
                    int colunasB = 2;

                    // Crie a matriz de inteiros e preencha-a com os elementos
                    int[][] matrizA = new int[linhasA][colunasA];
                    int[][] matrizB = new int[linhasB][colunasB];
                    
                    for (int i = 0; i < linhasA; i++) {
                        for (int j = 0; j < colunasA; j++) {
                            matrizA[i][j] = Integer.parseInt(elementosA[i * colunasA + j]);
                        }
                    }
                    
                    for (int i = 0; i < linhasB; i++) {
                        for (int j = 0; j < colunasB; j++) {
                            matrizB[i][j] = Integer.parseInt(elementosB[i * colunasA + j]);
                        }
                    }

                    int[][] matriz = produtoMatrizes(matrizA,matrizB);
                    
                    String matrizString = "";

                    for (int i = 0; i < matriz.length; i++) {
                        for (int j = 0; j < matriz[0].length; j++) {
                            matrizString += matriz[i][j] + ",";
                        }
                        matrizString = matrizString.substring(0, matrizString.length() - 1) + ";";
                    }

                    matrizString = matrizString.substring(0, matrizString.length() - 1);
  
                    // writing the received message from
                    // client
                    System.out.printf(
                        " Resultado da Operação: %s\n",
                        matrizString);
                    out.println(matrizString);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                        clientSocket.close();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
