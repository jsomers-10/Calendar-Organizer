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
        mergeSort(items, 0, items.size()-1);
    }
    private static void mergeSort(ArrayList<CalendarItem> items, int left, int right) {
        int mid = (left + right)/2;
        ArrayList<CalendarItem> firstSplit = new ArrayList<>(items.subList(left, mid));
        ArrayList<CalendarItem> secondSplit = new ArrayList<>(items.subList(mid+1, right));
        if (firstSplit.size() != 1 && secondSplit.size() != 1) {
            return;
        }
        mergeSort(firstSplit, 0, items.size()-1);
        mergeSort(secondSplit, items.size(), items.size());
        merge(items, 0, mid, items.size());


    }
    private static void merge(ArrayList<CalendarItem> items, int left, int mid, int right) {
        ArrayList<CalendarItem> mergedItems = new ArrayList<>();
        ArrayList<CalendarItem> firstSplit = new ArrayList<>(items.subList(left, mid));
        ArrayList<CalendarItem> secondSplit = new ArrayList<>(items.subList(mid+1, right));
        int i = 0, j = 0;
        while (i < firstSplit.size() && j < secondSplit.size()) {
            if (firstSplit.get(i).compareTo(secondSplit.get(j)) >= 1) {
                mergedItems.add(secondSplit.get(j));
                j++;
            }
            else if (firstSplit.get(i).compareTo(secondSplit.get(j)) <= 1) {
                mergedItems.add(firstSplit.get(i));
                i++;
            }
        }
        if (i != firstSplit.size()) {
            for (int k = i; k < firstSplit.size(); k++) {
                mergedItems.add(firstSplit.get(i));
            }

        }
        if (j != secondSplit.size()) {
            for (int k = j; k < secondSplit.size(); k++) {
                mergedItems.add(secondSplit.get(j));
            }

        }
        items.clear();
        items.addAll(mergedItems);
    }
}
