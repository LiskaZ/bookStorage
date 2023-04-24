package components.starrating;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

public class StarRating extends javax.swing.JPanel {

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
        if (star == 1) {
            star1ActionPerformed(null);
        } else if (star == 2) {
            star2ActionPerformed(null);
        } else if (star == 3) {
            star3ActionPerformed(null);
        } else if (star == 4) {
            star4ActionPerformed(null);
        } else {
            star5ActionPerformed(null);
        }
    }

    private List<EventStarRating> events = new ArrayList<>();
    private int star;

    private Star star1;
    private Star star2;
    private Star star3;
    private Star star4;
    private Star star5;

    public void starsReset() {
        star1.setSelected(false);
        star2.setSelected(false);
        star3.setSelected(false);
        star4.setSelected(false);
        star5.setSelected(false);
        star = 0;
        runEvent();
    }

    public StarRating() {
        initComponents();
        init();
    }

    private void init() {
        setOpaque(false);
        setBackground(new Color(204, 204, 204));
        setForeground(new Color(238, 236, 0));
    }

    private void initComponents() {

        star1 = new Star();
        star2 = new Star();
        star3 = new Star();
        star4 = new Star();
        star5 = new Star();

        setLayout(new java.awt.GridLayout(1, 5));

        star1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                star1ActionPerformed(evt);
            }
        });
        add(star1);

        star2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                star2ActionPerformed(evt);
            }
        });
        add(star2);

        star3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                star3ActionPerformed(evt);
            }
        });
        add(star3);

        star4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                star4ActionPerformed(evt);
            }
        });
        add(star4);

        star5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                star5ActionPerformed(evt);
            }
        });
        add(star5);
    }

    private void star1ActionPerformed(java.awt.event.ActionEvent evt) {
        star1.setSelected(true);
        star2.setSelected(false);
        star3.setSelected(false);
        star4.setSelected(false);
        star5.setSelected(false);
        star = 1;
        runEvent();
    }

    private void star2ActionPerformed(java.awt.event.ActionEvent evt) {
        star1.setSelected(true);
        star2.setSelected(true);
        star3.setSelected(false);
        star4.setSelected(false);
        star5.setSelected(false);
        star = 2;
        runEvent();
    }

    private void star3ActionPerformed(java.awt.event.ActionEvent evt) {
        star1.setSelected(true);
        star2.setSelected(true);
        star3.setSelected(true);
        star4.setSelected(false);
        star5.setSelected(false);
        star = 3;
        runEvent();
    }

    private void star4ActionPerformed(java.awt.event.ActionEvent evt) {
        star1.setSelected(true);
        star2.setSelected(true);
        star3.setSelected(true);
        star4.setSelected(true);
        star5.setSelected(false);
        star = 4;
        runEvent();
    }

    private void star5ActionPerformed(java.awt.event.ActionEvent evt) {
        star1.setSelected(true);
        star2.setSelected(true);
        star3.setSelected(true);
        star4.setSelected(true);
        star5.setSelected(true);
        star = 5;
        runEvent();
    }

    @Override
    public void setBackground(Color color) {
        super.setBackground(color);
        for (Component com : getComponents()) {
            com.setBackground(color);
        }
    }

    @Override
    public void setForeground(Color color) {
        super.setForeground(color);
        for (Component com : getComponents()) {
            com.setForeground(color);
        }
    }

    public void addEventStarRating(EventStarRating event) {
        events.add(event);
    }

    private void runEvent() {
        for (EventStarRating event : events) {
            event.selected(star);
        }
    }
}