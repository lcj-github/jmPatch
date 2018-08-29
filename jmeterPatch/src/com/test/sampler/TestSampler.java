package com.test.sampler;

import org.apache.jmeter.samplers.Entry;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.SampleResult;

public class TestSampler extends AbstractSampler {

    public final static String FUNCTION = "function"; 
    @Override
    public SampleResult sample(Entry entry) {
        // TODO Auto-generated method stub
        SampleResult res = new SampleResult();
        res.sampleStart();
        System.out.println(this.getProperty(FUNCTION));//输出GUI界面所输入的函数方法返回结果
        res.sampleEnd();
        res.setSuccessful(true);
        return res;
    }
	 
}