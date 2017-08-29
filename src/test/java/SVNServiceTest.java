import java.util.List;

import com.shark.search4SVN.service.wrapper.SVNAdapter;
import org.apache.tika.Tika;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNNodeKind;


/**
 * Hello world!
 *
 */
public class SVNServiceTest {
    public static void main( String[] args ) throws Exception{
         String username = "liuqinghua";
         String password = "netcasvnlqh";
         String url = "http://192.168.0.11:8081/svn/netca/产品/业务运营管理平台/src/trunk/PKIDemo/readme.txt";
         //String url = "http://192.168.0.11:8081/svn/netca/产品/业务运营管理平台/3.设计";
 		 SVNAdapter svn = new SVNAdapter(username, password);
 		 Object[] bytes = svn.checkoutFile(url);
 		 System.out.println(bytes.length);
 		/* String xml = svn.checkoutFileToString(url);
		  
         System.out.println(xml);*/
 		 
 		 list(svn, url);
 		
    }
    
    
    
    public static void list(SVNAdapter svn, String url){
    	 if(url == null || "".endsWith(url.trim())){
    		 return;
    	 }
    	 List<SVNDirEntry> entrys =  svn.listFolder(url);
    	 if(entrys != null){
	 		 for(SVNDirEntry entry:entrys){
	 			   if(SVNNodeKind.DIR.equals(entry.getKind())){
	 				   
	 				   String newUrl = url + "/" + entry.getRelativePath();
	 				   System.out.println("newUrl " + newUrl);
	 				   list(svn, newUrl);
	 			   }else{
	 				  
	 				  String newUrl = url + "/" + entry.getRelativePath();
					   Object[] bytes = svn.checkoutFile(newUrl);
	 				  
	 				  Tika tika = new Tika();
	 				  
	 				  //String mimeType = tika.detect(bytes);
	 				  
	 				  //System.out.println(entry.getName() + " 类型为  " + mimeType + " 大小为 " + entry.getSize() + " Byte(字节)");
	 				   
	 			   }
	 		 }
    	 }
    }
}
