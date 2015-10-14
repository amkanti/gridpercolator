/****************************************************************************
 *  Compilation:  javac PercolationStats.java
 *  Execution:  java PercolationStats N T
 *  Dependencies: StdIn.java StdStats.java Percolation.java WeightedQuickUNionUF.java
 *
 *  Percolation Statistics.
 *
 ****************************************************************************/
/**
 * Class Name: PercolationStats.java Description: The <tt>PercolationStats</tt>
 * .java program performs a series of T computations on N*N grid. It determines
 * the mean, standard deviation of percolation threshold and also the confidence
 * interval using methods mean(),stddev(), confidenceLo() and confidenceHi().
 * <p>
 * This implementation uses Percolation.java to compute the threshold for each
 * computation.
 * 
 * @author Amulya Manchikanti 
 * date : 06/26/2015
 */

public class PercolationStats {
   private int comp; // Number of computations
   private double[] threshold; // Percolation thresholds of T computations
   private double mean, stddev, confilo, confihi;

   /**
    * Computes percolation threshold on N by N grid for T computations and
    * stores the result in a data structure.
    * 
    * @throws java.lang.IllegalArgumentException
    *             if N <= 0 or T<=0
    * @param N
    *            indicates the size of the grid
    * @param T
    *            indicates the number of computations
    */
   public PercolationStats(int N, int T) // perform T independent experiments
                                 // on an N-by-N grid
   {
      if (N <= 0 || T <= 0)
         throw new IllegalArgumentException("Entered argument is not valid");

      int open = 0;
      comp = T;
      threshold = new double[T];
      for (int k = 0; k < T; k++) {
         Percolation p = new Percolation(N);
         while (!p.percolates()) {
            int a = StdRandom.uniform(1, N + 1);
            int b = StdRandom.uniform(1, N + 1);
            // System.out.println("a : " + a + " , b : " + b);
            if (!p.isOpen(a, b))
               open++;
            p.open(a, b);
            p.isFull(a, b);
         }
         // System.out.println("threshold - " + (double)p.openSites/(N*N));
         threshold[k] = (double) open / (N * N);
         open = 0;
      }

   }

   /**
    * Computes sample mean of the thresholds
    * 
    * @return mean value
    */
   public double mean() // sample mean of percolation threshold
   {
      return StdStats.mean(threshold);
   }

   /**
    * Computes sample standard deviation of the thresholds
    * 
    * @return standard deviation value
    */
   public double stddev() // sample standard deviation of percolation threshold
   {
      return StdStats.stddev(threshold);
   }

   /**
    * Computes the low endpoint of the confidence interval
    * 
    * @return low endpoint value
    */
   public double confidenceLo() // low endpoint of 95% confidence interval
   {
      return (mean() - ((1.96 * stddev()) / Math.sqrt(comp)));
   }

   /**
    * Computes the high endpoint of the confidence interval
    * 
    * @return high endpoint value
    */
   public double confidenceHi() // high endpoint of 95% confidence interval
   {
      return (mean() + ((1.96 * stddev()) / Math.sqrt(comp)));
   }

   public static void main(String[] args) // test client (described below)
   {
      // System.out.println("Enter the size of the grid : ");
      int N = StdIn.readInt();
      // System.out.println("Enter the number of computations : ");
      int T = StdIn.readInt();
      PercolationStats pstats = new PercolationStats(N, T);
      pstats.mean = pstats.mean();
      pstats.stddev = pstats.stddev();
      pstats.confilo = pstats.confidenceLo();
      pstats.confihi = pstats.confidenceHi();
      System.out.println("mean       = " + pstats.mean);
      System.out.println("stddev       = " + pstats.stddev);
      System.out.println("95% confidence interval       = " + pstats.confilo
            + ", " + pstats.confihi);
   }

}
