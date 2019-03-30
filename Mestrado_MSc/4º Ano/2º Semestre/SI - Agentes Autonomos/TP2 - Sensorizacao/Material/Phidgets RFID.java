/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package irfdreader;

import com.phidgets.*;
import com.phidgets.event.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fabio
 */
public class Main {

    static RFIDPhidget ik;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            ik = new RFIDPhidget();

            //listener para quando o rfid e conectado ao computador...
            ik.addAttachListener(new AttachListener() {

                public void attached(AttachEvent ae) {
                    try {
                        ((RFIDPhidget) ae.getSource()).setAntennaOn(true);
                        System.out.println("Attached!");
                    } catch (PhidgetException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });

            //listener para quando o rfid e desconectado do computador...
            ik.addDetachListener(new DetachListener() {
                public void detached(DetachEvent de) {
                        System.out.println("Detached!" +de.getSource());
                }
            });

            ik.addTagGainListener(new TagGainListener() {

                public void tagGained(TagGainEvent oe) {
                    try {

                        ((RFIDPhidget) oe.getSource()).setLEDOn(true);
                        System.out.println("Tag"+oe.getValue());

                    } catch (PhidgetException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            ik.addTagLossListener(new TagLossListener() {

                public void tagLost(TagLossEvent oe) {
                    try {
                        ((RFIDPhidget) oe.getSource()).setLEDOn(false);
                        System.out.println(oe.getValue());
                    } catch (PhidgetException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });

            ik.addOutputChangeListener(new OutputChangeListener() {

                public void outputChanged(OutputChangeEvent oe) {
                    System.out.println(oe);
                }
            });

            ik.addErrorListener(new ErrorListener() {

                public void error(ErrorEvent ee) {
                    System.out.println("error event for " + ee);
                }
            });

            ik.openAny();
            ik.waitForAttachment();
            
            try {
                System.in.read();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

            System.out.println("Done !");
            ik.close();
            ik = null;
        } catch (PhidgetException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
