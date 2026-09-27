import sys, Ice
import Demo
 
communicator = Ice.initialize(sys.argv)

base1 = communicator.stringToProxy("SimplePrinter1:tcp -h 34.203.80.210 -p 5678")
base2 = communicator.stringToProxy("SimplePrinter2:tcp -h 34.203.80.210 -p 5678")
printer1 = Demo.PrinterPrx.checkedCast(base1)
printer2 = Demo.PrinterPrx.checkedCast(base2)
if (not printer1) or (not printer2):
    raise RuntimeError("Invalid proxy")

rep = printer1.printString("Hello World from printer1!")
print(rep)
rep = printer2.printString("Hello World from printer2!")
print(rep)

print(printer1.toUpperCase("hello from printer1"))
print(printer2.toUpperCase("hello from printer2"))

print(printer1.concat("printer1: ", "concatenated!"))
print(printer2.concat("printer2: ", "concatenated!"))

communicator.waitForShutdown()
