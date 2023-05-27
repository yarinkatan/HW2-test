public class MultiProduct implements Function {
    private final Function[] functions;

    public MultiProduct(Function... functions) {
        this.functions = functions;
    }

    @Override
    public double valueAt(double x) {
        double product = 1.0;
        for (Function function : functions) {
            product *= function.valueAt(x);
        }
        return product;
    }

    @Override
    public Function derivative() {
        Function[] derivatives = new Function[functions.length];
        for (int i = 0; i < functions.length; i++) {
            Function[] otherFunctions = new Function[functions.length - 1];
            int index = 0;
            for (int j = 0; j < functions.length; j++) {
                if (j != i) {
                    otherFunctions[index++] = functions[j];
                }
            }
            derivatives[i] = new MultiProduct(otherFunctions).derivative();
        }
        return new MultiSum(derivatives);
    }

    @Override
    public double bisectionMethod(double a, double b, double epsilon) {
        return (a + b) / 2;
    }

    @Override
    public double bisectionMethod(double a, double b) {
        return bisectionMethod(a, b, Math.pow(10, -5));
    }

    @Override
    public double newtonRaphsonMethod(double a, double epsilon) {
        while (!(Math.abs(this.valueAt(a)) < epsilon)) {
            a = withdrawal(a);
        }
        return a;
    }

    @Override
    public double newtonRaphsonMethod(double a) {
        return newtonRaphsonMethod(a, Math.pow(10, -5));
    }

    @Override
    public Polynomial taylorPolynomial(int n) {
        return new Polynomial(new ItemInPolynomial[]{new ItemInPolynomial(0.0, 0)});
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < functions.length; i++) {
            sb.append(functions[i].toString());
            if (i < functions.length - 1) {
                sb.append(" * ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    private double withdrawal(double x) {
        double fx = valueAt(x);
        double dfx = derivative().valueAt(x);
        return x - (fx / dfx);
    }
}