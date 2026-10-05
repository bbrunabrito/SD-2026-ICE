"""
Cenario 1: Cliente Python conectando ao Servidor Java
Executar o servidor Java primeiro: cd java && gradle runServer
Depois executar este cliente: python cenario1_client_python.py
"""
import sys
import Ice
import Demo

communicator = Ice.initialize(sys.argv)

base = communicator.stringToProxy("SimplePrinter:tcp -h localhost -p 5678")
printer = Demo.PrinterPrx.checkedCast(base)
if not printer:
    raise RuntimeError("Proxy invalido")

print("[Python Client -> Java Server]")

r1 = printer.printString("Hello World from Python Client!")
print(f"printString retornou: {r1}")

r2 = printer.toUpperCase("hello ice from python")
print(f"toUpperCase retornou: {r2}")

r3 = printer.concat("Python + ", "Java!")
print(f"concat retornou: {r3}")

communicator.destroy()
