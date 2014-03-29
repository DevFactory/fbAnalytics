package net.mormolhs.facebook.fbanalytics.utils;


import java.util.Comparator;
import java.util.Vector;

public class ColumnSorter implements Comparator {
    int colIndex;
    boolean ascending;
    public ColumnSorter(int colIndex, boolean ascending) {
        System.out.println("ColumnSorter.ColumnSorter(---colIndex--:"+colIndex+" ,ascending: "+ascending);

        this.colIndex = colIndex;
        this.ascending = ascending;
        System.out.println("ColumnSorter.ColumnSorter()");
    }
    public int compare(Object a, Object b) {
        System.out.println("compare-----:");
        Vector v1 = (Vector)a;
        Vector v2 = (Vector)b;
        Object o1 = v1.get(colIndex);
        Object o2 = v2.get(colIndex);
        System.out.println("ColumnSorter.compare(): -o1- :"+o1+" ,o2: "+o2);
// Treat empty strains like nulls
        if (o1 instanceof String && ((String)o1).length() == 0) {
            o1 = null;
        }
        if (o2 instanceof String && ((String)o2).length() == 0) {
            o2 = null;
        }

// Sort nulls so they appear last, regardless
// of sort order
        if (o1 == null && o2 == null) {
            return 0;
        } else if (o1 == null) {
            return 1;
        } else if (o2 == null) {
            return -1;
        } else if (o1 instanceof Comparable) {
            if (ascending) {
                System.out.println("ascending-->ColumnSorter.compare()-((Comparable)o1).compareTo(o2): "+(((Comparable)o1).compareTo(o2)));
                return ((Comparable)o1).compareTo(o2);
            } else {
                System.out.println("Desending-->ColumnSorter.compare()-((Comparable)o1).compareTo(o2): "+(((Comparable)o1).compareTo(o2)));
                return ((Comparable)o2).compareTo(o1);
            }
        } else {
            if (ascending) {
                System.out.println("ColumnSorter.compare()---o1.toString().compareTo(o2.toString())---: "+(o1.toString().compareTo(o2.toString())));
                return o1.toString().compareTo(o2.toString());
            } else {
                return o2.toString().compareTo(o1.toString());
            }
        }
    }
}