package edu.grinnell.csc207.sorting;

import java.util.Comparator;

/**
 * Something that sorts using insertion sort.
 *
 * @param <T>
 *   The types of values that are sorted.
 *
 * @author Samuel A. Rebelsky
 */

public class InsertionSorter<T> implements Sorter<T> {
  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The way in which elements are ordered.
   */
  Comparator<? super T> order;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a sorter using a particular comparator.
   *
   * @param comparator
   *   The order in which elements in the array should be ordered
   *   after sorting.
   */
  public InsertionSorter(Comparator<? super T> comparator) {
    this.order = comparator;
  } // InsertionSorter(Comparator)

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+
  
  /**
   * Decomposition- We are sorting one element to its correct spot
   * @param values the generic array our element needs to be sorted in 
   * @param index the index of the element we are sorting
   */
  public void insert(T[] values, int index) {
    T valNeedSorted = values[index];
    int comparing;
    int indexShift = index;

    while((indexShift > 0) && (order.compare(valNeedSorted, values[indexShift-1]) < 0))  {
      values[indexShift] = values [indexShift - 1];
      indexShift--;
    }
    values[indexShift] = valNeedSorted;
  }
  
  /**
   * Sort an array in place using insertion sort.
   *
   * @param values
   *   an array to sort.
   *
   * @post
   *   The array has been sorted according to some order (often
   *   one given to the constructor).
   * @post
   *   For all i, 0 &lt; i &lt; values.length,
   *     order.compare(values[i-1], values[i]) &lt;= 0
   */
  @Override
  public void sort(T[] values) {
    for(int i = 1; i < values.length ; i++){
      insert(values, i);
    }
  } // sort(T[])
} // class InsertionSorter
