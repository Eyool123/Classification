package com.company.NeuralNetwork.activation_functions;

import com.company.NeuralNetwork.interfaces.IActivationFunction;

public class ReLUActivation implements IActivationFunction {
    @Override
    public float calculateActivation(float value) {
        return Math.max(0f, value);
    }

    @Override
    public float calculateDerivative(float value) {
        return value>0 ? 1 : 0;
    }
}
