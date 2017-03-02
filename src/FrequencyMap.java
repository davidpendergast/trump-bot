import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FrequencyMap {
    /**
     * Keyword that represents the start of a phrase.
     */
    public static final String START = "~!START!~";
    
    private int depth = 1;
    
    /**
     * A mapping from word chains to their followers, counts.
     * (key, value) pairs look like ({the, dog}, (barked, 1))
     */
    private  Map<List<String>, Map<String, Integer>> chainFollowers;
    
    /**
     * A mapping from words to their total frequencies in the source material.
     * (key, value) = (dog, 3)
     */
    private Map<String, Integer> allWords;
    /**
     * A mapping from words to the word chains they belong to.
     * (key, value) = ("dog", {(the, dog), (dog, barked), (dog, ran)})
     */
    private Map<String, Set<List<String>>> chainsIBelongTo;
    
    private Map<List<String>, List<String>> chainCache;
    
    public FrequencyMap(int depth) {
        if (depth >= 1) {
            this.depth = depth;
        }
        this.chainFollowers = new HashMap<List<String>, Map<String, Integer>>();
        this.allWords = new HashMap<String, Integer>();
        this.chainsIBelongTo = new HashMap<String, Set<List<String>>>();
        this.chainCache = new HashMap<List<String>, List<String>>();
    }
    
    public int getDepth() {
        return depth;
    }
    
    /**
     * Copies and returns an immutable view of the given chain. All chains used
     * as keys and values in the state-storing maps should be cleaned first
     * via this method.
     */
    private List<String> getImmutableCopy(List<String> chain) {
        if (!chainCache.containsKey(chain)) {
            List<String> newChain = new ArrayList<String>(chain);
            List<String> immutableChain = Collections.unmodifiableList(newChain);
            chainCache.put(immutableChain, immutableChain);
        }
        return chainCache.get(chain);
    }
    
    public void addChainFollower(List<String> chain, String follower, int frequency) {
        if (chain.size() == 0) {
            return;
        }
        if (chain.size() > getDepth()) {
            throw new IllegalArgumentException("Chain length greater than " + 
                    " depth: "+chain.size()+" > "+getDepth());
        }
        List<String> immChain = getImmutableCopy(chain);
        addWord(follower, 0);
        for (String word : chain) {
            addWord(word, 0);
        }
        
        if (!chainFollowers.containsKey(immChain)) {
            chainFollowers.put(immChain, new HashMap<String, Integer>());
        }
        if (!chainFollowers.get(immChain).containsKey(follower)) {
            chainFollowers.get(immChain).put(follower, 0);
        }
        int count = chainFollowers.get(immChain).get(follower);
        chainFollowers.get(immChain).put(follower, count + frequency);
    }
    
    public void addPhrase(List<String> phrase) {
        for (String word : phrase) {
            addWord(word, 1);
        }
        for (int size = 1; size <= depth; size++) {
            for (int start = 0; start < phrase.size() - size; start++) {
                int end = start + size;
                addChainFollower(phrase.subList(start, end), 
                        phrase.get(end), 1);
            }
        }
    }
    
    public Map<String, Integer> getFollowers(List<String> chain) {
        if (!chainFollowers.containsKey(chain)) {
            return new HashMap<String, Integer>();
        } else {
            return chainFollowers.get(chain);
        }
    }
    
    public List<String> getSortedFollowers(List<String> chain) {
        Map<String, Integer> map = getFollowers(chain);
        List<String> res = map.keySet().stream().collect(Collectors.toList());
        res.sort((s1, s2) -> map.get(s2) - map.get(s2));
        return res;
    }
    
    public void addWord(String word, int frequency) {
        Integer val = allWords.get(word);
        if (val == null) {
            allWords.put(word, frequency);
            chainsIBelongTo.put(word, new HashSet<List<String>>());
        } else {
            allWords.put(word, frequency + val);
        }
    }
    
//    private static class WordInfo {
//        // info about the word itself
//        public String word;
//        public int frequency = 0;
//        
//        // info about followers
//        public Map<String,Integer> followedByCount = new HashMap<String,Integer>();
//        public Set<String> leaders = new HashSet<String>();
//        
//        public WordInfo(String word) {
//            this.word = word;
//        }
//        
//        public void addFollower(String word) {
//            if (followedByCount.containsKey(word)) {
//                int val = followedByCount.get(word);
//                followedByCount.put(word, val + 1);
//            } else {
//                followedByCount.put(word, 1);
//            }
//        }
//        
//        public boolean deleteFollower(String word) {
//            return followedByCount.remove(word) != null;
//        }
//        
//        public List<String> getFollowers() {
//            List<String> res = new ArrayList<String>(followedByCount.keySet());
//            res.sort((s1, s2) -> followedByCount.get(s1) - followedByCount.get(s2));
//            return res;
//        }
//        
//        public List<String> getLeaders() {
//            return new ArrayList<String>(leaders);
//        }
//        
//        public boolean deleteLeader(String word) {
//            return leaders.remove(word);
//        }
//        
//        public void addLeader(String word) {
//            leaders.add(word);
//        }
//        
//        public String toString() {
//            StringBuilder sb = new StringBuilder();
//            sb.append("("+word+", "+frequency+") : followers=[");
//            int index = 0;
//            for (String key : followedByCount.keySet()) {
//                int count = followedByCount.get(key);
//                sb.append("(" + key + "->" + count + ")");
//                index++;
//                if (index != followedByCount.size()) {
//                    sb.append(", ");
//                }
//                    
//            }
//            sb.append("], leaders=[");
//            index = 0;
//            for (String leader : leaders) {
//                sb.append(leader);
//                index++;
//                if (index != leaders.size()) {
//                    sb.append(", ");
//                }
//            }
//            sb.append("]");
//            return sb.toString();
//            
//        }
//    }
    
//    public FrequencyMap() {
//        frequencyData = new HashMap<String, WordInfo>();
//    }
//    
//    public List<String> getWords() {
//        return new ArrayList<String>(frequencyData.keySet());
//    }
    
//    public List<String> getSortedWords() {
//        List<String> words = getWords();
//        words.sort((w1,w2) -> {
//            return getFrequency(w2) - getFrequency(w1);
//        });
//        return words;
//    }
//    
//    public WordInfo getInfo(String word) {
//        return frequencyData.get(word);
//    }
//    
//    public int getFrequency(String word) {
//        if (frequencyData.containsKey(word)) {
//            return frequencyData.get(word).frequency;
//        } else {
//            return 0;
//        }
//    }
    
//    public List<String> getFollowers(String word) {
//        if (!frequencyData.containsKey(word)) {
//            return new ArrayList<String>();
//        } else {
//            return frequencyData.get(word).getFollowers();
//        }
//    }
//    
//    public List<String> getFollowers(List<String> wordSequence) {
//        int n = wordSequence.size();
//        if (n == 0) {
//            
//        }
//        List<List<String>> result = new ArrayList<List<String>>();
//        result.add(getFollowers(wordSequence.get(n-1)));
//        return result;
//    }
//    
//    private void addWord(String word, int freq) {
//        if (!frequencyData.containsKey(word)) {
//            frequencyData.put(word, new WordInfo(word));
//        } 
//        frequencyData.get(word).frequency += freq;
//    }
//    
//    private void addFollower(String first, String next) {
//        addWord(first, 0);
//        addWord(next, 0);
//        frequencyData.get(first).addFollower(next);
//        frequencyData.get(next).addLeader(first);
//    }
//    
//    private void removeFollower(String first, String next) {
//        if (frequencyData.containsKey(first)) {
//            frequencyData.get(first).deleteFollower(next);
//        }
//        if (frequencyData.containsKey(next)) {
//            frequencyData.get(next).deleteLeader(first);
//        }
//    }
    
    public void load(List<String> phrases) {
        Logger.log("Starting frequency analysis on " + phrases.size()
                + " tweets...", getClass().getName());
        for(String rawText : phrases) {
            List<String> phrase = split(rawText);
            if (phrase.size() == 0) {
                continue;
            }
            phrase.add(0, START);
            addPhrase(phrase);
            
        }
        //cleanUpTerminals();
//        Logger.log("Unique words:\t" + frequencyData.keySet().size(), 
//                getClass().getName());
//        Logger.log("Most common:\t" + getSortedWords().subList(0, 25), 
//                getClass().getName());
    }
    
//    private void cleanUpTerminals() {
//        for (String nonTerm : WordLists.NON_TERMINAL_WORDS) {
//            for (String terminal : WordLists.TERMINAL_PUNCTUATION) {
//                removeFollower(nonTerm, terminal);
//            }
//        }
//    }
    
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
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (List<String> chain : chainFollowers.keySet()) {
            sb.append(chain + " ->\t" + chainFollowers.get(chain) + "\n");
        }
        return sb.toString();
    }
    
    public static void main(String[] args) {
        FrequencyMap map = new FrequencyMap(3);
        List<String> phrase = Arrays.asList("the", "dog", "barked", "and", "the", "dog", "ran");
        map.addPhrase(phrase);
        System.out.println(map);
        System.out.println(map.getFollowers(Arrays.asList("the", "dog")));
    }
    
}
