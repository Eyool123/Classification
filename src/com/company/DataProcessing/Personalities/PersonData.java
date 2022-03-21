package com.company.DataProcessing.Personalities;

import com.company.DataProcessing.WordFreq;

import java.io.Serializable;
import java.util.List;

public class PersonData implements Serializable {

    private WordFreq personWordFreq;
    private PersonalityType personalityType;

    public PersonData(String personalityType, List<String> words){

        this.personalityType = new PersonalityType(personalityType);
        this.personWordFreq= new WordFreq(words);

    }
    public PersonData(PersonalityType personalityType, WordFreq wordFreq){
        this.personalityType = personalityType;
        this.personWordFreq = wordFreq;
    }


    public PersonalityType getPersonalityType() {
        return personalityType;
    }

    public WordFreq getPersonWordFreq() {
        return personWordFreq;
    }
}
