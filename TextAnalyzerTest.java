import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.w3c.dom.Text;
import java.lang.reflect.Field;
import java.io.IOException;
import java.util.TreeMap;
import static org.junit.jupiter.api.Assertions.*;

class TextAnalyzerTest {

    //valid URL test
    @Test
    public void testInput(){
        String validURL = "https://www.gutenberg.org/files/1065/1065-h/1065-h.htm";
        Assertions.assertDoesNotThrow(() -> new TextAnalyzer(validURL));
    }
    //Invalid URL test
    @Test
    public void testInput2(){
        String invalidURL = "https://www.invalidurl.com/nonHTM";
        Assertions.assertThrows(IOException.class, () -> {
            Connection connection = Jsoup.connect(invalidURL);
            connection.get();
        });
    }
    }