package com.shark.search4SVN.service.wrapper;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.wc.SVNClientManager;


/**
 * 注意，一组 (用户名, 密码) 对应 一个 SVNAdapter
*/
public class SVNAdapter {
	private static Logger logger = Logger.getLogger(SVNAdapter.class);
	
	static {
		DAVRepositoryFactory.setup();
	}

	SVNClientManager  svnClientManager = null;
	
	public SVNAdapter(){}

	public SVNAdapter(String userName, String passwd) {
		init(userName, passwd);
	}

	public void init(String userName,String passwd){
		DefaultSVNOptions options = new DefaultSVNOptions();
		svnClientManager = SVNClientManager.newInstance(options,userName,passwd);
	}

	
	/**获取文档内容
	 * @param url
	 * @return
	 */
	public Object[] checkoutFile(String url){//"", -1, null
		SVNRepository repository = createRepository(url);
		ByteArrayOutputStream outputStream = null;
		try {
			SVNDirEntry entry = repository.getDir("", -1, false, null);
			int size = (int)entry.getSize();
			outputStream = new ByteArrayOutputStream(size);
			SVNProperties properties = new SVNProperties();
			repository.getFile("", -1, properties, outputStream);

			Object[] objects = new Object[2];
			objects[0] = entry;
			objects[1] = outputStream.toByteArray();

			return objects;
		} catch (SVNException e) {
			e.printStackTrace();
		}finally {
			if(outputStream != null){
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	
	/**进入子目录
	 * @param folder
	 * @return
	 */
	public String toChildFolder(String url, String folder){
		if(url!=null){
			StringBuffer sb = new StringBuffer(url);
			boolean a = url.endsWith("/");
			boolean b = folder.startsWith("/");
			if(a^b){
				sb.append(folder);
			}else if(a&b){
				sb.deleteCharAt(sb.length()-1);
				sb.append(folder);
			}else{
				sb.append('/').append(folder);
			}
			if(checkPath(sb.toString())==1){
				return sb.toString();
			}
		}
		return null;
	}


	/**列出指定SVN 地址目录下的子目录
	 * @param url
	 * @return
	 * @throws SVNException
	 */
	public List<SVNDirEntry> listFolder(String url){
		if(checkPath(url)==1){
			
			SVNRepository repository = createRepository(url);
			try {
				Collection<SVNDirEntry> list = repository.getDir("", -1, null, (List<SVNDirEntry>)null);
				List<SVNDirEntry> dirs = new ArrayList<SVNDirEntry>(list.size());
				dirs.addAll(list);
				return dirs;
			} catch (SVNException e) {
				logger.error("listFolder error",e);
			}

		}
		return null;
	}
	
	private SVNRepository createRepository(String url){
		
		try {
			return svnClientManager.createRepository(SVNURL.parseURIEncoded(url), true);
		} catch (SVNException e) {
			logger.error("createRepository error",e);
		}
		return null;
	}
	
	/**检查路径是否存在
	 * @param url
	 * @return 1：存在    0：不存在   -1：出错
	 */
	public int checkPath(String url){
		SVNRepository repository = createRepository(url);
		SVNNodeKind nodeKind;
		try {
			nodeKind = repository.checkPath("", -1);
			boolean result = nodeKind == SVNNodeKind.NONE ? false : true;
			if(result) return 1;
		} catch (SVNException e) {
			logger.error("checkPath error",e);
			return -1;
		}
		return 0;
	}

	public void clear(){

	}
}

