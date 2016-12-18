package edu.umn.cs.spatialHadoop.delaunay;

import edu.umn.cs.spatialHadoop.core.Point;
import edu.umn.cs.spatialHadoop.operations.Head;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.IOException;
import java.util.List;

/**
 * Unit test for the utility class {@link Head}.
 */
public class GSImprovedAlgorithmTest extends TestCase {

  /**
   * Create the test case
   *
   * @param testName
   *          name of the test case
   */
  public GSImprovedAlgorithmTest(String testName) {
    super(testName);
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite() {
    return new TestSuite(GSImprovedAlgorithmTest.class);
  }

  /**
   * Test Delaunay Triangulation for a toy dataset.
   */
  public void testTriangulations() {
    String[] datasetNames = {"test_dt1", "test_dt2", "test_dt3"};
    try {
      for (String datasetName : datasetNames) {
        Point[] points = GSDTAlgorithmTest.readPoints("src/test/resources/"+datasetName+".points");
        List<Point[]> correctTriangulation = GSDTAlgorithmTest.readTriangles("src/test/resources/"+datasetName+".triangles", points);

        GSImprovedAlgorithm algo = new GSImprovedAlgorithm(points, null);
        Triangulation answer = algo.getFinalTriangulation();

        int iTriangle = 0;
        for (Point[] triangle : answer.iterateTriangles()) {
          boolean found = false;
          int i = 0;
          while (!found && i < correctTriangulation.size()) {
            found = GSDTAlgorithmTest.arrayEqualAnyOrder(triangle, correctTriangulation.get(i));
            if (found)
              correctTriangulation.remove(i);
            else
              i++;
          }
          assertTrue(String.format("Triangle #%d (%f, %f), (%f, %f), (%f, %f) not found",
              iTriangle,
              triangle[0].x, triangle[0].y,
              triangle[1].x, triangle[1].y,
              triangle[2].x, triangle[2].y), found);
          iTriangle++;
        }
        for (Point[] triangle : correctTriangulation) {
          System.out.printf("Triangle not found (%f, %f) (%f, %f) (%f, %f)\n",
              triangle[0].x, triangle[0].y,
              triangle[1].x, triangle[1].y,
              triangle[2].x, triangle[2].y
          );
        }
        assertTrue(String.format("%d triangles not found", correctTriangulation.size()),
            correctTriangulation.isEmpty());
      }
    } catch (IOException e) {
      fail("File not found");
    }
  }
}