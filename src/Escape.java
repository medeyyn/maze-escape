import java.util.Arrays;

/**
 * Mission 3. Escape using iterative DFS.
 * Neighbour order: up, right, down, left.
 */
public class Escape {

    public record Result(int[] path, int visited, int deadEnds) {}

    public static Result escape(Input.Walls w, int[] broken) {

        int rows = w.rows();
        int cols = w.cols();
        int n = rows * cols;
        int exit = n - 1;

        int[][] graph = Graphs.open(w, broken);

        int deadEnds = 0;

        for (int i = 0; i < n; i++) {
            if (graph[i].length == 1) {
                deadEnds++;
            }
        }

        boolean[] visited = new boolean[n];
        int[] parent = new int[n];
        int[] nextDirection = new int[n];

        Arrays.fill(parent, -1);

        int[] stack = new int[n];
        int top = 0;

        stack[0] = 0;
        visited[0] = true;

        int visitedCount = 1;

        while (top >= 0) {

            int current = stack[top];

            if (current == exit) {
                break;
            }

            boolean moved = false;

            while (nextDirection[current] < 4) {

                int direction = nextDirection[current]++;
                int neighbor = -1;

                if (direction == 0 && current >= cols) {
                    neighbor = current - cols;
                } else if (direction == 1 &&
                           current % cols < cols - 1) {
                    neighbor = current + 1;
                } else if (direction == 2 &&
                           current / cols < rows - 1) {
                    neighbor = current + cols;
                } else if (direction == 3 &&
                           current % cols > 0) {
                    neighbor = current - 1;
                }

                if (neighbor == -1 || visited[neighbor]) {
                    continue;
                }

                boolean connected = false;

                for (int next : graph[current]) {
                    if (next == neighbor) {
                        connected = true;
                        break;
                    }
                }

                if (!connected) {
                    continue;
                }

                visited[neighbor] = true;
                visitedCount++;
                parent[neighbor] = current;

                stack[++top] = neighbor;
                moved = true;
                break;
            }

            if (!moved) {
                top--;
            }
        }

        if (!visited[exit]) {
            return new Result(new int[0], visitedCount, deadEnds);
        }

        int pathLength = 0;

        for (int cell = exit; cell != -1; cell = parent[cell]) {
            pathLength++;
        }

        int[] path = new int[pathLength];
        int index = pathLength - 1;

        for (int cell = exit; cell != -1; cell = parent[cell]) {
            path[index--] = cell;
        }

        return new Result(path, visitedCount, deadEnds);
    }
}
