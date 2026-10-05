import com.zeroc.Ice.*;

public class Client {

    static String invoke(Communicator comm, ObjectPrx proxy, String operation, String... params) {
        OutputStream out = new OutputStream(comm);
        out.startEncapsulation();
        for (String p : params) {
            out.writeString(p);
        }
        out.endEncapsulation();

        com.zeroc.Ice.Object.Ice_invokeResult result =
                proxy.ice_invoke(operation, OperationMode.Normal, out.finished());

        if (!result.returnValue) {
            throw new RuntimeException("Invocacao falhou: " + operation);
        }

        InputStream in = new InputStream(comm, result.outParams);
        in.startEncapsulation();
        String ret = in.readString();
        in.endEncapsulation();
        return ret;
    }

    public static void main(String[] args) {
        try (Communicator communicator = Util.initialize(args)) {
            ObjectPrx proxy = communicator.stringToProxy(
                    "SimplePrinter:tcp -h localhost -p 5678");

            System.out.println("[Java Client] Conectado ao servidor na porta 5678");

            String r1 = invoke(communicator, proxy, "printString", "Hello World from Java Client!");
            System.out.println("printString retornou: " + r1);

            String r2 = invoke(communicator, proxy, "toUpperCase", "hello ice from java");
            System.out.println("toUpperCase retornou: " + r2);

            String r3 = invoke(communicator, proxy, "concat", "Java + ", "Python!");
            System.out.println("concat retornou: " + r3);
        }
    }
}
