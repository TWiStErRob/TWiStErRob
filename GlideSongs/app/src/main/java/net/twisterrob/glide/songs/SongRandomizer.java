package net.twisterrob.glide.songs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SongRandomizer {
    protected List<String> adverb = Arrays.asList("brightly", "curiously", "delightfully", "happily");
    protected List<String> verb = Arrays.asList("amuse", "cure", "drumming", "jog");

    protected List<String> adjective = Arrays.asList("beautiful", "red", "mysterious", "witty", "fluffy");
    protected List<String> noun = Arrays.asList("idea", "sea", "love", "joy");

    protected List<String> qualifier = Arrays.asList("(Remix)", "(Part I)", "(Part II)", "(Radio Edit)", "(Extended)");

    protected double probAdverbVerb = 0.15;
    protected double probQualifier = 0.20;
    protected double probCoauthor = 0.05;

    public void setAdverbs(List<String> adverbs) {
        this.adverb = check(adverbs);
    }

    public void setVerbs(List<String> verbs) {
        this.verb = check(verbs);
    }

    public void setAdjectives(List<String> adjectives) {
        this.adjective = check(adjectives);
    }

    public void setNouns(List<String> nouns) {
        this.noun = check(nouns);
    }

    public void setQualifiers(List<String> qualifiers) {
        this.qualifier = check(qualifiers);
    }

    public void setCoAuthorProbabilty(double percentage) {
        this.probCoauthor = check(percentage);
    }

    public void setActionProbability(double percentage) {
        this.probAdverbVerb = check(percentage);
    }

    public void setQualifierProbability(double percentage) {
        this.probQualifier = check(percentage);
    }

    public int getArtistCount() {
        int artistCount = adjective.size() * noun.size();
        return artistCount * artistCount; // account for feat.
    }

    public int getTitleCount() {
        int adverbVerbCount = adverb.size() * verb.size();
        int adjectiveNounCount = adjective.size() * noun.size();
        return (adverbVerbCount + adjectiveNounCount) * qualifier.size();
    }

    public String randomArtist() {
        return randomArtist(true);
    }

    public String randomArtist(boolean allowCoauthor) {
        StringBuilder title = new StringBuilder();
        title.append(rnd(adjective)).append(" ").append(rnd(noun));

        if (allowCoauthor && Math.random() < probCoauthor) {
            title.append(" feat. ").append(randomArtist(false));
        }

        camelize(title);
        return title.toString();
    }

    public String randomTitle() {
        StringBuilder title = new StringBuilder();
        if (Math.random() < probAdverbVerb) {
            title.append(rnd(adverb)).append(" ").append(ing(rnd(verb)));
        } else {
            title.append(rnd(adjective)).append(" ").append(rnd(noun));
        }

        if (Math.random() < probQualifier) {
            title.append(" ").append(rnd(qualifier));
        }

        camelize(title);
        return title.toString();
    }

    private static void camelize(StringBuilder title) {
        boolean afterSeparator = true;
        for (int i = 0; i < title.length(); ++i) {
            char c = title.charAt(i);
            if (afterSeparator && !isFeaturingAt(title, i)) {
                title.setCharAt(i, Character.toUpperCase(c));
            }
            afterSeparator = !Character.isLetter(c);
        }
    }

    private static boolean isFeaturingAt(StringBuilder title, int i) {
        return title.subSequence(i, Math.min(i + 5, title.length() - 1)).equals("feat.");
    }

    private String ing(String verb) {
        if (verb.endsWith("ing")) return verb;
        if (verb.endsWith("e")) verb = verb.substring(0, verb.length() - 1);
        return verb + "ing";
    }

    private static final Random RND = new Random();

    protected static String rnd(List<String> arr) {
        int randomIndex = RND.nextInt(arr.size());
        return arr.get(randomIndex);
    }


    protected List<String> check(List<String> list) {
        if (list == null) {
            throw new NullPointerException("Cannot set a null list");
        }
        return list;
    }

    protected double check(double percentage) {
        if (percentage < 0 || 1 < percentage || Double.isNaN(percentage)) {
            throw new IllegalArgumentException("Expected a percentage expressed within [0, 1], got " + percentage);
        }
        return percentage;
    }

    public static List<String> read(InputStream file) throws IOException {
        List<String> lines = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file, "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("//")) continue;
                if (line.trim().isEmpty()) continue;
                lines.add(line);
            }
            return lines;
        } finally {
            file.close();
        }
    }

    public static SongRandomizer from(InputStream adjectives, InputStream adverbs, InputStream nouns, InputStream verbs, InputStream qualifiers) throws IOException {
        SongRandomizer randomizer = new SongRandomizer();
        if (adjectives != null) randomizer.setAdjectives(read(adjectives));
        if (adverbs != null) randomizer.setAdverbs(read(adverbs));
        if (verbs != null) randomizer.setVerbs(read(verbs));
        if (nouns != null) randomizer.setNouns(read(nouns));
        if (qualifiers != null) randomizer.setQualifiers(read(qualifiers));
        return randomizer;
    }
}
