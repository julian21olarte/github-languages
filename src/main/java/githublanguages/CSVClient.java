package githublanguages;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVClient {

    public CSVClient() {

    }

    public List<String> getTodayCSVLineFormat(List<Language> languages) {
        List<String> todayFormat = new ArrayList<>(); // date:YYYY-MM-dd
        todayFormat.add("date:" + Util.getDateFormat());
        for(Language lang: languages) {
            todayFormat.add(lang.name + ":" + lang.repositories);
        }
        // date:YYYY-MM-dd,Java:10,Javascript:120,Cpp:195,.....
        // split by ',' and each pos with split by ':'
        // the first pos is date and the rest are the languages
        return todayFormat;
    }

    public void appendToSCVFile(String path, String[] newLine) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(path, true));
        writer.writeNext(newLine);
        writer.close();
    }
}
