package pippin;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.function.Consumer;

public class WindowListenerFactory {
	static WindowListener windowClosingFactory(Consumer<WindowEvent> c) {
        return new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                c.accept(e);
            }
        };
    }
}
