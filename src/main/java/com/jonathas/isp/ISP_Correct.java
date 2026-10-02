package com.jonathas.isp;

/**
 * Correct implementation of the Interface Segregation Principle (ISP) ✅
 *
 * Matches «interface» Printer / Scanner / FaxMachine in
 * engineering/isp.excalidraw: each capability lives in its own small role
 * interface, so BasicPrinter implements only Printer while MultifunctionPrinter
 * implements all three. No class is forced to depend on methods it does not
 * use - and there is no empty or throwing stub anywhere (contrast ISP_Incorrect).
 */
public class ISP_Correct {
    public static void main(String[] args) {
        // A print-only client depends ONLY on Printer: both devices are
        // substitutable here, and scan/fax never leak into this client.
        printDemo(new BasicPrinter());
        printDemo(new MultifunctionPrinter());

        // Every capability is consumed through its own role interface, so each
        // client holds exactly the methods it uses - nothing more.
        Printer printer = new MultifunctionPrinter();
        Scanner scanner = new MultifunctionPrinter();
        FaxMachine faxMachine = new MultifunctionPrinter();

        printer.print("monthly-report.pdf");
        scanner.scan("signed-contract.pdf");
        faxMachine.fax("purchase-order.pdf");
    }

    private static void printDemo(Printer printer) {
        printer.print("invoice.pdf");
    }
}

// One capability per role interface («interface» Printer in the diagram):
// clients that only print depend on exactly one method.
interface Printer {
    void print(String document);
}

// Separate role («interface» Scanner in the diagram).
interface Scanner {
    void scan(String document);
}

// Separate role («interface» FaxMachine in the diagram).
interface FaxMachine {
    void fax(String document);
}

// Implementation from the diagram: prints only, so implements only Printer.
// No scan/fax stubs to fake or maintain.
class BasicPrinter implements Printer {
    @Override
    public void print(String document) {
        System.out.println("Printing " + document + " (simulation)");
    }
}

// Implementation from the diagram: the device that really does all three,
// implementing every role interface it supports.
class MultifunctionPrinter implements Printer, Scanner, FaxMachine {
    @Override
    public void print(String document) {
        System.out.println("Printing " + document + " (simulation)");
    }

    @Override
    public void scan(String document) {
        System.out.println("Scanning " + document + " (simulation)");
    }

    @Override
    public void fax(String document) {
        System.out.println("Faxing " + document + " (simulation)");
    }
}
