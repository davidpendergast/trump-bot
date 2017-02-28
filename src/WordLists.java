import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WordLists {
    
    public static final Set<String> GARBAGE = new HashSet<String>();
    public static final Set<String> BASIC = new HashSet<String>();
    public static final List<String> TERMINAL_PUNCTUATION = new ArrayList<String>();
    public static final List<String> MIDSENTENCE_PUNCTUATION = new ArrayList<String>();
    public static final List<String> ALL_PUNCTUATION = new ArrayList<String>();
    public static final Set<String> WORDS_WITH_PERIODS = new HashSet<String>();
    
    static {
        String[] garbage = {"000", "&amp", "http", "https", "//t.co/", "\"", ")", "("};
        Collections.addAll(GARBAGE, garbage);
        
        String[] basic = {"the", "to", "a", "is", "and", "of", "in", "i", "on", 
                "for", "be", "will", "you", "at", "that", "are", "with", "have",
                "by", "it", "not", "was", "has", "my", "our", "your", "he", 
                "all", "from", "his", "just", "as", "this", "we", "about", 
                "who", "should", "they", "me", "but", "so", "people", "out", 
                "very", "do", "get", "can", "an", "would", "like", "more", 
                "what"};
        Collections.addAll(BASIC, basic);
        
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
    }

}
