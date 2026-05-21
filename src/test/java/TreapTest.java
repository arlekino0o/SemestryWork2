import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class TreapTest {
    private Treap treap;

    @BeforeEach
    void setUp() {
        treap = new Treap();
    }

    // 1. Вставка и поиск
    @Test
    void testInsertAndSearch() {
        treap.insert(10);
        treap.insert(20);
        treap.insert(5);
        assertTrue(treap.search(10));
        assertTrue(treap.search(20));
        assertTrue(treap.search(5));
        assertFalse(treap.search(99));
    }

    // 2. Удаление
    @Test
    void testDelete() {
        treap.insert(10);
        treap.insert(20);
        treap.insert(5);
        treap.delete(10);
        assertFalse(treap.search(10));
        assertTrue(treap.search(20));
        assertTrue(treap.search(5));
        treap.delete(5);
        assertFalse(treap.search(5));
        assertTrue(treap.search(20));
    }

    // 3. Удаление несуществующего ключа не должно ломать дерево
    @Test
    void testDeleteNonExistent() {
        treap.insert(10);
        treap.delete(999); // не должно быть ошибки
        assertTrue(treap.search(10));
    }

    // 4. Свойство BST: inorder обход даёт отсортированный порядок
    @Test
    void testInorderIsSorted() {
        int[] keys = {50, 30, 70, 20, 40, 60, 80};
        for (int k : keys) treap.insert(k);
        // захватываем вывод printInOrder
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));
        treap.printInOrder();
        String output = out.toString().trim();
        int[] sorted = Arrays.stream(keys).sorted().toArray();
        StringBuilder expected = new StringBuilder();
        for (int v : sorted) expected.append(v).append(" ");
        assertEquals(expected.toString().trim(), output);
        System.setOut(System.out);
    }

    // 5. Свойство кучи (max-heap): приоритет родителя > приоритетов детей
    @Test
    void testHeapProperty() {
        // Вставляем много случайных чисел и проверяем свойство
        Random r = new Random(42);
        for (int i = 0; i < 1000; i++) {
            treap.insert(r.nextInt(10000));
        }
        assertTrue(checkHeapProperty(treap));
    }

    private boolean checkHeapProperty(Treap treap) {
        // Используем рефлексию для доступа к корню, т.к. поля private
        try {
            java.lang.reflect.Field rootField = Treap.class.getDeclaredField("root");
            rootField.setAccessible(true);
            Object root = rootField.get(treap);
            return checkHeap(root);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkHeap(Object node) throws Exception {
        if (node == null) return true;
        java.lang.reflect.Field keyField = node.getClass().getDeclaredField("key");
        java.lang.reflect.Field priorityField = node.getClass().getDeclaredField("priority");
        java.lang.reflect.Field leftField = node.getClass().getDeclaredField("left");
        java.lang.reflect.Field rightField = node.getClass().getDeclaredField("right");
        keyField.setAccessible(true);
        priorityField.setAccessible(true);
        leftField.setAccessible(true);
        rightField.setAccessible(true);
        int priority = (int) priorityField.get(node);
        Object left = leftField.get(node);
        Object right = rightField.get(node);
        if (left != null) {
            int leftPrio = (int) priorityField.get(left);
            if (leftPrio > priority) return false;
            if (!checkHeap(left)) return false;
        }
        if (right != null) {
            int rightPrio = (int) priorityField.get(right);
            if (rightPrio > priority) return false;
            if (!checkHeap(right)) return false;
        }
        return true;
    }

    // 6. После вставки множества элементов дерево не теряет элементы
    @Test
    void testAllInsertedElementsPresent() {
        Set<Integer> expected = new HashSet<>();
        Random r = new Random(1);
        for (int i = 0; i < 500; i++) {
            int val = r.nextInt(2000);
            expected.add(val);
            treap.insert(val);
        }
        for (int val : expected) {
            assertTrue(treap.search(val), "Элемент " + val + " не найден");
        }
    }

    // 7. Удаление всех элементов по одному
    @Test
    void testDeleteAll() {
        List<Integer> list = Arrays.asList(10, 20, 30, 40, 50);
        for (int v : list) treap.insert(v);
        for (int v : list) treap.delete(v);
        for (int v : list) assertFalse(treap.search(v));

        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));
        treap.printBFS();
        assertTrue(out.toString().contains("empty"));
        System.setOut(System.out);
    }
}