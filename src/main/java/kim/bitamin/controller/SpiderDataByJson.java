package kim.bitamin.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.json.JSONObject;

import kim.bitamin.util.FileIo;

public class SpiderDataByJson {
	static int count = 0;
	static HashSet<String> set = new HashSet<String>();
	static List<String> list = new ArrayList<>();
	static StringBuilder sb = new StringBuilder();
	static final String FILE_NAME = "number";
	
	static String url = "https://m.10010.com/NumApp/NumberCenter/qryNum?callback=jsonp_queryMoreNums&provinceCode=31&cityCode=310&monthFeeLimit=0&groupKey=1500263519&searchCategory=1&net=01&amounts=200&codeTypeCode=&searchValue=&qryType=02&goodsNet=4&_=1514523545992"; 
	
	public static void main(String[] args) {
		try {
			for (int i=0;i<100;i++){
				String numArrayJson = loadJson(url).substring(20, loadJson(url).length()-2);
				addNumberToSet(getNumArray(numArrayJson));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.getMessage();
		}finally {
			System.out.println("经过筛选去重，共有"+set.size()+"条可用号码");
			list.addAll(set);
			Collections.sort(list);
			System.out.println("排序完成");
			if (set.isEmpty()){
				System.out.println("没获取到数据");
				return;
			}
			System.out.println("正在写入到文件"+FILE_NAME);
			for (String i : list){
				sb.append(i+"\n");
			}
			FileIo.writeFile("\\"+FILE_NAME+".txt",sb.toString() );
			System.out.println("写入完毕");
		}
	}
	
	public static String loadJson (String url) {  
        StringBuilder json = new StringBuilder();  
        try {  
            URL urlObject = new URL(url);  
            URLConnection uc = urlObject.openConnection();  
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));  
            String inputLine = null;  
            while ( (inputLine = in.readLine()) != null) {  
                json.append(inputLine);  
            }  
            in.close();  
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return json.toString();  
    }  
	
	public static void addNumberToSet(String[] numArray){
		for (String str : numArray){
			if (str.length() > 10){
				set.add(str);
				count++;
				System.out.println(str);
			}
		}
		System.out.println("已经获取到"+count+"条数据");
	}
	
	public static String[] getNumArray(String numArrayJson){
		  
        JSONObject jsonObject = new JSONObject(numArrayJson);
        String num =  jsonObject.get("numArray").toString();
        num = num.substring(1, num.length()-1);
        String[] numArray = num.split(",");
        return numArray;
	}
	
}
