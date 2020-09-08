/**
 * 
 */
//package MemoryStorage;

import java.util.Scanner;

/**
 * @author Lihui Zhang/lihuiz
 * @author Haosu Wang/whaosu
 * @version 2.0
 */
public class CommandParser {
    private String operation = "";
    private String updateOperation = "";
    private String value = "";
    
    /**
     * @param commandLine
     *          row command line read from input file
     */
    public CommandParser(String commandLine) {
        try {
            Scanner cmd = new Scanner(commandLine); // read a command line
            operation = cmd.next(); // read operation
            
            if (operation.equals("update")) { 
                // if the operation is update, read next operation
                updateOperation = cmd.next();
            }
            
            while (cmd.hasNext()) { // read values.
                String nextValue = cmd.next();
               
                value += nextValue + " ";
            }
            value = value.substring(0, value.length() - 1);
            cmd.close();
        } 
        catch (Exception e)  {
            this.operation = "ignore";
        }
    }

    /**
     * @return operation of the command line
     *          include: add/delete/print/update
     */
    public String getOperation() {
        return operation;
    }


    /**
     * @param operation
     *          setter for operation
     */
    public void setOperation(String operation) {
        this.operation = operation;
    }



    /**
     * @return
     *      return update operation
     */
    public String getUpdateOperation() {
        return updateOperation;
    }



    /**
     * @param updateOperation
     *          setter for update operation
     */
    public void setUpdateOperation(String updateOperation) {
        this.updateOperation = updateOperation;
    }



    /**
     * @return 
     *          the value that will be operated
     */
    public String getValue() {
        return value;
    }



    /**
     * @param value
     *          setter for value operated
     */
    public void setValue(String value) {
        this.value = value;
    }
}
