import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

 /**
 * This class connects to an HTML document and creates a list of the top 20 words
 * that appear most frequently.
 */
public class TextAnalyzer {

   /**
     *  Constructs a TextAnalyzer object with the specified document URL.
     * @param doc the URL of the HTML document to analyze
     * @throws IOException if an error occurs while connecting to the document
     *
     */
    public TextAnalyzer(String doc) throws IOException {
        Connection connection = Jsoup.connect(doc);
        try {
            Document doc1 = connection.get();

            //Consider only from Title(h1) to any sibling elements of p after h1
            Element h1 = doc1.selectFirst("h1");
            Elements p = h1.nextElementSiblings().select("p");
            String content = p.text();

            //TreeMap to save key set of word and times it appears in text
            TreeMap<String, Integer> wordList = new TreeMap<> ();

            Integer count = 0; //variable that will be used to count frequency of word
            String words[] = content.split("[\\s—()\"--,.;!?]"); //save each word of the html document and turns into lowercase
            for (String word: words) {
                word = word.toLowerCase();

                //Logic for TreeMap
                if(wordList.containsKey(word)) {
                    count = wordList.get(word);
                    wordList.put(word, count + 1);
                }
                else {
                    wordList.put(word, 1);
                }
            }
            // Create a list of entries from the wordList map
            List<Map.Entry<String, Integer>> entryList = new ArrayList<>(wordList.entrySet());

            // Sort the entryList based on the values in ascending order using a custom comparator
            Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
                public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
                    return entry2.getValue().compareTo(entry1.getValue());
                }
            });
            int counter = 0;
            //display the top 20 words from the TreeMap
            for (Map.Entry<String, Integer> entry : entryList) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
                counter++;
                if (counter == 20) {
                    break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     /**
      * The main method creates a TextAnalyzer object with a specified HTML document URL.
      * @param args the command-line arguments
      */
    public static void main(String[] args) throws IOException {
        new TextAnalyzer("https://www.gutenberg.org/files/1065/1065-h/1065-h.htm");
    }
}


