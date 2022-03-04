package com.company.DataProcessing.Evaluation;

import com.company.NeuralNetwork.NeuralNetwork;

public class Evaluation {

    public static void evaluateNeuralNetwork(NeuralNetwork neuralNetwork, float[][] testFeatures, float[][] outputTest){


        int correct0 = 0;
        int correct1 = 0;

        int wrong0 = 0;
        int wrong1 = 1;

        for(int i=0; i< testFeatures.length; i++){
            try {
                neuralNetwork.feedForward(testFeatures[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }

            int output = neuralNetwork.getOutputVec()[0] > 0.5f ? 1 : 0 ;
            int realOutput = (int) outputTest[i][0];

            if(realOutput == 1){
                if(output == 1) correct1++;
                else wrong1++;
            }
            else {
                if(output == 0) correct0++;
                else wrong0++;
            }

        }

        System.out.println("Correct 0: "+correct0 +" incorrect0: "+wrong0);
        System.out.println("Correct 1: "+correct1 +" incorrect1: "+wrong1);



    }
}
