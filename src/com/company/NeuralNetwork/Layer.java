package com.company.NeuralNetwork;

import java.io.Serializable;

public class Layer implements Serializable {

    public Neuron[] neurons; //array of neurons in layer

    private int numberOfNeurons; // number of neurons in layer

    private int numberOfNeuronsPrevLayer; //number of neurons in previous layer


    public int getNumberOfNeurons() {
        return numberOfNeurons;
    }


    public int getNumberOfNeuronsPrevLayer() {
        return numberOfNeuronsPrevLayer;
    }

    //initializes a new layer in neural network with a given number of neurons
    public Layer(int numberOfNeurons, int previousLayerNeurons){

        this.numberOfNeurons = numberOfNeurons;

        this.numberOfNeuronsPrevLayer = previousLayerNeurons;

        this.neurons = new Neuron[numberOfNeurons];

        //init for every neuron in layer
        for(int i=0; i<neurons.length; i++){
            this.neurons[i] = new Neuron(previousLayerNeurons);
        }

    }


}
