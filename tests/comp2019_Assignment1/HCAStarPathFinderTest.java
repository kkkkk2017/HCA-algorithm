package comp2019_Assignment1;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.DisableOnDebug;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class HCAStarPathFinderTest {

    private static final int MAX_TIME_STEPS = 100;

    /* each test shall complete in less than 3 seconds */
    @Rule
    public TestRule timeout = new DisableOnDebug(new Timeout(3, TimeUnit.SECONDS)); // requires JUnit >= 4.12

    @Test
    public void test01_Q2Example() {
        String[] correctPaths = {
                "(1,1)(1,2)(1,3)(0,3)",
                "(1,2)(1,3)(2,3)(1,3)(1,2)(1,1)",
                null}; // skip Agent 2's path as this is checked later on
        int[] correctPathCosts = { 3, 5, 3 };

        List<Path> calculatedPaths = findAndCheckPaths("resources/terrain21.txt", "resources/agents21.txt", correctPaths, correctPathCosts);

        // check alternative paths for Agent 2
        String path2str = calculatedPaths.get(2).toLocationsString();
        assertTrue("Path incorrect",
                path2str.equals("(3,3)(3,3)(3,3)(2,3)") ||
                path2str.equals("(3,3)(2,3)(3,3)(2,3)") ||
                path2str.equals("(3,3)(3,2)(3,3)(2,3)"));
    }

    @Test
    public void test02_IndependentPaths() {
        String[] correctPaths = {
                "(9,2)(8,2)(7,2)(6,2)(5,2)(4,2)(3,2)(3,1)(2,1)(2,0)(1,0)(0,0)",
                "(6,5)(5,5)(5,4)(5,3)(4,3)(3,3)(2,3)(2,4)",
                "(9,9)(9,8)(9,7)(8,7)(7,7)(6,7)(5,7)(4,7)(3,7)(2,7)(1,7)(1,8)"
        };
        int[] correctPathCosts = {11, 7, 11};

        findAndCheckPaths("resources/terrain22.txt", "resources/agents22.txt", correctPaths, correctPathCosts);
    }

    @Test
    public void test03_CrossingPaths() {
        String[] correctPaths = {
                "(9,2)(8,2)(7,2)(6,2)(5,2)(4,2)(3,2)(3,1)(2,1)(2,0)(1,0)(0,0)",
                "(8,4)(8,3)(8,2)(8,1)(8,0)(7,0)",
                "(3,1)(3,2)(3,3)(2,3)(2,4)"
        };
        int[] correctPathCosts = {11, 5, 4};

        findAndCheckPaths("resources/terrain22.txt", "resources/agents23.txt", correctPaths, correctPathCosts);
    }

    @Test
    public void test04_ObstructedPaths() {
        String[] correctPaths = {
                "(9,2)(8,2)(7,2)(6,2)(5,2)(4,2)(3,2)(3,1)(2,1)(2,0)(1,0)(0,0)",
                "(8,3)(8,3)(8,2)(8,1)(8,0)(7,0)"
        };
        int[] correctPathCosts = {11, 5};

        findAndCheckPaths("resources/terrain22.txt", "resources/agents24.txt", correctPaths, correctPathCosts);
    }

    @Test
    public void test05_HeadOnCollision() {
        RectangularMap map = RectangularMapParser.fromFile("resources/terrain23.txt");
        List<Agent> agents = AgentListParser.fromFile("resources/agents25.txt");
        HCAStarPathFinder tester = new HCAStarPathFinder(map, agents, MAX_TIME_STEPS);
        List<Path> calculatedPaths = tester.findPaths();
        assertNull("Solution must not exist", calculatedPaths);
    }

    @Test
    public void test06_FeasibleScenario() {
        String[] correctPaths = {
                "(4,4)(3,4)(2,4)(1,4)(0,4)",
                null // there are multiple possible paths
        };
        int[] correctPathCosts = {4, 5};

        List<Path> calculatedPaths = findAndCheckPaths("resources/terrain23.txt", "resources/agents26.txt", correctPaths, correctPathCosts);

        // check alternative paths for Agent 1
        String path2str = calculatedPaths.get(1).toLocationsString();
        assertTrue("Path incorrect",
                path2str.equals("(0,4)(1,4)(1,4)(1,5)(1,4)(2,4)") ||
                         path2str.equals("(0,4)(0,4)(1,4)(1,5)(1,4)(2,4)") ||
                         path2str.equals("(0,4)(1,4)(1,5)(1,5)(1,4)(2,4)"));
    }

    @Test
    public void test07_ObstructedPaths2() {
        String[] correctPaths = {
                "(9,3)(8,3)(7,3)(6,3)(5,3)(4,3)(3,3)(2,3)(1,3)(0,3)",
                "(8,3)(7,3)(6,3)(6,4)(6,3)(7,3)",
                "(1,3)(0,3)(0,4)(0,5)(1,5)(2,5)(3,5)(4,5)(5,5)(5,4)(5,3)(4,3)(3,3)"
        };
        int[] correctPathCosts = {9, 5, 12};

        findAndCheckPaths("resources/terrain24.txt", "resources/agents27.txt", correctPaths, correctPathCosts);
    }


    @Test
    public void test08_LargeWorld() {
        String[] correctPaths = {
                "(50,30)(50,31)(50,32)(50,33)(50,34)(50,35)(50,36)(50,37)(50,38)(50,39)(50,40)(50,41)(50,42)(50,43)(50,44)(50,45)(50,46)(50,47)(50,48)(50,49)(50,50)(50,51)(50,52)(50,53)(50,54)(50,55)(50,56)(50,57)(50,58)(50,59)(50,60)(50,61)(50,62)(50,63)(50,64)(50,65)(50,66)(50,67)(50,68)(50,69)(50,70)(50,71)(50,72)(50,73)(50,74)(50,75)(50,76)(50,77)(50,78)(50,79)(50,80)",
                null // there are 20 different correct paths (each has one WAIT action in the first half of the path)
        };
        int[] correctPathCosts = {50, 51};
        findAndCheckPaths("resources/terrain02.txt", "resources/agents28.txt", correctPaths, correctPathCosts);
    }

    @Test
    public void test09_ManyAgents() {
        String[] correctPaths = {
                "(0,0)(0,1)(0,2)(0,3)(0,4)(0,5)(0,6)(0,7)(0,8)(0,9)(1,9)(2,9)(2,8)(2,7)(2,6)(2,5)(2,4)(2,3)(2,2)(3,2)(4,2)(4,3)(4,4)(4,5)(4,6)(4,7)",
                "(0,1)(0,2)(0,3)(0,4)(0,5)(0,6)(0,7)(0,8)(0,9)(1,9)(2,9)(2,8)(2,7)(2,6)(2,5)(2,4)(2,3)(2,2)(3,2)(4,2)(4,3)(4,4)(4,5)(4,6)(4,7)(5,7)(6,7)(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(9,9)(10,9)(10,8)(10,7)(10,6)(10,5)(10,4)(10,3)(10,2)(10,1)(10,0)(9,0)(8,0)(7,0)(6,0)(5,0)(4,0)(3,0)(2,0)(1,0)(0,0)(0,1)",
                "(0,2)(0,3)(0,4)(0,5)(0,6)(0,7)(0,8)(0,9)(1,9)(2,9)(2,8)(2,7)(2,6)(2,5)(2,4)(2,3)(2,2)(3,2)(4,2)(4,3)(4,4)(4,5)(4,6)(4,7)(5,7)(6,7)(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(9,9)(10,9)(10,8)(10,7)(10,6)(10,5)(10,4)(10,3)(10,2)(10,1)(10,0)(9,0)(8,0)(7,0)(6,0)(5,0)(4,0)(3,0)(2,0)(1,0)(0,0)(0,1)(0,2)",
                "(0,3)(0,4)(0,5)(0,6)(0,7)(0,8)(0,9)(1,9)(2,9)(2,8)(2,7)(2,6)(2,5)(2,4)(2,3)(2,2)(3,2)(4,2)(4,3)(4,4)(4,5)(4,6)(4,7)(5,7)(6,7)(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(9,9)(10,9)(10,8)(10,7)(10,6)(10,5)(10,4)(10,3)(10,2)(10,1)(10,0)(9,0)(8,0)(7,0)(6,0)(5,0)(4,0)(3,0)(2,0)(1,0)(0,0)(0,1)(0,2)(0,3)",
                "(0,4)(0,5)(0,6)(0,7)(0,8)(0,9)(1,9)(2,9)(2,8)(2,7)(2,6)(2,5)(2,4)(2,3)(2,2)(3,2)(4,2)(4,3)(4,4)(4,5)(4,6)(4,7)(5,7)(6,7)(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(9,9)(10,9)(10,8)(10,7)(10,6)(10,5)(10,4)(10,3)(10,2)(10,1)(10,0)(9,0)(8,0)(7,0)(6,0)(5,0)(4,0)(3,0)(2,0)(1,0)(0,0)(0,1)(0,2)(0,3)(0,4)",
                "(0,5)(0,6)(0,7)(0,8)(0,9)(1,9)(2,9)(2,8)(2,7)(2,6)(2,5)(2,4)(2,3)(2,2)(3,2)(4,2)(4,3)(4,4)(4,5)(4,6)(4,7)(5,7)(6,7)(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(9,9)(10,9)(10,8)(10,7)(10,6)(10,5)(10,4)(10,3)(10,2)(10,1)(10,0)(9,0)(8,0)(7,0)(6,0)(5,0)(4,0)(3,0)(2,0)(1,0)(0,0)(0,1)(0,2)(0,3)(0,4)(0,5)",
                "(0,6)(0,7)(0,8)(0,9)(1,9)(2,9)(2,8)(2,7)(2,6)(2,5)(2,4)(2,3)(2,2)(3,2)(4,2)(4,3)(4,4)(4,5)(4,6)(4,7)(5,7)(6,7)(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(9,9)(10,9)(10,8)(10,7)(10,6)(10,5)(10,4)(10,3)(10,2)(10,1)(10,0)(9,0)(8,0)(7,0)(6,0)(5,0)(4,0)(3,0)(2,0)(1,0)(0,0)(0,1)(0,2)(0,3)(0,4)(0,5)(0,6)",
                "(0,7)(0,8)(0,9)(1,9)(2,9)(2,8)(2,7)(2,6)(2,5)(2,4)(2,3)(2,2)(3,2)(4,2)(4,3)(4,4)(4,5)(4,6)(4,7)(5,7)(6,7)(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(9,9)(10,9)(10,8)(10,7)(10,6)(10,5)(10,4)(10,3)(10,2)(10,1)(10,0)(9,0)(8,0)(7,0)(6,0)(5,0)(4,0)(3,0)(2,0)(1,0)(0,0)(0,1)(0,2)(0,3)(0,4)(0,5)(0,6)(0,7)",
                "(0,8)(0,9)(1,9)(2,9)(2,8)(2,7)(2,6)(2,5)(2,4)(2,3)(2,2)(3,2)(4,2)(4,3)(4,4)(4,5)(4,6)(4,7)(5,7)(6,7)(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(9,9)(10,9)(10,8)(10,7)(10,6)(10,5)(10,4)(10,3)(10,2)(10,1)(10,0)(9,0)(8,0)(7,0)(6,0)(5,0)(4,0)(3,0)(2,0)(1,0)(0,0)(0,1)(0,2)(0,3)(0,4)(0,5)(0,6)(0,7)(0,8)",
                "(0,9)(1,9)(2,9)(2,8)(2,7)(2,6)(2,5)(2,4)(2,3)(2,2)(3,2)(4,2)(4,3)(4,4)(4,5)(4,6)(4,7)(5,7)(6,7)(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(9,9)(10,9)(10,8)(10,7)(10,6)(10,5)(10,4)(10,3)(10,2)(10,1)(10,0)(9,0)(8,0)(7,0)(6,0)(5,0)(4,0)(3,0)(2,0)(1,0)(0,0)(0,1)(0,2)(0,3)(0,4)(0,5)(0,6)(0,7)(0,8)(0,9)",
                "(1,9)(2,9)(2,8)(2,7)(2,6)(2,5)(2,4)(2,3)(2,2)(3,2)(4,2)(4,3)(4,4)(4,5)(4,6)(4,7)(5,7)(6,7)(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(9,9)(10,9)(10,8)(10,7)(10,6)(10,5)(10,4)(10,3)(10,2)(10,1)(10,0)(9,0)(8,0)(7,0)(6,0)(5,0)(4,0)(3,0)(2,0)(1,0)(0,0)(0,1)(0,2)(0,3)(0,4)(0,5)(0,6)(0,7)(0,8)(0,9)(1,9)",
                "(2,9)(2,8)(2,7)(2,6)(2,5)(2,4)(2,3)(2,2)(3,2)(4,2)(4,3)(4,4)(4,5)(4,6)(4,7)(5,7)(6,7)(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(9,9)(10,9)(10,8)(10,7)(10,6)(10,5)(10,4)(10,3)(10,2)(10,1)(10,0)(9,0)(8,0)(7,0)(6,0)(5,0)(4,0)(3,0)(2,0)(1,0)(0,0)(0,1)(0,2)(0,3)(0,4)(0,5)(0,6)(0,7)(0,8)(0,9)(1,9)(2,9)",
                "(2,8)(2,7)(2,6)(2,5)(2,4)(2,3)(2,2)(3,2)(4,2)(4,3)(4,4)(4,5)(4,6)(4,7)(5,7)(6,7)(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(9,9)(10,9)(10,8)(10,7)(10,6)(10,5)(10,4)(10,3)(10,2)(10,1)(10,0)(9,0)(8,0)(7,0)(6,0)(5,0)(4,0)(3,0)(2,0)(1,0)(0,0)(0,1)(0,2)(0,3)(0,4)(0,5)(0,6)(0,7)(0,8)(0,9)(1,9)(2,9)(2,8)",
                "(2,7)(2,6)(2,5)(2,4)(2,3)(2,2)(3,2)(4,2)(4,3)(4,4)(4,5)(4,6)(4,7)(5,7)(6,7)(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(9,9)(10,9)(10,8)(10,7)(10,6)(10,5)(10,4)(10,3)(10,2)(10,1)(10,0)(9,0)(8,0)(7,0)(6,0)(5,0)(4,0)(3,0)(2,0)(1,0)(0,0)(0,1)(0,2)(0,3)(0,4)(0,5)(0,6)(0,7)(0,8)(0,9)(1,9)(2,9)(2,8)(2,7)",
                "(2,6)(2,5)(2,4)(2,3)(2,2)(3,2)(4,2)(4,3)(4,4)(4,5)(4,6)(4,7)(5,7)(6,7)(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(9,9)(10,9)(10,8)(10,7)(10,6)(10,5)(10,4)(10,3)(10,2)(10,1)(10,0)(9,0)(8,0)(7,0)(6,0)(5,0)(4,0)(3,0)(2,0)(1,0)(0,0)(0,1)(0,2)(0,3)(0,4)(0,5)(0,6)(0,7)(0,8)(0,9)(1,9)(2,9)(2,8)(2,7)(2,6)",
                "(2,5)(2,4)(2,3)(2,2)(3,2)(4,2)(4,3)(4,4)(4,5)(4,6)(4,7)(5,7)(6,7)(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(9,9)(10,9)(10,8)(10,7)(10,6)(10,5)(10,4)(10,3)(10,2)(10,1)(10,0)(9,0)(8,0)(7,0)(6,0)(5,0)(4,0)(3,0)(2,0)(1,0)(0,0)(0,1)(0,2)(0,3)(0,4)(0,5)(0,6)(0,7)(0,8)(0,9)(1,9)(2,9)(2,8)(2,7)(2,6)(2,5)",
                "(2,4)(2,3)(2,2)(3,2)(4,2)(4,3)(4,4)(4,5)(4,6)(4,7)(5,7)(6,7)(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(9,9)(10,9)(10,8)(10,7)(10,6)(10,5)(10,4)(10,3)(10,2)(10,1)(10,0)(9,0)(8,0)(7,0)(6,0)(5,0)(4,0)(3,0)(2,0)(1,0)(0,0)(0,1)(0,2)(0,3)(0,4)(0,5)(0,6)(0,7)(0,8)(0,9)(1,9)(2,9)(2,8)(2,7)(2,6)(2,5)(2,4)",
                "(2,3)(2,2)(3,2)(4,2)(4,3)(4,4)(4,5)(4,6)(4,7)(5,7)(6,7)(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(9,9)(10,9)(10,8)(10,7)(10,6)(10,5)(10,4)(10,3)(10,2)(10,1)(10,0)(9,0)(8,0)(7,0)(6,0)(5,0)(4,0)(3,0)(2,0)(1,0)(0,0)(0,1)(0,2)(0,3)(0,4)(0,5)(0,6)(0,7)(0,8)(0,9)(1,9)(2,9)(2,8)(2,7)(2,6)(2,5)(2,4)(2,3)",
                "(2,2)(3,2)(4,2)(4,3)(4,4)(4,5)(4,6)(4,7)(5,7)(6,7)(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(9,9)(10,9)(10,8)(10,7)(10,6)(10,5)(10,4)(10,3)(10,2)(10,1)(10,0)(9,0)(8,0)(7,0)(6,0)(5,0)(4,0)(3,0)(2,0)(1,0)(0,0)(0,1)(0,2)(0,3)(0,4)(0,5)(0,6)(0,7)(0,8)(0,9)(1,9)(2,9)(2,8)(2,7)(2,6)(2,5)(2,4)(2,3)(2,2)",
                "(3,2)(4,2)(4,3)(4,4)(4,5)(4,6)(4,7)(5,7)(6,7)(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(9,9)(10,9)(10,8)(10,7)(10,6)(10,5)(10,4)(10,3)(10,2)(10,1)(10,0)(9,0)(8,0)(7,0)(6,0)(5,0)(4,0)(3,0)(2,0)(1,0)(0,0)(0,1)(0,2)(0,3)(0,4)(0,5)(0,6)(0,7)(0,8)(0,9)(1,9)(2,9)(2,8)(2,7)(2,6)(2,5)(2,4)(2,3)(2,2)(3,2)",
                "(4,2)(4,3)(4,4)(4,5)(4,6)(4,7)(5,7)(6,7)(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(9,9)(10,9)(10,8)(10,7)(10,6)(10,5)(10,4)(10,3)(10,2)(10,1)(10,0)(9,0)(8,0)(7,0)(6,0)(5,0)(4,0)(3,0)(2,0)(1,0)(0,0)(0,1)(0,2)(0,3)(0,4)(0,5)(0,6)(0,7)(0,8)(0,9)(1,9)(2,9)(2,8)(2,7)(2,6)(2,5)(2,4)(2,3)(2,2)(3,2)(4,2)",
                "(4,3)(4,4)(4,5)(4,6)(4,7)(5,7)(6,7)(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(9,9)(10,9)(10,8)(10,7)(10,6)(10,5)(10,4)(10,3)(10,2)(10,1)(10,0)(9,0)(8,0)(7,0)(6,0)(5,0)(4,0)(3,0)(2,0)(1,0)(0,0)(0,1)(0,2)(0,3)(0,4)(0,5)(0,6)(0,7)(0,8)(0,9)(1,9)(2,9)(2,8)(2,7)(2,6)(2,5)(2,4)(2,3)(2,2)(3,2)(4,2)(4,3)",
                "(4,4)(4,5)(4,6)(4,7)(5,7)(6,7)(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(9,9)(10,9)(10,8)(10,7)(10,6)(10,5)(10,4)(10,3)(10,2)(10,1)(10,0)(9,0)(8,0)(7,0)(6,0)(5,0)(4,0)(3,0)(2,0)(1,0)(0,0)(0,1)(0,2)(0,3)(0,4)(0,5)(0,6)(0,7)(0,8)(0,9)(1,9)(2,9)(2,8)(2,7)(2,6)(2,5)(2,4)(2,3)(2,2)(3,2)(4,2)(4,3)(4,4)",
                "(4,5)(4,6)(4,7)(5,7)(6,7)(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(9,9)(10,9)(10,8)(10,7)(10,6)(10,5)(10,4)(10,3)(10,2)(10,1)(10,0)(9,0)(8,0)(7,0)(6,0)(5,0)(4,0)(3,0)(2,0)(1,0)(0,0)(0,1)(0,2)(0,3)(0,4)(0,5)(0,6)(0,7)(0,8)(0,9)(1,9)(2,9)(2,8)(2,7)(2,6)(2,5)(2,4)(2,3)(2,2)(3,2)(4,2)(4,3)(4,4)(4,5)",
                "(4,6)(4,7)(5,7)(6,7)(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(9,9)(10,9)(10,8)(10,7)(10,6)(10,5)(10,4)(10,3)(10,2)(10,1)(10,0)(9,0)(8,0)(7,0)(6,0)(5,0)(4,0)(3,0)(2,0)(1,0)(0,0)(0,1)(0,2)(0,3)(0,4)(0,5)(0,6)(0,7)(0,8)(0,9)(1,9)(2,9)(2,8)(2,7)(2,6)(2,5)(2,4)(2,3)(2,2)(3,2)(4,2)(4,3)(4,4)(4,5)(4,6)",
                "(4,7)(5,7)(6,7)(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(7,9)",
                "(5,7)(6,7)(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(7,9)(6,9)",
                "(6,7)(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(7,9)(6,9)(5,9)",
                "(6,6)(6,5)(6,4)(6,3)(6,2)(7,2)(8,2)(8,3)(8,4)(8,5)(8,6)(8,7)(8,8)(8,9)(7,9)(6,9)(5,9)(4,9)"
        };
        int[] correctPathCosts = {25, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 62, 17, 17, 17, 17};
        findAndCheckPaths("resources/terrain26.txt", "resources/agents29.txt", correctPaths, correctPathCosts);
    }


    static List<Path> findAndCheckPaths(String mapFile, String agentsFile, String[] correctPaths, int[] correctPathCosts) {
        RectangularMap map = RectangularMapParser.fromFile(mapFile);
        List<Agent> agents = AgentListParser.fromFile(agentsFile);
        return findAndCheckPaths(map, agents, correctPaths, correctPathCosts);
    }

    static List<Path> findAndCheckPaths(RectangularMap map, List<Agent> agents, String[] correctPaths, int[] correctPathCosts) {
        HCAStarPathFinder tester = new HCAStarPathFinder(map, agents, MAX_TIME_STEPS);
        List<Path> calculatedPaths = tester.findPaths();

        PathTestUtils.assertValidPaths(calculatedPaths, agents, map);
        PathTestUtils.assertCorrectPathCosts(calculatedPaths, correctPathCosts);
        PathTestUtils.assertPathsEqual(calculatedPaths, correctPaths);

        return calculatedPaths;
    }
}