package bookmyshow.model;

import java.util.ArrayList;
import java.util.List;

public class Screen {
    private final String screenId;
    private final String name;
    private final int rows;
    private final int cols;

    public Screen(String screenId, String name, int rows, int cols) {
        this.screenId = screenId;
        this.name = name;
        this.rows = rows;
        this.cols = cols;
    }

    public String getScreenId() {
        return screenId;
    }

    public String getName() {
        return name;
    }

    public List<String> seatIds() {
        List<String> seats = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            char rowChar = (char) ('A' + r);
            for (int c = 1; c <= cols; c++) {
                seats.add(rowChar + String.valueOf(c));
            }
        }
        return seats;
    }
}
