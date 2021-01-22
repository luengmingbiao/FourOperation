package Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import dao.Fraction;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 *  @author: 梁铭标
 *  @Date：2018.10.22
 *  @Content：简单真分数运算类
 */
public class GenerateFractionTitleService {
    private static List<Fraction> correctAnswerList = new ArrayList<>();
    private static List<String> operatorExpressionList = new ArrayList<>();
    private int operationNum = 2;   //生成运算数的数量
    private int operatorNum = operationNum - 1;   // 生成运算符的数量


    /**
     *  Test已通过
     */
    @Test
    public void testGenerate(){
        generateFractionTitleJsonStr(2);
    }

    /**
     *
     * @param titleNum
     * @return
     */
    public JSONArray generateFractionTitleJsonStr(int titleNum) {
        JSONArray fractionTitleArrayJson = new JSONArray();
        for (int i = 0; i < titleNum; i++) {
            generateFormula();
            String operationExpression = operatorExpressionList.get(i);
            Fraction correctAnswer = correctAnswerList.get(i);
            JSONObject FractionTitleJson = generateFractionTitleJson(operationExpression, correctAnswer.toString());
            fractionTitleArrayJson.add(FractionTitleJson);
        }
//        System.out.println(fractionTitleArrayJson);
        return fractionTitleArrayJson;
    }

    /**
     *  该方法实现生成多条真分数题目并存进以JSON格式进行存储
     * @param operationExpression
     * @param coorectAnswer
     * @return
     */
    public JSONObject generateFractionTitleJson(String operationExpression, String coorectAnswer) {
        String  fractionTitleJsonStr = "{\"operationExpression\":\"" + operationExpression
                + "\",\"correctAnswer\":\"" + coorectAnswer + "\"}";
        JSONObject fractionTitleJson = JSONObject.parseObject(fractionTitleJsonStr);
        return fractionTitleJson;
    }


    /**
     *  生成真分数运算公式并计算运算结果
     */
    @Test
    public void generateFormula() {
        List<Fraction> newFractions;
        List<String> newOperators;
        Fraction newAnswer;
        do {
            List<String> operators = OperationsList.storeOpInList(operatorNum);
            newOperators = DeepCopy.deepCopy(operators);
            List<Fraction> fractionsList = generateFractionList();
            newFractions = DeepCopy.deepCopy(fractionsList);
            newAnswer = generateCorrectAnswer(operators, fractionsList);
        } while (isLessThanZero(newAnswer));

        String fractionExpression = printFractionExpression(newFractions, newOperators);
        System.out.println(newAnswer.toString());
        correctAnswerList.add(newAnswer);
        operatorExpressionList.add(fractionExpression);
    }

    //判断分数是否为0
    private boolean isLessThanZero(Fraction fraction) {
        int numerator = fraction.getNumerator();
        int denominator = fraction.getDenominator();
        if (numerator < 0 && denominator > 0)
            return true;
        else if (numerator > 0 && denominator < 0)
            return true;
        else
            return false;
    }

    //生成一组运算分数
    private List<Fraction> generateFractionList() {
        List<Fraction> fractionsList = new ArrayList<>();
        for (int i = 0; i < operationNum; i++)
            fractionsList.add(generateFraction());
        return fractionsList;
    }

    //打印分数运算表达式
    private String printFractionExpression(List<Fraction> fractionsList, List<String> operators) {
        StringBuilder stringBuilder = new StringBuilder();
        Fraction fraction;
        for (int i = 0; i < operationNum - 1; i++) {
            fraction = fractionsList.get(i);
            stringBuilder.append(fraction.toString());
            stringBuilder.append(" " + operators.get(i) + " ");
        }
        fraction = fractionsList.get(operationNum - 1);
        stringBuilder.append(fraction.toString() + " = ");
        System.out.println(stringBuilder.toString());

        return stringBuilder.toString();
    }

    // 计算运算式的正确结果
    public Fraction generateCorrectAnswer(List<String> operators, List<Fraction> operations) {
        //遍历运算符容器，完成乘除运算
        for (int i = 0; i < operators.size(); i++) {
            String operator = operators.get(i);
            if (operator.equals("*") || operator.equals("/")) {
                operators.remove(i);                    //乘除符号将其从集合中移除
                Fraction fractionLeft = operations.remove(i);      //拿运算符左侧的数字
                Fraction fractionRight = operations.remove(i);     //拿运算符右侧的数字
                if (operator.equals("*"))
                    operations.add(fractionMultiple(fractionLeft, fractionRight));
                else
                    operations.add(fractionDivide(fractionLeft, fractionRight));
                i--;  //运算符容器的指针回到原来的位置,防止跳过下一个运算符
            }
        }

        //遍历运算符容器，完成加减运算，当运算符容器为空时，运算结束
        while (!operators.isEmpty()) {
            String operator = operators.remove(0);
            Fraction fractionLeft = operations.remove(0);
            Fraction fractionRight = operations.remove(0);
            if (operator.equals("+"))
                fractionLeft = fractionAdd(fractionLeft, fractionRight);
            else
                fractionLeft = fractionSubtract(fractionLeft, fractionRight);
            operations.add(0, fractionLeft);
        }

        //返回计算结果
        return operations.get(0);
    }

