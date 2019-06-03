package githublanguages;

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class GithubClient {
    String url = "https://api.github.com/search/repositories";
    private List<String> languages;
    private String token;

    public GithubClient() {
        // getting the properties
        InputStream s = GithubClient.class.getResourceAsStream("/application.properties");
        Properties props = new Properties();
        try {
            props.load(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.token = props.get("github.token").toString();

        // languages
        this.languages = Arrays.asList(
                "Java",
                "Javascript",
                "Go",
                "PHP",
                "C",
                "Cpp", // C++
                "C#",
                "Python",
                "Html",
                "Typescript",
                "Clojure",
                "Rust",
                "R",
                "Ruby",
                "Swift",
                "Kotlin"
        );
    }

    public List<String> getLanguages() {
        return this.languages;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private Language getLanguageFromQuery(String lang, URIBuilder uri) throws IOException, JSONException, URISyntaxException {
        InputStream is = null;
        HttpURLConnection con = (HttpURLConnection) new URL(URLDecoder.decode(uri.build().toString(), "UTF-8")).openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        //add request header
        con.setRequestProperty("User-Agent", "request");
        con.setRequestProperty("Authorization", "token " + this.token);

        is = con.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        String jsonText = this.readAll(rd);
        JSONObject json = new JSONObject(jsonText);
        final Language language = new Language(lang, json.getInt("total_count"));
        is.close();
        return language;
    }

    public List<Language> getLanguagesRepos() throws URISyntaxException, ExecutionException, InterruptedException {
        List<Language> languagesData = new ArrayList<>();
        String date = getDateFormat();
        URIBuilder githubUrl = new URIBuilder(url);

        // iterating languages and save the completables
        List<CompletableFuture<Language>> futures = this.languages
        .stream()
        .map(lang -> {
            return CompletableFuture
                .supplyAsync(() -> githubUrl.setParameter("q", this.getGithubQuery(lang, date)))
                .thenApply(uriBuilder -> {
                    try {
                        return this.getLanguageFromQuery(lang, uriBuilder);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
        }).collect(Collectors.toList());

        // wating for the completables and stored it
        for(CompletableFuture<Language> future: futures) {
            languagesData.add(future.get());
        }
        return languagesData;
    }

    private String getDateFormat() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd");
        LocalDate localDate = LocalDate.now().minusDays(1);
        return dtf.format(localDate);
    }

    private String getGithubQuery(String lang, String date) {
        return String.format("language:%s+created:%s+size:>0", lang, date);
    }
}