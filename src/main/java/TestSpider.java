import Entity.TitleAuthorLink;
import netscape.security.UserTarget;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestSpider {

    public static String getDoiNumber(int year, int month, int number){
        return Integer.toString(year % 100) + String.format("%02d", month) +
                "." + String.format("%05d", number);
    }
    public static void main(String[] args) throws IOException {
        String firstUrl = "https://arxiv.org/abs/";
        String url;
        int number = 5045;
        try {
            do {
                number++;
                url = firstUrl + getDoiNumber(10, 2, number);
                Document document = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
                Elements titleEl = document.getElementsByClass("title mathjax");
                Elements authorEl = document.getElementsByClass("authors");
                System.out.println(document.getElementsByClass("title mathjax").text());
            } while (Jsoup.parse(new URL(firstUrl + getDoiNumber(10, 2, number+1)).openStream(), "UTF-8", url) != null);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            // 建立连接
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/testjdbc", "root", "333666");
            Statement statement = connection.createStatement();

            String sql = "insert into t_usr (usrName, pwd, regTime) value ('cappucciono', 464645, now())";
            statement.execute(sql);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

//        String url = "https://arxiv.org/abs/1002.99999";
//        Document document = Jsoup.parse(new URL(url).openStream(),"GBK", url);
//
//        HSSFWorkbook wb = new HSSFWorkbook();
//        Elements titleEl = document.getElementsByClass("title mathjax");
//        Elements authorEl = document.getElementsByClass("authors");
//        Elements linkEl = document.getElementsByClass("list-title is-inline-block");
//
//        HSSFSheet titleSheet = wb.createSheet("标题");
//        HSSFSheet authorSheet = wb.createSheet("作者");
//        HSSFSheet linkSheet = wb.createSheet("链接");
//
//
//            String title = titleEl.text();
//            String author = authorEl.text();
//            if (title == null){
//                System.out.println("cuo");
//            }
//            HSSFRow row = titleSheet.createRow(0);
//            row.createCell(0).setCellValue(title);
//            row.createCell(1).setCellValue(author);
//
//        try {
//            FileOutputStream fout = new FileOutputStream("D:/上海招聘公司一览表.xls");
//            wb.write(fout);
//            fout.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
