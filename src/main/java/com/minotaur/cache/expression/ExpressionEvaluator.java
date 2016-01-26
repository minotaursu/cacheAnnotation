package com.minotaur.cache.expression;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * expression evaluator
 * cache the key expression. key like ClassName#Method#keyExpression
 * Created by minotaur on 15/11/19.
 */
public class ExpressionEvaluator {

    private SpelExpressionParser parser              = new SpelExpressionParser();

    // shared param discoverer since it caches data internally
    private ParameterNameDiscoverer paramNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

    private Map<String, Expression> conditionCache      = new ConcurrentHashMap<String, Expression>();

    private Map<String, Expression> keyCache            = new ConcurrentHashMap<String, Expression>();

    public EvaluationContext createEvaluationContext(CacheExpressionRootObject rootObject) {
        return new LazyParamAwareEvaluationContext(rootObject, this.paramNameDiscoverer, rootObject.getMethod(),
            rootObject.getArgs(), rootObject.getTargetClass());
    }

    public boolean condition(String conditionExpression, Method method, EvaluationContext evalContext) {
        String key = toString(method, conditionExpression);
        Expression condExp = this.conditionCache.get(key);
        if (condExp == null) {
            condExp = this.parser.parseExpression(conditionExpression);
            this.conditionCache.put(key, condExp);
        }
        return condExp.getValue(evalContext, boolean.class);
    }

    public String key(String keyExpression, Method method, EvaluationContext evalContext) {
        String key = toString(method, keyExpression);
        Expression keyExp = this.keyCache.get(key);
        if (keyExp == null) {
            keyExp = this.parser.parseExpression(keyExpression);
            this.keyCache.put(key, keyExp);
        }
        return keyExp.getValue(evalContext,String.class);
    }

    private String toString(Method method, String expression) {
        StringBuilder sb = new StringBuilder();
        sb.append(method.getDeclaringClass().getName());
        sb.append("#");
        sb.append(method.toString());
        sb.append("#");
        sb.append(expression);
        return sb.toString();
    }
}
