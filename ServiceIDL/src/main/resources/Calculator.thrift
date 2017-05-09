namespace java org.aiden.lab.microserve.calculator

const double PI = 3.14;

struct Result {
    1: double result,
    2: bool isCorrect,
}

service CalculatorService {

    Result add(1:double first, 2:double second),
    Result minus(1:double first, 2:double second),
    Result multiply(1:double first, 2:double second),
    Result division(1:double first, 2:double second),
    Result circularArea(1:double r),
    Result CircularPerimeter(1:double r)

}