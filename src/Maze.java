/**
 * Missions 1 and 2. Building the maze out of the walls.
 * A wall k joins the cells w.a()[k] and w.b()[k]
 * and costs w.cost()[k].
 * Indexes start from 0.
 * The line number in walls.txt is the index + 1.
 */
public class Maze {

    /**
     * Mission 1.
     * Break walls in the order of the file.
     * A wall is broken only if its cells belong
     * to different sets.
     */
    public static int[] breakWalls(Input.Walls w) {

        int n = w.cells();
        DisjointSet dsu = new DisjointSet(n);

        int[] broken = new int[n - 1];
        int count = 0;

        for (int i = 0; i < w.count(); i++) {

            int a = w.a()[i];
            int b = w.b()[i];

            if (dsu.union(a, b)) {

                broken[count] = i;
                count++;

                if (count == n - 1) {
                    break;
                }
            }
        }

        return java.util.Arrays.copyOf(broken, count);
    }

    /**
     * Mission 2.
     * Kruskal algorithm.
     * Will be implemented in Mission 2.
     */
    public static int[] cheapestMaze(Input.Walls w) {
        // TODO: Mission 2
        return new int[0];
    }

    /**
     * Mission 2.
     * Prim algorithm using a priority queue.
     * Will be implemented in Mission 2.
     */
    public static long primCost(Input.Walls w) {
        // TODO: Mission 2
        return 0;
    }
}
