package myfirstpackage;

public class MySecondClass {
    private int firstNum;
    private int secondNum;

    public MySecondClass(int first, int second){
        firstNum = first;
        secondNum = second;
    }

    public int getFirstNumber() {
        return firstNum;
    }
    public int getSecondNumber() {
        return secondNum;
    }

    public void setFirstNumber(int val){
        firstNum = val;
    }
    public void setSecondNumber(int val){
        secondNum = val;
    }

    public int bitAnd() {
        return firstNum & secondNum;
    }

}