package arbitraryarithmetic;

public class AInteger {
    private String num; // number in string form (without sign)
    private boolean isNegative; // Sign of number

    public AInteger() {
        this.num = "0"; //initially consider as 0
        this.isNegative = false; //initially sign taken as positive
    }

    // empty string then invalid
    public AInteger(String s) {
        if (s == null || s.trim().isEmpty()) {
            throw new IllegalArgumentException("invalid input");
        }
        s = s.trim();           //remove extra space 
        if (s.startsWith("-")) {    
            this.isNegative = true;
            this.num = s.substring(1);
        } else {
            this.isNegative = false;
            this.num = s;
        }
        this.num = this.num.replaceFirst("^0+(?!$)", ""); //0010 to 10
        if (this.num.isEmpty()) {
            this.num = "0";
            this.isNegative = false;
        }
        // if string has other than numbers 
        if (!this.num.matches("\\d+")) {
            throw new IllegalArgumentException("invalid input");
        }
    }

    // Copy of that number 
    public AInteger(AInteger copy) {
        this.num = copy.num;
        this.isNegative = copy.isNegative;
    }

    // Static parse method
    public static AInteger parse(String s) {
        return new AInteger(s);
    }

   public AInteger add(AInteger other) {
        if (this.isNegative == other.isNegative) {       // if same sign just add magnitudes
            String result = addMagnitudes(this.num, other.num);
            AInteger sum = new AInteger(result);
            sum.isNegative = this.isNegative;  //sign of adding numbers is sign of sum
            return sum;
        } else {
            // if loop runs if mag of this.num is greater or equal to other.num and next subtracting and sign is same as this.num
            if (compareMagnitudes(this.num, other.num) >= 0) {
                String result = subtractMagnitudes(this.num, other.num);
                AInteger diff = new AInteger(result);
                diff.isNegative = this.isNegative;
                return diff;
            } else {
                String result = subtractMagnitudes(other.num, this.num);
                AInteger diff = new AInteger(result);
                diff.isNegative = other.isNegative;
                return diff;
            }
        }
    }

    public AInteger subtract(AInteger other) {
        AInteger negOther = new AInteger(other); // make a copy of other number 
        negOther.isNegative = !other.isNegative; //change sign of other number
        return this.add(negOther); // add updated other number and number
    }

    public AInteger multiply(AInteger other) {
        String result = multiplyMagnitudes(this.num, other.num);
        AInteger product = new AInteger(result);
        product.isNegative = this.isNegative != other.isNegative; // if both have opp signs then result is neg
        return product;
    }

    public AInteger divide(AInteger other) {
        if (other.num.equals("0")) {
            throw new ArithmeticException("Division by zero");
        }
        String result = divideMagnitudes(this.num, other.num);
        AInteger quotient = new AInteger(result);
        quotient.isNegative = this.isNegative != other.isNegative;
        return quotient;
    }

    private String addMagnitudes(String a, String b) {
        StringBuilder result = new StringBuilder();
        int carry = 0;
        int i = a.length() - 1; // starting from unit digit
        int j = b.length() - 1;

        while (i >= 0 || j >= 0 || carry > 0) {
            int sum = carry;
            if (i >= 0) sum += a.charAt(i--) - '0'; // adding digits while loop is running
            if (j >= 0) sum += b.charAt(j--) - '0'; // convert to integer
            carry = sum / 10;
            result.append(sum % 10);
        }
        return result.reverse().toString();
    }

    private String subtractMagnitudes(String a, String b) { // a>=b
        StringBuilder result = new StringBuilder();
        int borrow = 0;
        int i = a.length() - 1; // starting from unit digits 
        int j = b.length() - 1;
        while (i >= 0 || j >= 0) {
            int digita = i >= 0 ? a.charAt(i--) - '0' : 0;
            int digitb = j >= 0 ? b.charAt(j--) - '0' : 0;
            digita -= borrow;
            if (digita < digitb) {
                digita += 10;
                borrow = 1;
            } else {
                borrow = 0;
            }
            result.append(digita - digitb);
        }
        String res = result.reverse().toString();
        res = res.replaceFirst("^0+(?!$)", ""); //we will eliminate zeros to avoid adding of zeros at end
        return res.isEmpty() ? "0" : res;
    }

    public int compareMagnitudes(String a, String b) {
        a = a.replaceFirst("^0+(?!$)", "");
        b = b.replaceFirst("^0+(?!$)", "");
        if (a.length() != b.length()) {
            return Integer.compare(a.length(), b.length());
        }
        return a.compareTo(b); //returns 1 if a>b and 0 if equal
    }

    private String multiplyMagnitudes(String a, String b) {
        int[] result = new int[a.length() + b.length()];
        for (int i = a.length() - 1; i >= 0; i--) {
            int digitA = a.charAt(i) - '0';
            for (int j = b.length() - 1; j >= 0; j--) {
                int digitB = b.charAt(j) - '0';
                int pla = (a.length() - 1 - i) + (b.length() - 1 - j); // gives place of digit
                result[pla] += digitA * digitB;
                result[pla + 1] += result[pla] / 10;
                result[pla] %= 10;
            }
        }
        StringBuilder res = new StringBuilder();
        for (int i = result.length - 1; i >= 0; i--) {
            res.append(result[i]);
        }
        res = new StringBuilder(res.toString().replaceFirst("^0+(?!$)", ""));
        return res.length() == 0 ? "0" : res.toString();
    }

    private String divideMagnitudes(String a, String b) {
        if (compareMagnitudes(a, b) < 0) {   // operation on int
            return "0";                       
        }
        StringBuilder quotient = new StringBuilder();
        StringBuilder dividend = new StringBuilder(a);
        StringBuilder divisor = new StringBuilder(b);

        while (dividend.length() < divisor.length()) {
            dividend.insert(0, '0');
        }
        StringBuilder current = new StringBuilder();
        for (int i = 0; i < dividend.length(); i++) {
            current.append(dividend.charAt(i));
            current = new StringBuilder(current.toString().replaceFirst("^0+(?!$)", ""));
            int q = 0;
            while (compareMagnitudes(current.toString(), divisor.toString()) >= 0) {
                current = new StringBuilder(subtractMagnitudes(current.toString(), divisor.toString()));
                q++;
            }
            quotient.append(q);
        }
        quotient = new StringBuilder(quotient.toString().replaceFirst("^0+(?!$)", ""));
        return quotient.length() == 0 ? "0" : quotient.toString();
    }

    @Override
    public String toString() {
        if (num.equals("0")) {
            return "0";
        }
        return (isNegative ? "-" : "") + num; // return as a string
    }
}