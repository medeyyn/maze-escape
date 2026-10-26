import java.util.*;

public class Maze {

    // Mission 1: Build the maze using DSU
    public static int[] breakWalls(Input.Walls w) {
        int n = w.cells();
        DisjointSet dsu = new DisjointSet(n);

        int[] broken = new int[n - 1];
        int count = 0;

        for (int i = 0; i < w.count(); i++) {
            if (dsu.union(w.a()[i], w.b()[i])) {
                broken[count++] = i;
                if (count == n - 1) {
                    break;
                }
            }
        }

        return Arrays.copyOf(broken, count);
    }

    // Mission 2: Kruskal algorithm
    public static int[] cheapestMaze(Input.Walls w) {
        int n = w.cells();
        Integer[] order = new Integer[w.count()];

        for (int i = 0; i < order.length; i++) {
            order[i] = i;
        }

        Arrays.sort(order, Comparator.comparingInt(i -> w.cost()[i]));

        DisjointSet dsu = new DisjointSet(n);
        int[] result = new int[n - 1];
        int count = 0;

        for (int index : order) {
            if (dsu.union(w.a()[index], w.b()[index])) {
                result[count++] = index;

                if (count == n - 1) {
                    break;
                }
            }
        }

        return Arrays.copyOf(result, count);
    }

    // Mission 2: Prim algorithm
    public static long primCost(Input.Walls w) {
        int n = w.cells();

        List<List<int[]>> graph = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < w.count(); i++) {
            int a = w.a()[i];
            int b = w.b()[i];
            int cost = w.cost()[i];

            graph.get(a).add(new int[]{b, cost});
            graph.get(b).add(new int[]{a, cost});
        }

        boolean[] visited = new boolean[n];

        PriorityQueue<int[]> pq = new PriorityQueue<>(
            Comparator.comparingInt(edge -> edge[1])
        );

        pq.add(new int[]{0, 0});

        long totalCost = 0;
        int visitedCount = 0;

        while (!pq.isEmpty() && visitedCount < n) {
            int[] current = pq.poll();
            int cell = current[0];
            int cost = current[1];

            if (visited[cell]) {
                continue;
            }

            visited[cell] = true;
            visitedCount++;
            totalCost += cost;

            for (int[] edge : graph.get(cell)) {
                if (!visited[edge[0]]) {
                    pq.add(edge);
                }
            }
        }

        if (visitedCount != n) {
            throw new IllegalStateException("Maze graph is disconnected");
        }

        return totalCost;
    }
}
