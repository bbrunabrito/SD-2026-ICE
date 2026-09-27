import sys, Ice
import Demo
 
class PrinterI(Demo.Printer):
    def printString(self, s, current=None):
        print(s)
        return s + "*"

    def toUpperCase(self, s, current=None):
        result = s.upper()
        print(result)
        return result

    def concat(self, a, b, current=None):
        result = a + b
        print(result)
        return result

communicator = Ice.initialize(sys.argv) 

adapter = communicator.createObjectAdapterWithEndpoints("SimpleAdapter", "default -p 5678")
object = PrinterI()
adapter.add(object, Ice.Identity("SimplePrinter"))
adapter.activate()

communicator.waitForShutdown()
