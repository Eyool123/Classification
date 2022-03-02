package com.company.NeuralNetwork;

import java.util.Random;

public class Neuron {

    float value; // the value of this neuron
    float[] weights; //the weights from every neuron in the previous layer to this neuron
    float[] deltas; //the delta change of every weight and bias according to error
    float error; //the delta change according to error
    float bias; //the neuron bias



    // Neron constructor
    public Neuron(int prevLayerN){
        weights = new float[prevLayerN];
        deltas = new float[prevLayerN];

        this.randomize(); //init with random values in weight and bias

    }


    //used to randomize the neuron bias and weights
    public void randomize(){
        Random rand = new Random();

        //init weights from prev layer neurons to current neuron
        for(int i=0; i<weights.length; i++){
            this.weights[i] = rand.nextFloat()*2f-1f; //initialize random weights between 0-1.0
        }
        this.bias = rand.nextFloat()*2f-1f; // initialize the bias randomly
    }


    // Setter for the neuron value
    public void setValue(float value){
        this.value = value;

    }


    // Returns the neuron value
    public float getValue(){
        return this.value;
    }

    public float getError(){
        return this.error;
    }

    public void setError(float error){
        this.error = error;
    }

}
