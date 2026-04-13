package util;
import java.util.*;
import java.io.*;
public class Main {

    static Scanner fileReader = null;
    static PrintWriter outputWriter = null;

    public static void main(String[] args) throws InvalidCalendarItemException {
        ArrayList<CalendarItem> calendar = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Enter calender item (TYPE, title, startDT, endDT, owner, location, " +
                    "Comma separated (, ), 'Done' if complete," +
                    "Dates must follow: MM/DD/YYYY @ hh:mm:ss am/pm): ");
            String item = sc.nextLine();
            if (item.equalsIgnoreCase("Done"))
                break;
            CalendarItem formattedItem = parseItem(item);
            if (formattedItem == null) {
                System.out.println("Invalid input. Please re-enter the item.");
            } else {
                calendar.add(formattedItem);
            }



        }
        while (true) {
            System.out.print("Enter Command: ");
            String command = sc.nextLine();
            if (command.equals("print")) {
                printItems(calendar, true);
            }
            else if (command.startsWith("happening on ")) {
                String dtText = command.substring(13).trim();
                DateTime dt = parseDateTime(dtText);
                if (dt == null)
                    System.out.println("Unknown command");
                else {
                    ArrayList<CalendarItem> result = new ArrayList<>();
                    for (CalendarItem item : calendar) {
                        if (item.contains(dt)) {
                            result.add(item);
                        }
                }
                    CalendarItem.mergeSort(result);
                    for(CalendarItem item: result){
                        System.out.println(item);
                    }
                }
            }
            else if(command.startsWith("owned by ")) {
                String nameInput = command.substring(9);
                for(CalendarItem item: calendar) {
                    if (item.getOwner().equals(nameInput))
                        System.out.println(item);
                }
            }
            else if (command.startsWith("type is ")) {
                String typeInput = command.substring(8).toUpperCase();
                for(CalendarItem item: calendar){
                    String[] itemList = item.toString().split(", ");
                    if(itemList[0].equals(typeInput))
                        System.out.println(item);
                }
            }
            else if(command.equals("help")) {
                System.out.println("print\n" +
                        "happening on <DateTime>\n" +
                        "owned by <ownerName>\n" +
                        "type is <TYPE>\n" +
                        "import <filename>\n" +
                        "export <filename>\n" +
                        "help\n" +
                        "exit");
            }
            else if (command.startsWith("export ")) {
                String fileName = command.substring(7);
                File file = new File(fileName);
                try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
                    CalendarItem.mergeSort(calendar);
                    for (CalendarItem item : calendar) {
                        if (calendar.get(calendar.size() - 1).equals(item))
                            writer.print(item.toString().replaceAll(", ", "\t"));
                        else
                            writer.println(item.toString().replaceAll(", ", "\t"));

                    }
                    System.out.println("Export completed");
                }
                catch (IOException e) {
                    System.out.println("Export failed");
                }

            }
            else if (command.startsWith("import ")) {
                String fileName = command.substring(7);
                File file = new File(fileName);
                try(Scanner reader = new Scanner(file)) {
                    while (reader.hasNextLine()) {
                        String line = reader.nextLine().replaceAll("\t", ", ");
                        CalendarItem item;
                        try {
                            item = parseItem(line);
                        } catch (InvalidCalendarItemException e) {
                            continue;
                        }

                        calendar.add(item);
                    }
                    System.out.println("Import completed");
                }
                catch(IOException e){
                    System.out.println("Import failed");
                }

            }
            else if (command.startsWith("find title ")) {
                System.out.println(binarySearchByTitle(calendar, command.substring(11)));
            }
            else if (command.startsWith("count owned by ")) {
                System.out.println(countOwnedBy(calendar, command.substring(15)));
            }
            else if (command.equals("sort titles")) {
                CalendarItem.mergeSort(calendar, true);
                printItems(calendar, false);
            }
            else if (command.equals("quit"))
                break;
            else
                System.out.println("Invalid command");


        }
    }
    private static void printItems(ArrayList<CalendarItem> items, boolean sorting) {
        ArrayList<CalendarItem> sortedCalendar = new ArrayList<>();
        sortedCalendar.addAll(items);
        if (sorting)
            CalendarItem.mergeSort(sortedCalendar);
        for (CalendarItem item : sortedCalendar) {
            System.out.println(item);
        }
    }






    public static CalendarItem parseItem(String item) throws InvalidCalendarItemException{
        String[] itemList = item.split(",");



        for (int i = 0; i < itemList.length; i++) {
            itemList[i] = itemList[i].trim();
        }




        switch (itemList[0].toUpperCase()) {
            case "MEETING":
                if (itemList.length != 8)
                    throw new InvalidCalendarItemException("Wrong number of fields");
                return createMeeting(itemList);
            //calendar.add(new Meeting(itemList[1], startDT, endDT, itemList[4], itemList[5], Integer.parseInt(itemList[6]), itemList[7]));

            case "ONLINE":
                if (itemList.length != 10)
                    throw new InvalidCalendarItemException("Wrong number of fields");
                return createOnlineMeeting(itemList);
            //calendar.add(new OnlineMeeting(itemList[1], startDT, endDT, itemList[4], itemList[5], Integer.parseInt(itemList[6]), itemList[7], itemList[8], itemList[9]));

            case "APPOINTMENT":
                if (itemList.length != 7)
                    throw new InvalidCalendarItemException("Wrong number of fields");
                return createAppointment(itemList);
            //calendar.add(new Appointment(itemList[1], startDT, endDT, itemList[4], itemList[5], itemList[6]));

            case "TASK":
                if (itemList.length != 8)
                    throw new InvalidCalendarItemException("Wrong number of fields");
                return createTask(itemList);
            //calendar.add(new Task(itemList[1], startDT, endDT, itemList[4], itemList[5], Integer.parseInt(itemList[6]), Boolean.parseBoolean(itemList[7])));
            default:
                throw new InvalidCalendarItemException("Invalid item type");
        }

    }

    public static Meeting createMeeting(String[] itemList) throws InvalidCalendarItemException{
        String title = checkIfEmpty(itemList[1]);
        DateTime startDT = checkIfDateTime(itemList[2]);
        DateTime endDT = checkIfDateTime(itemList[3]);
        String owner = checkIfEmpty(itemList[4]);
        String location = checkIfEmpty(itemList[5]);
        int invitees = Integer.parseInt(itemList[6]);
        String agenda = checkIfEmpty(itemList[7]);
        if (invitees < 0)
            throw new InvalidCalendarItemException("Invitees must be positive");
        if (startDT.compareTo(endDT) >= 0)
            throw new InvalidCalendarItemException("Start date must end before end date");
        return new Meeting(title, startDT, endDT, owner, location, invitees, agenda);
    }
    public static OnlineMeeting createOnlineMeeting(String[] itemList) throws InvalidCalendarItemException {
        String title = checkIfEmpty(itemList[1]);
        DateTime startDT = checkIfDateTime(itemList[2]);
        DateTime endDT = checkIfDateTime(itemList[3]);
        String owner = checkIfEmpty(itemList[4]);
        String location = checkIfEmpty(itemList[5]);
        int invitees = Integer.parseInt(itemList[6]);
        String agenda = checkIfEmpty(itemList[7]);
        String platform = checkIfEmpty(itemList[8]);
        String meetingLink = checkIfEmpty(itemList[9]);
        if (invitees < 0)
            throw new InvalidCalendarItemException("Invitees must be positive");
        if (startDT.compareTo(endDT) >= 0)
            throw new InvalidCalendarItemException("Start date must end before end date");
        return new OnlineMeeting(title, startDT, endDT, owner, location, invitees, agenda, platform, meetingLink);
    }
    public static Appointment createAppointment(String[] itemList) throws InvalidCalendarItemException{
        String title = checkIfEmpty(itemList[1]);
        DateTime startDT = checkIfDateTime(itemList[2]);
        DateTime endDT = checkIfDateTime(itemList[3]);
        String owner = checkIfEmpty(itemList[4]);
        String location = checkIfEmpty(itemList[5]);
        String withWhom = checkIfEmpty(itemList[6]);
        if (startDT.compareTo(endDT) >= 0)
            throw new InvalidCalendarItemException("Start date must end before end date");
        return new Appointment(title, startDT, endDT, owner, location, withWhom);
    }
    public static Task createTask(String[] itemList) throws InvalidCalendarItemException{
        String title = checkIfEmpty(itemList[1]);
        DateTime startDT = checkIfDateTime(itemList[2]);
        DateTime endDT = checkIfDateTime(itemList[3]);
        String owner = checkIfEmpty(itemList[4]);
        String location = checkIfEmpty(itemList[5]);
        int priority = Integer.parseInt(itemList[6]);
        boolean completed;
        if (itemList[7].equalsIgnoreCase("true"))
            completed = true;
        else if (itemList[7].equalsIgnoreCase("false"))
            completed = false;
        else
            throw new InvalidCalendarItemException("invalid item type");
        if (priority < 1 && priority > 5)
            throw new InvalidCalendarItemException("Priority outside 1-5");
        if (startDT.compareTo(endDT) >= 0)
            throw new InvalidCalendarItemException("Start date must end before end date");
        return new Task(title, startDT, endDT, owner, location, priority, completed);
    }
    public static String checkIfEmpty(String value) {
        if (value == null || value.trim().isEmpty())
            throw new IllegalArgumentException();
        return value.trim();
    }
    public static DateTime checkIfDateTime(String value) throws InvalidCalendarItemException {
        DateTime dt = parseDateTime(value);
        if (dt == null)
            throw new IllegalArgumentException();
        return dt;
    }
    public static DateTime parseDateTime(String text) throws InvalidCalendarItemException{
        String[] atParts = text.trim().split("\\s*@\\s*");
        if (atParts.length != 2) {
            throw new InvalidCalendarItemException("Invalid DateTime");
        }

        String[] dateParts = atParts[0].trim().split("/");
        String[] timeParts = atParts[1].trim().split("\\s+");
        if (dateParts.length != 3 || timeParts.length != 2) {
            throw new InvalidCalendarItemException("Invalid DateTime");
        }

        String[] hms = timeParts[0].split(":");
        if (hms.length != 3) {
            throw new InvalidCalendarItemException("Invalid DateTime");
        }

        try {
            int month = Integer.parseInt(dateParts[0]);
            int day = Integer.parseInt(dateParts[1]);
            int year = Integer.parseInt(dateParts[2]);
            int hour = Integer.parseInt(hms[0]);
            int minute = Integer.parseInt(hms[1]);
            int second = Integer.parseInt(hms[2]);

            String meridiem = timeParts[1].toLowerCase();
            if (!meridiem.equals("am") && !meridiem.equals("pm")) {
                throw new InvalidCalendarItemException("Invalid DateTime");
            }
            if (hour < 1 || hour > 12) {
                throw new InvalidCalendarItemException("Invalid DateTime");
            }

            DateTime dt = new DateTime(year, month, day, hour, minute, second, meridiem.equals("am"));
            return DateTime.isLegal(dt) ? dt : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
    public static int binarySearchByTitle(ArrayList<CalendarItem> items, String target) {
        return binarySearchByTitle(items, target, 0, items.size());
    }
    private static int binarySearchByTitle(ArrayList<CalendarItem> items, String target, int left, int right) {
        while(left < right) {
            int mid = (left+right)/2;
            if (items.get(mid).getTitle().equalsIgnoreCase(target))
                return mid;
            int compare = target.compareTo(items.get(mid).getTitle());
            if (compare >= 1)
                binarySearchByTitle(items, target, mid, right);
            else if (compare <= 1)
                binarySearchByTitle(items, target, left, mid);

        }
        return -1;
    }
    public static int countOwnedBy(ArrayList<CalendarItem> items, String owner) {
        return countOwnedBy(items, owner, 0);
    }
    private static int countOwnedBy(ArrayList<CalendarItem> items, String owner, int index) {
        int count = 0;
        if (items.get(index).getOwner().equals(owner))
            count++;
        if (index == items.size()-1)
            return count;
        count += countOwnedBy(items, owner, index+1);
        return count;
    }
}

