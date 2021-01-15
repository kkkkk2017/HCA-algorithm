package comp2019_Assignment1;

import java.util.Comparator;

/**
 * This class represents a location on a rectangular map.
 * A location is a pair (row,column).
 * The top left corner of the map is location (0,0);
 * The top right corner is at (0,w-1), where w is the number of columns in the grid.
 *
 * DO NOT MODIFY THE SIGNATURE OF EXISTING METHODS.
 * Otherwise, JUnit tests will fail and you will receive no credit for your code.
 * Of course, you can add additional methods and classes in your implementation.
 *
 */
public class Location implements Comparable{
    private int row, column;
    private double g, h, f;
    private Location parent;

    public Location(int row, int column) {
        this.row = row;
        this.column = column;
    }


    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    public Location getParent() {
        return parent;
    }

    public void setParent(Location parent) {
        this.parent = parent;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + column;
        result = prime * result + row;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        Location other = (Location) obj;
        return this == obj || other.column == column && other.row == row;
    }

    @Override
    public String toString() {
        return "(" + row + "," + column + ")";
    }

    @Override
    public int compareTo(Object o) {
        Location location = (Location) o;
        if (f < location.getF()){
            return -1;
        } else if (f > location.getF()){
            return 1;
        }
        return 0;
    }
}

