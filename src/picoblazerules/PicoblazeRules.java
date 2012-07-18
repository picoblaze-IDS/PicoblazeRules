/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package picoblazerules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dc386
 */
public class PicoblazeRules {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        ArrayList<Rule>  rules = new ArrayList<Rule>();
        String word;
        
        if (args.length == 0)
        {
            System.out.println("Please specify a rules file.");
            System.exit(0);
        }
        Parser parser = new Parser(args[0]);
        rules = parser.getRules();
        Tree tree = new Tree(rules);
        //rule.print();
        
        

        //tree.print();
        //System.out.println(tree.getFormattedTable());
        System.out.println(tree.getHexaFormattedTable());
        //System.out.println(tree.getTable());
        //tree.printBinaryTable();
        //System.out.println(tree.getInstructionTable());
        System.out.println(tree.getVhdlTable());
    }
}
