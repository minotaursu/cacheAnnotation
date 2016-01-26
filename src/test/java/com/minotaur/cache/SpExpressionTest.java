package com.minotaur.cache;

import java.util.Date;

import com.minotaur.cache.support.LogUtils;
import org.apache.commons.logging.Log;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;


/**
 * Created by minotaur on 15/11/17.
 */
public class SpExpressionTest {

    private static final Log warnLog           = LogUtils.WARN_LOGGER;
    /**
     * 为啥paramNames会是空,shit...
     * String[] parameterNames = this.paramDiscoverer.getParameterNames(this.method);
     * @param args
     * @throws NoSuchMethodException
     */
    public static void main(String[] args) throws NoSuchMethodException {

        warnLog.warn("hello");

        StandardEvaluationContext ctx = new StandardEvaluationContext();
        ctx.setVariable("name", "lilei");
        ctx.setVariable("age", 21);
        ctx.setVariable("birthDay", new Date());

        SpelExpressionParser parser = new SpelExpressionParser();
        String key = parser.parseExpression("'getUser'+#name+#age+#birthDay").getValue(ctx, String.class);
        System.out.println(key);
        boolean condition = parser.parseExpression("#name.length()>5").getValue(ctx, boolean.class);
        System.out.println(condition);
        condition = parser.parseExpression("#name.length()<=5").getValue(ctx, boolean.class);
        System.out.println(condition);

        User user = new User();
        user.birthDay = new Date();
        user.age = 25;
        user.name = "hanmeimei";
        ctx.setVariable("user", user);
        key = parser.parseExpression("'getUser'+#user.age+#user.name+#user.birthDay").getValue(ctx, String.class);
        System.out.println(key);

    }
}
