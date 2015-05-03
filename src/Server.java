/**
 * Created by sehong on 5/1/15.
 */
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Server
{
    public static void main(String[] args) throws IOException
    {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(10007);
        }
        catch (IOException e)
        {
            System.err.println("Could not listen on port: 10007.");
            System.exit(1);
        }
        Scanner in = new Scanner(System.in);
        System.out.println("How many players?");
        int playerCount = Integer.parseInt(in.next());

        Socket[] clientSockets = new Socket[playerCount];
        System.out.println ("Waiting for connection.....");

        try {
            for(int i = 0 ; i < playerCount;i++){
                clientSockets[i] = serverSocket.accept();
            }
            System.out.println("Player "+ (i+1) +"connected.");
        }
        catch (IOException e)
        {
            System.err.println("Accept failed.");
            System.exit(1);
        }

        System.out.println ("All connection successful");
        System.out.println ("Setting up I/O");
        BufferedReader[] in = new BufferedReader[playerCount];
        PrintWriter[] out = new PrintWriter[playerCount];

        for(int i=0 ; i < playerCount;i++ ){
            in[i] = new BufferedReader( new InputStreamReader(clientSockets[i].getInputStream()));
            out[i] = new PrintWriter(clientSockets[i].getOutputStream(),true);
        }
        System.out.println ("Waiting for input.....");

        String inputLine="";
        String inputLine2="";


        while ( (inputLine2 = in2.readLine())!= null)
        {
            if(!inputLine.equals("")) {
                System.out.println("Server: " + inputLine);
                out.println(inputLine);
            }else {
                System.out.println("Server: " + inputLine);
                out2.println(inputLine2);
            }

            if (inputLine.equals("Bye."))
                break;
        }

        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    }
}