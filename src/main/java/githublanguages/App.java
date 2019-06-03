package githublanguages;

import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        System.out.println( "Getting Github data" );
        GithubClient client = new GithubClient();
        try {
            System.out.println(client.getLanguagesRepos().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
