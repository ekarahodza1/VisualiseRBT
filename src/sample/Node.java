package sample;

public class Node<E extends Comparable<E>> {
    public E element;
    public Node<E> left;
    public Node<E> right;
    public int height = 0;
    private boolean red = true;

    public Node(E e){
        element = e;
    }

    public boolean isRed() {
        return red;
    }

    public boolean isBlack() {
        return !red;
    }

    public void setBlack() {
        red = false;
    }

    public void setRed() {
        red = true;
    }

    int blackHeight;
}
