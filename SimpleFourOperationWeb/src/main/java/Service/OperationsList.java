package Service;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OperationsList {
    public static List<String> operatorsList;

    public static List<String> getOperatorsList() {
        init();
        return operatorsList;
    }

    private static void init(){
        String[] operators = {"+", "-", "*", "/"};
        operatorsList = Arrays.asList(operators);
    }

    @Test
    private void testOperators() {
        init();
        String[] operators = {"+", "-", "*", "/"};
        operatorsList = Arrays.asList(operators);
        List<String> operatorsList = storeOpInList(2);
        for (String operator : operatorsList) {
            System.out.println(operator);
        }
    }

    //生成operatorsNum个运算符并存入List集合中
    public static List<String> storeOpInList(int operatorNum) {
        init();
        List<String> operatorsList = new ArrayList<>();
        for (int i = 0; i < operatorNum; i++) {
            String operator = generateOperator();
            operatorsList.add(operator);
        }
        return operatorsList;
    }

    //生成运算符
    private static String generateOperator() {
        int x = (int) (Math.random() * 4);
        String operator = operatorsList.get(x);
        return operator;
    }
}
