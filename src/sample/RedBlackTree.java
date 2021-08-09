package sample;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class RedBlackTree<E extends Comparable<E>>{

    public Node<E> root;
    public int size = 0;
    private List<E> searchArray = new ArrayList<>();

    public RedBlackTree() {
    }

    public RedBlackTree(E[] elements) {

        for (E o: elements)
            insert(o);
    }


    public Node<E> createNewNode(E e) {
        return new Node<>(e);
    }



    private boolean search1(Node<E> root, E e){
        if(root == null)
            return false;
        else if(e.compareTo(root.element) == 0){
            searchArray.add(root.element);
            return true;
        }

        else{
            if(e.compareTo(root.element) > 0) {
                searchArray.add(root.element);
                return search1(root.right, e);
            }
            else {
                searchArray.add(root.element);
                return search1(root.left, e);
            }
        }
    }

    public int height(Node<E> N) {
        if (N == null)
            return 0;
        return N.height;
    }


    public boolean search(E e) {
        searchArray.clear();
        return search1(root, e);
    }

    public List<E> getSearchArray() {
        //List<E> tempArray = new ArrayList<>();
        //tempArray = searchArray;
        return searchArray;
    }

    public Node<E> insert2(Node<E> root, E e){
        if(root == null)
            root = createNewNode(e);
        else{
            if(e.compareTo(root.element) > 0)
                root.right = insert2(root.right, e);
            else if(e.compareTo(root.element) < 0)
                root.left = insert2(root.left, e);
            else
                return null;
        }
        return root;

    }

    public boolean insert1(E e) {
        root = insert2(root,e);
        if(root == null)
            return false;
        size++;
        return true;
    }


    public boolean insert(E e) {
        boolean successful = insert1(e);
        if (!successful)
            return false;
        else {
            ensureRBTree(e);
        }

        return true;
    }

    private void ensureRBTree(E e) {
        ArrayList<Node<E>> path = path(e);
        int i = path.size() - 1;
        Node<E> u = (path.get(i));
        Node<E> v = (u == root) ? null : (path.get(i - 1));
        u.setRed();
        if (u == root)
            u.setBlack();
        else if (v != null & v.isRed())
            fixDoubleRed(u, v, path, i);
    }

    private void fixDoubleRed(Node<E> u, Node<E> v,
                              ArrayList<Node<E>> path, int i) {
        Node<E> w = (path.get(i - 2));
        Node<E> parentOfw = (w == root) ? null : path.get(i - 3);

        Node<E> x = (w.left == v) ? (w.right) :(w.left);

        if (x == null || x.isBlack()) {
            if (w.left == v && v.left == u) {
                restructureRecolor(u, v, w, w, parentOfw);

                w.left = v.right; // v.right is y3 in Figure 48.6
                v.right = w;
            }
            else if (w.left == v && v.right == u) {
                restructureRecolor(v, u, w, w, parentOfw);
                v.right = u.left;
                w.left = u.right;
                u.left = v;
                u.right = w;
            }
            else if (w.right == v && v.right == u) {
                restructureRecolor(w, v, u, w, parentOfw);
                w.right = v.left;
                v.left = w;
            }
            else {
                restructureRecolor(w, u, v, w, parentOfw);
                w.right = u.left;
                v.left = u.right;
                u.left = w;
                u.right = v;
            }
        }
        else {
            w.setRed();
            u.setRed();
            ((w.left)).setBlack();
            ((w.right)).setBlack();

            if (w == root) {
                w.setBlack();
            }
            else if (parentOfw != null && (parentOfw).isRed()) {
                u = w;
                v = parentOfw;
                fixDoubleRed(u, v, path, i - 2);
            }
        }
    }

    private void restructureRecolor(Node<E> a, Node<E> b,
                                    Node<E> c, Node<E> w, Node<E> parentOfw) {
        if (parentOfw == null)
            root = b;
        else if (parentOfw.left == w)
            parentOfw.left = b;
        else
            parentOfw.right = b;

        b.setBlack();
        a.setRed();
        c.setRed();
    }


    public boolean delete(E e) {
        Node<E> current = root;
        while (current != null) {
            if (e.compareTo(current.element) < 0) {
                current = current.left;
            }
            else if (e.compareTo(current.element) > 0) {
                current = current.right;
            }
            else
                break;
        }

        if (current == null)
            return false;

        java.util.ArrayList<Node<E>> path;
        if (current.left != null && current.right != null) {
            Node<E> rightMost = current.left;
            while (rightMost.right != null) {
                rightMost = rightMost.right;
            }
            path = path(rightMost.element);
            current.element = rightMost.element;
        }
        else
            path = path(e);
        deleteLastNodeInPath(path);

        size--;
        return true;
    }

    public void deleteLastNodeInPath(ArrayList<Node<E>> path) {
        int i = path.size() - 1;
        Node<E> u = (path.get(i));
        Node<E> parentOfu = (u == root) ? null : (path.get(i - 1));
        Node<E> grandparentOfu = (parentOfu == null ||
                parentOfu == root) ? null : (path.get(i - 2));
        Node<E> childOfu = (u.left == null) ? (u.right) :(u.left);

        // Delete node u. Connect childOfu with parentOfu
        connectNewParent(parentOfu, u, childOfu);

        // Recolor the nodes and fix double black if needed
        if (childOfu == root || u.isRed())
            return; // Done if childOfu is root or if u is red
        else if (childOfu != null && childOfu.isRed())
            childOfu.setBlack(); // Set it black, done
        else // u is black, childOfu is null or black
            // Fix double black on parentOfu
            fixDoubleBlack(grandparentOfu, parentOfu, childOfu, path, i);
    }

    private void fixDoubleBlack(
            Node<E> grandparent, Node<E> parent,
            Node<E> db, ArrayList<Node<E>> path, int i) {
        // Obtain y, y1, and y2
        Node<E> y = (parent.right == db) ? (parent.left) : (parent.right);
        Node<E> y1 = (y.left);
        Node<E> y2 = (y.right);

        if (y.isBlack() && y1 != null && y1.isRed()) {
            if (parent.right == db) {
                // Case 1.1: y is a left black sibling and y1 is red
                connectNewParent(grandparent, parent, y);
                recolor(parent, y, y1); // Adjust colors

                // Adjust child links
                parent.left = y.right;
                y.right = parent;
            }
            else {
                // Case 1.3: y is a right black sibling and y1 is red
                connectNewParent(grandparent, parent, y1);
                recolor(parent, y1, y); // Adjust colors

                // Adjust child links
                parent.right = y1.left;
                y.left = y1.right;
                y1.left = parent;
                y1.right = y;
            }
        }
        else if (y.isBlack() && y2 != null && y2.isRed()) {
            if (parent.right == db) {
                // Case 1.2: y is a left black sibling and y2 is red
                connectNewParent(grandparent, parent, y2);
                recolor(parent, y2, y); // Adjust colors

                // Adjust child links
                y.right = y2.left;
                parent.left = y2.right;
                y2.left = y;
                y2.right = parent;
            }
            else {
                // Case 1.4: y is a right black sibling and y2 is red
                connectNewParent(grandparent, parent, y);
                recolor(parent, y, y2); // Adjust colors

                // Adjust child links
                y.left = parent;
                parent.right = y1;
            }
        }
        else if (y.isBlack()) {
            // Case 2: y is black and y's children are black or null
            y.setRed(); // Change y to red
            if (parent.isRed())
                parent.setBlack(); // Done
            else if (parent != root) {
                // Propagate double black to the parent node
                // Fix new appearance of double black recursively
                db = parent;
                parent = grandparent;
                grandparent =
                        (i >= 3) ? (Node<E>)(path.get(i - 3)) : null;
                fixDoubleBlack(grandparent, parent, db, path, i - 1);
            }
        }
        else { // y.isRed()
            if (parent.right == db) {
                parent.left = y2;
                y.right = parent;
            }
            else {
                parent.right = y.left;
                y.left = parent;
            }

            parent.setRed(); // Color parent red
            y.setBlack(); // Color y black
            connectNewParent(grandparent, parent, y); // y is new parent
            fixDoubleBlack(y, parent, db, path, i - 1);
        }
    }

    public boolean getRed(Node<E> e) {
        Node<E> aNode = e;
        boolean b = true;
        if(aNode.isBlack())
        {
            b = false;
        }
        return b;
    }

    private void recolor(Node<E> parent,
                         Node<E> newParent, Node<E> c) {
        if (parent.isRed())
            newParent.setRed();
        else
            newParent.setBlack();

        parent.setBlack();
        c.setBlack();
    }

    private void connectNewParent(Node<E> grandparent,
                                  Node<E> parent, Node<E> newParent) {
        if (parent == root) {
            root = newParent;
            if (root != null)
                newParent.setBlack();
        }
        else if (grandparent.left == parent)
            grandparent.left = newParent;
        else
            grandparent.right = newParent;

    }

    //@Override
    public void inorder() {
        inorder1(root);
    }

    private void inorder1(Node<E> root){
        if(root != null){
            inorder1(root.left);
            System.out.print(root.element+" ");
            inorder1(root.right);
        }
    }

    private void preorder1(Node<E> root){
        if(root != null){
            System.out.print(root.element+" ");
            preorder1(root.left);
            preorder1(root.right);
        }
    }

    //@Override
    public int getSize() {
        return size;
    }

    public Node<E> getRoot(){
        return root;
    }

    private void postorder1(Node<E> root){
        if(root != null){
            postorder1(root.left);
            postorder1(root.right);
            System.out.print(root.element +" ");
        }
    }

    //@Override
    public void postorder() {
        postorder1(root);
    }

    //@Override
    public void preorder() {
        preorder1(root);
    }

    public ArrayList<Node<E>> path(E e){
        ArrayList<Node<E>> list = new ArrayList<>();
        Node<E> current = root;
        while(current != null){
            list.add(current);
            if(e.compareTo(current.element) < 0)
                current = current.left;
            else if(e.compareTo(current.element) > 0)
                current = current.right;
            else
                break;
        }
        return list;
    }

    //@Override
    public boolean isEmpty() {
        return getSize() == 0;
    }



    private E findMax(Node<E> root){
        Node<E> temp = root;
        while(temp.right != null)
            temp = temp.right;
        return temp.element;
    }
}
