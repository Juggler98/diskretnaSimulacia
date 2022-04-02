package charts;

import org.jfree.chart.ui.ApplicationFrame;

import java.awt.event.WindowEvent;

public class MyApplicationFrame extends ApplicationFrame {

    public MyApplicationFrame(String title) {
        super(title);
    }

    public void windowClosing(WindowEvent event) {
        if (event.getWindow() == this) {
            this.dispose();
            //System.exit(0);
        }
    }

}
