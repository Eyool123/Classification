package com.company.DataProcessing.Production;

import com.company.DataProcessing.DataPartition;
import com.company.NeuralNetwork.NeuralNetwork;

import java.io.*;

public class PersonalityClassification implements Serializable {

    NeuralNetwork[] neuralNetworks;
    SampleGenerator[] sampleGenerators;

    public PersonalityClassification(){
        this.neuralNetworks = new NeuralNetwork[4]; // A neural network for each dimension
        this.sampleGenerators = new SampleGenerator[4]; // A sample generator for each dimension
    }


    public void setNeuralNetwork(NeuralNetwork neuralNetwork ,int dimension){
        this.neuralNetworks[dimension-1] = neuralNetwork;
    }

    public void setSampleGenerator(SampleGenerator sampleGenerator, int dimension){
        this.sampleGenerators[dimension-1] = sampleGenerator;
    }

    public ResultPersonalityType getPersonalityTypeFromText(String text) throws Exception {

        float[] outputs = new float[4];

        for(int i=0; i<4; i++){
            // we initialize the inputs for every model
            float[] inputs = sampleGenerators[i].generateSample(text);
            neuralNetworks[i].feedForward(inputs);
            outputs[i] = neuralNetworks[i].getOutputVec()[0]; // we feed forward and set outputs for each dimension

        }

        return new ResultPersonalityType(outputs[0], outputs[1], outputs[2], outputs[3]);

    }


    public void savePersonalityClassificationToFile(String filePath){

        try {

            FileOutputStream fileOut = new FileOutputStream(filePath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this);
            objectOut.close();
            objectOut.flush();

        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            System.out.println("The personality classification was successfully written to a file "+filePath);
        }

    }

    public static PersonalityClassification loadDataFromFile(String filePath){

        PersonalityClassification personalityClassification = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            personalityClassification = (PersonalityClassification) ois.readObject();
            ois.close();
            fis.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            System.out.println("Personality Classification read from file "+filePath);
        }

        return personalityClassification;

    }



}
