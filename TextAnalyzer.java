import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.xml.transform.Result;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;

/**
 * This class connects to an HTML document and inserts each word to a database table
 */
public class TextAnalyzer {
    private boolean completed = false;
     public static Connection getConnection() throws Exception {
         try {
             String driver = "com.mysql.cj.jdbc.Driver";
             String url = "jdbc:mysql://localhost:3306/sys";
             String username = "root";
             String password = "Hara361996kyc.";
             Class.forName(driver);

             java.sql.Connection conn = DriverManager.getConnection(url, username, password);
             return conn;
         } catch (Exception e) {
             System.out.println(e);
             e.printStackTrace();
         }
         return null;
     }

         /**
          * Constructs a TextAnalyzer object with the specified document URL.
          *
          * @param doc the URL of the HTML document to analyze
          * @throws IOException if an error occurs while connecting to the document
          */
     public TextAnalyzer(String doc) throws IOException {
             org.jsoup.Connection connection = Jsoup.connect(doc);
             try {
                 Document doc1 = connection.get();


                 //Consider only from Title(h1) to any sibling elements of p after h1
                 Element h1 = doc1.selectFirst("h1");
                 Elements p = h1.nextElementSiblings().select("p");
                 String content = p.text();

                 System.out.println("Content: " + content);
                 String words[] = content.split("[\\s—()\"--,.;!?]"); //save each word of the html document and turns into lowercase
                 System.out.println("Extracted Words: " + Arrays.toString(words));
                 Connection conn = getConnection();

                 String insertQuery = "INSERT INTO sys.word_occurrences (word) VALUES (?)";
                 try (PreparedStatement posted = conn.prepareStatement(insertQuery)) {
                     for (String word : words) {
                         word = word.toLowerCase();
                         if (!word.isEmpty()) {
                             posted.setString(1, word);
                             posted.addBatch();
                         }
                     }
                     System.out.println("Batch Size: " + posted.executeBatch().length);
                     posted.executeBatch();
                     PreparedStatement posted2 = conn.prepareStatement("SELECT * FROM sys.word_occurrences");
                     ResultSet result = posted2.executeQuery();
                     while (result.next()) {
                         String word = result.getString("word");
                         System.out.println("Word: " +  word);
                     }
                 }
                 completed = true;
             } catch (IOException e) {
                 e.printStackTrace();
             } catch (SQLException e) {
                 throw new RuntimeException(e);
             } catch (Exception e) {
                 throw new RuntimeException(e);
             }
             }

     public boolean isCompleted () {
    return completed;
     }
 }




