package Q2_ModuleLoader;

import java.util.*;

public class Q2 
{
    public static boolean dependency(int n, List<List<Integer>> edges) 
    {
        List<List<Integer>> adj = new ArrayList<>();

        for (int i = 0; i < n; i++) 
        {
            adj.add(new ArrayList<>());
        }

        for (List<Integer> edge : edges) 
        {
            int from = edge.get(0);
            int to = edge.get(1);
            adj.get(from).add(to);
        }

        return false;
    }

    public static void main(String[] args) 
    {
        int n = 4;

        List<List<Integer>> edges = Arrays.asList(Arrays.asList(0, 1), Arrays.asList(1, 2), Arrays.asList(2, 3));

        System.out.println(dependency(n, edges));
    }
}
