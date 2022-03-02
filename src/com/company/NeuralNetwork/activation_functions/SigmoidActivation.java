package com.company.NeuralNetwork.activation_functions;

import com.company.NeuralNetwork.interfaces.IActivationFunction;

public class SigmoidActivation implements IActivationFunction {

    //calculate the sigmoid activation function
    @Override
    public float calculateActivation(float value) {
        // 1/(1+e^-x)
        return (float) (1f / (1+Math.exp(-value)));
    }


    //calculate the derivative of the sigmoid activation function given an already sigmoid value
    @Override
    public float calculateDerivative(float sigmoidValue) {
        return sigmoidValue*(1f-sigmoidValue);
    }

}
