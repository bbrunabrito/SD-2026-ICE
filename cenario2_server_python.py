"""
Cenario 2: Servidor Python recebendo conexoes do Cliente Java
Executar este servidor: python cenario2_server_python.py
Depois executar o cliente Java: cd java && gradle runClient
"""
import sys
import Ice
import Demo


class PrinterI(Demo.Printer):
    def printString(self, s, current=None):
        print(f"[Python Server] printString: {s}")
        return s + "*"

    def toUpperCase(self, s, current=None):
        result = s.upper()
        print(f"[Python Server] toUpperCase: {result}")
        return result

    def concat(self, a, b, current=None):
        result = a + b
        print(f"[Python Server] concat: {result}")
        return result


communicator = Ice.initialize(sys.argv)

adapter = communicator.createObjectAdapterWithEndpoints(
    "SimpleAdapter", "default -p 5678")
adapter.add(PrinterI(), Ice.Identity("SimplePrinter"))
adapter.activate()

print("[Python Server] Aguardando conexoes na porta 5678...")
communicator.waitForShutdown()
