import java.util.Arrays;

/**
 * Missions 4 and 5.
 * Strongly connected components and routes between zones.
 */
public class Zones {

    /**
     * Mission 4: Kosaraju algorithm.
     * Uses iterative DFS to avoid stack overflow.
     */
    public static int[] kosaraju(Input.Slides s) {
        int n = s.n();

        int[][] outgoing = Graphs.slidesFrom(s);
        int[][] incoming = Graphs.slidesInto(s);

        boolean[] visited = new boolean[n];
        int[] finishOrder = new int[n];
        int finishCount = 0;

        int[] stack = new int[n];
        int[] nextEdge = new int[n];

        // First DFS pass: calculate finishing order.
        for (int start = 0; start < n; start++) {
            if (visited[start]) {
                continue;
            }

            int top = 0;
            stack[0] = start;
            visited[start] = true;

            while (top >= 0) {
                int current = stack[top];

                if (nextEdge[current] < outgoing[current].length) {
                    int edge = outgoing[current][nextEdge[current]++];
                    int neighbor = s.to()[edge];

                    if (!visited[neighbor]) {
                        visited[neighbor] = true;
                        stack[++top] = neighbor;
                    }
                } else {
                    finishOrder[finishCount++] = current;
                    top--;
                }
            }
        }

        // Second DFS pass on the reversed graph.
        int[] zoneOf = new int[n];
        Arrays.fill(zoneOf, -1);

        int zoneCount = 0;

        for (int i = finishCount - 1; i >= 0; i--) {
            int start = finishOrder[i];

            if (zoneOf[start] != -1) {
                continue;
            }

            int top = 0;
            stack[0] = start;
            zoneOf[start] = zoneCount;

            while (top >= 0) {
                int current = stack[top--];

                for (int edge : incoming[current]) {
                    int neighbor = s.from()[edge];

                    if (zoneOf[neighbor] == -1) {
                        zoneOf[neighbor] = zoneCount;
                        stack[++top] = neighbor;
                    }
                }
            }

            zoneCount++;
        }

        return zoneOf;
    }

    /**
     * Mission 5: Longest chain and cheapest route.
     * Zone numbers are in topological order.
     */
    public static long[] routes(
            Input.Slides s,
            int[] zoneOf,
            int zoneCount) {

        if (zoneCount == 0) {
            return new long[]{0, 0};
        }

        // Build zone adjacency using slide indexes.
        int[] head = new int[zoneCount];
        Arrays.fill(head, -1);

        int[] next = new int[s.from().length];

        for (int i = 0; i < s.from().length; i++) {
            int fromZone = zoneOf[s.from()[i]];
            int toZone = zoneOf[s.to()[i]];

            if (fromZone == toZone) {
                continue;
            }

            next[i] = head[fromZone];
            head[fromZone] = i;
        }

        // Longest chain in the entire zone DAG.
        long[] longest = new long[zoneCount];
        Arrays.fill(longest, 1);

        // Shortest route from the starting zone.
        long infinity = Long.MAX_VALUE / 4;
        long[] distance = new long[zoneCount];
        Arrays.fill(distance, infinity);

        int startZone = zoneOf[0];
        int exitZone = zoneOf[s.n() - 1];

        distance[startZone] = 0;

        long flowStages = 1;

        for (int zone = 0; zone < zoneCount; zone++) {
            flowStages = Math.max(flowStages, longest[zone]);

            for (int edge = head[zone];
                 edge != -1;
                 edge = next[edge]) {

                int target = zoneOf[s.to()[edge]];

                longest[target] = Math.max(
                    longest[target],
                    longest[zone] + 1
                );

                if (distance[zone] != infinity) {
                    long candidate =
                        distance[zone] + s.time()[edge];

                    if (candidate < distance[target]) {
                        distance[target] = candidate;
                    }
                }
            }
        }

        return new long[]{
            flowStages,
            distance[exitZone]
        };
    }
}
