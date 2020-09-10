/**
 * Stub for hash table class
 *
 * @author CS3114 staff
 * @version August 2018
 */
//package MemoryStorage;

import java.io.IOException;

/**
 * @author Lihui Zhang/lihuiz
 * @author Haosu Wang/whaosu
 * @version 2.0
 */
public class Hash {
    private String[] keys;
    private String[] vals;
    private int size;
    private int records;

     /**
     * Create a new Hash object.
     * @param size initial Hash size
     */
    public Hash(int size)
    {
        keys = new String[size];
        vals = new String[size];
        records = 0;
        this.size = size;
    }
    
    /**
     * @param key
     *          object of search
     * @param index 
     *          initial slot of key
     * @return
     *          index of the key return -1 if key not exist
     */
    public int searchKey(String key, int index) {
        int i = 0;
        
        while (keys[index] != null || vals[index] != null) {
           
            if (keys[index] != null && keys[index].equals(key)) {
                return index;
            }
            
            index = (index + i * i) % size;
            i += 1;
        }
        
        return -1;
    }
   
    /**
     * @throws IOException
     */
    private void expandHash() throws IOException {
        // double the size of table
        String[] newKeyArray = new String[size * 2];
        String[] newValArray = new String[size * 2];

        // rehash whole table
        for (int i = 0; i < size; i++) {
            if (vals[i] != null) {
                String key = vals[i].split("<SEP>")[0];
                int newH = h(key, size * 2);
                int j = 0;
                
                while (newValArray[newH] != null) {
                    newH = (newH + j * j) % (size * 2);
                    j += 1;
                }
                
                newKeyArray[newH] = keys[i];
                newValArray[newH] = vals[i];
            }
        }
        
        keys = newKeyArray;
        vals = newValArray;
        size *= 2;
        System.out.println(
            "Name hash table size doubled to " + size + " slots.");
    }
    
    /**
     * @param key
     *          the key used to calculate hash value
     * @param value
     *          stored value
     * @return
     *          success or not of this operation
     * @throws IOException
     */
    public boolean add(String key) 
        throws IOException 
    {
        
        // expand hash table if records greater than half of size
        if (records >= size / 2) {
            expandHash();
        }
        
        // search key in hash table return false if key exist
        int hVal = h(key, size);
        if (searchKey(key, hVal) > -1) {
            return false;
        }
        
        // quadratic probing
        // add key to a empty slot or tombStone
        int i = 0;
        while (keys[hVal] != null || keys[hVal] != null) {
            hVal = (hVal + i * i) % size;
            i += 1;
        }
        
        keys[hVal] = key;
        vals[hVal] = key;
        records += 1;
        
        // successful add
        return true;
    }
    
    /**
     * only delete the key in keys array
     * if key[h] == null and value[h] != null then here is a tombStone
     * @param key
     *          the key of deleted value
     * @return
     *          success or not of the deletion
     */
    public boolean delete(String key) {
        int hVal = h(key, size);
        int index = searchKey(key, hVal);
        
        if (index == -1) {
            // key not found
            return false;
        } 
        else {
            // delete key
            // keep value in vals to make a tombStone 
            keys[index] = null;
            records -= 1;
            
            return true;
        }
    }

    /**
     * @param operation
     *          update operation type include add and delete
     * @param value
     *          it consists of key and fields
     *          in add case. Find field that not in exist fields and add them
     *          in delete case. delete specified field in value
     * @return
     *          return updated record or failure message
     */         
    public String update(String operation, String value) {
        if (operation.equals("add")) {
            String[] fields = value.split("<SEP>");
            for (int i = 0; i < fields.length; i++) {
                fields[i] = fields[i].trim();
            }

            String key = fields[0];
            
            // search key
            int hVal = h(key, size);
            int index = searchKey(key, hVal); 
            if (index == -1) {
                return "|" + key + "| not updated" + 
                    " because it does not exist in the Name database.";
            }
            
            // original fields
            String[] existFields = vals[index].split("<SEP>");
            // fields not in value
            String[] fieldNeed = new String[existFields.length];
            // mark insert position of fieldNeed array
            int i = 0;
            
            // free old blocks
            for (int j = 1; j < existFields.length; j++) {
                boolean repeat = false;
                
                for (int k = 1; k < fields.length; k++) {
                    if (existFields[j].equals(fields[k])) {
                        repeat = true;
                        break;
                    }
                }
                    
                if (!repeat) {
                    fieldNeed[i] = existFields[j];
                    i++;
                }
            }
            
            // new value
            String newRecord = key;
            for (int j = 0; j < i; j++) {
                newRecord += "<SEP>" + fieldNeed[j];
            }
            
            // start from index 1 because fields[0] is key
            for (int j = 1; j < fields.length; j++) {
                newRecord += "<SEP>" + fields[j];
            }
                
            vals[index] = newRecord;
            // return updated record
            return "Updated Record: |" + vals[index] + "|";
        } 
        else {
            // update delete haven't been coded yet
            // it's a mock return
            return "";
        }
    }
        
        
    /**
     * @param type
     *          specify printing hash table or blocks
     * @throws IOException
     */
    public void print(String type) throws IOException {
        if (type.equals("hashtable")) {
            for (int i = 0; i < size; i++) {
                if (keys[i] != null) {
                    System.out.print("|" + keys[i] + "| " + i + "\n");
                }
            }
            System.out.print("Total records: " + records + "\n");
        } 
        else {
            System.out.println("");
        }
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
