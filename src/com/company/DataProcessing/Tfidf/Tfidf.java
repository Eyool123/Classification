package com.company.DataProcessing.Tfidf;

import com.company.DataProcessing.WordCorpus.Corpus;
import com.company.DataProcessing.WordFreq;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Tfidf implements Serializable {

    Map<String, Float> idf;



    public Tfidf(Corpus corpus){
        idf = new HashMap<>();
        //initialize the idf according to corpus
        for(Map.Entry<String, Integer> dfEntry : corpus.getDocumentFreqOfWords().getWordCount().entrySet()){
            idf.put(dfEntry.getKey(), calculateIDF(dfEntry.getValue(),corpus.getNumberOfPeople()));
        }
    }

    private static float calculateIDF(int df, int numberOfDocuments){
        return (float) Math.log((float) numberOfDocuments/df);
    }

    public static float calculateTFIDF(float tf, float idf){
        return tf*idf;
    }
    public float calculateTFIDF(WordFreq documentWords, String word){
        return calculateTFIDF(documentWords.getWordProb(word), idf.get(word));

    }


    void print(){
        this.idf.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .forEach(System.out::println);
    }



}
