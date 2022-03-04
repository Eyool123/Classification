package com.company.NeuralNetwork;

import com.company.NeuralNetwork.interfaces.IActivationFunction;
import com.company.NeuralNetwork.interfaces.IErrorFunction;

import java.io.*;
import java.util.Random;

public class NeuralNetwork implements Serializable {


    private Layer[] layers;
    private IActivationFunction activationFunction;
    private IErrorFunction errorFunction;
    float learningRate;


    // Constructor
    // param is array consists of int number that represent the number of neurons in each layer, the length is number of layers
    // e.g {4,10,10,1} 4 input neurons in input layer
    // 2 hidden layer consists of 10 neurons each
    // 1 output neuron
    public NeuralNetwork(int[] neuronsPerLayer, IActivationFunction activationFunction, IErrorFunction errorFunction, float learningRate){


        // Sets the activation function for the neural network, e.g sigmoid
        this.activationFunction = activationFunction;

        this.errorFunction = errorFunction;

        //sets the learning rate for neural network
        this.learningRate = learningRate;


        layers = new Layer[neuronsPerLayer.length];

        layers[0] = new Layer(neuronsPerLayer[0],0); // first (input) layer has 0 neurons in prev layer

        //for every layer we initialize the number of neurons and number of neurons in previous layer
        for(int i=1; i<neuronsPerLayer.length; i++){
            layers[i] = new Layer(neuronsPerLayer[i], neuronsPerLayer[i-1]);
        }

    }


