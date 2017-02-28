import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FrequencyMap {
    Map<String, WordInfo> frequencyData;
    public static final String START = "~!START!~";
    
    private static class WordInfo {
        public String word;
        public int frequency = 0;
        public Map<String,Integer> followedByCount = new HashMap<String,Integer>();
        
        public WordInfo(String word) {
            this.word = word;
        }
        
        public void addFollower(String word) {
            if (followedByCount.containsKey(word)) {
                int val = followedByCount.get(word);
                followedByCount.put(word, val + 1);
            } else {
                followedByCount.put(word, 1);
            }
        }
        
        public boolean deleteFollower(String word) {
            return followedByCount.remove(word) != null;
        }
        
        public List<String> getFollowers() {
            List<String> res = new ArrayList<String>(followedByCount.keySet());
            res.sort((s1, s2) -> followedByCount.get(s1) - followedByCount.get(s2));
            return res;
        }
        
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("("+word+", "+frequency+") : [");
            for (String key : followedByCount.keySet()) {
                int count = followedByCount.get(key);
                sb.append("(" + key + "->" + count + ")");
            }
            
            sb.append("]");
            return sb.toString();
            
        }
    }
    
    public FrequencyMap() {
        frequencyData = new HashMap<String, WordInfo>();
    }
    
    public List<String> getWords() {
        return new ArrayList<String>(frequencyData.keySet());
    }
    
    public List<String> getSortedWords() {
        List<String> words = getWords();
        words.sort((w1,w2) -> {
            return getFrequency(w2) - getFrequency(w1);
        });
        return words;
    }
    
    public WordInfo getInfo(String word) {
        return frequencyData.get(word);
    }
    
    public int getFrequency(String word) {
        if (frequencyData.containsKey(word)) {
            return frequencyData.get(word).frequency;
        } else {
            return 0;
        }
    }
    
    public List<String> getFollowers(String word) {
        if (!frequencyData.containsKey(word)) {
            return new ArrayList<String>();
        } else {
            return frequencyData.get(word).getFollowers();
        }
    }
    
    private void addWord(String word, int freq) {
        if (!frequencyData.containsKey(word)) {
            frequencyData.put(word, new WordInfo(word));
        } 
        frequencyData.get(word).frequency += freq;
    }
    
    private void addFollower(String first, String next) {
        addWord(first, 0);
        addWord(next, 0);
        frequencyData.get(first).addFollower(next);
    }
    
    private void removeFollower(String first, String next) {
        if (frequencyData.containsKey(first)) {
            frequencyData.get(first).deleteFollower(next);
        }
    }
    
    public void load(List<String> phrases) {
        Logger.log("Starting frequency analysis on " + phrases.size()
                + " tweets...", getClass().getName());
        for(String phrase : phrases) {
            List<String> words = split(phrase);
            if (words.size() == 0) {
                continue;
            }
            addFollower(START, words.get(0));
            addWord(words.get(0), 1);
            for (int i = 1; i < words.size(); i++) {
                addWord(words.get(i), 1);
                addFollower(words.get(i-1), words.get(i));
            }
        }
        cleanUpTerminals();
        Logger.log("Unique words:\t" + frequencyData.keySet().size(), 
                getClass().getName());
        Logger.log("Most common:\t" + getSortedWords().subList(0, 25), 
                getClass().getName());
       
        
    }
    
    private void cleanUpTerminals() {
        for (String nonTerm : WordLists.NON_TERMINAL_WORDS) {
            for (String terminal : WordLists.TERMINAL_PUNCTUATION) {
                removeFollower(nonTerm, terminal);
            }
        }
    }
    
    private List<String> split(String phrase) {
        String[] splitterooni = phrase.split(" ");
        ArrayList<String> res = new ArrayList<String>();
        for (String x : splitterooni) { //why java
            res.addAll(subsplit(x));
        }
        return res;
    }
    
    public List<String> subsplit(String word) {
        List<String> res = new ArrayList<String>();
        if (word.equals("")) {
            return res;
        }
        for (String garbo : WordLists.GARBAGE) {
            if (word.contains(garbo)){
                return res;
            }
        }
        
        List<String> toSplitOn = new ArrayList<String>();
        toSplitOn.addAll(WordLists.WORDS_WITH_PERIODS);
        toSplitOn.addAll(WordLists.ALL_PUNCTUATION);
        
        for (String punct : toSplitOn) {
            if (word.equals(punct)) {
                res.add(punct);
                return res;
            } else if (word.contains(punct)) {
                int idx = word.indexOf(punct);
                res.addAll(subsplit(word.substring(0, idx)));
                res.add(word.substring(idx,idx + punct.length()));
                res.addAll(subsplit(word.substring(idx + punct.length())));
                return res;
            }
        }
        res.add(word);
        return res;
    }
    
}
