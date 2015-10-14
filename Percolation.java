/****************************************************************************
 *  Compilation:  javac Percolation.java
 *  Execution:  java Percolation N
 *  Dependencies: StdIn.java StdOut.java WeightedQuickUnionUF.java
 *
 *  Percolation.
 *
 ****************************************************************************/
/**
 * Class Name: Percolation.java Description: The <tt>Percolation</tt> .java
 * program checks if a randomly distributed grid with blocked sites percolates
 * or not using WeighedQuickUnionUF.class. It supports open(), isOpen(),
 * isFull() and percolates() operations. It determines the least number of open
 * sites required for the system to percolate and computes the threshold value -
 * p*.
 * <p>
 * This implementation uses weighted quick union by size(w/o path compression).
 * Initializing a data structure with <em>N*N</em> objects takes quadratic time.
 * Afterwards, <em>open</em>, <em>isOpen</em>, <em>isFull</em>, and
 * <em>percolates</em> takes constant time.
 * 
 * @author Amulya Manchikanti
 * @date : 06/26/2015
 */

public class Percolation {
    private int length; // Number of sites in the grid
    private byte[][] arr; // To store the status of sites
    private WeightedQuickUnionUF objPerc; // WeightedQuickUnionFind object
    private WeightedQuickUnionUF objFull; // WeightedQuickUnionFind back-object

    /**
     * Initializes an empty percolation grid with N*N isolated components with
     * '0' indicating blocked. It also connects top and bottom virtual sites to
     * the respective row elements
     * 
     * @throws java.lang.IllegalArgumentException
     *             if N <= 0
     * @param N
     *            indicates the size of the grid
     */
    public Percolation(int N) // create N-by-N grid, with all sites blocked
    {

        if (N <= 0)
            throw new IllegalArgumentException("Entered argument is not valid");

        objPerc = new WeightedQuickUnionUF(N * N + 2);
        objFull = new WeightedQuickUnionUF(N * N + 2);

        arr = new byte[N + 1][N + 1];
        length = N;
        for (int i = 1; i < N + 1; i++)
            for (int j = 1; j < N + 1; j++)
                arr[i][j] = 0;
    }

    /**
     * Open the site if blocked, increment the number of open sites and connect
     * to all neighboring sites if they are already open
     * 
     * @param i
     *            indicates row value, validates it with validate()
     * @param j
     *            indicates column value, validates it with validate()
     * @throws java.lang.IndexOutOfBoundsException
     *             unless 0 < i <= N and 0 < j <= N
     */
    public void open(int i, int j) // open site (row i, column j) if it is not
                                    // open already
    {

        validate(i);
        validate(j);
        if (arr[i][j] == 1) {
            return;
        }

        arr[i][j] = 1;
        // check left
        if (i - 1 > 0) {
            if (isOpen(i - 1, j)) {
                objPerc.union(xyTo1D(i - 1, j), xyTo1D(i, j));
                objFull.union(xyTo1D(i - 1, j), xyTo1D(i, j));
            }
        }

        // check right
        if (i + 1 < length + 1) {
            if (isOpen(i + 1, j)) {
                objPerc.union(xyTo1D(i + 1, j), xyTo1D(i, j));
                objFull.union(xyTo1D(i + 1, j), xyTo1D(i, j));
            }
        }

        // check up
        if (j - 1 > 0) {
            if (isOpen(i, j - 1)) {
                objPerc.union(xyTo1D(i, j - 1), xyTo1D(i, j));
                objFull.union(xyTo1D(i, j - 1), xyTo1D(i, j));
            }
        }

        // check down
        if (j + 1 < length + 1) {
            if (isOpen(i, j + 1)) {
                objPerc.union(xyTo1D(i, j + 1), xyTo1D(i, j));
                objFull.union(xyTo1D(i, j + 1), xyTo1D(i, j));
            }
        }

        // check: if I am on top row
        // Then connect top virtual site to this site
        if (i == 1 && !objFull.connected(length * length, xyTo1D(i, j))) {
            objPerc.union(length * length, xyTo1D(i, j));
            objFull.union(length * length, xyTo1D(i, j));
        }

        // check: if I am on bottom row
        // Then connect bottom virtual site to this site
        if (i == length) {
            objPerc.union(xyTo1D(i, j), length * length + 1);
        }
    }

    /**
     * Checks if the site is open
     * 
     * @param i
     *            indicates row index
     * @param j
     *            indicates column index
     * @return true if the site is marked open
     */
    public boolean isOpen(int i, int j) // is site (row i, column j) open?
    {
        validate(i);
        validate(j);

        if (arr[i][j] == 1)
            return true;
        return false;
    }

    /**
     * Checks if the site is full? - A site that can be connected to an open
     * site in the top row via a chain of neighboring (left, right, up, down)
     * open sites.
     * 
     * @param i
     *            indicates the row index
     * @param j
     *            indicates the column index
     * @return true if the path exists between virtual top site to the site
     *         passed as the argument
     */
    // (N*N) == top-most virtual site
    // (N*N + 1) == bottom-most virtual site
    public boolean isFull(int i, int j) // is site (row i, column j) full?
    {
        validate(i);
        validate(j);
        if (!isOpen(i, j))
            return false;

        if (objFull.connected(length * length, xyTo1D(i, j)))
            return true;

        return false;
    }

    /**
     * Checks if the whole system percolates or not
     * 
     * @return true if percolates
     */
    public boolean percolates() // does the system percolate?
    {
        if (objPerc.connected(length * length, length * length + 1))
            return true;
        return false;
    }

    /**
     * To uniquely map 2D coordinates to 1D coordinates.
     * 
     * @param i
     *            indicates row value
     * @param j
     *            indicates column value
     * @return 1D coordinate of i,j
     */
    private int xyTo1D(int i, int j) {
        return (i - 1) * length + (j - 1);
    }

    /**
     * 
     * @param p
     *            the index to be validated
     */
    private void validate(int p) // validate that p is a valid index
    {

        if (p < 1 || p > length)
            throw new IndexOutOfBoundsException("index " + p
                    + " is not between 1 and " + length);
    }

    public static void main(String[] args) // test client (optional)
    {
        // System.out.println("Enter the size of the grid : ");
        int gridSize = StdIn.readInt();
        int open = 0;
        Percolation p = new Percolation(gridSize);
        while (!p.percolates()) {
            int a = StdRandom.uniform(1, gridSize + 1);
            int b = StdRandom.uniform(1, gridSize + 1);
            // System.out.println("a : " + a + " , b : " + b);
            if (p.isOpen(a, b))
                open++;
            p.open(a, b);
            p.isFull(a, b);
        }

        System.out.println("System Percolates");
        System.out.println("Number of opensites are " + open);
        System.out.println("Percolation threshold = " + (float) open
                / (gridSize * gridSize));

    }
}
