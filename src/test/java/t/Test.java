package t;

public class Test {
    public static void main(String[] args) {
        // () -> 实例化了TestLambda
        Test test = new Test(() -> {
            // 因为TestLambda只有一个String的非default方法
            // 所以会被直接应用, 如果有多个则会报错
            return "testing";
        });

        test.say(() -> {
            return "say hi";
        });
    }

    public Test(TestLambda testing) {
        System.out.println(testing.getString());
    }

    public void say(TestLambda say) {
        System.out.println("say: " + say.getString());
    }
}

interface TestLambda {
    String getString();
}

