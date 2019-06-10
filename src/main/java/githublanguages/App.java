package githublanguages;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Hello world!
 *
 */
public class App 
{
    private static String LANGUAGES_CSV = "languages.csv";

    public static void main( String[] args ) {

        System.out.println( "Getting Github data" );
        String path = "src/main/resources/"+LANGUAGES_CSV;
        System.out.println(path);
        GithubClient client = new GithubClient();
        CSVClient csvClient = new CSVClient();

        try {
            List<Language> languages = client.getLanguagesRepos();
            System.out.println(languages.toString());

            List<String> newLine = csvClient.getTodayCSVLineFormat(languages);
            System.out.println(newLine.toString());
            csvClient.appendToSCVFile(path, newLine.toArray(new String[newLine.size()]));

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
