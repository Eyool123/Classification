package com.company.NeuralNetwork.interfaces;

public interface IActivationFunction {

    //execute the activation function on a given input and returns result
    public  float calculateActivation(float value);


    //execute the derivative of the activation on input function and returns result
    public  float calculateDerivative(float value);

}
