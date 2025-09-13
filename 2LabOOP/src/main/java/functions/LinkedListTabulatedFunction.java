package functions;

import java.util.Arrays;

public class LinkedListTabulatedFunction extends AbstractTabulatedFunction{

    private Node head;
    private int count;

    //конструктор с массивами значений
    public LinkedListTabulatedFunction(double[] xVal, double[] yVal){
        if(xVal.length != yVal.length){
            throw new IllegalArgumentException("Массивы должны быть одинаковой длины");
        }

        for(int i = 1; i < xVal.length; i++){
            if(xVal[i] <= xVal[i-1]){
                throw new IllegalArgumentException("Значения должны строго возрастать в xVal");
            }
        }

        this.count = 0;
        this.head = null;

        for(int i = 0; i < xVal.length;i++){
            addNode(xVal[i], yVal[i]);
        }
    }

    //конструктор с дискретизацией функции
    public LinkedListTabulatedFunction(MathFunction s, double xFrom, double xTo, int count){

        if (xFrom > xTo){
            double temp = xFrom;
            xFrom = xTo;
            xTo = temp;
        }

        this.count = 0;
        this.head = null;

        if(xFrom == xTo){
            double yVal = s.apply(xFrom);
            for(int i = 0; i < count; i++){
                addNode(xFrom, yVal);
            }
        }else{
            double step = (xTo - xFrom) / (count - 1);
            for(int i = 0; i < count; i++){
                double x = xFrom + i * step;
                double y = s.apply(x);
                addNode(x, y);
            }
        }
    }

    private void addNode(double x, double y){
        Node nwNode = new Node(x, y);

        if (head == null){
            head = nwNode;
            head.next = head;
            head.prev = head;
        }else{
            Node last = head.prev;

            last.next = nwNode;
            nwNode.prev = last;

            nwNode.next = head;
            head.prev = nwNode;
        }

        count++;
    }

    private Node getNode(int index){
        if (index < 0 || index >= count){
            throw new IndexOutOfBoundsException("Индекс: " + index + ", Размер: " + count);
        }

        if(index < count / 2){
            Node curr = head;
            for(int i = 0; i < index; i++){
                curr = curr.next;
            }
            return curr;
        } else{
            Node curr = head.prev;
            for (int i = count - 1; i > index; i--){
                curr = curr.prev;
            }
            return curr;
        }
    }

    @Override
    protected int floorIndexOfX(double x) {
        if(head == null){
            throw new IllegalArgumentException("Список пуст, ы");
        }
        if(x < head.x){
            return 0;
        }
        if(x > head.prev.x){
            return count;
        }

        Node curr = head;
        for(int i = 0; i < count - 1; i++){
            if(x >= curr.x && x < curr.next.x){
                return i;
            }
            curr = curr.next;
        }
        return count - 1;
    }

    @Override
    protected double extrapolateLeft(double x) {
        if(count == 1){
            return head.y;
        }
        return interpolate(x, head.x, head.next.x, head.y, head.next.y);
    }

    @Override
    protected double extrapolateRight(double x) {
        if(count == 1){
            return head.y;
        }
        Node last = head.prev;
        Node prevLast = last.prev;
        return interpolate(x, prevLast.x, last.x, prevLast.y, last.y);
    }

    @Override
    protected double interpolate(double x, int floorIndex) {
        if(count == 1){
            return head.y;
        }
        Node leftNode = getNode(floorIndex);
        Node rightNode = leftNode.next;
        return interpolate(x, leftNode.x, rightNode.x, leftNode.y, rightNode.y);
    }

    @Override
    public int getCount(){
        return count;
    }

    @Override
    public double getX(int index){
        return getNode(index).x;
    }

    @Override
    public double getY(int index){
        return getNode(index).y;
    }

    @Override
    public void setY(int index, double val){
        getNode(index).y = val;
    }

    @Override
    public int indexOfX(double x){
        if (head == null){
            return -1;
        }

        Node curr = head;
        for(int i = 0; i < count; i++){
            if(java.lang.Math.abs(curr.x - x) < 1e-10){
                return i;
            }
            curr = curr.next;
        }
        return -1;
    }

    @Override
    public int indexOfY(double y){
        if (head == null){
            return -1;
        }

        Node curr = head;
        for(int i = 0; i < count; i++){
            if(java.lang.Math.abs(curr.y - y) < 1e-10){
                return i;
            }
            curr = curr.next;
        }
        return -1;
    }

    @Override
    public double leftBound() {
        if(head == null){
            throw new IllegalArgumentException("Список пуст, ы");
        }
        return head.x;
    }

    @Override
    public double rightBound() {
        if(head == null){
            throw new IllegalArgumentException("Список пуст, ы");
        }
        return head.prev.x;
    }

    @Override
    public double apply(double x) {
        if (x < getX(0)) {
            return extrapolateLeft(x);
        }

        if (x > getX(count - 1)) {
            return extrapolateRight(x);
        }

        int exactIndex = indexOfX(x);
        if (exactIndex != -1) {
            return getY(exactIndex);
        }

        int floorIndex = floorIndexOfX(x);
        return interpolate(x, floorIndex);
    }
}