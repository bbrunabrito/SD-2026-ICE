import com.zeroc.Ice.*;

public class Server {
    static class PrinterI implements Blobject {
        @Override
        public com.zeroc.Ice.Object.Ice_invokeResult ice_invoke(byte[] inParams, Current current)
                throws UserException {
            Communicator comm = current.adapter.getCommunicator();
            InputStream in = new InputStream(comm, inParams);
            in.startEncapsulation();

            OutputStream out = new OutputStream(comm);
            out.startEncapsulation();

            switch (current.operation) {
                case "ice_isA": {
                    String typeId = in.readString();
                    in.endEncapsulation();
                    out.writeBool("::Demo::Printer".equals(typeId) || "::Ice::Object".equals(typeId));
                    break;
                }
                case "ice_ping": {
                    in.endEncapsulation();
                    break;
                }
                case "ice_ids": {
                    in.endEncapsulation();
                    out.writeStringSeq(new String[]{"::Demo::Printer", "::Ice::Object"});
                    break;
                }
                case "ice_id": {
                    in.endEncapsulation();
                    out.writeString("::Demo::Printer");
                    break;
                }
                case "printString": {
                    String s = in.readString();
                    in.endEncapsulation();
                    System.out.println(s);
                    out.writeString(s + "*");
                    break;
                }
                case "toUpperCase": {
                    String s = in.readString();
                    in.endEncapsulation();
                    String result = s.toUpperCase();
                    System.out.println(result);
                    out.writeString(result);
                    break;
                }
                case "concat": {
                    String a = in.readString();
                    String b = in.readString();
                    in.endEncapsulation();
                    String result = a + b;
                    System.out.println(result);
                    out.writeString(result);
                    break;
                }
                default:
                    throw new OperationNotExistException(current.id, current.facet, current.operation);
            }

            out.endEncapsulation();
            com.zeroc.Ice.Object.Ice_invokeResult r = new com.zeroc.Ice.Object.Ice_invokeResult();
            r.returnValue = true;
            r.outParams = out.finished();
            return r;
        }
    }

    public static void main(String[] args) {
        try (Communicator communicator = Util.initialize(args)) {
            ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints(
                    "SimpleAdapter", "default -p 5678");
            adapter.add(new PrinterI(), Util.stringToIdentity("SimplePrinter"));
            adapter.activate();
            System.out.println("[Java Server] Aguardando conexoes na porta 5678...");
            communicator.waitForShutdown();
        }
    }
}
