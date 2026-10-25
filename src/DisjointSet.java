/**
 * Mission 0. Disjoint Set Union (DSU).
 * Uses union by size and path compression.
 */
public class DisjointSet {

    private int[] parent;
    private int[] size;
    private int components;

    public DisjointSet(int n) {
        parent = new int[n];
        size = new int[n];
        components = n;

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    /** Finds the root of the set containing x. */
    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    /** Joins two sets using union by size. */
    public boolean union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);

        if (rootA == rootB) {
            return false;
        }

        if (size[rootA] < size[rootB]) {
            int temp = rootA;
            rootA = rootB;
            rootB = temp;
        }

        parent[rootB] = rootA;
        size[rootA] += size[rootB];
        components--;

        return true;
    }

    /** Checks whether two elements belong to the same set. */
    public boolean connected(int a, int b) {
        return find(a) == find(b);
    }

    /** Returns the current number of sets. */
    public int count() {
        return components;
    }
}
