package com.company.DataProcessing.Production;

import com.company.DataProcessing.Personalities.PersonData;
import com.company.DataProcessing.Personalities.PersonalityType;
import com.company.DataProcessing.Tfidf.Tfidf;
import com.company.DataProcessing.VectorCorpus.CorpusVec;
import com.company.DataProcessing.WordFreq;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.company.TextProcessing.TextProcessing.formatStr;
import static com.company.TextProcessing.TextProcessing.tokensProcessor;

public class SampleGenerator implements Serializable {


    private Map<String, Integer> wordToIndex;
    private String indexToWord[];

    public float[] maxValues; // max values of each feature, used for normalization

    private Tfidf tfidf;

    public SampleGenerator(CorpusVec corpusVec){

        this.wordToIndex = corpusVec.wordToIndex;
        this.indexToWord = corpusVec.indexToWord;
        this.maxValues = corpusVec.maxValues;
        this.tfidf = corpusVec.tfidf;

    }


    // gets a string as a parameter and returns a float as input to the model
    public float[] generateSample(String string){

        String formattedString = formatStr(string); //initial clear for the text
        List<String> wordTokens = tokensProcessor(formattedString); //tokenizes the text and stems the word



        float[] modelInputs = new float[indexToWord.length];


        List<String> filteredTokens = new LinkedList<>();


        //check words to filter, insert only words that in the corpus/tfidf
        for (String words : wordTokens){

            if (wordToIndex.containsKey(words)){
                filteredTokens.add(words);
            }
        }

        //We create words freq for the filtered sample

        WordFreq wordFreq = new WordFreq(filteredTokens);


        //for each unique word in wordFreq calculates tfidf and inserts to model inputs
        for(String word : wordFreq.getWordCount().keySet()){
            int index = wordToIndex.get(word);
            modelInputs[index] = tfidf.calculateTFIDF(wordFreq,word);
        }

        CorpusVec.normalizeDocument(modelInputs, maxValues);

        return modelInputs;

    }

    public void saveSampleGeneratorToFile(String filePath){

        try {

            FileOutputStream fileOut = new FileOutputStream(filePath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this);
            objectOut.close();
            objectOut.flush();

        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            System.out.println("The sample generator was successfully written to a file "+filePath);
        }

    }


}
