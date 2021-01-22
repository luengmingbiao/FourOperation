package Servlet;

import Service.GenerateFractionTitleService;
import Service.GenerateMixTitleService;
import Service.GenerateSimpleOpTitleService;
import Service.JsonService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 */
public class GetTitleInfoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected  void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 设置请求与响应的编码
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/x-javascript;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");

        // 获取从页面中提交过来的JSON格式数据数据
        String titleInfo = JsonService.readJSONData(request);
        JSONObject titleInfoJson = JSONObject.parseObject(titleInfo);

        // 根据前台传来的数据进行解析后生成相应的题目，并以JsonArray格式存储
        JSONArray titleJsonArray = generateTitleJsonArray(titleInfoJson);
        System.out.println(titleJsonArray);

        PrintWriter printWriter = response.getWriter();
        printWriter.write(titleJsonArray.toJSONString());
        printWriter.flush();
        printWriter.close();
    }

    @Test
    public void testGenerateTitleJsonArray() {
        String titleInfoJsonStr = "{\"titleType\":\"整数式\",\"titleNum\":4}";
        JSONObject titleInfoJson = JSONObject.parseObject(titleInfoJsonStr);
        JSONArray jsonArray = generateTitleJsonArray(titleInfoJson);
        System.out.println(jsonArray);
    }

    /**
     *
     * @param titleInfoJson
     * @return
     */
    public JSONArray generateTitleJsonArray(JSONObject titleInfoJson) {
        String titleType = titleInfoJson.getString("titleType");
        int titleNum = titleInfoJson.getInteger("titleNum");
        if (titleType.equals("整数式")) {
            GenerateSimpleOpTitleService generateSimpleOpTitleService = new GenerateSimpleOpTitleService();
            JSONArray SimpleFourOpTitleArrayJson = generateSimpleOpTitleService.generateSimpleFourTitleJsonArray(titleNum);
            return SimpleFourOpTitleArrayJson;
        }
        else if (titleType.equals("分数式")) {
            GenerateFractionTitleService generateFractionTitleService = new GenerateFractionTitleService();
            JSONArray FractionOpTitleArrayJson = generateFractionTitleService.generateFractionTitleJsonStr(titleNum);
            return FractionOpTitleArrayJson;
        }
        else {
            GenerateMixTitleService generateMixTitleService = new GenerateMixTitleService();
            JSONArray MixTitleArrayJson = generateMixTitleService.generateMixTitleJsonArray(titleNum);
            return MixTitleArrayJson;
        }
    }
}
