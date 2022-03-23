package rssreader;
import java.net.*;
import java.io.*;
import java.sql.*;
import Project.ConnectionProvider1;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;

class Extract implements Runnable {
    Thread thread;
    @Override
    public void run() {
      try{
          URL rssUrl = new URL("http://rss.cnn.com/rss/edition.rss");
          BufferedReader in = new BufferedReader(new InputStreamReader (rssUrl.openStream()));
          String sourceCode= "";
          String line =  in.readLine();
          int itemNo =1;
          while((line= in.readLine())!= null) {
              int titleEndIndex = 0;
              int titleStartIndex = 0;
              while( titleStartIndex >= 0){
                  String item="";
                  titleStartIndex = line.indexOf("<item>", titleEndIndex);
                  if(titleStartIndex >= 0) {
                      titleEndIndex = line.indexOf("</item>", titleStartIndex);
                      item= line.substring(titleStartIndex + "<item>".length(), titleEndIndex) + "\n";
                      evaluate(item, itemNo);
                      itemNo++;
                  }
              }
          }
              in.close();
          }
          catch(MalformedURLException ue) {
              System.out.println("Malformed URL");
          }
          catch(IOException ioe) {
              System.out.println("Something went wrong!");
          }
      
    }
    public static void evaluate(String item, int itemNo) {
        String[] startTags= {"<title>","<description>","<pubDate>","<link>"};
        String[] endTags= {"</title>","</description>","</pubDate>","</link>"};
        try{
           Connection con=ConnectionProvider1.getCon();
           Statement st= con.createStatement();
           String SQL ="insert into record ( itemNo) values("+ itemNo +")";
           st.executeUpdate(SQL);
           for(int i=0;i<startTags.length;i++){
               int firstIndex = item.indexOf(startTags[i]);
               int lastIndex = item.indexOf(endTags[i]);
               String columnName = startTags[i].substring(1, startTags[i].length()-1);
               if(firstIndex!=-1) {
                    String value = item.substring(firstIndex+startTags[i].length(), lastIndex);
                    String query= "Update record set "+ columnName +"= ? where itemNo =?";
                    PreparedStatement pstmt = con.prepareStatement(query);
                    pstmt.setString(1,value);
                    pstmt.setInt(2,itemNo);
                    pstmt.executeUpdate();
               }   
           }
        }
        catch(Exception e) {
            e.printStackTrace();
       }
    }

 
    public void start() {
       System.out.println("Thread started");
       if (thread == null) {
        thread = new Thread(this);
        thread.start();
       }
    }


}
public class RSSReader {
    public static void main(String[] args) {
    Extract thread = new Extract();
    thread.start();
    }
}