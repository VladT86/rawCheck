package rawCheck;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

public class rCheck {
	
	private static ArrayList<photo> photos = new ArrayList<photo>();
	private static String exportPath;

	public static void main(String[] args) {

		//String a = "J:/poze/";
		String a = args[0];
		exportPath = args[1];
		ArrayList<File> fileList = new ArrayList<File>();
		getAllFiles(a,fileList);
		

		try {
			for (File f:fileList){
				{
					rawCheck rc = new rawCheck(f.getAbsoluteFile());
					photo p = new photo(f.toString(),rc.getLensType(), rc.getISO(), rc.getFocalLength(),rc.getExpTime(),rc.getExpType(),rc.getMetering());
					photos.add(p);
				}	
			}
		}
		catch (Exception e){
			System.out.println(e);
		}
		
		printLensType(photos);
		printFocalLength(photos);
		printISO(photos);
		printExpProgram(photos);
		printMetering(photos);
		printExpTime(photos);
	}
	
	private static void getAllFiles(String directoryName, ArrayList<File> files){
		File directoy = new File(directoryName);
		File[] fList = directoy.listFiles();
		
		for (File f:fList){
			if (f.isFile()){
				if (f.getName().substring(f.getName().length()-3).toString().compareTo("NEF")==0) 
					{
					files.add(f);
					}
			}
			else if (f.isDirectory()) {
				getAllFiles(f.getAbsolutePath(),files);
			}
		}
	}
	
	private static void printLensType(ArrayList<photo> photos){
		try{
			PrintWriter writer = new PrintWriter(exportPath + "/lensUsed.json","UTF-8");
			HashMap<String, Integer> lenstype = new HashMap<String,Integer>();
			
			writer.println("{");
			writer.println("  \"cols\": [");
			writer.println("        {\"id\":\"\",\"label\":\"Lens type\",\"pattern\":\"\",\"type\":\"string\"},");
			writer.println("        {\"id\":\"\",\"label\":\"# of photos taken\",\"pattern\":\"\",\"type\":\"number\"}");
			writer.println("      ],");
			writer.println("  \"rows\": [");
			
			
			for (photo p:photos){
				if (lenstype.containsKey(p.lens)){
					lenstype.put(p.lens, lenstype.get(p.lens)+1);
				}
				else {
					lenstype.put(p.lens,1);
				}
			}
			int count = 0;
			int noLenses = lenstype.keySet().size();
			int q = 1;
			for (String key:lenstype.keySet()){
				String lens = (key.replace("[Nikon Makernote] ", ""));
				count = count + lenstype.get(key);
				writer.print("        {\"c\":[{\"v\":\"" + lens + "\",\"f\":null},{\"v\":" + lenstype.get(key) + ",\"f\":null}]}");
				if (q < noLenses) {
					writer.print(",");
				}
				writer.println("");
				q++;
			}
			writer.println("      ]");
			writer.println("}");
			writer.close();
		}
		catch (Exception e){
			
		}
	}
	
	private static void printFocalLength(ArrayList<photo> photos){
		try {
			PrintWriter writer = new PrintWriter(exportPath + "/FocalLength.json","UTF-8");

			HashMap<Integer, Integer> focalLength = new HashMap<Integer,Integer>();
			for (photo p:photos){
				String temp = p.focalLength.replace("[Exif SubIFD] Focal Length - ", "");
				temp = temp.replace(" mm", "");
				if (focalLength.containsKey(Integer.parseInt(temp))){
					focalLength.put(Integer.parseInt(temp), focalLength.get(Integer.parseInt(temp))+1);
				}
				else {
					focalLength.put(Integer.parseInt(temp),1);
				}
			}
			TreeMap<Integer, Integer> sorted = new TreeMap<>(focalLength);
			Set<Entry<Integer, Integer>> mappings = sorted.entrySet();
			
			
			int count = focalLength.keySet().size();
			int q = 0;
			writer.println("{");
			writer.println("  \"cols\": [");
			writer.println("        {\"id\":\"\",\"label\":\"Focal Length\",\"pattern\":\"\",\"type\":\"string\"},");
			writer.println("        {\"id\":\"\",\"label\":\"Value\",\"pattern\":\"\",\"type\":\"number\"}");
			writer.println("      ],");
			writer.println("  \"rows\": [");
			
			//for (String key:focalLength.keySet()){
			for (Entry<Integer, Integer> key : mappings){
				Integer fl = key.getKey();
				writer.print("        {\"c\":[{\"v\":\"" + fl + " mm \",\"f\":null},{\"v\":" + key.getValue() + ",\"f\":null}]}");
				if (q < count) {
					writer.print(",");
				}
				writer.println("");
				q++;
			}
			writer.println("      ]");
			writer.println("}");
			writer.close();
		}
		catch (Exception e){
			
		}
	}
	
