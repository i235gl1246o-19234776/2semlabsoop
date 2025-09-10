import myfirstpackage.MySecondClass;

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