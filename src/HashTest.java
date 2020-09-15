import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
     * Read contents of a file into a string
     * 
     * @param path
     *            File name
     * @return the string
     * @throws IOException
     */
    static String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded);
    }
    
    /**
     * @throws Exception
     */
    public void testAdd() throws Exception {
        String[] args = new String[3];
        args[0] = "32";
        args[1] = "10";
        args[2] = "src/testAdd.txt";
        
        MemMan.main(args);
        
        assertFuzzyEquals(
            readFile("src/addOutput.txt"), 
            systemOut().getHistory());
    }
}