	private static void printISO(ArrayList<photo> photos){
		try{
			PrintWriter writer = new PrintWriter(exportPath + "/iso.json","UTF-8");
			HashMap<Integer, Integer> iso = new HashMap<Integer,Integer>();
			
			writer.println("{");
			writer.println("  \"cols\": [");
			writer.println("        {\"id\":\"\",\"label\":\"ISO\",\"pattern\":\"\",\"type\":\"string\"},");
			writer.println("        {\"id\":\"\",\"label\":\"Value\",\"pattern\":\"\",\"type\":\"number\"}");
			writer.println("      ],");
			writer.println("  \"rows\": [");
			
			
			for (photo p:photos){
				String isoValue = (p.iso.replace("[Exif SubIFD] ISO Speed Ratings - ", ""));
				int temp = Integer.parseInt(isoValue);
				if (iso.containsKey(temp)){
					iso.put(temp, iso.get(temp)+1);
				}
				else {
					iso.put(temp,1);
				}
			}
			
			TreeMap<Integer, Integer> sorted = new TreeMap<>(iso);
			Set<Entry<Integer, Integer>> mappings = sorted.entrySet();			
			
			
			int values = iso.keySet().size();
			int q = 1;
			for (Entry<Integer, Integer> key : mappings){
				writer.print("        {\"c\":[{\"v\":\"" + key.getKey() + "\",\"f\":null},{\"v\":" + key.getValue() + ",\"f\":null}]}");
				if (q < values) {
					writer.print(",");
				}
				writer.println("");
				q++;
			}
			writer.println("      ]");
			writer.println("}");
			writer.close();
		}
		catch (Exception e){
			System.out.println("Error: " + e);
		}
	}

	private static void printExpProgram(ArrayList<photo> photos){
		try{
			PrintWriter writer = new PrintWriter(exportPath + "/expType.json","UTF-8");
			HashMap<String, Integer> expType = new HashMap<String,Integer>();
			
			writer.println("{");
			writer.println("  \"cols\": [");
			writer.println("        {\"id\":\"\",\"label\":\"Exposure program\",\"pattern\":\"\",\"type\":\"string\"},");
			writer.println("        {\"id\":\"\",\"label\":\"# of photos taken\",\"pattern\":\"\",\"type\":\"number\"}");
			writer.println("      ],");
			writer.println("  \"rows\": [");
			
			
			for (photo p:photos){
				if (expType.containsKey(p.expType)){
					expType.put(p.expType, expType.get(p.expType)+1);
				}
				else {
					expType.put(p.expType,1);
				}
			}
			int noLenses = expType.keySet().size();
			int q = 1;
			for (String key:expType.keySet()){
				writer.print("        {\"c\":[{\"v\":\"" + key + "\",\"f\":null},{\"v\":" + expType.get(key) + ",\"f\":null}]}");
				if (q < noLenses) {
					writer.print(",");
				}
				writer.println("");
				q++;
			}
			writer.println("      ]");
			writer.println("}");
			writer.close();
		}
		catch (Exception e){
			
		}
	}
	
	private static void printMetering(ArrayList<photo> photos){
		try{
			PrintWriter writer = new PrintWriter(exportPath + "/metering.json","UTF-8");
			HashMap<String, Integer> Metering = new HashMap<String,Integer>();
			
			writer.println("{");
			writer.println("  \"cols\": [");
			writer.println("        {\"id\":\"\",\"label\":\"Metering mode\",\"pattern\":\"\",\"type\":\"string\"},");
			writer.println("        {\"id\":\"\",\"label\":\"# of photos taken\",\"pattern\":\"\",\"type\":\"number\"}");
			writer.println("      ],");
			writer.println("  \"rows\": [");
			
			
			for (photo p:photos){
				if (Metering.containsKey(p.metering)){
					Metering.put(p.metering, Metering.get(p.metering)+1);
				}
				else {
					Metering.put(p.metering,1);
				}
			}
			int noLenses = Metering.keySet().size();
			int q = 1;
			for (String key:Metering.keySet()){
				writer.print("        {\"c\":[{\"v\":\"" + key + "\",\"f\":null},{\"v\":" + Metering.get(key) + ",\"f\":null}]}");
				if (q < noLenses) {
					writer.print(",");
				}
				writer.println("");
				q++;
			}
			writer.println("      ]");
			writer.println("}");
			writer.close();
		}
		catch (Exception e){
			
		}
	}
	
	private static void printExpTime(ArrayList<photo> photos){
		try{
			PrintWriter writer = new PrintWriter(exportPath + "/time.json","UTF-8");
			HashMap<Double, Integer> expTime = new HashMap<Double,Integer>();
			
			writer.println("{");
			writer.println("  \"cols\": [");
			writer.println("        {\"id\":\"\",\"label\":\"Exposure time\",\"pattern\":\"\",\"type\":\"string\"},");
			writer.println("        {\"id\":\"\",\"label\":\"# of photos taken\",\"pattern\":\"\",\"type\":\"number\"}");
			writer.println("      ],");
			writer.println("  \"rows\": [");
			
			
			for (photo p:photos){
				double temp = parseRatio(p.time);
				if (expTime.containsKey(temp)){
					expTime.put(temp, expTime.get(temp)+1);
				}
				else {
					expTime.put(temp,1);
				}
			}
			
			TreeMap<Double, Integer> sorted = new TreeMap<>(expTime);
			Set<Entry<Double, Integer>> mappings = sorted.entrySet();
			
			int noLenses = expTime.keySet().size();
			int q = 1;
			for (Entry<Double, Integer> key : mappings){
				String tempRatio ="";
				for (photo p:photos){
					if (parseRatio(p.time)==key.getKey()) {
						tempRatio = p.time;
					}
				}
				writer.print("        {\"c\":[{\"v\":\"" + tempRatio + "\",\"f\":null},{\"v\":" + key.getValue() + ",\"f\":null}]}");
				if (q < noLenses) {
					writer.print(",");
				}
				writer.println("");
				q++;
			}
			writer.println("      ]");
			writer.println("}");
			writer.close();
		}
		catch (Exception e){
			
		}
	}
	
	private static double parseRatio(String ratio) {
	    if (ratio.contains("/")) {
	        String[] rat = ratio.split("/");
	        return Double.parseDouble(rat[0]) / Double.parseDouble(rat[1]);
	    } else {
	        return Double.parseDouble(ratio);
	    }
	}
}
