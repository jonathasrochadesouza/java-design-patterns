package com.jonathas.isp;

/**
 * Incorrect implementation of the Interface Segregation Principle (ISP) ❌
 *
 * One fat «interface» Printer bundles print + scan + fax into a single
 * contract. BasicPrinter only prints, yet it is forced to implement methods it
 * will never use - so it fakes scan()/fax() with bodies that just throw. Any
 * client holding a Printer also depends on scan/fax it may never call.
 * engineering/isp.excalidraw fixes this by splitting the contract into role
 * interfaces (see ISP_Correct).
 */
public class ISP_Incorrect {
    public static void main(String[] args) {
        // The client sees only the fat contract and calls any method it advertises...
        Printer printer = new BasicPrinter();
        printer.print("invoice.pdf");

        // ...but scan() reaches BasicPrinter and explodes at runtime. The
        // compiler never warned us, because the fat contract lied.
        printer.scan("invoice.pdf");
    }

    // FAT interface: every implementor must carry all three capabilities.
    interface Printer {
        void print(String document);

        void scan(String document);

        void fax(String document);
    }

    // Violation: BasicPrinter only prints, but is forced to implement scan()
    // and fax() - methods it will never use - just to satisfy the fat contract.
    static class BasicPrinter implements Printer {
        @Override
        public void print(String document) {
            System.out.println("Printing " + document + " (simulation)");
        }

        @Override
        public void scan(String document) {
            throw new UnsupportedOperationException("BasicPrinter cannot scan");
        }

        @Override
        public void fax(String document) {
            throw new UnsupportedOperationException("BasicPrinter cannot fax");
        }
    }
}
