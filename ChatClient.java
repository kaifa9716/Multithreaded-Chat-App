import java.io.*;
import java.util.Scanner;
import java.net.*;





public class ChatClient {
    static String SERVER_ADD = "localhost";
    static int PORT = 3000;
    public static void main(String[] args) {
        try {
            Socket socket = new Socket(SERVER_ADD, PORT);
            System.out.println("Connected to chat server");
            
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
            
            // Thread to receive messages from server
            Thread receiveThread = new Thread(() -> {
                try {
                    String message;
                    while ((message = input.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (Exception e) {
                    System.out.println("Connection lost");
                }
            });
            receiveThread.start();
            
            String userInput;
            while ((userInput = scanner.nextLine()) != null) {
                output.println(userInput);
                if (userInput.equals("bye")) {
                    break;
                }
            }
            
            socket.close();
            scanner.close();
            
        } catch (Exception e) {
            System.out.println("Could not connect to server");
        }
    }
}