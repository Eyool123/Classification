package com.company;

import com.company.DataProcessing.DataPartition;
import com.company.DataProcessing.Evaluation.Evaluation;
import com.company.DataProcessing.Personalities.Dim1;
import com.company.DataProcessing.Tfidf.Tfidf;
import com.company.DataProcessing.VectorCorpus.CorpusVec;
import com.company.DataProcessing.WordCorpus.Corpus;
import com.company.NeuralNetwork.NeuralNetwork;
import com.company.NeuralNetwork.activation_functions.SigmoidActivation;
import com.company.NeuralNetwork.error_functions.SquareError;

import java.util.Arrays;

public class Main {

    static String CSV_FILE_PATH =  "src/com/company/files/filterCSVtyped.csv";
    static String DIM1_DATA_PARTITION =  "src/com/company/files/dim1DataPartition.data";
    static String DIM1_NEURAL_NETWORK =  "src/com/company/files/dim1Network.data";


    public static void main(String[] args) {

        DataPartition partition = DataPartition.loadDataFromFile(DIM1_DATA_PARTITION);
        NeuralNetwork ntTest = NeuralNetwork.loadDataFromFile(DIM1_NEURAL_NETWORK);

//        generateDim1DataPartition();



//
        float[][] testInputs = partition.featuresVecTest;
        float[][] testOutputs = partition.classVecTest;
        normlize(testInputs);
        Evaluation.evaluateNeuralNetwork(ntTest,testInputs, testOutputs);

//
//
//        for(int i=0; i<testInputs.length; i++){
//            for(int j=0; j<testInputs[0].length; j++)
//                testInputs[i][j] *= 1000;
//        }
//
//        Evaluation.evaluateNeuralNetwork(ntTest,testInputs, testOutputs);



        float[][] trainingInputs = partition.featuresVecTrain;
        float[][] trainingOutputs = partition.classVecTrain;

        normlize(trainingInputs);
//
//
//        for(int i=0; i<trainingInputs.length; i++){
//            for(int j=0; j<trainingInputs[0].length; j++)
//                trainingInputs[i][j] *= 1000;
//        }

//        Evaluation.evaluateNeuralNetwork(ntTest,trainingInputs, trainingOutputs);








        int[] neuronsPerLayer = {trainingInputs[0].length,400,  1};




        NeuralNetwork neuralNetwork = new NeuralNetwork(neuronsPerLayer, new SigmoidActivation(), new SquareError(), 1);

        Evaluation.evaluateNeuralNetwork(neuralNetwork, trainingInputs, trainingOutputs);

        neuralNetwork.fitNetwork(20, trainingInputs, trainingOutputs);
        neuralNetwork.saveNetworkToFile(DIM1_NEURAL_NETWORK);

        Evaluation.evaluateNeuralNetwork(neuralNetwork, trainingInputs, trainingOutputs);


    }

    static void normlize(float[][] inputs){

        float[] max = new float[inputs[0].length];
        Arrays.fill(max, 0);

        for (int i=0; i<inputs[0].length; i++){
            for (int j=0; j<inputs.length; j++){
                max[i] = Math.max(inputs[j][i], max[i]);
            }
        }

        for (int i=0; i<inputs[0].length; i++){
            for (int j=0; j<inputs.length; j++){
                inputs[j][i] = inputs[j][i]/ max[i];
            }
        }

    }

    static void generateDim1DataPartition(){

        Corpus corpus = Corpus.createCorpusFromFile(CSV_FILE_PATH); // create a corpus from whole csv comments file
        System.out.println("corpus read successfully");

        Corpus onlyExtrovertCorpus = new Corpus(corpus, 1, Dim1.EXTRAVERSION);
        System.out.println("Extroverts corpus created");

        Corpus onlyIntrovertCorpus = new Corpus(corpus, 1, Dim1.INTROVERSION);
        System.out.println("Introverts corpus created");

        Corpus filteredCorpus = new Corpus(onlyExtrovertCorpus, onlyIntrovertCorpus, 0.065f, 0.1f);
        System.out.println("Filtered corpus created");

        Tfidf tfidfDim1 = new Tfidf(filteredCorpus);
        System.out.println("tfidf created");

        CorpusVec corpusVec = new CorpusVec(filteredCorpus, tfidfDim1);
        System.out.println("Corpus vector created");


        DataPartition dataPartition = new DataPartition(corpusVec, 1, 0.7f);
        System.out.println("Data partition created");

        dataPartition.saveDataToFile(DIM1_DATA_PARTITION);



    }
}
