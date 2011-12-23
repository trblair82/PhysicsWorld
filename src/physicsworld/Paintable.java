/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package physicsworld;

/**
 *
 * @author trblair
 */
import java.awt.*;

public interface Paintable {//paintable interface containing paint function, overriden in all classes that require rendering
    public void paint(Graphics2D g);
    
}
