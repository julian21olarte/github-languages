package githublanguages;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Util {

    public static String getDateFormat() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd");
        LocalDate localDate = LocalDate.now().minusDays(1);
        return dtf.format(localDate);
    }
}
