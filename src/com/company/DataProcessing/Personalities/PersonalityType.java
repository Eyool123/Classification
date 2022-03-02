package com.company.DataProcessing.Personalities;

import java.io.Serializable;


public class PersonalityType implements Serializable {

    PersonalityTypes personalityType;

    public PersonalityType(String type){
        this.personalityType = PersonalityTypes.valueOf(type);

    }

    public  PersonalityTypes getPersonalityType(){
        return this.personalityType;
    }

    public Dim1 getDim1(){
        String strType = this.personalityType.toString();
        if(strType.charAt(0)=='E')
            return Dim1.EXTRAVERSION;
        else
            return Dim1.INTROVERSION;
    }

    Dim2 getDim2(){
        String strType = this.personalityType.toString();
        if(strType.charAt(1)=='S')
            return Dim2.SENSING;
        else
            return Dim2.INTUITION;
    }

    Dim3 getDim3(){
        String strType = this.personalityType.toString();
        if(strType.charAt(2)=='T')
            return Dim3.THINKING;
        else
            return Dim3.FEELING;
    }

    Dim4 getDim4(){
        String strType = this.personalityType.toString();
        if(strType.charAt(3)=='J')
            return Dim4.JUDGING;
        else
            return Dim4.PERCEIVING;
    }

    public Enum getDim(int dim){

        switch (dim){
            case 1:
                return getDim1();
            case 2:
                return getDim2();
            case 3:
                return getDim3();
            default:
                return getDim4();
        }
    }

    public boolean isDim(int dim){
        if(dim==1)
            return this.getDim1()==Dim1.EXTRAVERSION;
        if(dim==2)
            return  this.getDim2()==Dim2.SENSING;
        if(dim==3)
            return this.getDim3()==Dim3.THINKING;

        return this.getDim4()==Dim4.JUDGING;

    }

    public String getPersonalityTypeString(){
        return String.valueOf(this.personalityType);
    }


}
