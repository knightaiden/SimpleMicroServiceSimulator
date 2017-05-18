package org.aiden.lab.microserv.sample.service;

import org.aiden.lab.microserve.calculator.CalculatorService;
import org.aiden.lab.microserve.calculator.Result;
import org.aiden.lab.microserve.calculator.CalculatorConstants;
import org.apache.thrift.TException;

/**
 * Created by zhangzhe on 2017/5/9.
 */
public class CalculatorServiceImpl implements CalculatorService.Iface {
    @Override
    public Result add(double first, double second) throws TException {
        double result = first + second;
        return new Result(result, true);
    }

    @Override
    public Result minus(double first, double second) throws TException {
        double result = first - second;
        return new Result(result, true);
    }

    @Override
    public Result multiply(double first, double second) throws TException {
        double result = first * second;
        return new Result(result, true);
    }

    @Override
    public Result division(double first, double second) throws TException {
        double result = first / second;
        return new Result(result, true);
    }

    @Override
    public Result circularArea(double r) throws TException {
        double result = CalculatorConstants.PI * r * r;
        return new Result(result, true);
    }

    @Override
    public Result CircularPerimeter(double r) throws TException {
        double result = CalculatorConstants.PI * r * 2;
        return new Result(result, true);
    }
}
