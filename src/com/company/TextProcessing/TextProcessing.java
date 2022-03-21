package com.company.TextProcessing;

import java.util.*;

public class TextProcessing {


    private static Set<String> stopWords;


    public static List<String> tokensProcessor(String words) {


        String allWords[] = words.split(" ");


        List<String> tokens = new LinkedList<>();


        if(stopWords==null)
            initializeStopWords();

        String last = "";

        for(String word: allWords){

            word = word.trim();

            if(word.length()>1 && !stopWords.contains(word)) // if not a symbol such as HAPPY etc
                if(Character.isLowerCase(word.charAt(0))){
                    word = PorterStemmer.stemWord(word);
                }

            if(!stopWords.contains(word)&&word.length()>1){
                tokens.add(word);
                if(!last.isEmpty())
                    tokens.add(last + " " + word);
                last = word;

            }
        }


        return tokens;

    }

    private static void initializeStopWords()  {

        stopWords = new HashSet<>();
        String words[] = new String[]{
                "i","im", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours", "yourself", "yourselves", "he", "him", "his",
                "himself", "she", "her", "hers", "herself", "it", "its", "itself", "they", "them", "their", "theirs", "themselves", "what", "which",
                "who", "whom", "this", "that", "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have","ive", "has", "had", "having",
                "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while", "of", "at", "by", "for", "with",
                "about", "against", "between", "into", "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out", "on",
                "off", "over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each", "few",
                "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very","dont", "can", "will", "just",
                "don", "should", "now","doesnt"


        };
        stopWords.addAll(List.of(words));

    }


    //used to parse the json to a new format without quatation in order to process it further
    public static String[] parseString(String str){

        StringBuilder builder = new StringBuilder(str);
        boolean inQuotes = false;
        for (int currentIndex = 0; currentIndex < builder.length(); currentIndex++) {
            char currentChar = builder.charAt(currentIndex);
            if (currentChar == '\"') inQuotes = !inQuotes; // toggle state
            if (currentChar == ',' && inQuotes) {
                builder.setCharAt(currentIndex, ';'); // or '♡', and replace later
            }
        }


        return (builder.toString().split(","));
    }




    public static String formatStr(String str) {
//                String clearStr = str.trim().toLowerCase().replaceAll("https?://\\S+\\s?", " LINK ").replaceAll("[^a-zA-Z ]", " ");

        String clearStr = str.trim().toLowerCase()
                .replaceAll("https?://\\S+\\s?", " LINK ")
                .replaceAll("[;:][~-]?[)D]", " HAPPY ")
                .replaceAll("[;:][~-]?[(]", " SAD ")
                .replaceAll("'", "")
                .replaceAll("\\.\\.\\.", " THREEDOTS ")
                .replaceAll("\\.\\.\\.", " TYPE ")
                .replaceAll("\\?\\?+", " DOUBLEQUESTIONMARK ")
                .replaceAll("!!+", " DOUBLEEXCLAMETIONMARK ")
                .replaceAll("!", " EXCLAMETIONMARK ")
                .replaceAll("[^a-zA-Z ]", " ");
        return clearStr;
    }

}
