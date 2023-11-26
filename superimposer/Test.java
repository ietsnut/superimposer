package superimposer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Test extends JFrame {
    private final ArrayList<DraggableImage> images;

    public Test(ArrayList<BufferedImage> bufferedImages) {
        super("Image Dragging Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        images = new ArrayList<>();
        for (BufferedImage image : bufferedImages) {
            DraggableImage draggableImage = new DraggableImage(image);
            images.add(draggableImage);
        }

        add(new ImagePanel());

        setVisible(true);
    }

    private class ImagePanel extends JPanel {
        public ImagePanel() {
            setLayout(null); // Using null layout for manual positioning of images

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    for (DraggableImage image : images) {
                        if (image.contains(e.getPoint())) {
                            image.setDragging(true);
                            image.setLastPosition(e.getPoint());
                        }
                    }
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    for (DraggableImage image : images) {
                        image.setDragging(false);
                    }
                    repaint();
                }
            });

            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    for (DraggableImage image : images) {
                        if (image.isDragging()) {
                            Point currentPoint = image.getLocation();
                            Point newPoint = new Point(currentPoint.x + e.getX() - image.getLastPosition().x,
                                    currentPoint.y + e.getY() - image.getLastPosition().y);

                            // Check if the new position will cause overlap with other images
                            boolean canMove = true;
                            for (DraggableImage otherImage : images) {
                                if (otherImage != image && otherImage.getBounds().intersects(new Rectangle(newPoint, image.getSize()))) {
                                    canMove = false;
                                    break;
                                }
                            }

                            if (canMove) {
                                image.setLocation(newPoint);
                                image.setLastPosition(e.getPoint());
                            }
                        }
                    }
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (DraggableImage image : images) {
                g.drawImage(image.getImage(), image.getX(), image.getY(), this);
            }
        }
    }

    private class DraggableImage {
        private BufferedImage image;
        private Point location;
        private boolean isDragging;
        private Point lastPosition;

        public DraggableImage(BufferedImage image) {
            this.image = image;
            this.location = new Point(50, 50); // Initial position
            this.isDragging = false;
            this.lastPosition = new Point();
        }

        public BufferedImage getImage() {
            return image;
        }

        public int getX() {
            return location.x;
        }

        public int getY() {
            return location.y;
        }

        public Point getLocation() {
            return location;
        }

        public void setLocation(Point location) {
            this.location = location;
        }

        public boolean isDragging() {
            return isDragging;
        }

        public void setDragging(boolean dragging) {
            isDragging = dragging;
        }

        public Point getLastPosition() {
            return lastPosition;
        }

        public void setLastPosition(Point lastPosition) {
            this.lastPosition = lastPosition;
        }

        public Rectangle getBounds() {
            return new Rectangle(location, new Dimension(image.getWidth(), image.getHeight()));
        }

        public boolean contains(Point point) {
            return getBounds().contains(point);
        }

        public Dimension getSize() {
            return new Dimension(image.getWidth(), image.getHeight());
        }
    }
}