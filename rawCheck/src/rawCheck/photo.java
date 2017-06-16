package rawCheck;

public class photo {
	public String lens="";
	public String iso="";
	public String focalLength="";
	public String time="";
	public String expType="";
	public String metering="";
	public String name = "";
	
	public photo(String n, String l, String i, String fl, String t, String type, String met){
		this.name = n;
		this.lens = l;
		this.iso = i;
		this.focalLength = fl;
		this.time=t;
		this.expType = type;
		this.metering = met;
	}
}
