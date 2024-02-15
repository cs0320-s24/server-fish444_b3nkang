package edu.brown.cs.student.main;

// http://localhost:3232/loadcsv?filepath=data/prod/dol_ri_earnings_disparity.csv
// http://localhost:3232/loadcsv?filepath=data/prod/stardata.csv
// http://localhost:3232/viewcsv
// http://localhost:3232/searchcsv?searchvalue=14%&header=true&columnidentifier=Employed+Percent
// http://localhost:3232/broadband?state=California&county=Napa
// http://localhost:3232/broadband?strte=California&corunty=Napda
// http://localhost:3232/broadband?state=California&county=Napda
// http://localhost:3232/broadband?state=Califorewfnia&county=Napa
// http://localhost:3232/broadband?state=Hawaii&county=Napda

import static spark.Spark.after;

import edu.brown.cs.student.handlers.*;
import spark.Spark;

public class Server {

  public static String filepath;
  public static boolean loaded = false;

  public Server() {
    int port = 3232;

    Spark.port(port);
    /*
       Setting CORS headers to allow cross-origin requests from the client; this is necessary for the client to
       be able to make requests to the server.

       By setting the Access-Control-Allow-Origin header to "*", we allow requests from any origin.
       This is not a good idea in real-world applications, since it opens up your server to cross-origin requests
       from any website. Instead, you should set this header to the origin of your client, or a list of origins
       that you trust.

       By setting the Access-Control-Allow-Methods header to "*", we allow requests with any HTTP method.
       Again, it's generally better to be more specific here and only allow the methods you need, but for
       this demo we'll allow all methods.

       We recommend you learn more about CORS with these resources:
           - https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS
           - https://portswigger.net/web-security/cors
    */
    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });
    Spark.get("loadcsv", new LoadHandler());
    Spark.get("viewcsv", new ViewHandler());
    Spark.get("searchcsv", new SearchHandler());
    Spark.get("broadband", new BroadbandHandler());
    Spark.awaitInitialization();
  }

  //  public void setServerFilepath(String filepath){
  //    this.filepath = filepath;
  //  }

  public static void main(String[] args) {
    Server server = new Server();
    System.out.println("Server started; exiting main...");
  }
}
