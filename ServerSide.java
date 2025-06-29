import java.net.*;
import java.util.*;
import java.io.*;





class  ServerSide {
    static final int PORT = 3000;
    static ArrayList<HandleClient> clients = new ArrayList<>();
    
    public static void main(String[] args) {
        
        System.out.println("Server running in port " + PORT);

        try{
            ServerSocket serverSocket = new ServerSocket(PORT);
            
            while (true) 
            {
                Socket clientSocket = serverSocket.accept();
                HandleClient client = new HandleClient(clientSocket);
                clients.add(client);
                new Thread(client).start();
                System.out.println("New client connected");
            }
        }catch(Exception e){
            System.out.println("Server error: " + e.getMessage());
        }
    }
    
    public static void broadcast(String message, HandleClient sender) {
        for (HandleClient client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }
    
    public static void removeClient(HandleClient client) {
        clients.remove(client);
    }
}

class HandleClient implements Runnable {
    String name;
    Socket socket;
    BufferedReader input;
    PrintWriter output;
    
    public HandleClient(Socket socket) {
        this.socket = socket;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public void run() {
        try {
            output.println("Enter name :--  ");
            name = input.readLine();
            System.out.println(name + " joined CHATROOM");
            ServerSide.broadcast(name + " joined the CHATROOM", this);
            
            String message;
            while ((message = input.readLine()) != null) {
                if (message.equals("bye")) {
                    break;
                }
                System.out.println(name + ": " + message);
                ServerSide.broadcast(name + ": " + message, this);
            }
        } catch (Exception e) {
            System.out.println("Client disconnected");
        }
        
        try {
            socket.close();
        } catch (Exception e) {
            System.out.println("Error closing socket");
        }
        
        ServerSide.removeClient(this);
        ServerSide.broadcast(name + " left the chat", this);
        System.out.println(name + " left the chat");
    }
    
    public void sendMessage(String message) {
        output.println(message);
    }
}