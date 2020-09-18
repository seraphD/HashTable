import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import student.TestCase;

/**
 * @author lihui
 * @version 1.0
 */
public class MemTest extends TestCase {
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
     * Sets up the tests that follow. In general, used for initialization
     */
    public void setUp() {
        // Nothing needed
    }
    
    /**
     * Test syntax: Sample Input/Output
     * Comparing output in a file
     *
     * @throws Exception
     */
    public void testMemManage() throws Exception {
        String[] args = new String[3];
        args[0] = "8";
        args[1] = "10";
        args[2] = "src/memTest.txt";
        MemMan.main(args);
        assertFuzzyEquals(
            readFile("src/memTestOutput.txt"), 
            systemOut().getHistory());
    }
}
