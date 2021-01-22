package Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 *  @author: 梁铭标
 *  @Date：2018.10.22
 *  @Content：简单真分数运算的Service类
 */
public class GenerateSimpleOpTitleService {
    private static List<String> operatorExpressionList = new ArrayList<>();
    private static List<Integer> correctAnswerList = new ArrayList<>();

    @Test
    public void test() {
//        Random random = new Random();
//        int operationNum = random.nextInt(3) + 3;
//        System.out.println(operationNum);
        String json = "{\"name\":\"刘德华\",\"age\":35,\"some\":[{\"k1\":\"v1\",\"k2\":\"v2\"},{\"k3\":\"v3\",\"k4\":\"v4\"}]}";
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("some");
        System.out.println(jsonArray.toJSONString());
    }

    /**
     *  Test已通过
     * @Date：2018.10.24
     */
    @Test
    public void testGenerate() {
        generateSimpleFourTitleJsonArray(4);
    }

    /**
     *
     * @param titleNum
     * @return
     */
    public JSONArray generateSimpleFourTitleJsonArray(int titleNum) {
        JSONArray SimpleFourTitleArrayJson = new JSONArray();
        for (int i = 0; i < titleNum; i++) {
            if (i >= titleNum-1)
                generateFactoria();
            else
                generateFormula();
            storeJsonInJsonArray(SimpleFourTitleArrayJson, i);
        }
        System.out.println(SimpleFourTitleArrayJson);
        return SimpleFourTitleArrayJson;
    }

    /**
     *  将Json存进JsonArray中
     * @param SimpleFourTitleArrayJson
     * @param i
     */
    private void storeJsonInJsonArray(JSONArray SimpleFourTitleArrayJson, int i){
        String operationExpression = operatorExpressionList.get(i);
        int correctAnswer = correctAnswerList.get(i);
        //该方法实现生成多条整数题目并存进以JSON格式进行存储
        JSONObject simpleFourTitleJson = generateSimpleFourTitleJson(operationExpression, correctAnswer);
        SimpleFourTitleArrayJson.add(simpleFourTitleJson);
    }

    /**
     * 该方法实现一条整数题目并存进以JSON格式进行存储
     * @param operationExpression
     * @param correctAnswer
     * @return
     */
    public JSONObject generateSimpleFourTitleJson(String operationExpression, int correctAnswer) {
        String simpleFourTitleJsonStr = "{\"operationExpression\":\"" + operationExpression
                + "\",\"correctAnswer\":" + correctAnswer + "}";
        JSONObject simpleFourTitleJson = JSONObject.parseObject(simpleFourTitleJsonStr);
        return simpleFourTitleJson;
    }

    // 生成运算公式并计算正确结果，将最终的存入List中
    public void generateFormula() {
        Random random = new Random();
        int operationNum = random.nextInt(3) + 3;   //运算数的个数
        int operatorNum = operationNum - 1;               //运算符的个数

//        int answer;
//        //计算运算式答案
//        do {
//            List<String> operators = OperationsList.storeOpInList(operatorNum);
//            List<Integer> operations = operations(operationNum, operators);
//            newOperators = DeepCopy.deepCopy(operators);
//            newOperations = DeepCopy.deepCopy(operations);
//            answer = generateCorrectAnswer(operators, operations);
//        } while (answer < 0);

        List<String> operators = OperationsList.storeOpInList(operatorNum);
        List<Integer> operations = operations(operationNum, operators);
        List<String> newOperators = DeepCopy.deepCopy(operators);;
        List<Integer> newOperations = DeepCopy.deepCopy(operations);
        int answer = generateCorrectAnswer(operators, operations);

        StringBuilder stringBuilder = printSimpleOpEx(operationNum, newOperators, newOperations);
        operatorExpressionList.add(stringBuilder.toString());
        correctAnswerList.add(answer);
    }

