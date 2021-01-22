package dao;

import java.io.Serializable;

/**
 * @author: 梁铭标
 * @Date：2018.10.10
 * @Content：可序列化真分数对象
 */
public class Fraction implements Serializable {
    private static final long serialVersionUID = -3245478690496182643L;
    private int numerator;  //分子
    private int denominator;  //分母

    public Fraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public void setNumerator(int numerator) {
        this.numerator = numerator;
    }

    public void setDenominator(int denominator) {
        this.denominator = denominator;
    }

    @Override
    public String toString() {
        if (denominator == 1) {
            return String.valueOf(numerator);
        }else
            return numerator + "/" + denominator;
    }
}
