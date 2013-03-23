import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class PopUp {

  public static void main(String args[]) {
    ActionListener actionListener = new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        System.out.println("Selected: "
            + actionEvent.getActionCommand());
      }
    };

    PopupMenuListener popupMenuListener = new PopupMenuListener() {
      public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {
        System.out.println("Canceled");
      }

      public void popupMenuWillBecomeInvisible(
          PopupMenuEvent popupMenuEvent) {
        System.out.println("Becoming Invisible");
      }

      public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
        System.out.println("Becoming Visible");
      }
    };

    JFrame frame = new JFrame("Popup Example");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPopupMenu popupMenu = new JPopupMenu();
    popupMenu.addPopupMenuListener(popupMenuListener);

    JMenuItem cutMenuItem = new JMenuItem("Cut");
    cutMenuItem.addActionListener(actionListener);
    popupMenu.add(cutMenuItem);

    JMenuItem copyMenuItem = new JMenuItem("Copy");
    copyMenuItem.addActionListener(actionListener);
    popupMenu.add(copyMenuItem);

    JMenuItem pasteMenuItem = new JMenuItem("Paste");
    pasteMenuItem.addActionListener(actionListener);
    pasteMenuItem.setEnabled(false);
    popupMenu.add(pasteMenuItem);

    popupMenu.addSeparator();

    JMenuItem findMenuItem = new JMenuItem("Find");
    findMenuItem.addActionListener(actionListener);
    popupMenu.add(findMenuItem);

    MouseListener mouseListener = new JPopupMenuShower(popupMenu);
    frame.addMouseListener(mouseListener);

    frame.setSize(350, 250);
    frame.setVisible(true);
  }
}

class JPopupMenuShower extends MouseAdapter {

  private JPopupMenu popup;

  public JPopupMenuShower(JPopupMenu popup) {
    this.popup = popup;
  }

  private void showIfPopupTrigger(MouseEvent mouseEvent) {
    if (popup.isPopupTrigger(mouseEvent)) {
      popup.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent
          .getY());
    }
  }

  public void mousePressed(MouseEvent mouseEvent) {
    showIfPopupTrigger(mouseEvent);
  }

  public void mouseReleased(MouseEvent mouseEvent) {
    showIfPopupTrigger(mouseEvent);
  }
}