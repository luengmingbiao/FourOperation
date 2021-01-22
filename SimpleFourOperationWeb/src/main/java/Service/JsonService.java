package Service;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Scanner;

public class JsonService {
    //读取请求传递过来的JSON格式数据，将JSON转换了JavaBean并将值传入Stu_info对象中
    public static String readJSONData(HttpServletRequest request){

        StringBuffer json = new StringBuffer();
        String lineString = null;
//        String jsons = null;
        try{
//            InputStream in = request.getInputStream();
//            Scanner scanner = new Scanner(in);
//            jsons = scanner.next();
            BufferedReader reader = request.getReader();
            while((lineString = reader.readLine()) != null){
                json.append(lineString);
            }
//            jsons = new String(jsons.toString().getBytes(),"UTF-8");
//            jsons = URLDecoder.decode(jsons, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json.toString();
    }
}
