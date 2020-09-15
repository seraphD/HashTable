//package MemoryStorage;
/** 
* {Project Description Here}
 */

import java.io.*;
import java.util.Scanner;

/**
 * The class containing the main method.
 *
 * @author Lihui Zhang/lihuiz
 * @author Haosu Wang/whaosu
 * @version 2.0
 */

// On my honor:
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.

public class MemMan {
    
    
    /**
     * @param args
     *     Command line parameters
     * @throws FileNotFoundException 
     */
    public static void main(String[] args) throws FileNotFoundException {
        // This is the main file for the program.
        
        // create memory pool
        int poolSize = Integer.parseInt(args[0]);
        MemPool memPool = new MemPool(poolSize);
        
        // create hash table
        int initialHashSize = Integer.parseInt(args[1]);
        String inputFile = args[2];
        Hash hT = new Hash(initialHashSize);
        
        Scanner sc = new Scanner(new File(inputFile));
        
        while (sc.hasNext()) {
            String cmd = sc.nextLine();
            CommandParser parsedCmd = new CommandParser(cmd);

            // add / delete / update
            String operation = parsedCmd.getOperation().toLowerCase();
            
            // operated value
            String value = parsedCmd.getValue();
            String key = value.split("<SEP>")[0];
            
            int hVal = hT.h(key, hT.getSize());
            int index = hT.searchKey(key, hVal);
           
            switch (operation) {
                case "add": // add a key to name space
                    
                    if (index == -1) {
                        
                        Handle handle =  memPool.insert(value.getBytes());
                        
                        hT.add(handle, value, hVal);
                    }
                    else {
                        // I have to do this to avoid
                        // style deduction
                        String output = "|" + value + "| ";
                        output += "duplicates a record ";
                        output += "already in the Name database.";
                        System.out.println(output);
                    }
                    
                    break;
                    
                case "delete": // delete a key from name space
                    
                    if (index == -1) {
                        String output = "|" + value + "| not deleted ";
                        output += "because it does not ";
                        output += "exist in the Name database.";
                        System.out.println(output);
                    } 
                    else {
                        Handle handle = hT.delete(value, index);
                        memPool.freeMem(handle);
                    }
                    
                    break;
                    
                case "update": // update a key
                    
                    if (index == -1) {
                        
                        String output = "|" + key + "| not updated";
                        output += " because it does not ";
                        output += "exist in the Name database.";
                        System.out.println(output);
                        
                    } 
                    else {
                        String subOpe = parsedCmd.getUpdateOperation();
                        Handle handle = hT.getHandle(index);
                        String record = memPool.read(handle);
                        
                        String[] fields = value.split("<SEP>");
                        String fieldName = fields[1].trim();
                        
                        String[] existFields = record.split("<SEP>");
                        int existIndex = -1;
                        
                        for (int i = 1; i < existFields.length; i++) {
                            if (i % 2 == 1 && 
                                existFields[i].equals(fieldName)) {
                                
                                existIndex = i;
                                break;
                            }
                        }
                        
                        if (subOpe.equals("add")) {
                            String fieldValue = fields[2].trim();
                            String newRecord = existFields[0];
                            
                            for (int i = 1; i < existFields.length; i++) {
                                if (existIndex == -1 ||
                                    (i != existIndex 
                                     && i != existIndex + 1)) {
                                    
                                    newRecord += "<SEP>" + existFields[i];
                                }
                            }
                            
                            newRecord += "<SEP>" + fieldName;
                            newRecord += "<SEP>" + fieldValue;
                            
                            Handle newH = memPool.update(newRecord, handle);
                            hT.setHandle(index, newH);
                            
                            String output = "Updated Record: |";
                            output += newRecord + "|";
                            System.out.println(output);
                        }
                        else {
                            if (existIndex == -1) {
                                String output = "|" + key + "| ";
                                output += "not updated ";
                                output += "because the field |";
                                output += fieldName;
                                output += "| does not exist";
                                
                                System.out.println(output);
                            }
                            else {
                                String newRecord = existFields[0];
                        
                                for (int i = 1; i < existFields.length; i++) {
                                    if (i != existIndex 
                                        && i != existIndex + 1) {
                                        newRecord += "<SEP>";
                                        newRecord += existFields[i];
                                    }
                                }
                                
                                Handle newH = memPool.update(newRecord, handle);
                                hT.setHandle(index, newH);
                                
                                String output = "Updated Record: |";
                                output += newRecord + "|";
                                System.out.println(output);
                            }
                        }
                    }
                    break;
                case "print":
                    
                    if (value.equals("hashtable")) {
                        // print hash table
                        hT.print();
                    } 
                    else {
                        // print memory pool
                        memPool.print();
                    }
                    
                    break;
                default:
                    //  ignore empty lines
            }
        }
//        fw.close();
        sc.close();
    }

}
