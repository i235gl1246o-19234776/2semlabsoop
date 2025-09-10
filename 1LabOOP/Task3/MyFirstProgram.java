class MyFirstClass {
    public static void main(String[] s) {
       MySecondClass o = new MySecondClass(42, 52);

       System.out.println(o.bitAnd());

       for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                o.setFirstNumber(i);
                o.setSecondNumber(j);
                System.out.print(o.bitAnd());
                System.out.print(" ");
            }
            System.out.println();
         }
    }
}

class MySecondClass {
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