package com.test.sampler;

import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;

public class TestSamplerGUI extends AbstractSamplerGui{
    private JTextField functionTextField = null;
    public TestSamplerGUI(){
        init(); 
    }

    @Override
    public void configure(TestElement element) {
        super.configure(element);
        functionTextField.setText(element.getPropertyAsString(TestSampler.FUNCTION));
    }

    private void init() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        functionTextField = new JTextField(20);
        mainPanel.add(functionTextField);
        add(mainPanel);
    }

    @Override
    public TestElement createTestElement() {//创建所对应的Sampler
        TestElement sampler = new TestSampler();
        modifyTestElement(sampler);
        return sampler;
    }

    @Override
    public String getLabelResource() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void modifyTestElement(TestElement sampler) {
        super.configureTestElement(sampler);
        if (sampler instanceof TestSampler) {
            TestSampler testSmpler = (TestSampler) sampler;
            testSmpler.setProperty(TestSampler.FUNCTION, functionTextField.getText());      
        }
    }

    @Override
    public String getStaticLabel() {//设置显示名称
        // TODO Auto-generated method stub
        return "TestSampler";
    }

    private void initFields(){
        functionTextField.setText("");
    }

    @Override
    public void clearGui() {
        super.clearGui();
        initFields();
    }

}