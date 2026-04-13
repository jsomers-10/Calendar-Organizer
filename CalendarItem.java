package util;

import java.util.ArrayList;

public abstract class CalendarItem implements Comparable<CalendarItem>{
    String title;
    DateTime start;
    DateTime end;
    String owner;
    String location;
    public abstract String getType();
    public abstract String getDetails();

    public boolean contains(DateTime dt) {
        return start.compareTo(dt) <= 0 && end.compareTo(dt) >= 0;
    }

    @Override
    public int compareTo(CalendarItem other){
        if (this.start.compareTo(other.start) != 0)
            return this.start.compareTo(other.start);
        else if (this.start.compareTo(other.start) == 0 && this.end.compareTo(other.end) != 0)
            return -this.end.compareTo(other.end);
        else if (this.start.compareTo(other.start) == 0 && this.end.compareTo(other.end) == 0)
            return this.compareToTitle(other);

        /*Calendar items must be sorted by:
            Earliest start time first
            If start times are equal → Longer duration first
            If both equal → Alphabetical by title (case-insensitive)
         */
        return 0;
    }

    private int compareToTitle(CalendarItem other) {
        if (this.title.compareToIgnoreCase(other.title) != 0)
            return this.title.compareToIgnoreCase(other.title);
        else if (this.title.compareToIgnoreCase(other.title) == 0)
            return -this.start.compareTo(other.start);
        return this.owner.compareToIgnoreCase(other.owner);
    }
    public CalendarItem(String title, DateTime start, DateTime end, String owner, String location){
        this.title = title;
        this.start = start;
        this.end = end;
        this.owner = owner;
        this.location = location;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public DateTime getStart() {
        return start;
    }

    public DateTime getEnd() {
        return end;
    }


    public String getOwner() {
        return owner;
    }
    public static void mergeSort(ArrayList<CalendarItem> items) {
        mergeSort(items, 0, items.size()-1, false);
    }
    public static void mergeSort(ArrayList<CalendarItem> items, boolean sortByTitle) {
        if (sortByTitle)
            mergeSort(items, 0, items.size()-1, true);
    }
    private static void mergeSort(ArrayList<CalendarItem> items, int left, int right, boolean sortByTitle) {
        if (left >= right)
            return;
        int mid = (left + right)/2;
        ArrayList<CalendarItem> firstSplit = new ArrayList<>();
        ArrayList<CalendarItem> secondSplit = new ArrayList<>();
        for(int i = 0; i <= mid; i++) {
            firstSplit.add(items.get(i));
        }
        for(int i = mid+1; i <= right; i++) {
            secondSplit.add(items.get(i));
        }
        mergeSort(firstSplit, 0, firstSplit.size()-1, sortByTitle);
        mergeSort(secondSplit, 0, secondSplit.size()-1, sortByTitle);
        merge(items, firstSplit, secondSplit, sortByTitle);


    }
    private static void merge(ArrayList<CalendarItem> items, ArrayList<CalendarItem> firstSplit, ArrayList<CalendarItem> secondSplit, boolean sortByTitle) {
        int i = 0, j = 0, k = 0;
        if(!sortByTitle) {
            while (i < firstSplit.size() && j < secondSplit.size()) {
                if (firstSplit.get(i).compareTo(secondSplit.get(j)) >= 1) {
                    items.set(k, secondSplit.get(j));
                    j++;
                }
                else if (firstSplit.get(i).compareTo(secondSplit.get(j)) <= 1){
                    items.set(k, firstSplit.get(i));
                    i++;
                }
                k++;
            }
        }
        else {
            while (i < firstSplit.size() && j < secondSplit.size()) {
                if (firstSplit.get(i).compareToTitle(secondSplit.get(j)) >= 1) {
                    items.set(k, secondSplit.get(j));
                    j++;
                }
                else if (firstSplit.get(i).compareToTitle(secondSplit.get(j)) <= 1){
                    items.set(k, firstSplit.get(i));
                    i++;
                }
                k++;
            }
        }

        while (i < firstSplit.size()) {
            items.set(k, firstSplit.get(i));
            i++;
            k++;
        }
        while (j < secondSplit.size()) {
            items.set(k, secondSplit.get(j));
            j++;
            k++;
        }
    }
}
