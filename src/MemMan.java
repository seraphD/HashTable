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
     */
    public static void main(String[] args) {
        // This is the main file for the program.
        int initialHashSize = Integer.parseInt(args[1]);
        String inputFile = args[2];
        Hash hT = new Hash(initialHashSize);
        try {
            Scanner sc = new Scanner(new File(inputFile));
            
            while (sc.hasNext()) {
                String cmd = sc.nextLine();
                CommandParser parsedCmd = new CommandParser(cmd);
                String operation = parsedCmd.getOperation();
                String value = parsedCmd.getValue();
                
                String output;
                String tail;
                
                switch (operation) {
                    case "add": // add a key to name space
                        // in case if added value is in form like
                        // something<SEP>something
                        // String[] addArgs = value.split("<SEP>");
                        
                        if (hT.add(value, value)) {
                            output = "|" + value;
                            tail = "| has been added to the Name database.";
                            System.out.println(output + tail);
                        } 
                        else {
                            output = "|" + value + "| ";
                            tail = "duplicates a record ";
                            tail += "already in the Name database.";
                            System.out.println(output + tail);
                        }
                        break;
                    case "delete": // delete a key from name space
                        if (hT.delete(value)) {
                            output = "|" + value + "| ";
                            tail = "has been deleted from the Name database.";
                            System.out.println(output + tail);
                        } 
                        else {
                            output = "|" + value + "| not deleted";
                            tail = " because it does not exist ";
                            tail += "in the Name database.";
                            System.out.println(output + tail);
                        }
                        break;
                    case "update": // update a key
                        String updateOperation = parsedCmd.getUpdateOperation();
                        output = hT.update(updateOperation, value) + "\n";
                        System.out.print(output);
                        break;
                    case "print":
                        hT.print(value);
                        break;
                    default:
//                        System.out.println("empty Command!");
                }
            }
//            fw.close();
            sc.close();
        } 
        catch (IOException e) {
            // Handle File not found exception
            System.out.println("file not founded!");
        }
    }

}
