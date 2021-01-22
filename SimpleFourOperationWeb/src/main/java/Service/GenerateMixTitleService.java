package Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dao.Fraction;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *  @author: 梁铭标
 *  @Date：2018.10.23
 *  @Content：生成真分数和简单正整数运算式的Service类
 */
public class GenerateMixTitleService {

    /**
     *  Test已通过
     * @Date：2018.10.23
     */
    @Test
    public void testGenerateMixTitleJsonArray(){
        generateMixTitleJsonArray(4);
    }

    /**
     *
     * @param titleNum
     * @return
     */
    public JSONArray generateMixTitleJsonArray(int titleNum) {
        JSONArray mixTitleArrayJson = new JSONArray();
        Random random = new Random();
        //初始化生成简单正整数和真分数运算类
        GenerateSimpleOpTitleService generateSimpleOpTitleService = new GenerateSimpleOpTitleService();
        GenerateFractionTitleService generateFractionTitleService = new GenerateFractionTitleService();
        for (int i = 0; i < titleNum; i++) {
            int num = random.nextInt(2);
            // 假如num=0，则生成正整数运算式
            if (num == 0) {
                JSONObject simpleFourTitleJson = generateSimpleFourTitleJson(generateSimpleOpTitleService);
                mixTitleArrayJson.add(simpleFourTitleJson);
            }else{   //不然 生成真分数运算式
                JSONObject FractionTitleJson = generateFractionTitleJson((generateFractionTitleService));
                mixTitleArrayJson.add(FractionTitleJson);
            }
        }
        return mixTitleArrayJson;
    }

    /**
     *
     * @param generateSimpleOpTitleService
     * @return
     */
    private JSONObject generateSimpleFourTitleJson(GenerateSimpleOpTitleService generateSimpleOpTitleService) {
        generateSimpleOpTitleService.generateFormula();
        String operationExpression = generateSimpleOpTitleService.getOperatorExpression();
        int correctAnswer = generateSimpleOpTitleService.getCorrectAnswer();
        JSONObject simpleFourTitleJson = generateSimpleOpTitleService.generateSimpleFourTitleJson(operationExpression, correctAnswer);
        return simpleFourTitleJson;
    }

    /**
     *
     * @param generateFractionTitleService
     * @return
     */
    public JSONObject generateFractionTitleJson(GenerateFractionTitleService generateFractionTitleService) {
        generateFractionTitleService.generateFormula();
        String operationExpression = generateFractionTitleService.getOperatorExpression();
        String correctAnswer = generateFractionTitleService.getCorrectAnswer();
        JSONObject FractionTitleJson = generateFractionTitleService.generateFractionTitleJson(operationExpression, correctAnswer);
        return FractionTitleJson;
    }

}
