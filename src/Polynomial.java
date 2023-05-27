public class Polynomial implements Function {
    private final ItemInPolynomial[] polynomial;

    public Polynomial(double... args) {
        this.polynomial = new ItemInPolynomial[args.length];
        for(int i = 0; i < args.length; i++) {
            if(args[i] != 0) {
                this.polynomial[i] = new ItemInPolynomial(args[i], i);
            }
        }
    }

    public Polynomial(ItemInPolynomial[] polynomial) {
        this.polynomial = polynomial;
    }

    public ItemInPolynomial[] getPolynomial() {
        return polynomial;
    }

    @Override
    public double valueAt(double x) {
        double result = 0.0;
        for (ItemInPolynomial term : polynomial) {
            result += term.getCoefficient() * Math.pow(x, term.getExponent());
        }
        return result;
    }

    @Override
    public Function derivative() {
        ItemInPolynomial[] derivativePolynomial = new ItemInPolynomial[polynomial.length];
        for (int i = 0; i < polynomial.length; i++) {
            double ai = polynomial[i].getCoefficient() * polynomial[i].getExponent();
            int exponent = polynomial[i].getExponent() - 1;
            derivativePolynomial[i] = new ItemInPolynomial(ai, exponent);
        }
        return new Polynomial(derivativePolynomial);
    }

    @Override
    public double bisectionMethod(double a, double b, double epsilon) {
        double left = a;
        double right = b;
        double mid;
        while (right - left > epsilon) {
            mid = (left + right) / 2;
            if (valueAt(left) * valueAt(mid) > 0) {
                left = mid;
            } else {
                right = mid;
            }
        }
        return (left + right) / 2;
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
        ItemInPolynomial[] terms = new ItemInPolynomial[n + 1];
        for (int i = 0; i <= n; i++) {
            terms[i] = new ItemInPolynomial(0.0, i);
        }
        return new Polynomial(terms);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        boolean isFirstTerm = true;
        for (ItemInPolynomial term : polynomial) {
            double coefficient = term.getCoefficient();
            int exponent = term.getExponent();

            if (coefficient == 0.0) {
                continue;
            }

            if (!isFirstTerm && coefficient > 0.0) {
                builder.append("+");
            }

            if (exponent == 0) {
                builder.append(coefficient);
            } else {
                if (coefficient != 1.0 && coefficient != -1.0) {
                    builder.append(coefficient);
                } else if (coefficient == -1.0) {
                    builder.append("-");
                }

                builder.append("x");

                if (exponent != 1) {
                    builder.append("^").append(exponent);
                }
            }

            isFirstTerm = false;
        }

        if (builder.length() == 0) {
            builder.append("0");
        }

        return builder.toString();
    }

    private double withdrawal(double x) {
        double fx = valueAt(x);
        double dfx = derivative().valueAt(x);
        return x - (fx / dfx);
    }
}