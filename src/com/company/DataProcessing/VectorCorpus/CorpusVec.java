package com.company.DataProcessing.VectorCorpus;

import com.company.DataProcessing.Personalities.Dim1;
import com.company.DataProcessing.Personalities.PersonData;
import com.company.DataProcessing.Personalities.PersonDataVec;
import com.company.DataProcessing.Personalities.PersonalityType;
import com.company.DataProcessing.Tfidf.DifferenceTFIDF;
import com.company.DataProcessing.Tfidf.Tfidf;
import com.company.DataProcessing.WordCorpus.Corpus;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.*;

public class CorpusVec implements Serializable {



    private Map<String, Integer> wordToIndex;
    private String indexToWord[];


    public PersonalityType[] classVec;
    public float[][] featuresVec; //Feature matrix of documents and tfidf in each

    public int[] sumOfDims;

    public float[] maxValues; // max values of each feature, used for normalization






    //Corpus vec is used to migrate our corpus to a vector representation of tfidf features
    public CorpusVec(Corpus corpus, Tfidf tfidf){
        List<PersonDataVec> personDataVecList;

        sumOfDims = new int[4];
        Arrays.fill(sumOfDims, 0);



        personDataVecList = new LinkedList<>();

        for (PersonData personData : corpus.getPersonDataList()){
            personDataVecList.add(new PersonDataVec(personData,tfidf));

            if(personData.getPersonalityType().isDim(1))
                sumOfDims[0]++;

            if(personData.getPersonalityType().isDim(2))
                sumOfDims[1]++;

            if(personData.getPersonalityType().isDim(3))
                sumOfDims[2]++;

            if(personData.getPersonalityType().isDim(4))
                sumOfDims[3]++;

        }



        classVec = new PersonalityType[personDataVecList.size()];
        featuresVec = new float[personDataVecList.size()][corpus.getNumberOfUniqueWords()];

        wordToIndex = new HashMap<>();
        indexToWord = new String[corpus.getNumberOfUniqueWords()];


        //Initalizes the dictionery in order to link a word to index in vector\matrix
        int i=0;
        for(String uniqueWord : corpus.getDocumentFreqOfWords().getWordCount().keySet()){
            wordToIndex.put(uniqueWord, i);
            indexToWord[i] = uniqueWord;
            i++;
        }

        i=0;
        for(PersonDataVec personDataVec : personDataVecList){
            classVec[i] = personDataVec.getPersonalityType();
            for(Map.Entry<String, Float> tfidfWord : personDataVec.getTfidfWords().entrySet()){
                featuresVec[i][wordToIndex.get(tfidfWord.getKey())] = tfidfWord.getValue();
            }
            i++;
        }


        this.initMaxValues();
        CorpusVec.normalizeFeatures(featuresVec, this.maxValues);
    }



    public static void normalizeFeatures(float[][] featuresVec, float[] maxValuesM) {
        // min max normalization for feature vector


        for (int i=0; i<featuresVec[0].length; i++){
            for (int j=0; j<featuresVec.length; j++){
                featuresVec[j][i] = featuresVec[j][i]/ maxValuesM[i];
            }
        }


    }

    private void initMaxValues() {

        maxValues = new float[featuresVec[0].length];
        Arrays.fill(maxValues, 0);

        for (int i=0; i<featuresVec[0].length; i++){
            for (int j=0; j<featuresVec.length; j++){
                maxValues[i] = Math.max(featuresVec[j][i], maxValues[i]);
            }
        }
    }

    public int getNumberOfDocuments(){
        return classVec.length;
    }



    public void getSumTFIDFvec(){

        float sumI[] = new float[indexToWord.length];
        float sumE[] = new float[indexToWord.length];

        int numOfI = 0;
        int numOfE = 0;

        List<DifferenceTFIDF> printList = new LinkedList<>();

        for (int i=0; i<classVec.length; i++){
            if(classVec[i].getDim1()==Dim1.EXTRAVERSION){
                numOfE++;
                for(int j=0; j<indexToWord.length; j++){
                    sumE[j] += featuresVec[i][j];
                }

            }else {
                numOfI++;
                for(int j=0; j<indexToWord.length; j++){
                    sumI[j] += featuresVec[i][j];
                }
            }

        }

        for(int j=0; j<indexToWord.length; j++){
            sumI[j] /= ((double)numOfI);
            sumE[j] /= ((double) numOfE);

            printList.add(new DifferenceTFIDF((sumI[j]-sumE[j])/ ((float) sumE[j]+(float) sumI[j]),indexToWord[j] ));
        }

        Collections.sort(printList);

        for(DifferenceTFIDF differenceTFIDF : printList){
            differenceTFIDF.printDiff();
        }



    }

    public void writeVecToCsv(String fileName){
        PrintWriter writer;

        try {
            writer = new PrintWriter(fileName);

            StringBuilder sb = new StringBuilder();

            sb.append("class");


            for(int i=0; i<indexToWord.length; i++){
                sb.append(',');
                sb.append(indexToWord[i]);
            }
            sb.append('\n');


            for(int i=0; i<classVec.length; i++){
                if(classVec[i].getDim1()== Dim1.EXTRAVERSION)
                    sb.append('E');
                else
                    sb.append('I');
//                sb.append(classVec[i].getPersonalityTypeString());

                for(int j=0; j<indexToWord.length; j++){
                    sb.append(',');
                    sb.append(featuresVec[i][j]);
                }

                sb.append('\n');
            }


            sb.append('\n');

            writer.write(sb.toString());

            writer.close();





        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



    }
}
