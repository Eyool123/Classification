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

//
//        generateDim1DataPartition();

        DataPartition partition = DataPartition.loadDataFromFile(DIM1_DATA_PARTITION);

        float[][] testInputs = partition.featuresVecTest;
        float[][] testOutputs = partition.classVecTest;

        float[][] trainingInputs = partition.featuresVecTrain;
        float[][] trainingOutputs = partition.classVecTrain;



//        int[] neuronsPerLayer = {trainingInputs[0].length,400,  1};
//
//
//
//
//        NeuralNetwork neuralNetwork = new NeuralNetwork(neuronsPerLayer, new SigmoidActivation(), new SquareError(), 1);
        NeuralNetwork neuralNetwork = NeuralNetwork.loadDataFromFile(DIM1_NEURAL_NETWORK);
        Evaluation.evaluateNeuralNetwork(neuralNetwork, testInputs, testOutputs);


        Evaluation.evaluateNeuralNetwork(neuralNetwork, trainingInputs, trainingOutputs);

        neuralNetwork.fitNetwork(15, trainingInputs, trainingOutputs);
        neuralNetwork.saveNetworkToFile(DIM1_NEURAL_NETWORK);

        Evaluation.evaluateNeuralNetwork(neuralNetwork, trainingInputs, trainingOutputs);


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
