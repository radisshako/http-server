
/**
 * 
 * A class that handles HttpServerRequests  by processing requests
 */
class HttpServerRequest {

    private String file = null;
    private String host = null;
    private boolean done = false;
    private int line = 0;

    //Returns true if done
    public boolean isDone() {
        return done;
    }

    //Returns File as a String
    public String getFile() {
        return file;
    }

    //Returns host as a String
    public String getHost() {
        return host;
    }

    //Initialises an HttpServerRequest object
    public HttpServerRequest() {

    }


    //Processes the string passed in by parsing its data to get the host and file name
    public void process(String in) {
        String parts[] = in.split(" ");
        String filename = parts[1].substring(1);

        //if the first part is get, get the file requested
        if (parts[0].compareTo("GET") == 0) {

            file  = filename;
                //add index.html if the file ends in / or is empty
                if (parts[1].endsWith("/") || parts[1].isEmpty())
                {
                    file += "index.html";
                }
        }

        //get the host 
        else if (parts[0].startsWith("Host:"))
        {
            host = parts[1];
        }
        
        else{
            //do nothing
        }
        
        //increment line
        line += 1;
    
    }

}
