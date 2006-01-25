import java.net.*;
import java.io.*;


// To run against a digir compatible server
//java DigirClient [port number] digirRequest.xml

public class DigirClient {

  public static String serverFirst = "http://antelope.cs.umb.edu:";
  public static String serverSecond = "/efg/search";


 


  public static void main(String args[]) {
      String port = null;
      try{
	  if(args.length == 1){//default port
	      port = "8080";
	  }
	  else if(args.length != 2){
	      System.err.println("Usage java DigirClient [port] xmlFileName");
	      return;
	  }
	  else{
	      port = args[0];
	  }
	  StringBuffer buff = new StringBuffer();
	 
	  String server = serverFirst + port + serverSecond;
	  URL u = new URL(server);
	  URLConnection uc = u.openConnection();
	  HttpURLConnection connection = (HttpURLConnection) uc;
	  connection.setDoOutput(true);
	  connection.setDoInput(true); 
	  connection.setRequestMethod("POST");
	  
	  OutputStream out = connection.getOutputStream();
	  Writer wout = new OutputStreamWriter(out, "UTF-8");
	  buff.append("digir=");
	  FileInputStream fis = new FileInputStream(new File(args[1]));
	  int c = -1;
	  while (( c = fis.read()) != -1){
	      buff.append((char) c);
	  }
	  fis.close();
	  // Transmit the request XML document
	  wout.write(buff.toString(),0,buff.toString().length());
	  
	  wout.flush();
	  wout.close();      

	  StringBuffer response = new StringBuffer();

	  BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
	  
	  while ((c = in.read()) != -1){
	      response.append((char) c);
	  }
	  in.close();
	  connection.disconnect();
	  
	  //Write a parser to parse it
	  System.out.println(response.toString());
	  
      }
      catch(Exception e){
	  System.out.println(e.getMessage());
	  e.printStackTrace();
      }
  }
}

      
