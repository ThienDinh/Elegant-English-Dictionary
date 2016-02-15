/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import htmlprocessor.HTMLProcessor;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/**
 *
 * @author ThienDinh
 */
public class DictionaryDotCom extends Reference implements HTMLDoc{

    public DictionaryDotCom() {
    }

    @Override
    public void setWord(String word) {
        super.setWord(word);
        fetchData();
    }

    public void fetchData() {
        String example = "";
        try {            
            File exampleFile = new File("exam.kin");
            ArrayList<String> keepingList = new ArrayList<String>();
            keepingList.add("class=\"example example_show\"");
            keepingList.add("class=\"example example_hide\"");
            LinkedList<String> filteredLines = HTMLProcessor.getFilteredLines("http://www.reference.com/example-sentences/" + this.getWord(), keepingList);
            for (String line : filteredLines) {
                    example += "    " + HTMLProcessor.emptyTags(line) + "\n";                
            }
        } catch (Exception ex) {
            example = "The program couldn't search for the example!\n";
        }
        super.setContent(example);
    }

    public String getExample() {
        return this.getContent();
    }

}
