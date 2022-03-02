package com.company.DataProcessing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class WordFreq implements Serializable {

    private Map<String, Integer> wordCount;

    public int getNumberOfWords() {
        return numberOfWords;
    }

    private int numberOfWords;

    public WordFreq(){
        this.wordCount = new HashMap<>();
        this.numberOfWords = 0;
    }

    public WordFreq(String[] words){
        this();
        this.addAllWords(words);
    }

    public void addWord(String word){

        numberOfWords++;

        if(wordCount.containsKey(word))
            wordCount.put(word,wordCount.get(word)+1);
        else
            wordCount.put(word, 1);
    }

    public void addWordMul(String word, int n){

        numberOfWords+=n;

        if(wordCount.containsKey(word))
            wordCount.put(word,wordCount.get(word)+n);
        else
            wordCount.put(word, n);
    }

    public void addAllWords(String[] words){

        for(String word : words){
            this.addWord(word);
        }
    }

    public Map<String, Integer> getWordCount(){
        return this.wordCount;
    }

    public float getWordProb(String word){
        if(wordCount.containsKey(word))
            return ( (float) wordCount.get(word) ) /numberOfWords;
        else
            return 0;
    }


    public Map<String, Float>getWordsProbMap(){
        Map<String, Float> wordsFreq = new HashMap<>();

        for(String word : this.wordCount.keySet() ){
            wordsFreq.put(word, getWordProb(word));
        }
        return wordsFreq;
    }



    public void printWordCount(){
        wordCount.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(System.out::println);
    }

    public void printWordProb(){
        this.getWordsProbMap().entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(System.out::println);
    }


}