    public void fitNetwork(int epochs, float[][] inputs, float[][] outputs)  {

//        Random rnd = new Random();
        for (int i=0; i<epochs; i++){
            float error = 0;

            shuffleInputs(inputs, outputs);

            for(int j=0; j<inputs.length; j++){
                try {
                    error += trainNetwork(inputs[j], outputs[j]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            error /= inputs.length;
            System.out.println("Epoch: "+(i+1)+" avg error: "+error);
        }

    }


    // train the network for a given input and an output, returns the mean abs error
    public float trainNetwork(float[] inputVec, float[] targetOutputVec) throws Exception {


        this.feedForward(inputVec);

        this.backPropagate(targetOutputVec);

        return calculateOutputMeanError();


    }

    private void shuffleInputs(float[][] inputVec, float[][] targetOutputVec) {
        Random rnd = new Random();
        for (int i = inputVec.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            float[] inputTemp = inputVec[index];
            float[] outputTemp = targetOutputVec[index];

            inputVec[index] = inputVec[i];
            targetOutputVec[index] = targetOutputVec[i];

            inputVec[i] = inputTemp;
            targetOutputVec[i] = outputTemp;

        }
    }


    // calculates the avg absolute error of the output layer
    private float calculateOutputMeanError(){
        float error = 0;

        Neuron[] outputNeurons = layers[layers.length-1].neurons;

        for (Neuron outputNeuron : outputNeurons) { // for each neuron in output layer
            error += Math.abs(outputNeuron.getError());
        }
        return error/outputNeurons.length;

    }


    // The feed forward function receives an array of inputs for the neural network
    // it feeds the inputs forward through the neural network
    public void feedForward(float[] inputs) throws Exception {

        //See exception
        if(inputs==null||inputs.length!=layers[0].getNumberOfNeurons()){
            throw new Exception("Number of inputs "+  inputs.length +" must be the same size of input layer in the neural network "+ layers[0].getNumberOfNeurons());
        }


        // Sets the value for each neuron in input layer according to given inputs
        for(int i=0; i<layers[0].getNumberOfNeurons(); i++){
            layers[0].neurons[i].setValue(inputs[i]);
        }



        for(int i=1; i<layers.length; i++){ //for every layer follows input layer

            for (int j=0; j<layers[i].getNumberOfNeurons(); j++){ //for every neuron in current layer

                float inputsWeights = 0; // we sum the weights multiply by inputs from previous layer neurons to current neuron

                for(int k=0; k < layers[i].getNumberOfNeuronsPrevLayer(); k++)  { // for every neuron in previous layer
                    // add the input from previous neuron multiply by the weight from prev layer neuron to current neuron
                    inputsWeights+=layers[i].neurons[j].weights[k]*layers[i-1].neurons[k].getValue();
                }
                // finally, we apply the given activation function on the calculated sum plus bias
                layers[i].neurons[j].setValue(activationFunction.calculateActivation(inputsWeights + layers[i].neurons[j].bias));
            }

        }


    }

    //returns the output vector of the neural network
    public float[] getOutputVec(){

        //the output vector is the size of the number of neurons in the last layer (output layer)
        float[] outputVec = new float[this.layers[this.layers.length-1].getNumberOfNeurons()];

        for(int i=0; i<outputVec.length; i++){
            outputVec[i] = this.layers[this.layers.length-1].neurons[i].value;
        }
        return outputVec;

    }





    // gets the target output vector and trains the neural network (updates weights and biases) accordingly
    public void backPropagate(float[] targetOutputs) throws Exception {


        if(targetOutputs.length!=layers[layers.length-1].getNumberOfNeurons())
            throw new Exception("Size of target outputs must be the same size of the number of neuron in the output layer");

        this.calculateErrorsAndDeltas(targetOutputs);

        this.updateWeightsAndBiases();



    }


    // after we calculated the delta change we update the weights and biases for each neuron
    private void updateWeightsAndBiases(){

        for(int i=1; i<layers.length; i++){ // for each layer except first (input layer doesn't have weights\biases)


            for(Neuron neuron : layers[i].neurons){ // for each neuron in layer
                for(int j=0; j<neuron.deltas.length; j++){ // we update each weight according to calculated deltas
                    neuron.weights[j] += neuron.deltas[j];
                }
                neuron.bias += (learningRate)*neuron.error*activationFunction.calculateDerivative(neuron.getValue()); // we update the neuron bias according to error
            }
        }
    }


    // calculates the error for each neuron and its delta change according to the target output
    private void calculateErrorsAndDeltas(float[] targetOutputs){
        float error;


        // first we calculate the error of the output layer according to the target output vec
        for(int i=0; i<targetOutputs.length; i++){
            // we use the error function to calculate the neuron error regarding the given target
            error = errorFunction.calculateError(targetOutputs[i], layers[layers.length-1].neurons[i].getValue());
            // we set the error value of the neuron
            layers[layers.length-1].neurons[i].setError(error);

            this.calculateDeltas(layers[layers.length-1].neurons[i], layers.length-1); // calculate the deltas for the given neuron
        }


        // For each layer from last-1 to second (first layer doesn't have weights connecting to it, output layer already calculated)
        for(int i= layers.length-2; i>0; i--){

            // for each neuron j in layer k
            for (int j=0; j<layers[i].getNumberOfNeurons(); j++){
                // We calculate the sum of the error from front layer neuron multiply by the weight from the current neuron to the front neuron
                float sumError = 0;

                //for each neuron in the layer after current neuron j in layer k
                for(int k=0; k<layers[i+1].getNumberOfNeurons(); k++)
                    sumError += layers[i+1].neurons[k].getError() * layers[i+1].neurons[k].weights[j];

                layers[i].neurons[j].setError(sumError);

                this.calculateDeltas(layers[i].neurons[j], i); // calculate the deltas for the given neuron

            }

        }

    }


    // function that gets a single neuron and its layer and calculates the deltas for all its weights (error must be already calculated)
    // the formula is: -1 * lr * error'(neuronError) * activation'(neuronOutput) * output (from prev layer to current neuron)
    private void calculateDeltas(Neuron neuron, int neuronLayer) {
        // for every delta \ weight
        for (int i=0; i<neuron.deltas.length; i++){
            neuron.deltas[i] = -1f *learningRate* errorFunction.calculateDerivativeError(neuron.error) * activationFunction.calculateDerivative(neuron.value)*layers[neuronLayer-1].neurons[i].getValue();

        }

    }


    public void saveNetworkToFile(String filePath){

        try {

            FileOutputStream fileOut = new FileOutputStream(filePath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this);
            objectOut.close();
            objectOut.flush();

        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            System.out.println("The neural network  was successfully written to a file "+filePath);
        }

    }

    public static NeuralNetwork loadDataFromFile(String filePath){

        NeuralNetwork neuralNetwork = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            neuralNetwork = (NeuralNetwork) ois.readObject();
            ois.close();
            fis.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            System.out.println("Neural network  read from file "+filePath);
        }

        return neuralNetwork;

    }





}
