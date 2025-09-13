package functions;

import java.util.Arrays;

public class LinkedListTabulatedFunction extends AbstractTabulatedFunction implements Insertable, Removable {

    Node head;
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

    protected Node floorNodeOfX(double x){
        if (head == null){
            throw new IllegalStateException("Список пуст, ы");
        }
        if(x < head.x){
            return head;
        }

        if(x >= head.prev.x){
            return head.prev;
        }

        Node curr = head;
        for(int i = 0; i < count - 1; i++){
            if(x >= curr.x && x < curr.next.x){
                return curr;
            }
            curr = curr.next;
        }

        return head.prev;
    }

    @Override
    public double apply(double x) {
        if(x < leftBound()){
            return extrapolateLeft(x);
        }else if(x > rightBound()){
            return extrapolateRight(x);
        }else{
            Node floorNode = floorNodeOfX(x);

            if(java.lang.Math.abs(floorNode.x - x) < 1e-10){
                return floorNode.y;
            }

            if(floorNode.next == head && java.lang.Math.abs(floorNode.x - x) < 1e-10){
                return floorNode.y;
            }

            if(floorNode.next != head && x >= floorNode.x && x < floorNode.next.x){
                return interpolate(x, floorNode.x, floorNode.next.x, floorNode.y, floorNode.next.y);
            }

            return interpolate(x, floorNode.x, floorNode.next.x, floorNode.y, floorNode.next.y);
        }
    }

    @Override
    public void insert(double x, double y) {
        if (head == null) {
            addNode(x, y);
            return;
        }

        Node curr = head;
        do {
            if (Math.abs(curr.x - x) < 1e-10) {
                curr.y = y;
                return;
            }
            if (x < curr.x) {
                Node newNode = new Node(x, y);

                Node prevNode = curr.prev;
                prevNode.next = newNode;
                newNode.prev = prevNode;

                newNode.next = curr;
                curr.prev = newNode;

                if (curr == head) {
                    head = newNode;
                }

                count++;
                return;
            }

            curr = curr.next;
        } while (curr != head);

        Node last = head.prev;
        Node newNode = new Node(x, y);

        last.next = newNode;
        newNode.prev = last;

        newNode.next = head;
        head.prev = newNode;

        count++;
    }


    @Override
    public void remove(int index) {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Индекс: " + index + ", размер: " + count);
        }

        if (head == null) {
            throw new IllegalStateException("Список пуст");
        }

        Node nodeToRemove = getNode(index);

        if (count == 1) {
            head = null;
            count--;
            return;
        }

        Node prevNode = nodeToRemove.prev;
        Node nextNode = nodeToRemove.next;

        prevNode.next = nextNode;
        nextNode.prev = prevNode;

        if (nodeToRemove == head) {
            head = nextNode;
        }
        count--;
    }
}