package com.company.DataProcessing.Personalities;

import com.company.DataProcessing.Tfidf.Tfidf;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PersonDataVec implements Serializable {


    private Map<String, Float> tfidfWords;
    private PersonalityType personalityType;

    public PersonDataVec(PersonData personData, Tfidf tfidf) {

        this.personalityType = personData.getPersonalityType();

        tfidfWords = new HashMap<>();

        //for each unique word in wordFreq calculates tfidf and inserts to tfidf of words dictenery
        for(String word : personData.getPersonWordFreq().getWordCount().keySet()){
            tfidfWords.put(word,tfidf.calculateTFIDF(personData.getPersonWordFreq(),word));
        }

    }

    public Map<String, Float> getTfidfWords() {
        return tfidfWords;
    }


    public PersonalityType getPersonalityType() {
        return personalityType;
    }

}
