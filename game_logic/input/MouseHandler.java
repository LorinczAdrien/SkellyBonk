package game_logic.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandler implements MouseListener {
    private boolean leftMousePressed = false;

    @Override
    public void mouseClicked(MouseEvent event) {
        int mouseCode = event.getButton();
        if(mouseCode == MouseEvent.BUTTON1) {
            this.leftMousePressed = true;
        }
    }

    @Override
    public void mousePressed(MouseEvent event) {
        int mouseCode = event.getButton();
        if(mouseCode == MouseEvent.BUTTON1) {
            this.leftMousePressed = true;
        }
    }

    /* Getters */
    public boolean isLeftMousePressed() { return this.leftMousePressed; }

    /* Setters */
    public void setLeftMousePressed(boolean pressed) { this.leftMousePressed = pressed; }

    @Override
    public void mouseReleased(MouseEvent event) {

    }

    @Override
    public void mouseEntered(MouseEvent event) {

    }

    @Override
    public void mouseExited(MouseEvent event) {

    }
}
