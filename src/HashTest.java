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
        
        h.add("Death Note");
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
}
