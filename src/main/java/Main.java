public class Main {
    public static void main(String[] args) {
        Treap treap = new Treap();

        treap.insert(50);
        treap.insert(30);
        treap.insert(70);
        treap.insert(20);
        treap.insert(40);
        treap.insert(60);
        treap.insert(80);

        System.out.println("BFS:");
        treap.printBFS();

        System.out.println("DFS inorder:");
        treap.printInOrder();

        System.out.println("Search 40: " + treap.search(40));
        System.out.println("Search 100: " + treap.search(100));

        treap.delete(30);

        System.out.println("After deleting 30:");
        treap.printBFS();
    }
}