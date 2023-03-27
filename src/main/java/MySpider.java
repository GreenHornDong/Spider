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
import java.sql.*;
import java.util.Calendar;

import Entity.TitleAuthorLink;

import static java.lang.Thread.sleep;


public class MySpider {

//集成方法，不实用，等待开发
    //    public void fillExcel(String array[][], Document document){
//        HSSFWorkbook workBook = new HSSFWorkbook();
//        HSSFRow row;
//        for (int i = 0; i < array.length; i++) {
//            Elements element = document.getElementsByClass(array[i][0]);//获取classname
//            HSSFSheet sheet = workBook.createSheet(array[i][1]);//获取sheetname
//            for (int j = 0; j < element.size(); j++) {
//                String string = element.eq(j).text();
//                System.out.println(string);
//                row = sheet.createRow(j);
//                row.createCell(i).setCellValue(string);
//            }
//        }
//
//        try {
//            FileOutputStream fout = new FileOutputStream("D:/上海招聘公司一览表.xls");
//            workBook.write(fout);
//            fout.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    //g格式：https://arxiv.org/abs/1801.19000

    public void startSpider(int year, int month) throws IOException  {
        String firstUrl = "https://arxiv.org/abs/";
        int number = 0;
        try {
            while (true){
                number++;
                System.out.println(number);
                String url = firstUrl + getDoiNumber(year, month, number);
                Document document = Jsoup.parse(new URL(url).openStream(),"UTF-8", url);
                Elements titleEl = document.getElementsByClass("title mathjax");
                Elements authorEl = document.getElementsByClass("authors");
                Elements commentsEl = document.getElementsByClass("tablecell comments mathjax");
                Elements subjectsEl = document.getElementsByClass("primary-subject");
                Elements dateEl = document.getElementsByClass("dateline");
                Elements abstractEl = document.getElementsByClass("abstract mathjax");
                //记录当前时间和进度，以便下一次续爬，写入单独的数据库
//                TitleAuthorLink titleAuthorLink = new TitleAuthorLink(titleEl.text(), authorEl.text(), url);//后续的mybatis开发
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    // 建立连接
                    Connection connection = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/recSystem", "root", "root");
                    Statement statement = connection.createStatement();

                    String sql = "insert into paper (authors, title,  url, comments, subjects, update_date, paper_abstract) value (?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, titleEl.text());
                    preparedStatement.setString(2, authorEl.text());
                    preparedStatement.setString(3, url);
                    preparedStatement.setString(4, commentsEl.text());
                    preparedStatement.setString(5, subjectsEl.text());
                    preparedStatement.setString(6, dateEl.text());
                    preparedStatement.setString(7, (abstractEl.text().substring(10)));
                    preparedStatement.execute();
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void continueSpider() throws IOException  {
        //读取数据库的续爬节点
        int year = 0;
        int month = 0;
        int number = 0;

        String firstUrl = "https://arxiv.org/abs/";
        while (true){
            number++;
            String url = firstUrl + getDoiNumber(year, month, number);
            Document document = Jsoup.parse(new URL(url).openStream(),"UTF-8", url);
            Elements titleEl = document.getElementsByClass("title mathjax");
            Elements authorEl = document.getElementsByClass("authors");

            if (titleEl == null) break;
            TitleAuthorLink titleAuthorLink = new TitleAuthorLink(titleEl.text(), authorEl.text(), url);
        }
        //标记当前时间和进度
    }

    public String getDoiNumber(int year, int month, int number){
        return Integer.toString(year % 100) + String.format("%02d", month) +
                "." + String.format("%05d", number);
    }

    public static void main(String[] args) throws IOException, IOException, InterruptedException {
        int year = 2010, month = 2;
        Calendar calendar = Calendar.getInstance();

//        for (; year < calendar.get(Calendar.YEAR); year++){
//            for (; month < 13; month++) {
//                new MySpider().startSpider(year, month); //开始爬虫，每次只爬一个月的，不会超时
//                //sleep(600000);//休息10min
//            }
//        }
        new MySpider().startSpider(2010, 2);
    }
}
