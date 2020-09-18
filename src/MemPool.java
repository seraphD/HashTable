/**
 * @author Lihui Zhang/lihuiz
 * @author Haosu Wang/whaosu
 * @version 2.0
 */
public class MemPool {
    private byte[] pool;
    private int size;
    private MemBlock[] memList;
    private int maxN;
    
    /**
     * use Math.ceil to convert k to integer
     * get exponent of an integer of 2
     * @param n
     *      an integer 
     * @return
     *      k = log n
     */
    public static int getExp(int n) {
        int k = (int)Math.ceil((Math.log((double)n) / Math.log(2)));
        return k;
    }
    
    /**
     * calculate the result of 2 ^ n
     * @param n
     *      an integer
     * @return
     *      2 ^ n
     */
    public static int getPower(int n) {
        int k = (int)Math.pow(2, n);
        return k;
    }
    
    /**
     * @param size
     *      initial size of the memory pool
     */
    public MemPool(int size) {
        super();
        this.size = size;
        pool = new byte[size];
        
        maxN = getExp(size);
        memList = new MemBlock[maxN + 1];
        memList[maxN] = new MemBlock(0, size);
    }
    
    /**
     * @return size of the memory pool
     */
    public int getSize() {
        return size;
    }
    
    /**
     * add memory block to memory list
     * @param m
     *        memory block
     * @param pos
     *         position in the list
     */
    private void listAdd(MemBlock m, int pos) {
        if (memList[pos] == null) {
            memList[pos] = m;
        }
        else {
            MemBlock p = memList[pos];
            // insert a memory block
            // ordered by address
            
            if (p.getAddr() > m.getAddr()) {
                m.setNext(p);
                memList[pos] = m;
                return;
            }
            
            while (p.getNext() != null && p.getNext().getAddr() < m.getAddr()) {
                p = p.getNext();
            }
            m.setNext(p.getNext());
            p.setNext(m);
        }
    }
    
    /**
     * remove first block from memory list in the position
     * @param pos
     *      position in the list
     * @return
     *      removed block
     */
    private MemBlock listRemove(int pos) {
        MemBlock p = memList[pos];
        
        memList[pos] = memList[pos].getNext();
        p.setNext(null);
        
        return p;
    }
    
    /**
     * remove a specified block from memory list in the position
     * @param pos
     *      position in the list
     * @return
     *      removed block
     */
    private MemBlock listRemove(int pos, MemBlock m) {
        MemBlock p = memList[pos];
        
        if (p == m) {
            return listRemove(pos);
        } 
        else {
            while (p.getNext() != null) {
                if (p.getNext() == m) {
                    p.setNext(m.getNext());
                    m.setNext(null);
                    return m;
                }
            }
        }
        
        return null;
    }
    
    /**
     * @param handle
     *         marked start position and length of a record
     * @return record in string type
     */
    public String read(Handle handle) {
        int startPos = handle.getStartPos();
        int length = handle.getLength();
        byte[] array = new byte[length];
        
        for (int i = 0; i < length; i++) {
            array[i] = pool[i + startPos];
        }
        
        return new String(array);
    }
    
    /**
     * write record into a memory block
     * @param m
     *      written memory block
     * @param array
     *      inserted string as byte array
     */
    private void write(MemBlock m, byte[] array) {
        int pos = m.getAddr();
        
        for (int i = 0; i < array.length; i++) {
            pool[pos + i] = array[i];
        }
    }
    
    /**
     * expand memory pool to double size
     * @param length
     *      least size of the new block
     * @return i
     *      position of the new memory block in memory list
     */
    private int expandPool(int length) {
        int i = maxN + 1;
        
        // expand memory list
        MemBlock[] newList = new MemBlock[i + 1];
        for (int j = 0; j < memList.length; j++) {
            newList[j] = memList[j];
        }
        // add a free block to memory list
        // to do  bug fix
        int addr = size;
        MemBlock m = new MemBlock(addr, size);
        memList = newList;
        merge(m);
        
        // expand memory pool
        byte[] newPool = new byte[size * 2];
        for (int j = 0; j < pool.length; j++) {
            newPool[j] = pool[j];
        }
        pool = newPool;
        
        size *= 2;
        String output = "Memory pool expanded to be " + size + " bytes.";
        System.out.println(output);
        
        return i;
    }
    
