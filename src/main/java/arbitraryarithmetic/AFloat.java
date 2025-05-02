package arbitraryarithmetic;

public class AFloat {
    private AInteger integerPart; // is integer part of the number
    private String fractionalPart; // is fractional part
    private boolean isNegative; // sign of number
    private static final int PRECISION = 30;

    public AFloat() {
        this.integerPart = new AInteger("0"); // initially we are taking as 0
        this.fractionalPart = "0";
        this.isNegative = false;
    }

    public AFloat(String s) {
        if (s == null || s.trim().isEmpty()) { //checking string is empty
            throw new IllegalArgumentException("invalid input");
        }
        s = s.trim();
        this.isNegative = s.startsWith("-");
        if (this.isNegative) {
            s = s.substring(1);
        }
        String[] parts = s.split("\\.");
        if (parts.length > 2) { 
            throw new IllegalArgumentException("Invalid input");
        }
        this.integerPart = new AInteger(parts[0]);
        this.fractionalPart = parts.length == 2 ? parts[1] : "0";
        if (this.fractionalPart.length() > PRECISION) { //takes upto precision number of digits
            this.fractionalPart = this.fractionalPart.substring(0, PRECISION);
        }
        // Remove trailing zeros
        this.fractionalPart = this.fractionalPart.replaceAll("0+$", "");
        if (this.fractionalPart.isEmpty()) {
            this.fractionalPart = "0";
        }
        // Validate input
        if (!parts[0].matches("-?\\d+") || (parts.length == 2 && !parts[1].matches("\\d+"))) {
            throw new IllegalArgumentException("Input must contain valid digits");
        }
        if (this.integerPart.toString().equals("0") && this.fractionalPart.equals("0")) {
            this.isNegative = false;
        }
    }

    // Copy constructor
    public AFloat(AFloat other) {
        this.integerPart = new AInteger(other.integerPart);
        this.fractionalPart = other.fractionalPart;
        this.isNegative = other.isNegative;
    }

    // Static parse method
    public static AFloat parse(String s) {
        return new AFloat(s);
    }

    // Addition
    public AFloat add(AFloat other) {
        if (this.isNegative == other.isNegative) {
            // Same sign: add magnitudes
            return addMagnitudes(this, other, this.isNegative);
        } else {
            // Different signs: subtract the smaller from the larger
            int cmp = compareMagnitudes(this, other);
            if (cmp >= 0) {
                return subtractMagnitudes(this, other, this.isNegative);
            } else {
                return subtractMagnitudes(other, this, other.isNegative);
            }
        }
    }

    // Subtraction
    public AFloat subtract(AFloat other) {
        // a - b = a + (-b)
        AFloat negOther = new AFloat(other);
        negOther.isNegative = !other.isNegative;
        return this.add(negOther);
    }

    // Multiplication
    public AFloat multiply(AFloat other) {
        // Convert to integers by removing decimal points
        String a = this.integerPart.toString().replace("-", "") + this.fractionalPart;
        String b = other.integerPart.toString().replace("-", "") + other.fractionalPart;
        int decimalPlaces = this.fractionalPart.length() + other.fractionalPart.length();
        AInteger intA = new AInteger(a);
        AInteger intB = new AInteger(b);
        AInteger product = intA.multiply(intB);
        // Adjust for decimal places
        String result = product.toString();
        if (result.length() <= decimalPlaces) {
            result = "0".repeat(decimalPlaces - result.length() + 1) + result;
        }
        int decimalPos = result.length() - decimalPlaces;
        String intPart = decimalPos > 0 ? result.substring(0, decimalPos) : "0";
        String fracPart = result.substring(decimalPos);
        // Truncate to PRECISION
        if (fracPart.length() > PRECISION) {
            fracPart = fracPart.substring(0, PRECISION);
        }
        fracPart = fracPart.replaceAll("0+$", "");
        AFloat resultFloat = new AFloat(intPart + "." + (fracPart.isEmpty() ? "0" : fracPart));
        resultFloat.isNegative = this.isNegative != other.isNegative;
        return resultFloat;
    }

