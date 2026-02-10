package nl.sol.puzzles.clues;

import java.util.*;
import java.util.regex.Pattern;

import nl.sol.puzzles.dictionary.WordSaladDictionary;
import nl.sol.puzzles.dictionary.WeightedWordSet;
import nl.sol.puzzles.path.Path;
import nl.sol.puzzles.path.PathReducer;

public class WordSaladClue extends AbstractClue<Character, String> {

    private final List<Integer> lengths;
    private final String mainClue;
    private final double certainty; // 0..1
    private final WordSaladDictionary dict;

    // Cached “signature” of the main clue.
    private final WeightedWordSet mainSignature;

    // Cache per-word expanded signatures (huge perf win).
    private final Map<String, WeightedWordSet> wordSignatureCache = new HashMap<>();

    private static final EnumSet<WordSaladDictionary.Pos> CLUE_POS =
            EnumSet.of(WordSaladDictionary.Pos.NOUN, WordSaladDictionary.Pos.VERB, WordSaladDictionary.Pos.ADJ);

    private static final EnumSet<WordSaladDictionary.Pos> ANSWER_POS =
            EnumSet.of(WordSaladDictionary.Pos.NOUN, WordSaladDictionary.Pos.VERB);

    private static final Pattern NON_LETTERS = Pattern.compile("[^a-z]");

    public WordSaladClue(int length, String mainClue, double certainty, WordSaladDictionary dict) {
        this(List.of(length), mainClue, certainty, dict);
    }

    public WordSaladClue(List<Integer> lengths, String mainClue, double certainty, WordSaladDictionary dict) {
        this.lengths = List.copyOf(lengths);
        this.mainClue = Objects.requireNonNull(mainClue);
        this.certainty = certainty;
        this.dict = Objects.requireNonNull(dict);

        this.mainSignature = buildClueSignature(mainClue);
    }

    @Override
    public int getLength() {
        return lengths.stream().mapToInt(Integer::intValue).sum();
    }

    @Override
    public String toString() {
        return "WordSaladClue[length=" + getLength()
                + ", mainClue=" + mainClue
                + ", certainty=" + certainty
                + ", answers=" + getPossibleAnswers() + "]";
    }

    @Override
    public boolean isSolution(Path<Character> path, PathReducer<Character, String> pathReducer) {
        String reduced = pathReducer.reduce(path).orElse(null);
        if (reduced == null) return false;

        List<String> parts = splitByLengths(reduced, lengths);
        if (parts == null) return false;

        // All parts must be valid noun/verb words
        List<String> normalizedParts = new ArrayList<>(parts.size());
        for (String p : parts) {
            String w = normalize(p);
            if (w == null) return false;
            if (!dict.hasAnyPos(w, ANSWER_POS)) return false;
            normalizedParts.add(w);
        }

        // Build candidate signature as union of expanded signatures of its parts.
        WeightedWordSet candidate = new WeightedWordSet();
        for (String w : normalizedParts) {
            WeightedWordSet expanded = expandedWordSignature(w, ANSWER_POS);
            // Merge: keep the strongest weight per token
            merge(candidate, expanded);
        }

        // Score = intersectionWeight / mainTotalWeight
        double denom = Math.max(1e-9, mainSignature.totalWeight());
        double score = mainSignature.intersectionWeight(candidate) / denom;

        return score >= certainty;
    }

    private WeightedWordSet buildClueSignature(String clueText) {
        // Seed tokens from clue
        String[] rawTokens = clueText.split("\\s+");

        // Start with seed weights at 1.0
        WeightedWordSet sig = new WeightedWordSet();
        for (String t : rawTokens) {
            String w = normalize(t);
            if (w == null) continue;

            // only keep clue tokens that have desired POS
            if (!dict.hasAnyPos(w, CLUE_POS)) continue;

            sig.add(w, 1.0);
        }

        // Expand one hop from each seed word (depth 1)
        // and add definition tokens with a lower weight.
        // This is intentionally bounded.
        for (String seed : new ArrayList<>(sig.words())) {
            WeightedWordSet expanded = expandedWordSignature(seed, CLUE_POS);
            merge(sig, expanded);
        }

        return sig;
    }

    private WeightedWordSet expandedWordSignature(String word, EnumSet<WordSaladDictionary.Pos> pos) {
        // If you want different caches per POS, include pos in the key.
        String key = word + "|" + pos.toString();

        return wordSignatureCache.computeIfAbsent(key, k -> {
            WeightedWordSet sig = new WeightedWordSet();

            // The word itself gets full weight
            sig.add(word, 1.0);

            // Definitions get reduced weight
            // (Tune this constant; 0.6 is a decent starting point)
            double defWeight = 0.6;

            // Cap senses so we don’t explode
            int maxSenses = 2;

            for (String tok : dict.definitionTokens(word, pos, maxSenses)) {
                String w = normalize(tok);
                if (w == null) continue;

                // Only keep words that have acceptable POS (optional but recommended)
                if (!dict.hasAnyPos(w, pos)) continue;

                sig.add(w, defWeight);
            }

            return sig;
        });
    }

    private static void merge(WeightedWordSet into, WeightedWordSet from) {
    for (var e : from.entries()) {
        into.add(e.getKey(), e.getValue());
    }
}

    private static List<String> splitByLengths(String s, List<Integer> lens) {
        int idx = 0;
        var out = new ArrayList<String>(lens.size());
        for (int len : lens) {
            if (len < 0) return null;
            int next = idx + len;
            if (next > s.length()) return null;
            out.add(s.substring(idx, next));
            idx = next;
        }
        if (idx != s.length()) return null;
        return out;
    }

    private static String normalize(String token) {
        if (token == null) return null;
        String w = token.toLowerCase(Locale.ROOT);
        w = NON_LETTERS.matcher(w).replaceAll("");
        if (w.length() < 2) return null;
        return w;
    }
}