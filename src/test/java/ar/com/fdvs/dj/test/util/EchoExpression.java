package ar.com.fdvs.dj.test.util;

import ar.com.fdvs.dj.domain.CustomExpression;

import java.util.Map;

public class EchoExpression implements CustomExpression {

    private String printValue;

    public EchoExpression() {}
    public EchoExpression(String printValue) {
        this.printValue = printValue;
    }

    public Object evaluate(Map fields, Map variables, Map parameters) {
        return printValue;
    }

    public String getClassName() {
        return String.class.getName();
    }

    public String getPrintValue() {
        return printValue;
    }

    public void setPrintValue(String printValue) {
        this.printValue = printValue;
    }
}