    // Division
    public AFloat divide(AFloat other) {
        if (other.integerPart.toString().equals("0") && other.fractionalPart.equals("0")) {
            throw new ArithmeticException("Division by zero");
        }
        // To achieve PRECISION decimal places, multiply numerator by 10^(PRECISION)
        String a = this.integerPart.toString().replace("-", "") + this.fractionalPart;
        a = a + "0".repeat(PRECISION);
        String b = other.integerPart.toString().replace("-", "") + other.fractionalPart;
        AInteger intA = new AInteger(a);
        AInteger intB = new AInteger(b);
        AInteger quotient = intA.divide(intB);
        String result = quotient.toString();
        // Adjust for decimal places
        int decimalPos = result.length() - PRECISION;
        String intPart = decimalPos > 0 ? result.substring(0, decimalPos) : "0";
        String fracPart = decimalPos >= 0 ? result.substring(decimalPos) : "0".repeat(-decimalPos) + result;
        if (fracPart.length() > PRECISION) {
            fracPart = fracPart.substring(0, PRECISION);
        }
        fracPart = fracPart.replaceAll("0+$", "");
        AFloat resultFloat = new AFloat(intPart + "." + (fracPart.isEmpty() ? "0" : fracPart));
        resultFloat.isNegative = this.isNegative != other.isNegative;
        return resultFloat;
    }

    // Helper: Add magnitudes of two floats
    private AFloat addMagnitudes(AFloat a, AFloat b, boolean sign) {
        // Align fractional parts by padding with zeros
        String fracA = a.fractionalPart;
        String fracB = b.fractionalPart;
        int maxFracLen = Math.max(fracA.length(), fracB.length());
        fracA = fracA + "0".repeat(maxFracLen - fracA.length());
        fracB = fracB + "0".repeat(maxFracLen - fracB.length());
        // Combine integer and fractional parts
        String numA = a.integerPart.toString().replace("-", "") + fracA;
        String numB = b.integerPart.toString().replace("-", "") + fracB;
        AInteger sum = new AInteger(numA).add(new AInteger(numB));
        String result = sum.toString();
        // Split back into integer and fractional parts
        int fracLen = maxFracLen;
        String intPart = fracLen >= result.length() ? "0" : result.substring(0, result.length() - fracLen);
        String fracPart = fracLen >= result.length() ? "0".repeat(fracLen - result.length()) + result : result.substring(result.length() - fracLen);
        // Truncate to PRECISION
        if (fracPart.length() > PRECISION) {
            fracPart = fracPart.substring(0, PRECISION);
        }
        fracPart = fracPart.replaceAll("0+$", "");
        AFloat resultFloat = new AFloat(intPart + "." + (fracPart.isEmpty() ? "0" : fracPart));
        resultFloat.isNegative = sign;
        return resultFloat;
    }

    // Helper: Subtract magnitudes (a >= b)
    private AFloat subtractMagnitudes(AFloat a, AFloat b, boolean sign) {
        // Align fractional parts
        String fracA = a.fractionalPart;
        String fracB = b.fractionalPart;
        int maxFracLen = Math.max(fracA.length(), fracB.length());
        fracA = fracA + "0".repeat(maxFracLen - fracA.length());
        fracB = fracB + "0".repeat(maxFracLen - fracB.length());
        // Combine integer and fractional parts
        String numA = a.integerPart.toString().replace("-", "") + fracA;
        String numB = b.integerPart.toString().replace("-", "") + fracB;
        AInteger diff = new AInteger(numA).subtract(new AInteger(numB));
        String result = diff.toString();
        // Split back into integer and fractional parts
        int fracLen = maxFracLen;
        String intPart = fracLen >= result.length() ? "0" : result.substring(0, result.length() - fracLen);
        String fracPart = fracLen >= result.length() ? "0".repeat(fracLen - result.length()) + result : result.substring(result.length() - fracLen);
        // Truncate to PRECISION
        if (fracPart.length() > PRECISION) {
            fracPart = fracPart.substring(0, PRECISION);
        }
        fracPart = fracPart.replaceAll("0+$", "");
        AFloat resultFloat = new AFloat(intPart + "." + (fracPart.isEmpty() ? "0" : fracPart));
        resultFloat.isNegative = sign;
        return resultFloat;
    }

    // Helper: Compare magnitudes of two floats
    private int compareMagnitudes(AFloat a, AFloat b) {
        String intA = a.integerPart.toString().replace("-", "");
        String intB = b.integerPart.toString().replace("-", "");
        int intCmp = a.integerPart.compareMagnitudes(intA, intB);
        if (intCmp != 0) {
            return intCmp;
        }
        // Compare fractional parts
        String fracA = a.fractionalPart + "0".repeat(PRECISION - a.fractionalPart.length());
        String fracB = b.fractionalPart + "0".repeat(PRECISION - b.fractionalPart.length());
        return fracA.compareTo(fracB);
    }

    @Override
    public String toString() {
        String intPart = integerPart.toString();
        if (intPart.equals("0") && fractionalPart.equals("0")) {
            return "0.0";
        }
        String result = (isNegative ? "-" : "") + intPart;
        if (!fractionalPart.equals("0")) {
            result += "." + fractionalPart;
        }
        return result;
    }
}