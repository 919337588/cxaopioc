package com.chixin.web;


import com.chixin.note.CxAutowired;
import com.chixin.note.CxControl;
import com.chixin.service.TestService;

@CxControl
public class TestControl {
    @CxAutowired
    private TestService testService;

    public void hellowWord(){
        System.out.println("TestControl hellow word");
        testService.hellowWord();
    }
}
