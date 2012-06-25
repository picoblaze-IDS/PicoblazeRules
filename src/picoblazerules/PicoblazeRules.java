/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package picoblazerules;

import java.io.IOException;

/**
 *
 * @author dc386
 */
public class PicoblazeRules {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Parser parser = new Parser("rules.txt");
        parser.print();
        List<String> words = new ArrayList<String>();

        words.add("her");
        words.add("his");
        words.add("she");
        
        Tree tree = new Tree(words);
        //tree.print();
        System.out.println(tree.getFormattedTable());
        System.out.println(tree.getTable());
        tree.printBinaryTable();
    }
}