    /**
     * insert a string into memory pool
     * @param array
     *      bytes array to be inserted
     * @return
     *      a handle of the string
     */
    public Handle insert(byte[] array) {
        int length = array.length;
        int k = getExp(length);
        
        int i = k;
        while (i <= maxN && memList[i] == null) {
            i++;
        }
        
        while (i > maxN) {
            // expand memory pool
            
            maxN = expandPool(length);
            i = k;
            while (i <= maxN && memList[i] == null) {
                i++;
            }
        }
        
        MemBlock m = listRemove(i);
        i--;
        
        while (i >= k) {
            
            int addr1 = m.getAddr();
            int addr2 = addr1 + m.getSize() / 2;
            int mSize = m.getSize();
            
            MemBlock m1 = new MemBlock(addr1, mSize / 2);
            MemBlock m2 = new MemBlock(addr2, mSize / 2);
            
            listAdd(m1, i);
            listAdd(m2, i);
            
            m = listRemove(i);
            
            i--;
        }
        
        write(m, array);
        Handle handle = new Handle(m.getAddr(), length, m.getSize());
        return handle;
    }
    
    // bug check
    /**
     * merge a memory block into memory list
     * @param m
     *      memory block to be merged
     */
    private void merge(MemBlock m) {
        int mSize = m.getSize();
        int k = getExp(mSize);
        int addr = m.getAddr();
        
        int buddyAddr1 = addr + mSize;
        int buddyAddr2 = addr - mSize;
        
        MemBlock newM = null;
        
        MemBlock p = memList[k];
        if (p == null) {
            memList[k] = m;
        }
        else {
            if (p.getAddr() == buddyAddr1 && addr % (mSize * 2) == 0) {
                newM = new MemBlock(addr, mSize * 2);
                listRemove(k, p);
                merge(newM);
            }
            else if (p.getAddr() == buddyAddr2 && addr % (mSize * 2) == mSize) {
                newM = new MemBlock(buddyAddr2, mSize * 2);
                listRemove(k, p);
                merge(newM);
            } 
            else {
                listAdd(m, k);
            }
        }
    }
    
    /**
     * free a memory block
     * @param handle
     *          stored in hash table
     */
    public void freeMem(Handle handle) {
        int pos = handle.getStartPos();
        int mSize = handle.getSize();
        
        MemBlock m = new MemBlock(pos, mSize);
        merge(m);
    }
    
    /**
     * update memory free old blocks allocate new blocks
     * @param value
     *    new record
     * @param handle
     *    old handle
     * @return
     *      new handle
     */
    public Handle update(String value, Handle handle) {
        byte[] array = value.getBytes();
        freeMem(handle);
        Handle newHandle = insert(array);
        return newHandle;
    }
    
    /**
     * print memory pool 
     * free : used
     */
    public void print() {
        String output = "";
        
        for (int i = 0; i < memList.length; i++) {
            if (memList[i] != null) {
                output += getPower(i) + ":";
                MemBlock p = memList[i];
                
                while (p != null) {
                    output += " " + p.getAddr();
                    p = p.getNext();
                }
                output += "\n";
            }
        }
        
        
        if (!output.equals("")) {
            System.out.print(output);
        }
        else {
            System.out.println("No free blocks are available.");
        }
    }
}

/**
 * memory block in the memory pool
 * the size of a block must be a power of 2
 * 
 * @author Lihui Zhang/lihuiz
 * @author Haosu Wang/whaosu
 * @version 2.0
 */
class MemBlock {
    private int addr;
    private int size;
    // used in memory list to access next
    // memory block of same size
    private MemBlock next;
    
    /**
     *  constructor of memory block
     * @param addr
     *      start address of a block
     * @param size
     *      size of a block
     *      power of 2
     */
    public MemBlock(int addr, int size) {
        super();
        this.addr = addr;
        this.size = size;
        this.next = null;
    }
    
    /**
     * @return size of the memory block
     */
    public int getSize() {
        return size;
    }
    
    /**
     * @return address of the memory block
     */
    public int getAddr() {
        return addr;
    }
    
    /**
     * next getter
     * @return next memory block
     */
    public MemBlock getNext() {
        return next;
    }
    
    /**
     * next setter
     * @param m 
     *      next memory block
     */
    public void setNext(MemBlock m) {
        this.next = m;
    }
}
