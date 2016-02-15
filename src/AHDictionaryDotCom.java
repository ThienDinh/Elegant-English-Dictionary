/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import htmlprocessor.HTMLProcessor;
import htmlprocessor.PairString;
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
public class AHDictionaryDotCom extends Reference implements HTMLDoc {

    private String pronunciation;

    public AHDictionaryDotCom() {
    }

    @Override
    public void setWord(String word) {
        super.setWord(word);
        fetchData();
    }

    @Override
    public void fetchData() {
        String definition = "";
        String IPA = "";
        try {
            //HTMLProcessor processorPronunciation = new HTMLProcessor(pronunciationFile, "UTF-8");
            ArrayList<String> pronouncingList = new ArrayList<String>();
            pronouncingList.add("a class=\"questionmark\" href=\"/help/luna/IPA_pron_key.html\" target=\"_blank\" target=\"_blank\"");
            for (String line : HTMLProcessor.getFilteredLines("http://dictionary.reference.com/browse/" + this.getWord(), pronouncingList)) {
                if (line.contains(this.getWord())) {
                    IPA = HTMLProcessor.emptyTags(
                            HTMLProcessor.getPortionBetween(line, new PairString("/b", " class=\"questionmark\""), false));
                    break;
                }
            }
            //HTMLProcessor processor = new HTMLProcessor(download, "UTF-8");
            ArrayList<String> keepingList = new ArrayList<String>();
            keepingList.add("\"rtseg\"");
            keepingList.add("</script> (");
            keepingList.add("Share:");
            ArrayList<PairString> replacingList = new ArrayList<>();
            replacingList.add(new PairString("\"rtseg\"", "\n--------------------------------------------------------\n"));
            replacingList.add(new PairString("\"pseg\"", "\n--------------------------------------------------------\n"));
            replacingList.add(new PairString("\"etyseg\"", "\n--------------------------------------------------------\n"));
            replacingList.add(new PairString("\"idmseg\"", "\n--------------------------------------------------------\n"));
            replacingList.add(new PairString("\"runseg\"", "\n--------------------------------------------------------\n"));
            replacingList.add(new PairString("\"ds-list\"", "\n     "));
            replacingList.add(new PairString("\"anttx\"", "\n     "));
            replacingList.add(new PairString("\"syntx\"", "\n     "));
            replacingList.add(new PairString("\"ds-single\"", "\n     "));
            replacingList.add(new PairString("\"sds-list\"", "\n          "));
            LinkedList<String> fileteredLines = HTMLProcessor.getFilteredLines("https://www.ahdictionary.com/word/search.html?q=" + this.getWord(), keepingList);
            for (String line : fileteredLines) {
                if (line.contains(keepingList.get(0))) {
                    definition += HTMLProcessor.emptyTags(
                            HTMLProcessor.getPortionBetween(line, new PairString("class=\"rtseg\"", "/font"), true),
                            replacingList);
                    String waveLink = HTMLProcessor.getPortionBetween(line, new PairString("/wavs/", "/wavs/"), true);
                    pronunciation = "";
                    for (int i = waveLink.indexOf(".wav") - 1; i > 0; i--) {
                        if (waveLink.charAt(i) == '/') {
                            break;
                        }
                        pronunciation = waveLink.charAt(i) + pronunciation;
                    }
                }
                if (line.contains(keepingList.get(1))) {
                    if (IPA.equals("")) {
                        definition += HTMLProcessor.emptyTags(line.replace("", "\""));
                    } else {
                        definition += IPA;
                    }
                }
                if (line.contains(keepingList.get(2))) {
                    definition += HTMLProcessor.emptyTags(
                            HTMLProcessor.getPortionBetween(line.replace("", "\""), new PairString("div class=\"pseg\"", "span class=\"copyright\""), true),
                            replacingList);
                }
            }
        } catch (Exception ex) {
            definition = "The program couldn't search for the word!\n";
        }
        this.setContent(definition);
    }

    public String getDefinition() {
        return this.getContent();
    }

    public String getPronunciation() {
        return pronunciation;
    }

}
