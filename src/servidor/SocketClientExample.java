package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketClientExample {

    private static String serverName = "localhost";
    private static int port = 9876;
    
    public static void main(String[] args) {
		try (Socket socket = new Socket(serverName, port)) {

			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			Scanner sc = new Scanner(System.in);
			String line = null;

			while (!"exit".equalsIgnoreCase(line)) {
				System.out.println("Coloque 2 matrizes com formatação:  Exemplo = [[1,2],[1,2]] [[3,4],[3,4]]");
				line = sc.nextLine();
				out.println(line);
				out.flush();
				System.out.println("Mensagem recebida do Servidor: " + in.readLine());
			}

			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

