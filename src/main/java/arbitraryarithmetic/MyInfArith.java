package arbitraryarithmetic;

public class MyInfArith {
    public static void main(String[] args) {
        if (args.length != 4) {
            System.err.println("Require four arguements.");
            System.exit(1);
        }

        String type = args[0].toLowerCase();
        String operation = args[1].toLowerCase();
        String num1 = args[2];
        String num2= args[3];

        try {
            String result;
            if (type.equals("int")) {
                AInteger a = AInteger.parse(num1);
                AInteger b = AInteger.parse(num2);
                switch (operation) {
                    case "add":
                        result = a.add(b).toString();
                        break;
                    case "sub":
                        result = a.subtract(b).toString();
                        break;
                    case "mul":
                        result = a.multiply(b).toString();
                        break;
                    case "div":
                        result = a.divide(b).toString();
                        break;
                    default:
                        System.err.println("Invalid operation");
                        System.exit(1);
                        return;
                }
            } else if (type.equals("float")) {
                AFloat a = AFloat.parse(num1);
                AFloat b = AFloat.parse(num2);
                switch (operation) {
                    case "add":
                        result = a.add(b).toString();
                        break;
                    case "sub":
                        result = a.subtract(b).toString();
                        break;
                    case "mul":
                        result = a.multiply(b).toString();
                        break;
                    case "div":
                        result = a.divide(b).toString();
                        break;
                    default:
                        System.err.println("Invalid operation");
                        System.exit(1);
                        return;
                }
            } else {
                System.err.println("Invalid type");
                System.exit(1);
                return;
            }
            System.out.println(result);
         }finally {
            System.out.println("");
        }
    }
}