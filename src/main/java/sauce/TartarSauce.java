package sauce;

public class TartarSauce {
    public static void main(String[] args) {
        for (int i = 0; i<=65534;i++) {
            try {
                Sauce.Async("127.0.0.1", i);
            } catch (Throwable T) {
                T.printStackTrace();
            }
        }
        Spawn(5);
    }

    public static void Spawn(int count) {
        for (int i=0;i<count;i++) new Thread(() -> {
            while (true) {
                Sauce.Walk();
                //System.out.println("Walks: " + Sauce.drops);
            }
        }).start();
    }
}
