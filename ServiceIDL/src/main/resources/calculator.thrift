namespace java org.aiden.lab.microserve.calculator

const double PI = 3.14;

service calculatorService {

    double add(1:double first, 2:double second)
    double minus(1:double first, 2:double second)
    double multiply(1:double first, 2:double second)
    double division(1:double first, 2:double second)
    double circularArea(1:double first, 2:double second)
    double CircularPerimeter(1:double first, 2:double second)

}