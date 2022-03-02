package com.company.DataProcessing.Tfidf;

public class DifferenceTFIDF implements Comparable<DifferenceTFIDF>{

    private float difference;
    String word;

    public DifferenceTFIDF(float difference, String word){
        this.difference = difference;
        this.word = word;
    }

    public void printDiff(){
        System.out.println(word +": "+difference);

    }



    @Override
    public int compareTo(DifferenceTFIDF o) {
        if(this.difference>((DifferenceTFIDF)o).difference)
            return 1;
        else if(this.difference<((DifferenceTFIDF)o).difference)
            return -1;
        return 0;
    }
}
