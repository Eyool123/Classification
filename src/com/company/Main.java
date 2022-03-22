package com.company;

import com.company.DataProcessing.DataPartition;
import com.company.DataProcessing.Evaluation.Evaluation;
import com.company.DataProcessing.Personalities.Dim1;
import com.company.DataProcessing.Production.PersonalityClassification;
import com.company.DataProcessing.Production.ResultPersonalityType;
import com.company.DataProcessing.Production.SampleGenerator;
import com.company.DataProcessing.Tfidf.Tfidf;
import com.company.DataProcessing.VectorCorpus.CorpusVec;
import com.company.DataProcessing.WordCorpus.Corpus;
import com.company.NeuralNetwork.NeuralNetwork;
import com.company.NeuralNetwork.activation_functions.ReLUActivation;
import com.company.NeuralNetwork.activation_functions.SigmoidActivation;
import com.company.NeuralNetwork.error_functions.BinaryCross;
import com.company.NeuralNetwork.error_functions.SquareError;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    static String CSV_FILE_PATH = "src/com/company/files/filterCSVtyped.csv";
    static String IMDB_FILE_PATH = "src/com/company/files/IMDB.csv";
    static String KAGGLE_CSV_PATH = "src/com/company/files/mbti_1.csv";
    static String DIM1_DATA_PARTITION = "src/com/company/files/dim1DataPartition.data";
    static String DIM1_NEURAL_NETWORK = "src/com/company/files/dim1Network.data";

    static String PERSONALITY_CLASSIFICATION_PATH = "src/com/company/files/personalityClassification.data";



    public static void main(String[] args) throws Exception {


        int dim = 1;

        generateDimDataPartition(DIM1_DATA_PARTITION, dim);


//        generateimdbDataPartition();

//        PersonalityClassification personalityClassification = PersonalityClassification.loadDataFromFile(PERSONALITY_CLASSIFICATION_PATH);
        PersonalityClassification personalityClassification = new PersonalityClassification();

//        while (i<100){
//            classifyText(personalityClassification);
//        }




        DataPartition partition = DataPartition.loadDataFromFile(DIM1_DATA_PARTITION);

        float[][] testInputs = partition.featuresVecTest;
        float[][] testOutputs = partition.classVecTest;

        float[][] trainingInputs = partition.featuresVecTrain;
        float[][] trainingOutputs = partition.classVecTrain;


        int[] neuronsPerLayer = {trainingInputs[0].length, 800, 800,800,800,800, 1};
//
//
//

        NeuralNetwork neuralNetwork = new NeuralNetwork(neuronsPerLayer, new SigmoidActivation(), new SquareError(), 0.0004f);
//        NeuralNetwork neuralNetwork = NeuralNetwork.loadDataFromFile(DIM1_NEURAL_NETWORK);
//        Evaluation.evaluateNeuralNetwork(neuralNetwork, testInputs, testOutputs);


//        Evaluation.evaluateNeuralNetwork(neuralNetwork, trainingInputs, trainingOutputs);
        neuralNetwork.fitNetwork(20, trainingInputs, trainingOutputs, 0.007f);


//        neuralNetwork.fitNetwork(8, trainingInputs, trainingOutputs);
        neuralNetwork.saveNetworkToFile(DIM1_NEURAL_NETWORK);

//        PersonalityClassification personalityClassification = PersonalityClassification.loadDataFromFile(PERSONALITY_CLASSIFICATION_PATH);
        personalityClassification.setNeuralNetwork(neuralNetwork, dim);
        personalityClassification.savePersonalityClassificationToFile(PERSONALITY_CLASSIFICATION_PATH);


        Evaluation.evaluateNeuralNetwork(neuralNetwork, trainingInputs, trainingOutputs);
        Evaluation.evaluateNeuralNetwork(neuralNetwork, testInputs, testOutputs);


    }




    static PersonalityClassification createPersonalityClassificationModel(){

        PersonalityClassification personalityClassification = new PersonalityClassification();






    }


    static void generateDimDataPartition(String dataPartitionFile, int dimToSplit ) {

        Corpus corpus = Corpus.createCorpusFromFile(KAGGLE_CSV_PATH); // create a corpus from whole csv comments file
        System.out.println("corpus read successfully");



        Corpus onlyClass1Corpus = new Corpus(corpus, dimToSplit, true);
        System.out.println("Class1 corpus created");

        Corpus onlyClass2Corpus = new Corpus(corpus, dimToSplit, false);
        System.out.println("Class2 corpus created");

        Corpus filteredCorpus = new Corpus(onlyClass1Corpus, onlyClass2Corpus, 0.065f, 1f);
        System.out.println("Filtered corpus created");

        Tfidf tfidfDim1 = new Tfidf(filteredCorpus);
        System.out.println("tfidf created");

        CorpusVec corpusVec = new CorpusVec(filteredCorpus, tfidfDim1);
        System.out.println("Corpus vector created");


        DataPartition dataPartition = new DataPartition(corpusVec, dimToSplit, 0.8f);
        System.out.println("Data partition created");

        dataPartition.saveDataToFile(dataPartitionFile);


        SampleGenerator sampleGenerator = new SampleGenerator(corpusVec);

        PersonalityClassification personalityClassification = PersonalityClassification.loadDataFromFile(PERSONALITY_CLASSIFICATION_PATH);
        personalityClassification.setSampleGenerator(sampleGenerator, dimToSplit);
        personalityClassification.savePersonalityClassificationToFile(PERSONALITY_CLASSIFICATION_PATH);


    }

    public static void classifyText(PersonalityClassification classification) throws Exception {
        Scanner sc= new Scanner(System.in); //System.in is a standard input stream
        System.out.print("Enter a string: ");
        String str= sc.nextLine();              //reads string
        System.out.print("You have entered: "+str);
        ResultPersonalityType resultPersonalityType =  classification.getPersonalityTypeFromText(str);
        System.out.println("Dim1: "+resultPersonalityType.getDim1f());
        System.out.println("Dim2: "+resultPersonalityType.getDim2f());
        System.out.println("Dim3: "+resultPersonalityType.getDim3f());
        System.out.println("Dim4: "+resultPersonalityType.getDim4f());

    }

    static void generateimdbDataPartition() {

        Corpus corpus = Corpus.createCorpusFromFile(IMDB_FILE_PATH); // create a corpus from whole csv comments file
        System.out.println("corpus read successfully");

        Corpus onlyExtrovertCorpus = new Corpus(corpus, 1, true);
        System.out.println("Extroverts corpus created");

        Corpus onlyIntrovertCorpus = new Corpus(corpus, 1, false);
        System.out.println("Introverts corpus created");

        Corpus filteredCorpus = new Corpus(onlyExtrovertCorpus, onlyIntrovertCorpus, 0.065f, 1f);
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
