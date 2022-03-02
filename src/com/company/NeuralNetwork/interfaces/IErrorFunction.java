package com.company.NeuralNetwork.interfaces;

public interface IErrorFunction {

    public float calculateError(float target, float guess);

    public float calculateDerivativeError(float target, float guess);

    public float calculateDerivativeError(float error);

}