    // 真分数相乘运算
    public Fraction fractionMultiple(Fraction fractionLeft, Fraction fractionRight){
        int numerator = fractionLeft.getNumerator() * fractionRight.getNumerator();
        int denominator = fractionLeft.getDenominator() * fractionRight.getDenominator();

        Fraction fraction = getsimplificationFraction(numerator, denominator);
        return fraction;
    }

    // 真分数相除运算
    public Fraction fractionDivide(Fraction fractionLeft, Fraction fractionRight) {
        int numerator = fractionLeft.getNumerator() * fractionRight.getDenominator();
        int denominator = fractionLeft.getDenominator() * fractionRight.getNumerator();

        Fraction fraction = getsimplificationFraction(numerator, denominator);
        return fraction;
    }

    // 真分数相加运算
    public Fraction fractionAdd(Fraction fractionLeft, Fraction fractionRight) {
        int denominator;
        int numerator;
        //如果分母相同时直接分子相加
        if (fractionLeft.getDenominator() == fractionRight.getDenominator()) {
            denominator = fractionLeft.getDenominator();
            numerator = fractionLeft.getNumerator() + fractionRight.getNumerator();
        }
        else {
            denominator = fractionLeft.getDenominator() * fractionRight.getDenominator();
            numerator = fractionLeft.getNumerator() * fractionRight.getDenominator()
                    +  fractionRight.getNumerator() * fractionLeft.getDenominator();
        }
        Fraction fraction = getsimplificationFraction(numerator, denominator);
        return fraction;
    }

    @Test
    public void testFraction() {
        Fraction fraction1 = new Fraction(1,5);
        Fraction fraction2 = new Fraction(2,10);
        Fraction fraction = fractionSubtract(fraction1, fraction2);
        System.out.println(fraction.toString());
    }

    // 真分数相减运算
    public Fraction fractionSubtract(Fraction fractionLeft, Fraction fractionRight) {
        int denominator;
        int numerator;
        //如果分母相同时直接分子相减
        if (fractionLeft.getDenominator() == fractionRight.getDenominator()) {
            denominator = fractionLeft.getDenominator();
            numerator = fractionLeft.getNumerator() - fractionRight.getNumerator();
        }
        else {
            denominator = fractionLeft.getDenominator() * fractionRight.getDenominator();
            numerator = fractionLeft.getNumerator() * fractionRight.getDenominator()
                    -  fractionRight.getNumerator() * fractionLeft.getDenominator();
        }
//        Fraction fraction = new Fraction(numerator, denominator);
        Fraction fraction = getsimplificationFraction(numerator, denominator);
        return fraction;
    }



    //获得最大公约数并且将真分数化简
    public Fraction getsimplificationFraction(int numerator, int denominator) {
        int greatestCommonDivisor = greatestCommonDivisor(numerator, denominator);
        numerator /= greatestCommonDivisor;
        denominator /= greatestCommonDivisor;
        return new Fraction(numerator, denominator);
    }

    //生成一个真分数
    private Fraction generateFraction() {
        int numerator = generateNum();
        int denominator = regenerate(numerator, generateNum());
        int greatestCommonDivisor = greatestCommonDivisor(numerator, denominator);
        if (greatestCommonDivisor > 1) {
            numerator /= greatestCommonDivisor;
            denominator /= greatestCommonDivisor;
        }
        Fraction fraction = new Fraction(numerator, denominator);
        return fraction;
    }

    //递归生成与分子不相等的分母
    private int regenerate(int numerator, int denominator) {
        if (numerator == denominator) {
            denominator = generateNum();
            return regenerate(numerator, denominator);
        } else {
            return denominator;
        }
    }

    //生成1-20随机数
    private int generateNum(){
        int num = (int) (Math.random() * 20) + 1;
        return num;
    }

    // 求a和b的最大公约数
    private int greatestCommonDivisor(int a, int b) {
        if (a < b) {
            int c = a;
            a = b;
            b = c;
        }
        int r = a % b;
        while (r != 0) {
            a = b;
            b = r;
            r = a % b;
        }
        return b;
    }

    // 最小公倍数
    public int get_lcm(int n1, int n2) {
        return n1 * n2 / greatestCommonDivisor(n1, n2);
    }

    public String getCorrectAnswer(){
        return correctAnswerList.remove(0).toString();
    }

    public String getOperatorExpression() {
        return operatorExpressionList.remove(0);
    }

}
