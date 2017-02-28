import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordLists {
    
    public static final List<String> GARBAGE = new ArrayList<String>();
    public static final List<String> NON_TERMINAL_WORDS = new ArrayList<String>();
    public static final List<String> TERMINAL_PUNCTUATION = new ArrayList<String>();
    public static final List<String> MIDSENTENCE_PUNCTUATION = new ArrayList<String>();
    public static final List<String> ALL_PUNCTUATION = new ArrayList<String>();
    public static final List<String> WORDS_WITH_PERIODS = new ArrayList<String>();
    
    static {
        String[] garbage = {"000", "&amp", "http", "https", "//t.co/", "\"", ")", "("};
        Collections.addAll(GARBAGE, garbage);
        
        
        String[] terminalPunctuation = {".", "!", "?"};
        Collections.addAll(TERMINAL_PUNCTUATION, terminalPunctuation);
        
        String[] midPunctuation = {",", "...", "--", "-", "–", ":", ";", "\""};
        Collections.addAll(MIDSENTENCE_PUNCTUATION, midPunctuation);
        
        String[] withPeriods = {"U.S.", "P.M.", "A.M.", "1:00", "2:00", "3:00",
                "4:00", "5:00", "6:00", "7:00", "8:00", "9:00", "10:00", 
                "11:00", "12:00", "etc."};
        Collections.addAll(WORDS_WITH_PERIODS, withPeriods);
        
        ALL_PUNCTUATION.addAll(MIDSENTENCE_PUNCTUATION);
        ALL_PUNCTUATION.addAll(TERMINAL_PUNCTUATION);
        
        String[] nonTerminals = {",", "the", "a", "and", "in", "on", "my", 
                "our", "to", "of", "for", "with"};
        Collections.addAll(NON_TERMINAL_WORDS, nonTerminals);
        NON_TERMINAL_WORDS.addAll(ALL_PUNCTUATION);
    }

}
