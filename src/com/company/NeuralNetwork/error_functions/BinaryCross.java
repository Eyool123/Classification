package com.company.NeuralNetwork.error_functions;


import com.company.NeuralNetwork.interfaces.IErrorFunction;

import java.io.Serializable;

public class BinaryCross implements IErrorFunction, Serializable {
    // Error calculation square function
    // E(g) = (t-g)^2
    @Override
    public float calculateError(float target, float guess) {


            float bce = (float) (target*Math.log(guess) + (1f-target)*Math.log(1-guess));
            return -1*bce;


    }


    // The Derivative function with respect to guess,
    // E(g) = (t-g)^2
    // E'(g) = -2(t-g)
    @Override
    public float calculateDerivativeError(float target, float guess) {
        return (1f*(target-guess) ); // -1f same as -2f because we are only interested in the direction
    }

    @Override
    public float calculateDerivativeError(float error) {
        return -error;
    }

}