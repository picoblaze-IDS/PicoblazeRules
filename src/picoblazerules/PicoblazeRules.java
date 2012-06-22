/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package picoblazerules;

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
    public static void main(String[] args) {
        // TODO code application logic here
        List toto = new ArrayList<String>();
        
        toto.add("her");
        toto.add("his");
        toto.add("she");
        
        Tree tree = new Tree(toto);
        //tree.print();
        System.out.println(tree.getTable());
    }
}
