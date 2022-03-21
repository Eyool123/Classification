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

        System.out.println("Correct 0:\t"+correct0 +"\tincorrect 0:\t"+wrong0);
        System.out.println("Correct 1:\t"+correct1 +"\tincorrect 1:\t"+wrong1);


        float precision_0 = (float)correct0/(correct0+wrong1);
        float recall_0 = (float)correct0/(correct0+wrong0);

        System.out.println("0 Precision:\t"+precision_0);
        System.out.println("0 Recall:\t"+recall_0);
        System.out.println("0 F1Score:\t"+2f*(precision_0*recall_0)/(precision_0+recall_0));

        float precision_1 = (float)correct1/(correct1+wrong0);
        float recall_1 = (float)correct1/(correct1+wrong1);

        System.out.println("1 Precision:\t"+precision_1);
        System.out.println("1 Recall:\t"+recall_1);
        System.out.println("1 F1Score:\t"+2f*(precision_1*recall_1)/(precision_1+recall_1));

        System.out.println("Model AUC\t"+(float)(correct0+correct1)/(correct0+correct1+wrong0+wrong1));






    }
}
