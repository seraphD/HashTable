/**
 * Stub for hash table class
 *
 * @author CS3114 staff
 * @version August 2018
 */
//package MemoryStorage;


/**
 * @author Lihui Zhang/lihuiz
 * @author Haosu Wang/whaosu
 * @version 2.0
 */
public class Hash {
    private int size;
    private int records;
    private HashEntry[] array;

     /**
     * Create a new Hash object.
     * @param size initial Hash size
     */
    public Hash(int size)
    {
        array = new HashEntry[size];
        records = 0;
        this.size = size;
    }
    
    /**
     * @return
     *      size of the hash table
     */
    public int getSize() {
        return size;
    }
    
    /**
     * @param pos
     *         position of the record
     * @return
     *      record in hash table
     */
    public HashEntry getRecord(int pos) {
        return array[pos];
    }
    
    /**
     * search a key in hash table
     * @param key
     *          object of search
     * @param pos 
     *          initial slot of key
     * @return
     *          index of the key 
     *          return -1 if key not exist
     */
    public int searchKey(String key, int pos) {
        int i = 0;
        int newPos = pos;
        
        while (array[newPos] != null) {
            
            HashEntry item = array[newPos];
            if (item.isActive() && item.getKey().equals(key)) {
                return newPos;
            }
            
            i += 1;
            
            if (i > size) {
                return -1;
            }
            
            newPos = (pos + i * i) % size;
        }
        
        return -1;
    }
    
    /**
     * expand hash table to double size
     */
    public void expandHash() {
        // new array
        HashEntry[] newArray = new HashEntry[size * 2];
        
        // rehash
        for (int i = 0; i < size; i++) {
            if (array[i] != null && array[i].isActive()) {
                int pos = h(array[i].getKey(), size * 2);
                int j = 0;
                
                // find an empty slot
                // there will not be tomb stone in new array
                int newPos = pos;
                while (newArray[newPos] != null) {
                    j += 1;
                    newPos = (pos + j * j) % (size * 2);
                }
                newArray[newPos] = array[i];
            }
        }
        
        array = newArray;
        size *= 2;
        
        String output = "Name hash table size doubled to " + size + " slots.";
        System.out.println(output);
    }
    
    
    /**
     * delete a key from name database
     * @param key
     *          record key
     * @param index 
     *          position of the record
     * @return handle of the string
     */
    public Handle delete(String key, int index) {
       
        array[index].setActive(false);
        String output = "|" + key + "| has been deleted";
        output += " from the Name database.";
        System.out.println(output);
        records -= 1;
        
        Handle handle = array[index].getHandle();
        array[index].setHandle(null);
        
        return handle;
        
    }
    
   
    /**
     * @param handle
     *          memory block position handle
     * @param key 
     *          inserted key
     * @param pos
     *          hash value
     */
    public void add(Handle handle, String key, int pos) {
        if (records >= size / 2) {
            expandHash();
            pos = h(key, size);
        }
        
        int i = 0;
        int newPos = pos;
        
        while (array[newPos] != null && array[newPos].isActive()) {
            i += 1;
            newPos = (pos + i * i) % size;
            
            if (i > size) {
                expandHash();
                
                i = 0;
                pos = h(key, size);
                newPos = pos;
            }
        }
        
        array[newPos] = new HashEntry(handle, key);
        records += 1;
        
        String output = "|" + key + "| ";
        output += "has been added to the Name database.";
        System.out.println(output);
    }
    
    /**
     * print hash table
     */
    public void print() {
        // print key
        for (int i = 0; i < size; i++) {
            if (array[i] != null && array[i].isActive()) {
                System.out.println("|" + array[i].getKey() + "| " + i);
            }
        }
        	
        // print total records
        System.out.println("Total records: " + records);
    }
    
    /**
     * @param pos
     *      position of the record
     * @return handle of the record
     */
    public Handle getHandle(int pos) {
        return array[pos].getHandle();
    }
    
    /**
     * set new handle to the record
     * @param pos
     *          index in the hash table
     * @param handle
     *          new handle in the memory
     */
    public void setHandle(int pos, Handle handle) {
        array[pos].setHandle(handle);
    }

    /**
     * Compute the hash function. Uses the "sfold" method from the OpenDSA
     * module on hash functions
     *
     * @param s
     *            The string that we are hashing
     * @param m
     *            The size of the hash table
     * @return The home slot for that string
     */
    // You can make this private in your project.
    // This is public for distributing the hash function in a way
    // that will pass milestone 1 without change.
    public int h(String s, int m)
    {
        int intLength = s.length() / 4;
        long sum = 0;
        for (int j = 0; j < intLength; j++)
        {
            char[] c = s.substring(j * 4, (j * 4) + 4).toCharArray();
            long mult = 1;
            for (int k = 0; k < c.length; k++)
            {
                sum += c[k] * mult;
                mult *= 256;
            }
        }

        char[] c = s.substring(intLength * 4).toCharArray();
        long mult = 1;
        for (int k = 0; k < c.length; k++)
        {
            sum += c[k] * mult;
            mult *= 256;
        }

        return (int)(Math.abs(sum) % m);
    }
}

/**
 * element for hash table
 * @author Lihui Zhang/lihuiz
 * @author Haosu Wang/whaosu
 * @version 2.0
 */
class HashEntry {
    private Handle handle;
    private String key;
    /**  if isActive == none 
     *   the position is a deleted element
     *   thus a tomb stone
     */
    private boolean active;
    
    /**
     * @return key of this element
     */
    public String getKey() {
        return key;
    }
    
    /**
     *  constructor of elements
     *  @param handle
     *          handle of memory
     *  @param key
     *          key in the hash table
     */
    public HashEntry(Handle handle, String key) {
        super();
        this.key = key;
        this.active = true;
        this.handle = handle;
    }
    
    /**
     * @return handle of the memory
     */
    public Handle getHandle() {
        return handle;
    }
    
    /**
     * update handle of the element
     * @param handle 
     *          new handle that will be set
     */
    public void setHandle(Handle handle) {
        this.handle = handle;
    }
    
    /**
     * active getter
     * @return if record is active
     */
    public boolean isActive() {
        return active;
    }
    
    /**
     * active setter
     * @param active
     *          is record is active
     */
    public void setActive(boolean active) {
        this.active = active;
    }
    
    
}
