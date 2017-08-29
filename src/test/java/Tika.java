import java.io.File;
import java.io.IOException;

/**
 * Created by liuqinghua on 2017-08-29.
 */
public class Tika {
    public static void main(String[] args) throws IOException {
        org.apache.tika.Tika tika = new org.apache.tika.Tika();

        System.out.println(tika.detect(new File("D:\\GitHub\\search4SVN\\src\\test\\resources\\123.xlsx")));
        System.out.println(tika.detect(new File("D:\\GitHub\\search4SVN\\src\\test\\resources\\666.docx")));
        System.out.println(tika.detect(new File("D:\\GitHub\\search4SVN\\src\\test\\resources\\666.txt")));
        System.out.println(tika.detect(new File("D:\\GitHub\\search4SVN\\src\\test\\resources\\PKITS.pdf")));
    }
}
