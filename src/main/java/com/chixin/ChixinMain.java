package com.chixin;


import com.chixin.note.CxAutowired;
import com.chixin.note.CxCompent;
import com.chixin.spring.ApplicationContext;
import com.chixin.web.TestControl;

@CxCompent
public class ChixinMain {
    @CxAutowired
    private TestControl testControl;
    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext=new ApplicationContext("com.chixin");
        ChixinMain chixinMain= applicationContext.getBean(ChixinMain.class);
        chixinMain.hellowWord();
    }
    public void hellowWord(){
        System.out.println("ChixinMain hellow word");
        testControl.hellowWord();
    }
}
