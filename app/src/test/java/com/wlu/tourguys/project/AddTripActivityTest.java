import static org.junit.Assert.*;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AddTripActivityTest {

    @Test
    public void calculateNumDays_validDates_returnsCorrectNumberOfDays() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        String startDate = "2024-12-01";
        String endDate = "2024-12-05";

        long start = sdf.parse(startDate).getTime();
        long end = sdf.parse(endDate).getTime();

        long diff = end - start;
        int days = (int) (diff / (1000 * 60 * 60 * 24)) + 1; // Including the start day

        assertEquals(5, days);
    }

    @Test
    public void calculateNumDays_endDateBeforeStartDate_returnsInvalid() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        String startDate = "2024-12-05";
        String endDate = "2024-12-01";

        long start = sdf.parse(startDate).getTime();
        long end = sdf.parse(endDate).getTime();

        assertTrue(end < start);
    }
}
