package rawCheck;

import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ftpUpload{
	
	private String ftp;
	private String user;
	private String pass;
	private ArrayList<File> files = new ArrayList<File>();

	
	public ftpUpload(String ftp, String user, String pass) {
		this.ftp = ftp;
		this.user = user;
		this.pass = pass;
	}
	
	public void upload(String path){
		FTPClient client = new FTPClient();
		getJSON(path, files);
		try {
			 	client.connect(ftp);
		        client.login(user, pass);
		        System.out.print(client.getReplyString());
		        client.changeWorkingDirectory("public_html");
		    for (File f:files) {
			 	InputStream is = new FileInputStream(f.toString());
		        client.storeFile(f.getName(), is);
		        System.out.print(client.getReplyString());
		    }
		    client.logout();
		} 
		catch (IOException e) {
		    e.printStackTrace();
		} 
		finally {
		    try {
		        client.disconnect();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		 
	}
	
	private static void getJSON(String directoryName, ArrayList<File> files){
		File directoy = new File(directoryName);
		File[] fList = directoy.listFiles();
		
		for (File f:fList){
			if (f.isFile()){
				if (f.getName().substring(f.getName().length()-4).toString().compareTo("json")==0) 
					{
					files.add(f);
					}
			}
			else if (f.isDirectory()) {
				getJSON(f.getAbsolutePath(),files);
			}
		}
	}

}
