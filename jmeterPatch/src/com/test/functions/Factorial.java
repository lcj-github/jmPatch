package com.test.functions;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * 通过继承AbstractFunction抽象类，
 * 重写getArgumentDesc方法实现对函数参数的描述，
 * 重写setParameters方法来对函数的参数进行检查和设置，
 * 重写getReferenceKey方法告诉JMeter该函数在框架中的引用名称，
 * 重写execute方法，实现对该函数的执行并返回结果。
 * 
 *
 */
public class Factorial extends AbstractFunction {
	
	private static final Logger log = LoggingManager.getLoggerForClass();
    private static final List<String> desc = new LinkedList<String>();
    private static final String KEY = "__factorial";
    private Object[] values = null;
    
    static {
        desc.add(JMeterUtils.getResString("factorial_value"));
    }
	@Override
	public List<String> getArgumentDesc() {
		// TODO Auto-generated method stub
		return desc;
	}
	@Override
	public String execute(SampleResult previousResult, Sampler currentSampler)
			throws InvalidVariableException {
		// TODO Auto-generated method stub
		String numberString = ((CompoundVariable) values[0]).execute().trim();
		int num;
		try{
			num = Integer.valueOf(numberString);
		} catch (Exception e){
			return null;
		}
		
		return String.valueOf(factorial(num));
	}
	@Override
	public String getReferenceKey() {
		// TODO Auto-generated method stub
		return KEY;
	}
	@Override
	public void setParameters(Collection<CompoundVariable> parameters)
			throws InvalidVariableException {
		// TODO Auto-generated method stub
		//checkMinParameterCount(parameters, 1);
		checkParameterCount(parameters, 1, 1);
        values = parameters.toArray();
	}
	
	private int factorial(int num){
		int result = 1;
		if(num == 0){
			result = 1;
		} else {
			for(int i = num; i > 0; i--){
				result *= i;
			}
		}
		
		return result;
	}
}