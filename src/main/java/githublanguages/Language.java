package githublanguages;

public class Language {
    String name;
    int repositories;

    public Language(String name, int repositories){
        this.name = name;
        this.repositories = repositories;
    }

    @Override
    public String toString() {
        return "Language{" +
                "name='" + name == "Cpp" ? "C++" : name + '\'' +
                ", repositories=" + repositories +
                '}';
    }
}
