/**
 * Mission 0 - Disjoint Set Union Tests.
 * Tests union, find, connected, and count.
 */
public class DisjointSetTest {

    static int failed = 0;

    static void check(boolean ok, String what) {
        System.out.println((ok ? "ok    " : "FAIL  ") + what);
        if (!ok) {
            failed++;
        }
    }

    public static void main(String[] args) {

        // Original tests
        DisjointSet d = new DisjointSet(6);

        check(d.count() == 6, "6 sets at the start");
        check(!d.connected(0, 1), "0 and 1 are apart at the start");
        check(d.union(0, 1), "union(0, 1) joins two sets");
        check(d.connected(0, 1), "0 and 1 are together");
        check(!d.union(1, 0), "union(1, 0) again returns false");
        check(d.count() == 5, "5 sets after one union");

        d.union(2, 3);
        d.union(1, 3);

        check(d.connected(0, 2), "0 and 2 are together through 1 and 3");
        check(d.count() == 3, "3 sets: {0,1,2,3} {4} {5}");
        check(d.find(4) == 4 && d.find(5) == 5,
              "a single cell is its own root");

        // Test 1: Union with itself
        DisjointSet self = new DisjointSet(5);

        check(!self.union(2, 2),
              "union with itself returns false");
        check(self.count() == 5,
              "self union does not change count");

        // Test 2: Large chain
        int n = 10000;
        DisjointSet large = new DisjointSet(n);

        for (int i = 0; i < n - 1; i++) {
            large.union(i, i + 1);
        }

        check(large.connected(0, n - 1),
              "first and last elements are connected");
        check(large.count() == 1,
              "large chain forms one set");

        // Test 3: Count after many unions
        DisjointSet many = new DisjointSet(100);

        for (int i = 0; i < 50; i++) {
            many.union(i * 2, i * 2 + 1);
        }

        check(many.count() == 50,
              "100 elements form 50 sets after 50 unions");

        // Test 4: Repeated union
        check(!many.union(0, 1),
              "repeated union returns false");
        check(many.count() == 50,
              "repeated union does not change count");

        // Test 5: Root verification
        int root = large.find(0);

        check(large.find(n - 1) == root,
              "all elements share the same root");

        // Final result
        System.out.println(
            failed == 0
                ? "All checks passed."
                : failed + " checks failed."
        );
    }
}
