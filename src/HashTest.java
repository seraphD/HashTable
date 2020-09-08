import java.io.IOException;
import student.TestCase;


/**
 * @author lihui
 * @version 1.0
 */
public class HashTest extends TestCase {
    
    /**
     * set up test cases
     */
    public void setUp() {
        // Nothing needed
    }
    
    /**
     * @throws Exception
     */
    public void testHashUpdateAdd() throws Exception {
        Hash h = new Hash(10);
        String output;
        String ans;
        
        h.add("Death Note", "Death Note");
        output = h.update("add", "Death Note<SEP>Genre<SEP>Anime");
        ans = "Updated Record: |Death Note<SEP>Genre<SEP>Anime|";
        assertEquals(ans, output);
//        restoreStreams();
        
        output = h.update("add", "Death Note<SEP>Genre<SEP>Format");
        ans = "Updated Record: |Death Note<SEP>Anime<SEP>Genre<SEP>Format|";
        assertEquals(ans, output);
        
        output = h.update("add", "Death Note<SEP>Anime<SEP>Series");
        ans = "Updated Record: ";
        ans += "|Death Note<SEP>Genre<SEP>Format<SEP>Anime<SEP>Series|";
        assertEquals(ans, output);
        
        boolean flag = h.delete("Death Note");
        boolean deleteAns = true;
        assertEquals(flag, deleteAns);
    }
    
    /**
     * @throws IOException 
     * 
     */
    public void testAddAndDelete() throws IOException {
        Hash h = new Hash(10);
        
        boolean output;
        String key;
        
//        key = "2";
//        output = h.add(key, key);
//        assertEquals(output, true);
//        
//        key = "22";
//        output = h.add(key, key);
//        assertEquals(output, true);
//        
//        key = "222";
//        output = h.add(key, key);
//        assertEquals(output, true);
//        
//        key = "2222";
//        output = h.add(key, key);
//        assertEquals(output, true);
        
        key = "2     2";
        output = h.add(key, key);
        assertEquals(output, true);
        
        h.print("hashtable");
    }
}
