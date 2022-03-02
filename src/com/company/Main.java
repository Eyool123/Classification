package com.company;

import com.company.DataProcessing.DataPartition;
import com.company.DataProcessing.Personalities.Dim1;
import com.company.DataProcessing.Tfidf.Tfidf;
import com.company.DataProcessing.VectorCorpus.CorpusVec;
import com.company.DataProcessing.WordCorpus.Corpus;

public class Main {

    static String CSV_FILE_PATH =  "src/com/company/files/filterCSVtyped.csv";


    public static void main(String[] args) {

        Corpus corpus = Corpus.createCorpusFromFile(CSV_FILE_PATH); // create a corpus from whole csv comments file
        System.out.println("corpus read successfully");

        Corpus onlyExtrovertCorpus = new Corpus(corpus, 1, Dim1.EXTRAVERSION);
        System.out.println("Extroverts corpus created");

        Corpus onlyIntrovertCorpus = new Corpus(corpus, 1, Dim1.INTROVERSION);
        System.out.println("Introverts corpus created");

        Corpus filteredCorpus = new Corpus(onlyExtrovertCorpus, onlyIntrovertCorpus, 0.065f, 1f);
        System.out.println("Filtered corpus created");

        Tfidf tfidfDim1 = new Tfidf(filteredCorpus);
        System.out.println("tfidf created");

        CorpusVec corpusVec = new CorpusVec(filteredCorpus, tfidfDim1);
        System.out.println("Corpus vector created");


        DataPartition dataPartition = new DataPartition(corpusVec, 1, 0.7f);
        System.out.println("Data partition created");





    }
}
