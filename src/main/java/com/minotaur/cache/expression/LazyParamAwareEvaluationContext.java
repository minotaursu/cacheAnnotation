package com.minotaur.cache.expression;

import java.lang.reflect.Method;

import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ObjectUtils;

/**
 * Copy from Spring
 * 因为method已经是某个具体实现类的method.不再需要通过反射获取.
 * 删除了targetMethod = AopUtils.getMostSpecificMethod(this.method, this.targetClass);这段代码
 * Created by minotaur on 15/11/19.
 */
class LazyParamAwareEvaluationContext extends StandardEvaluationContext {

    private final ParameterNameDiscoverer paramDiscoverer;

    private final Method                  method;

    private final Object[]                args;

    private final Class<?>                targetClass;

    private boolean                       paramLoaded = false;

    LazyParamAwareEvaluationContext(Object rootObject, ParameterNameDiscoverer paramDiscoverer, Method method,
                                    Object[] args, Class<?> targetClass) {
        super(rootObject);

        this.paramDiscoverer = paramDiscoverer;
        this.method = method;
        this.args = args;
        this.targetClass = targetClass;
    }

    /**
     * Load the param information only when needed.
     */
    @Override
    public Object lookupVariable(String name) {
        Object variable = super.lookupVariable(name);
        if (variable != null) {
            return variable;
        }
        if (!this.paramLoaded) {
            loadArgsAsVariables();
            this.paramLoaded = true;
            variable = super.lookupVariable(name);
        }
        return variable;
    }

    private void loadArgsAsVariables() {
        // shortcut if no args need to be loaded
        if (ObjectUtils.isEmpty(this.args)) {
            return;
        }

        // save arguments as indexed variables
        for (int i = 0; i < this.args.length; i++) {
            setVariable("p" + i, this.args[i]);
        }

        String[] parameterNames = this.paramDiscoverer.getParameterNames(this.method);
        // save parameter names (if discovered)
        if (parameterNames != null) {
            for (int i = 0; i < parameterNames.length; i++) {
                setVariable(parameterNames[i], this.args[i]);
            }
        }
    }

}