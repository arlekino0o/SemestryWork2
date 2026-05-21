import java.util.*;

public class Treap {
    private static class Node {
        int key;
        int priority;
        Node left;
        Node right;

        Node(int key, int priority) {
            this.key = key;
            this.priority = priority;
        }
    }

    private Node root;
    private final Random random = new Random();

    private long steps;

    public long getSteps() {
        return steps;
    }

    public void resetSteps() {
        steps = 0;
    }

    // ---------------- INSERT ----------------

    public void insert(int key) {
        int priority = random.nextInt();
        root = insert(root, new Node(key, priority));
    }

    private Node insert(Node current, Node newNode) {
        steps++;

        if (current == null) {
            return newNode;
        }

        if (newNode.key < current.key) {
            current.left = insert(current.left, newNode);

            if (current.left.priority > current.priority) {
                current = rotateRight(current);
            }
        } else if (newNode.key > current.key) {
            current.right = insert(current.right, newNode);

            if (current.right.priority > current.priority) {
                current = rotateLeft(current);
            }
        }

        // Если ключ уже есть, ничего не вставляем.
        return current;
    }

    // ---------------- SEARCH ----------------

    public boolean search(int key) {
        return search(root, key);
    }

    private boolean search(Node current, int key) {
        steps++;

        if (current == null) {
            return false;
        }

        if (key == current.key) {
            return true;
        }

        if (key < current.key) {
            return search(current.left, key);
        } else {
            return search(current.right, key);
        }
    }

    // ---------------- DELETE ----------------

    public void delete(int key) {
        root = delete(root, key);
    }

    private Node delete(Node current, int key) {
        steps++;

        if (current == null) {
            return null;
        }

        if (key < current.key) {
            current.left = delete(current.left, key);
        } else if (key > current.key) {
            current.right = delete(current.right, key);
        } else {
            current = merge(current.left, current.right);
        }

        return current;
    }

    // ---------------- MERGE ----------------

    private Node merge(Node left, Node right) {
        steps++;

        if (left == null) {
            return right;
        }

        if (right == null) {
            return left;
        }

        if (left.priority > right.priority) {
            left.right = merge(left.right, right);
            return left;
        } else {
            right.left = merge(left, right.left);
            return right;
        }
    }

    // ---------------- ROTATIONS ----------------

    private Node rotateRight(Node node) {
        Node newRoot = node.left;
        node.left = newRoot.right;
        newRoot.right = node;
        return newRoot;
    }

    private Node rotateLeft(Node node) {
        Node newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;
        return newRoot;
    }

    // ---------------- BFS ----------------

    public void printBFS() {
        if (root == null) {
            System.out.println("Tree is empty");
            return;
        }

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            System.out.print("(" + current.key + ", p=" + current.priority + ") ");

            if (current.left != null) {
                queue.add(current.left);
            }

            if (current.right != null) {
                queue.add(current.right);
            }
        }

        System.out.println();
    }

    // ---------------- DFS INORDER ----------------

    public void printInOrder() {
        printInOrder(root);
        System.out.println();
    }

    private void printInOrder(Node current) {
        if (current == null) {
            return;
        }

        printInOrder(current.left);
        System.out.print(current.key + " ");
        printInOrder(current.right);
    }
}