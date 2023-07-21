/**
 * @BinaryTree_Assignment
 * @Author: Jack Jiang
 * @Date: 2023-05-23
 * @Description:
 * This is a binary tree class that I implemented from scratch. Specifically, it implements a binary search tree data structure.
 * It provides functionality for adding nodes, deleting nodes, finding node depth, counting leaves, checking if the
 * tree is balanced, and displaying the tree in different traversal orders.
 */
package BinaryTreeAssign;
public class BinaryTree {
    private Node root;
    public static final int IN = 1, PRE = 2, POST = 3;

    public BinaryTree(){
        root = null;
    }
    public int depth(int value){
        return depth(root, value, 1);
    }

    private int depth(Node node, int value, int depth) {
        //base cases
        if (node == null) return -1;  //if the node is null return -1
        if (node.getValue() == value) return depth; //if the number is found return the depth

        if (value < node.getValue())
            return depth(node.getLeft(), value, depth+1);  //checking for the direction to search (left or right)
        else
            return depth(node.getRight(), value, depth+1);
    }
    public int countLeaves(){
        return countLeaves(root);
    }
    private int countLeaves(Node branch){
        if (branch == null) return 0; //if the tree is empty return 0
        else if(branch.getLeft() == null && branch.getRight() == null){
            return 1; // if the node is the leaf which have no children in its left and right
        }
        return countLeaves(branch.getLeft()) + countLeaves(branch.getRight()); //add the leaves in the two subtrees (left and right) together
    }

    public int height(){
        return height(root);
    }

    private int height(Node branch){
        if (branch == null) return 0;
        int left_depth = height(branch.getLeft()), right_depth = height(branch.getRight());
        return (left_depth > right_depth)? left_depth+1: right_depth+1; //current level: max height +1
    }

    public boolean isAncestor(int ancestor, int child){
            return findParent(root, ancestor, child);
    }

    private boolean findChild(Node branch, int parent, int child){
        if (branch == null) return false;

        if (branch.getValue() == child)
            //if the node is the child and the node are not the parent, then it is an ancestor
            return branch.getValue() != parent;
        else if (child > branch.getValue())
            //if the child value is greater than the current node's value recursively traverse the right subtree
            return findChild(branch.getRight(), parent, child);
        else if (child < branch.getValue())
            return findChild(branch.getLeft(), parent, child);
        return false;
    }

    private boolean findParent(Node branch, int parent, int child){
        if (branch == null) return false;

        if (branch.getValue() < parent)
            //if the current node's value is less than the ancestor value, recursively traverse the right subtree
            return findParent(branch.getRight(), parent, child);
        else if (branch.getValue() > parent)
            return findParent(branch.getLeft(), parent, child);
        else
            //if the current node's value is equal to the ancestor value, call the helper func to check if the child value is a descendant of the ancestor value
            return findChild(branch, parent, child);

    }

    private int findMax(Node branch){
        //find the maximum value in the subtree rooted at the branch node
        if (branch.getRight() == null)
            return branch.getValue(); //if there is no right child, the current node has the max value
        return findMax(branch.getRight()); //recursively traverse the right child
    }

    private int findMin(Node branch){
        if (branch.getLeft() == null)
            return branch.getValue();
        return findMin(branch.getLeft());
    }

    public void delete(int num){
        deleteNode(num, root);
    }
    private Node deleteNode(int num, Node branch){
        if (branch == null) return null; //if the tree is empty or the value is not found

        if(branch.getValue() > num) {
            //if the number is less than the current node, recursively traverse the left subtree.
            branch.setLeft(deleteNode(num, branch.getLeft()));
        }
        else if(branch.getValue() < num){
            //else if the value is greater than the current node, recursively traverse the right subtree
            branch.setRight(deleteNode(num, branch.getRight()));
        }
        else{
            //if the note that needs to be deleted is found
            if(branch.getRight() == null && branch.getLeft() == null) return null;  //if the node is a leaf, make it null to remove the node
            else if (branch.getLeft() != null) {
                //if the node has only a left child
                branch.setVal(findMax(branch.getLeft())); //replace the node's value with the maximum in the left subtree
                branch.setLeft(deleteNode(branch.getValue(), branch.getLeft())); //recursive delete the duplicate value in the left subtree
            }
            else{
                branch.setVal(findMin(branch.getRight()));
                branch.setRight(deleteNode(branch.getValue(), branch.getRight()));
            }

        }
        return branch;
    }

    public boolean isBalanced(){
        return isBalanced(root);
    }

    private boolean isBalanced(Node branch){
        if (branch == null)
            return true;
        if (Math.abs(height(branch.getLeft()) - height(branch.getRight())) <= 1){
            //check the height difference between the left and right subtrees by recursively check if both subtrees are balanced
            return isBalanced(branch.getLeft()) && isBalanced(branch.getRight());
        }
        return false;
    }
    public void add(int n){
        if(root == null){
            root = new Node(n);
        }
        else{
            add(n, root);
        }
    }

    public void add(int n, Node branch){
        if(n > branch.getValue()){
            if(branch.getRight() == null){
                branch.setRight(new Node(n));
            }
            else{
                add(n, branch.getRight());
            }
        }
        else if(n < branch.getValue()){
            if(branch.getLeft() == null){
                branch.setLeft(new Node(n));
            }
            else{
                add(n, branch.getLeft());
            }
        }
    }
    public void add(BinaryTree tree){
        addNode(tree.root);
    }
    private void addNode(Node branch){
        if (branch == null)
            return;

        add(branch.getValue());
        addNode(branch.getLeft());
        addNode(branch.getRight());
    }
    public void display(){
        print(root, IN);
        System.out.println();
    }
    public void display(int order){
        print(root, order);
        System.out.println();
    }
    public void print(Node branch, int order){
        if(branch != null){
            if (order == IN) {
                print(branch.getLeft(), order); //Traverse left subtree
                System.out.print(branch + " ");
                print(branch.getRight(), order); //Traverse right subtree
            }
            else if (order == PRE){
                System.out.print(branch +" ");
                print(branch.getLeft(), order);
                print(branch.getRight(), order);
            }
            else{
                print(branch.getLeft(), order);
                print(branch.getRight(), order);
                System.out.print(branch +" ");
            }
        }

    }
}

class Node {
    private int val;
    private Node left, right;

    public Node(int v){
        val = v;
        left = right = null;
    }
    public int getValue(){return val;}
    public Node getLeft(){return left;}
    public Node getRight(){return right;}

    public void setVal(int v){val = v;}
    public void setLeft(Node lef){left = lef;}
    public void setRight(Node r){right = r;}

    @Override
    public String toString(){
        return String.valueOf(val);
    }
}

