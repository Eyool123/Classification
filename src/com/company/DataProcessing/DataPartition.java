package com.company.DataProcessing;

import com.company.DataProcessing.VectorCorpus.CorpusVec;

public class DataPartition {



    float[][] classVecTrain;
    float[][] featuresVecTrain; //Feature matrix of documents and tfidf in each


    float[][] classVecTest;
    float[][] featuresVecTest;



    public DataPartition(CorpusVec corpusVec, int dimToSplit, float trainingPercentage){
        int fullLength = corpusVec.getNumberOfDocuments();
        int trainLength = (int) (trainingPercentage*fullLength);
        int testLength = fullLength - trainLength;

        classVecTrain = new float[trainLength][1];
        featuresVecTrain = new float[trainLength][];

        classVecTest = new float[testLength][1];
        featuresVecTest = new float[testLength][];


        int firstClass = corpusVec.sumOfDims[dimToSplit-1];
        int secondClass = fullLength-firstClass;


        int firstClassInTrain = (int) (firstClass*trainingPercentage);
        int secondClassInTrain = (int) (secondClass*trainingPercentage);


        int trainInd = 0;
        int testInd = 0;

        boolean isClass;


        /*
         *
         *   We split the data between test set and training set
         *   We make sure that the classes are distributed equally between test set and training
         *
         * */

        for (int i=0; i<fullLength; i++){
            // if document is part of first class
            if(corpusVec.classVec[i].isDim(dimToSplit)){
                // we check if it should be inserted to train or test
                if(firstClassInTrain>0 && trainInd<featuresVecTrain.length){
                    featuresVecTrain[trainInd] = corpusVec.featuresVec[i];
                    classVecTrain[trainInd][0] = 1f;
                    trainInd++;
                    firstClassInTrain--;

                }else {
                    featuresVecTest[testInd] = corpusVec.featuresVec[i];
                    classVecTest[testInd][0] = 1f;
                    testInd++;
                }
                // if document is part of second class
            }else {
                // we check if it should be inserted to train or test
                if(secondClassInTrain>0 && trainInd<featuresVecTrain.length){
                    featuresVecTrain[trainInd] = corpusVec.featuresVec[i];
                    classVecTrain[trainInd][0] = 0f;
                    trainInd++;
                    secondClassInTrain--;


                }else {
                    featuresVecTest[testInd] = corpusVec.featuresVec[i];
                    classVecTest[testInd][0] = 0f;
                    testInd++;

                }

            }

        }



    }

}
