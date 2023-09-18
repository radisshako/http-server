import java.net.*;
import java.io.*;
import java.util.*;

/**
 * 
 * A class that Creates an Http server. It facilitates a connection between the client and the server.
 * It allows the client to request a resource, if the resource does not exist it displays a 404 error.
 */
class HttpServer {
    public static void main(String args[]) {
        try {
            // Open a server socket at port 57777
            int PORT = 57777;
            ServerSocket ss = new ServerSocket(PORT);

            // Write the port being listened to the console
            if (args.length < 1) {
                System.out.println("Listening on port " + ss.getLocalPort());
            }

            while (true) {
                // Listen for a connection, assigning the connection to s
                Socket s = ss.accept();

                // InetAddress object of the socket object
                InetAddress addy = s.getInetAddress();
                // get the host address of the InetAddress variable
                String ipadd = addy.getHostAddress();

                // Write out the ip address of the connected client
                System.out.println("Connection recieved from IP: " + ipadd);

                // Declare an HttpServerSessio/logo.pngn object and start it
                HttpServerSession serverSession = new HttpServerSession(s);
                serverSession.start();
            }
        } catch (Exception e) {
            // Display the excpetion to the console
            System.err.println(e);
        }

    }

}

/** HttpServerSession.
 * A class that extends Thread. Takes a socket as a parameter and sends the request to 
 */
class HttpServerSession extends Thread {
   //Socket variable
    private Socket s;
    //Buffered reader variable
    private BufferedReader read;
   
    /**
     * Initialises an HttpServerSession object to the Socket passed in
     * @param s The Socket to use for the server session
     */
    public HttpServerSession(Socket s) {
        this.s = s;
    }

    /**
     * run
     * 
     * Gets the file host and intialises the output stream, and passes it on to returnRequest method.
     */
    public void run() {
        try {
            //Initialise Buffered reader object 
            read = new BufferedReader(new InputStreamReader(s.getInputStream()));
    
            //Initialise BufferedOutputStream object 
            BufferedOutputStream outputStream = new BufferedOutputStream(s.getOutputStream());

            //Initialise HttpServerRequest object
            HttpServerRequest serverRequest = new HttpServerRequest();

            //line variable to store read line
            String line;

            //While the line is not empty or null
            while ((line = read.readLine()) != null && !line.equals("")) {
                //pass the line to the process method
                serverRequest.process(line);
            }
            
            //Get the fil;e and host
            String file = serverRequest.getFile();
            String host = serverRequest.getHost();

            //Call the returnrequest emthod with file host and outputstream passed in
            returnRequest(file, host, outputStream);

            

        } catch (Exception e) {
            //Print the error to the console
            System.err.println(e);
        }
    }

    //println method mimicing PrintWriter println method, but compliant with HTTP
    private boolean println(BufferedOutputStream bos, String s) {
        String news = s + "\r\n";
        byte[] array = news.getBytes();
        try {
            bos.write(array, 0, array.length);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * returnRequest
     * 
     * Takes a file host and bufferedoutputstream object as parameters.
     * And sends the resource through, displays a 404 error message if the file is not found
     * @param file file name
     * @param host host name
     * @param output bufferedoutputstream object
     */
    public void returnRequest(String file, String host, BufferedOutputStream output) {
        //Initialising array for data to 1024 bytes
        byte[] array = new byte[1024];
        //declaring file path by concatenating host and file
        String filePath = host + "/" + file;

        try {
           
            try {

                FileInputStream inputStream = new FileInputStream(filePath);
         
                //send a 200 success message 
                println(output, "HTTP/1.1 200 OK");
                println(output, "");

                int bytesRead;
                //while inputstream.read isnt -1 write out the data
                while ((bytesRead = inputStream.read(array)) != -1) {
                    output.write(array, 0, bytesRead);
                    //sleep 100
                }
                
                inputStream.close();

            } catch (FileNotFoundException e) {
                //send a 404 not found error message
                println(output, "HTTP/1.1 404 Not Found");
                println(output, "");
                println(output, "404 File Not Found");

            }
            //flush the output stream
             output.flush();

            //close the socket
            s.close();

           
        } catch (Exception e) {
            //Display an error message
            System.err.println(e);
        }
    }
}