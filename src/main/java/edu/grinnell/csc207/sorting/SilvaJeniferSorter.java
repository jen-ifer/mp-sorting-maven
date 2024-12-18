package edu.grinnell.csc207.sorting;

import edu.grinnell.csc207.util.ArrayUtils;

import java.util.Comparator;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

/**
 * Something that sorts using a sort with the help of chatgpt
 *
 * @param <T>
 *   The types of values that are sorted.
 *
 * @author Jenifer Silva & Chat GPT 4o mini
 */
public class SilvaJeniferSorter <T> implements Sorter<T>{

    private final Comparator<? super T> order;

    // Constructor to initialize the comparator
    public SilvaJeniferSorter(Comparator<? super T> comparator) {
        this.order = comparator;
    }

    @Override
    public void sort(T[] values) {
        if (isNearlySorted(values)) {
            insertionSort(values, 0, values.length - 1);
        } else {
            advancedAdaptiveSort(values, 0, values.length - 1);
        }
    }

    // Pre-scan to check if the array is nearly sorted
    private boolean isNearlySorted(T[] values) {
        int unsortedCount = 0;
        int threshold = values.length / 10; // 10% unsorted
        for (int i = 1; i < values.length; i++) {
            if (order.compare(values[i - 1], values[i]) > 0) {
                unsortedCount++;
            }
            if (unsortedCount > threshold) return false;
        }
        return true;
    }

    // Insertion Sort for small or nearly sorted arrays
    private void insertionSort(T[] values, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            T key = values[i];
            int j = i - 1;
            while (j >= left && order.compare(values[j], key) > 0) {
                values[j + 1] = values[j];
                j--;
            }
            values[j + 1] = key;
        }
    }

    // Advanced adaptive sort that dynamically switches between algorithms
    private void advancedAdaptiveSort(T[] values, int left, int right) {
        final int INSERTION_SORT_THRESHOLD = 16;  
        final int QUICK_SORT_THRESHOLD = 500;    

        // Check for already sorted data or simple patterns
        if (right - left + 1 <= INSERTION_SORT_THRESHOLD) {
            insertionSort(values, left, right);
        }
        // Parallelize large chunks using quicksort or mergesort
        else if (right - left + 1 > QUICK_SORT_THRESHOLD) {
            parallelQuickSort(values, left, right);
        } else {
            mergeSort(values, left, right);
        }
    }

    // Parallel QuickSort for very large arrays
    private void parallelQuickSort(T[] values, int left, int right) {
        ForkJoinPool pool = new ForkJoinPool();
        pool.submit(() -> parallelQuickSortHelper(values, left, right)).join();
    }

    private void parallelQuickSortHelper(T[] values, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(values, left, right);
            parallelQuickSortHelper(values, left, pivotIndex - 1);
            parallelQuickSortHelper(values, pivotIndex + 1, right);
        }
    }

    // QuickSort partitioning step
    private int partition(T[] values, int left, int right) {
        T pivot = values[right];
        int i = left - 1;
        for (int j = left; j < right; j++) {
            if (order.compare(values[j], pivot) <= 0) {
                i++;
                swap(values, i, j);
            }
        }
        swap(values, i + 1, right);
        return i + 1;
    }

    // Helper method to swap elements in the array
    private void swap(T[] values, int i, int j) {
        T temp = values[i];
        values[i] = values[j];
        values[j] = temp;
    }

    // MergeSort for large arrays
    private void mergeSort(T[] values, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(values, left, mid);
            mergeSort(values, mid + 1, right);
            merge(values, left, mid, right);
        }
    }

    // Merge step of MergeSort
    private void merge(T[] values, int left, int mid, int right) {
        T[] temp = (T[]) new Object[right - left + 1];
        int i = left, j = mid + 1, k = 0;

        while (i <= mid && j <= right) {
            if (order.compare(values[i], values[j]) <= 0) {
                temp[k++] = values[i++];
            } else {
                temp[k++] = values[j++];
            }
        }

        while (i <= mid) {
            temp[k++] = values[i++];
        }

        while (j <= right) {
            temp[k++] = values[j++];
        }

        System.arraycopy(temp, 0, values, left, temp.length);
    }
}