    /**
     *  打印整数式运算公式
     * @param operationNum
     * @param operators
     * @param operations
     * @return
     */
    public StringBuilder printSimpleOpEx(int operationNum, List<String> operators, List<Integer> operations) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < operationNum - 1; i++) {
            int operation = operations.get(i);
            if (operation >= 0)
                stringBuilder.append(operation + " " + operators.get(i) + " ");
            else
                stringBuilder.append("(" + operation + ") " + operators.get(i) + " ");
        }
        int operation = operations.get(operationNum - 1);
        if (operation >=0 )
            stringBuilder.append(operation + " = ");
        else
            stringBuilder.append("(" + operation + ") = ");

        System.out.println(stringBuilder.toString());
        return stringBuilder;
    }

    // 计算运算式的正确结果
    public int generateCorrectAnswer(List<String> operators, List<Integer> operations) {
        //遍历运算符容器，完成乘除运算
        for (int i = 0; i < operators.size(); i++) {
            String operator = operators.get(i);
            if (operator.equals("*") || operator.equals("/")) {
                operators.remove(i);                    //乘除符号将其从集合中移除
                int operateLeft = operations.remove(i);      //拿运算符左侧的数字
                int operateRight = operations.remove(i);     //拿运算符右侧的数字
                if (operator.equals("*"))
                    operations.add(i, operateLeft * operateRight);
                else
                    operations.add(i, operateLeft / operateRight);
                i--;  //运算符容器的指针回到原来的位置,防止跳过下一个运算符
            }
        }

        //遍历运算符容器，完成加减运算，当运算符容器为空时，运算结束
        while (!operators.isEmpty()) {
            String operator = operators.remove(0);
            int operateLeft = operations.remove(0);
            int operateRight = operations.remove(0);
            if (operator.equals("+"))
                operateLeft = operateLeft + operateRight;
            else
                operateLeft = operateLeft - operateRight;
            operations.add(0, operateLeft);
        }

//        System.out.println(operations.get(0));
        return operations.get(0);
    }

    //生成operationNum个-50到100以内的随机数并存入List集合中
    public List<Integer> operations(int operationNum, List<String> operators) {
        List<Integer> operations = new ArrayList<>();
        for (int i = 0; i < operationNum; i++) {
            int num = generateNum();
            operations.add(num);
        }
        //当被除数不能整除除数时，随机生成能够整除的除数
        for (int i = 0; i < operators.size(); i++) {
            if (operators.get(i).equals("/")) {
                int x = decide(operations.get(i), operations.get(i + 1));
                operations.set(i + 1, x);
            }
        }
        return operations;
    }

    /**
     * 随即取x,y为1-100之间，x可以整除y的y值
     * 通过递归实现整除
     *
     * @param x
     * @param y
     * @return
     */
    private static int decide(int x, int y) {
        Random random = new Random();
        if (x % y != 0) {
            y = random.nextInt(100) + 1;
            return decide(x, y);
        } else
            return y;
    }

    /**
     *  测试运算数的生成范围
     */
    @Test
    public void testGenerateNum(){
        for (int i = 0; i < 50; i++) {
            int num = generateNum();
            System.out.print(num + " ");
        }
    }

    //生成运算数
    private int generateNum() {
        int num = (int) (Math.random() * 150) - 50;
        return num;
    }

    //根据阶乘数生成公式并计算结果,然后将结果和公式存进List中
    public void generateFactoria() {
        int factorial = generateFactorialNumber();
        int correctAnswer = getNFactorial(factorial);
        String factoriaOp = printFactoriaOp(factorial);
        operatorExpressionList.add(factoriaOp);
        correctAnswerList.add(correctAnswer);
    }

    //打印递归运算公式
    private String printFactoriaOp(int factoria) {
        String factoriaOp = factoria + "! =";
        return factoriaOp;
    }

    //递归计算运算结果
    private int getNFactorial(int n){
        if(n == 0)
            return 1;
        return n * getNFactorial(n - 1);
    }

    /**
     * @Param bound 设置随机种子的边界，生成[1，11)区间的整数
     * @return 返回阶乘数
     */
    private int generateFactorialNumber() {
        int bound = 10;
        Random random = new Random();
        int fractionNumber = random.nextInt(bound)  + 1;
        return fractionNumber;
    }


    public int getCorrectAnswer() {
        return correctAnswerList.remove(0);
    }

    public String getOperatorExpression() {
        return operatorExpressionList.remove(0);
    }

}
