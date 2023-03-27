package Entity;

public class TitleAuthorLink {
    String title;
    String author;
    String link;

    public TitleAuthorLink(String title, String author, String link){
        this.title = title;
        this.author = author;
        this.link = link;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
}
