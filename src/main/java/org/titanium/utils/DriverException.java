package org.titanium.utils;

import org.titanium.config.BaseClass;
import org.titanium.config.Log;
import org.titanium.engine.DriverEngine;


public class DriverException extends Exception{

    public static final long serialVersionUID = 700L;

    public DriverException(){}

    public DriverException(String message){
        super(message);
        DriverEngine.testStepResult = false;
        DriverEngine.testCaseResult = false;
        Log.error(message);
    }

    public DriverException(String message, Exception e){
        this(message);
        Log.error(e.getMessage());
        BaseClass.objProp.setErrorException(message);
    }
}
