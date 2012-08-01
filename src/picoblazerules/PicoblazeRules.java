/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package picoblazerules;

import java.util.ArrayList;

/**
 *
 * @author dc386
 */
public class PicoblazeRules {
    
    /**
     * @param args -> rule file path
     */
    public static void main(String[] args) {
        ArrayList<Rule>  rules = new ArrayList<Rule>();
        
        if (args.length == 0)
        {
            System.out.println("Please specify a rule file.");
            System.exit(0);
        }
        Parser parser = new Parser(args[0]);
        rules = parser.getRules();
        Tree tree = new Tree(rules);
        
        

        //tree.print();
        //System.out.println(tree.getFormattedTable());
        System.out.println(tree.getHexaFormattedTable());
        //System.out.println(tree.getTable());
        //tree.printBinaryTable();
        //System.out.println(tree.getInstructionTable());
        //System.out.println(tree.getVhdlTable());
    }
}
