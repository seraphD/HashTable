
/**
 * @author Lihui Zhang/lihuiz
 * @author Haosu Wang/whaosu
 * @version 2.0
 */
public class Handle {
    private int startPos;
    private int length;
    private int size;
    
    
    /**
     * @return start position
     */
    public int getStartPos() {
        return startPos;
    }


    /**
     * @param startPos
     *          set start position
     */
    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }


    /**
     * @return
     *        length of the record
     */
    public int getLength() {
        return length;
    }


    /**
     * @param length
     *          set length
     */
    public void setLength(int length) {
        this.length = length;
    }
    
    /**
     * @return block size
     */
    public int getSize() {
        return size;
    }


    /**
     * @param startPos
     *          starting position in the memory pool
     * @param length
     *          length of the record in memory pool
     * @param size 
     *          block size
     */
    public Handle(int startPos, int length, int size) {
        super();
        this.startPos = startPos;
        this.length = length;
        this.size = size;
    }
    
    
}
