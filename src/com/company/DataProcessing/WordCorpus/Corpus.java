package com.company.DataProcessing.WordCorpus;

import com.company.DataProcessing.Personalities.PersonData;
import com.company.DataProcessing.WordFreq;
import com.company.TextProcessing.TextProcessing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.company.TextProcessing.TextProcessing.*;

public class Corpus implements Serializable {


    private List<PersonData> personDataList;
    private WordFreq documentFreqOfWords; // word occur (1) in each document (person)
    private int totalNumberOfWords;

    private static final long serialVersionUID = 1L;


    public Corpus(){
        this.personDataList = new LinkedList<>();
        documentFreqOfWords = new WordFreq();
        this.totalNumberOfWords = 0;
    }




    //A copy constructor in order to filter words with minimum Document Frequency in corpus documents
    public Corpus(Corpus corpus1,Corpus corpus2, float filterMinimumDF, float filterMaximumDF){
        this();

        for(PersonData data: corpus1.getPersonDataList()){
            WordFreq filteredWordFreq = new WordFreq();


            //we iterate over each word freqMapCount and create a new word freqMap of filtered words only
            for (Map.Entry<String, Integer> word : data.getPersonWordFreq().getWordCount().entrySet()){

                //if word df is more than min than we add it to word count
//                float df = corpus.getDocumentFrequencyOfWord(word.getKey());
                float df1 = corpus1.getWordFreqInCorpus(word.getKey());
                float df2 = corpus2.getWordFreqInCorpus(word.getKey());

                if( (df1>=filterMinimumDF&&df1<=filterMaximumDF) ||  (df2>=filterMinimumDF&&df2<=filterMaximumDF) )
                    filteredWordFreq.addWordMul(word.getKey(), word.getValue());

            }

            addPersonToCorpus(new PersonData(data.getPersonalityType(), filteredWordFreq));
        }


        for(PersonData data: corpus2.getPersonDataList()){
            WordFreq filteredWordFreq = new WordFreq();


            //we iterate over each word freqMapCount and create a new word freqMap of filtered words only
            for (Map.Entry<String, Integer> word : data.getPersonWordFreq().getWordCount().entrySet()){

                //if word df is more than min than we add it to word count
//                float df = corpus.getDocumentFrequencyOfWord(word.getKey());
                float df1 = corpus1.getWordFreqInCorpus(word.getKey());
                float df2 = corpus2.getWordFreqInCorpus(word.getKey());

                if( (df1>=filterMinimumDF&&df1<=filterMaximumDF) ||  (df2>=filterMinimumDF&&df2<=filterMaximumDF)  )
                    filteredWordFreq.addWordMul(word.getKey(), word.getValue());

            }

            addPersonToCorpus(new PersonData(data.getPersonalityType(), filteredWordFreq));
        }


    }





    public Corpus(Corpus corpus, int dimension, Boolean isDim){
        this();

        for(PersonData data: corpus.getPersonDataList()){

            if(data.getPersonalityType().isDim(dimension)==isDim){
                WordFreq filteredWordFreq = new WordFreq();

                //we iterate over each word freqMapCount and create a new word freqMap of filtered types only
                for (Map.Entry<String, Integer> word : data.getPersonWordFreq().getWordCount().entrySet()){

                    filteredWordFreq.addWordMul(word.getKey(), word.getValue());

                }

                addPersonToCorpus(new PersonData(data.getPersonalityType(), filteredWordFreq));
            }

        }

    }

    public void addPersonToCorpus(PersonData personData){
        personDataList.add(personData);

        for(String word : personData.getPersonWordFreq().getWordCount().keySet()){
            documentFreqOfWords.addWord(word);
            totalNumberOfWords++;
        }
    }
    public int getNumberOfPeople(){
        return this.personDataList.size();
    }

    public List<PersonData> getPersonDataList() {
        return personDataList;
    }

    public WordFreq getDocumentFreqOfWords() {
        return documentFreqOfWords;
    }

    public int getDocumentFrequencyOfWord(String word){
        if(documentFreqOfWords.getWordCount().containsKey(word))
            return documentFreqOfWords.getWordCount().get(word);
        else
            return 0;
    }


    public int getNumberOfUniqueWords(){
        return documentFreqOfWords.getWordCount().size();
    }
    void printCorpus(){
        this.documentFreqOfWords.printWordCount();
        System.out.println();
        System.out.println("Number of people: "+this.getNumberOfPeople());
        System.out.println("Number of different words: "+this.documentFreqOfWords.getWordCount().size());
        System.out.println("Total number of words: "+totalNumberOfWords);
    }


    //get in how many documents in the corpus the word appears
    int getNumberOfDocumentWordAppearsIn(String word){

        if(documentFreqOfWords.getWordCount().containsKey(word)){
            return documentFreqOfWords.getWordCount().get(word);
        }else
            return 0;
    }

    //return 0-1 of word freq in corpus wordAppearInNDocuments / numberOfDocuments
    float getWordFreqInCorpus(String word){
        return (float) getNumberOfDocumentWordAppearsIn(word)/this.getNumberOfPeople();

    }

//    public static Corpus createCorpusFromFile(String filepath){
//
//        Corpus corpus = new Corpus();
//
//        BufferedReader reader = null;
//        String line = "";
//
//        try {
//            reader = new BufferedReader(new FileReader(filepath));
//            line = reader.readLine(); // reads first header line
//            int count = 0;
//            while ((line = reader.readLine()) != null ) {
//
//                String[] row = parseString(line); //used to split between csv columns
//
//                String formattedString = formatStr(row[1]); //inital clear for the text
//                String wordTokens[] = tokensProcessor(formattedString); //tokenizes the text and stemms the word
//
//                PersonData newPerson = new PersonData(row[2].replaceAll("[^a-zA-Z ]", "").toUpperCase(Locale.ROOT),wordTokens);
//                corpus.addPersonToCorpus(newPerson);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                reader.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return corpus;
//
//    }
public static Corpus createCorpusFromFile(String filepath){

        Corpus corpus = new Corpus();

        BufferedReader reader = null;
        String line = "";

        try {
            reader = new BufferedReader(new FileReader(filepath));
            line = reader.readLine(); // reads first header line
            int count = 0;
            while ((line = reader.readLine()) != null ) {

                String[] row = line.split(",", 2); //used to split between csv columns

                String formattedString = formatStr(row[1]); //inital clear for the text
                List<String> wordTokens = tokensProcessor(formattedString); //tokenizes the text and stemms the word

                PersonData newPerson = new PersonData(row[0], wordTokens);
                corpus.addPersonToCorpus(newPerson);


            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return corpus;

    }

    public static Corpus createCorpusFromImdbFile(String filepath){ //TODO REMOVE AND ADD ABOVE

        Corpus corpus = new Corpus();

        BufferedReader reader = null;
        String line = "";

        try {
            reader = new BufferedReader(new FileReader(filepath));
            line = reader.readLine(); // reads first header line
            int count = 0;
            while ((line = reader.readLine()) != null ) {

                String[] row = line.split(",", 2); //used to split between csv columns

                String formattedString = formatStr(row[0]); //inital clear for the text
                List<String> wordTokens = tokensProcessor(formattedString); //tokenizes the text and stemms the word



                if(row[1].equals("positive")){

                    PersonData newPerson = new PersonData("INFJ", wordTokens);
                    corpus.addPersonToCorpus(newPerson);

                }else if(row[1].equals("negative")){
                    PersonData newPerson = new PersonData("ENFJ", wordTokens);
                    corpus.addPersonToCorpus(newPerson);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return corpus;

    }






}
