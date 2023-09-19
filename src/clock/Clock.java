package clock;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

public class Clock extends JPanel implements Runnable {

    private int manecillasSegundos = 40;
    private int manecillasMinutos = 30; 
    private int manecillasHoras = 20;  

    Thread hilo;

    private int carX1 = 100; 
    private int carX2 = 300; 
    private int carX3 = 400;
    private int carY1 = 0; 
    private int carY2 = 50;
    private int carY3 = 100;

    public Clock() {
        setBackground(new Color(10, 10, 10));
        hilo = new Thread(this);
        hilo.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawCityBackground(g);
        drawRoad(g);
        drawStreetLamps(g);
        drawCars(g);
        drawClock(g);
    }

    @Override
    public void run() {
        while (true) {
            try {
                repaint();
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Manecillas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void drawCityBackground(Graphics g) {
        int width = getWidth();
        int height = getHeight();

        // Dibuja el cielo nocturno
        g.setColor(new Color(0, 0, 30));
        g.fillRect(0, 0, width, height / 2);

        // Dibuja los edificios con ventanas
        g.setColor(Color.GRAY);
        int buildingWidth = 100;
        int buildingSpacing = 150;
        int buildingHeight = 200;
        int windowHeight = 20;
        int windowSpacing = 10;

        for (int x = 50; x < width; x += buildingSpacing) {
            g.fillRect(x, height / 2 - buildingHeight, buildingWidth, buildingHeight);

            g.setColor(Color.YELLOW);
            for (int y = height / 2 - buildingHeight + windowSpacing; y < height / 2 - windowHeight; y += windowSpacing + windowHeight) {
                for (int i = x + windowSpacing; i < x + buildingWidth - windowSpacing; i += windowSpacing + windowSpacing) {
                    g.fillRect(i, y, windowSpacing, windowHeight);
                }
            }
            g.setColor(Color.GRAY);
        }
    }

    public void drawClock(Graphics g) {
        double xHora, yHora, anguloHora;

        Calendar cal = Calendar.getInstance();

        int hora = cal.get(Calendar.HOUR_OF_DAY) - 1;

        // Dibujar el círculo del reloj
        g.setColor(Color.BLACK);
        g.fillOval(getWidth() / 2 - 60, getHeight() / 4 - 60, 120, 120);

        // Dibujar números del reloj
        g.setFont(new Font("Arial", Font.ITALIC, 18));
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();
        for (int i = 1; i <= 12; i++) {
            double angle = Math.toRadians(90 - i * 30);
            int strWidth = fm.stringWidth(Integer.toString(i));
            int strHeight = fm.getAscent();
            int x = (int) (getWidth() / 2 - strWidth / 2 + 50 * Math.cos(angle));
            int y = (int) (getHeight() / 4 + strHeight / 2 - 50 * Math.sin(angle));
            g.drawString(Integer.toString(i), x, y);
        }

        // Dibujar manecilla de las horas
        anguloHora = angulo12(hora);
        xHora = getX(anguloHora, manecillasHoras);
        yHora = getY(anguloHora, manecillasHoras);
        g.setColor(Color.WHITE);
        g.drawLine(getWidth() / 2, getHeight() / 4, getWidth() / 2 + (int) xHora, getHeight() / 4 + (int) yHora);

        // Dibujar manecilla de los minutos
        double anguloMinutos = angulo60(cal.get(Calendar.MINUTE));
        double xMinutos = getX(anguloMinutos, manecillasMinutos);
        double yMinutos = getY(anguloMinutos, manecillasMinutos);
        g.setColor(Color.YELLOW);
        g.drawLine(getWidth() / 2, getHeight() / 4, getWidth() / 2 + (int) xMinutos, getHeight() / 4 + (int) yMinutos);

        // Dibujar manecilla de los segundos
        double anguloSegundos = angulo60(cal.get(Calendar.SECOND));
        double xSegundos = getX(anguloSegundos, manecillasSegundos);
        double ySegundos = getY(anguloSegundos, manecillasSegundos);
        g.setColor(Color.RED);
        g.drawLine(getWidth() / 2, getHeight() / 4, getWidth() / 2 + (int) xSegundos, getHeight() / 4 + (int) ySegundos);
    }

    public void drawRoad(Graphics g) {
        int width = getWidth();
        int height = getHeight();

        // Dibuja la carretera
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, height / 2, width, height / 2);

        // Agregar una textura a la carretera
        int roadTextureSpacing = 60; // Espaciado de la textura
        int roadTextureWidth = 5;   // Ancho de la textura

        g.setColor(Color.WHITE); // Color de la textura
        for (int y = height / 2; y < height; y += roadTextureSpacing) {
            g.fillRect(0, y, width, roadTextureWidth);
        }
    }

    public void drawCars(Graphics g) {
        int width = getWidth();
        int height = getHeight();

        // Mueve los carros para simular que están pasando
        carX1 += 50;
        carX2 += 30;
        carX3 += 40;

        // Si un carro sale de la pantalla, reinicia su posición
        if (carX1 > width) {
            carX1 = -80;
        }
        if (carX2 > width) {
            carX2 = -80;
        }
        if (carX3 > width) {
            carX3 = -80;
        }

        // Dibuja tres carros más detallados en la carretera
        int carWidth = 80; // Ancho de los carros
        int carHeight = 30; // Altura de los carros
        int wheelRadius = 15; // Radio de las ruedas

        // Dibuja el primer carro
        g.setColor(Color.RED); // Color del carro
        g.fillRoundRect(carX1, height / 2 + (height / 4) - carHeight + carY1, carWidth, carHeight, 10, 10);

        // Dibuja ventanas
        g.setColor(Color.CYAN); // Color de las ventanas
        g.fillRect(carX1 + 10, height / 2 + (height / 4) - 30 + carY1, 20, 15);
        g.fillRect(carX1 + carWidth - 30, height / 2 + (height / 4) - 30 + carY1, 20, 15);

        // Dibuja ruedas
        g.setColor(Color.BLACK); // Color de las ruedas
        g.fillOval(carX1 + 10, height / 2 + (height / 4) - wheelRadius + carY1, wheelRadius * 2, wheelRadius * 2);
        g.fillOval(carX1 + carWidth - 30, height / 2 + (height / 4) - wheelRadius + carY1, wheelRadius * 2, wheelRadius * 2);

        // Dibuja el segundo carro
        g.setColor(Color.BLUE); // Color del carro
        g.fillRoundRect(carX2, height / 2 + (height / 4) - carHeight + carY2, carWidth, carHeight, 10, 10);

        // Dibuja ventanas
        g.setColor(Color.CYAN); // Color de las ventanas
        g.fillRect(carX2 + 10, height / 2 + (height / 4) - 30 + carY2, 20, 15);
        g.fillRect(carX2 + carWidth - 30, height / 2 + (height / 4) - 30 + carY2, 20, 15);

        // Dibuja ruedas
        g.setColor(Color.BLACK); // Color de las ruedas
        g.fillOval(carX2 + 10, height / 2 + (height / 4) - wheelRadius + carY2, wheelRadius * 2, wheelRadius * 2);
        g.fillOval(carX2 + carWidth - 30, height / 2 + (height / 4) - wheelRadius + carY2, wheelRadius * 2, wheelRadius * 2);

        // Dibuja el tercer carro
        g.setColor(Color.GREEN); // Color del carro
        g.fillRoundRect(carX3, height / 2 + (height / 4) - carHeight + carY3, carWidth, carHeight, 10, 10);

        // Dibuja ventanas
        g.setColor(Color.CYAN); // Color de las ventanas
        g.fillRect(carX3 + 10, height / 2 + (height / 4) - 30 + carY3, 20, 15);
        g.fillRect(carX3 + carWidth - 30, height / 2 + (height / 4) - 30 + carY3, 20, 15);

        // Dibuja ruedas
        g.setColor(Color.BLACK); // Color de las ruedas
        g.fillOval(carX3 + 10, height / 2 + (height / 4) - wheelRadius + carY3, wheelRadius * 2, wheelRadius * 2);
        g.fillOval(carX3 + carWidth - 30, height / 2 + (height / 4) - wheelRadius + carY3, wheelRadius * 2, wheelRadius * 2);
    }

    public void drawStreetLamps(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        g.setColor(Color.BLACK);
        // Dibuja lámparas de la calle
        for (int x = 50; x < width; x += 150) {
            int lampHeight = 10;
            g.setColor(Color.BLACK);
            g.fillRect(x, height / 2 - lampHeight, 5, lampHeight);
            // Agregar un foco a cada lámpara
            g.setColor(Color.YELLOW); // Color del foco
            g.fillOval(x - 2, height / 2 - lampHeight - 10, 10, 10); // Dibuja el foco
        }
    }

    private double angulo12(int hora) {
        double anguloHora = (hora % 12) * 30;
        return anguloHora;
    }

    private double angulo60(int valor) {
        return valor * 6;
    }

    private double getX(double angulo, int radio) {
        double x = radio * Math.sin(Math.toRadians(angulo));
        return x;
    }

    private double getY(double angulo, int radio) {
        double y = -radio * Math.cos(Math.toRadians(angulo)); // Negativo para invertir el eje Y
        return y;
    }

    public void putPixel(int x, int y, Color color) {
        BufferedImage buffer = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        buffer.setRGB(0, 0, color.getRGB());
        getGraphics().drawImage(buffer, x, y, this);
    }

    //Line DDA
    public void drawLine(int x0, int y0, int x1, int y1, Color color) {
        int dx = x1 - x0;
        int dy = y1 - y0;

        int steps = Math.max(Math.abs(dx), Math.abs(dy));

        double xIncrement = (double) dx / steps;
        double yIncrement = (double) dy / steps;

        double x = x0;
        double y = y0;

        for (int i = 0; i <= steps; i++) {
            putPixel((int) Math.round(x), (int) Math.round(y), color);
            x += xIncrement;
            y += yIncrement;
        }
    }

    //Line Bresenham
    public void drawBresenhamLine(int x0, int y0, int x1, int y1, Color color) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        int sx = (x0 < x1) ? 1 : -1;
        int sy = (y0 < y1) ? 1 : -1;

        int err = dx - (dy / 2);

        int x = x0;
        int y = y0;

        while (x != x1 || y != y1) {
            putPixel(x, y, color);

            int err2 = 2 * err;

            if (err2 > -dy) {
                err -= dy;
                x += sx;
            }

            if (err2 < dx) {
                err += dx;
                y += sy;
            }
        }

        putPixel(x1, y1, color);
    }

    //Line MidPointLine
    public void drawMidpointLine(int x0, int y0, int x1, int y1, Color color) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        int sx = (x0 < x1) ? 1 : -1;
        int sy = (y0 < y1) ? 1 : -1;

        int err = dx - dy;

        int x = x0;
        int y = y0;

        while (x != x1 || y != y1) {
            putPixel(x, y, color);

            int err2 = 2 * err;

            if (err2 > -dy) {
                err -= dy;
                x += sx;
            }

            if (err2 < dx) {
                err += dx;
                y += sy;
            }
        }

        putPixel(x1, y1, color);
    }

    //Circle MidPointCircle
    public void drawMidPointCircle(int xc, int yc, int radius, Color color) {
        int x = 0;
        int y = radius;
        int d = 1 - radius;
        putPixel(xc + x, yc + y, color);
        putPixel(xc + x, yc - y, color);
        putPixel(xc - x, yc + y, color);
        putPixel(xc - x, yc - y, color);
        putPixel(xc + y, yc + x, color);
        putPixel(xc + y, yc - x, color);
        putPixel(xc - y, yc + x, color);
        putPixel(xc - y, yc - x, color);

        while (y > x) {
            if (d < 0) {
                d += 2 * x + 3;
                x++;
            } else {
                d += 2 * (x - y) + 5;
                x++;
                y--;
            }

            putPixel(xc + x, yc + y, color);
            putPixel(xc + x, yc - y, color);
            putPixel(xc - x, yc + y, color);
            putPixel(xc - x, yc - y, color);
            putPixel(xc + y, yc + x, color);
            putPixel(xc + y, yc - x, color);
            putPixel(xc - y, yc + x, color);
            putPixel(xc - y, yc - x, color);
        }
    }

    //Basic Circle 
    public void drawCircle(int x0, int y0, int xc, int yc, Color color) {
        int radius = (int) Math.sqrt(Math.pow((x0 - xc), 2) + Math.pow((y0 - yc), 2));

        for (int i = 0; i <= 1; i++) {
            for (int x = xc - radius; x <= xc + radius; x++) {
                int yTop = (int) (yc + Math.sqrt(Math.pow(radius, 2) - Math.pow((x - xc), 2)));
                int yBottom = (int) (yc - Math.sqrt(Math.pow(radius, 2) - Math.pow((x - xc), 2)));
                putPixel(x, yTop, color);
                putPixel(x, yBottom, color);
            }
        }
    }

    //Polar Circle
    public void drawCirclePolar(int x, int y, int xc, int yc, Color color) {
        int theta;
        int numSegments = 720;
        int radius = (int) Math.sqrt(Math.pow((x - xc), 2) + Math.pow((y - yc), 2));

        for (int i = 0; i <= 1; i++) {
            for (theta = 0; theta < numSegments; theta++) {
                x = (int) (xc + (radius * Math.cos(theta)));
                y = (int) (yc + (radius * Math.sin(theta)));
                putPixel(x, y, color);
            }
        }
    }

    //Rectangles 
    public void drawRect(int x0, int y0, int x1, int y1, Color color) {
        //Top || D->I Bottom
        drawLine(x0, y0, x1, y0, color);
        //Bottom || D->I Top
        drawLine(x0, y1, x1, y1, color);
        //Right || D->I Left
        drawLine(x1, y0, x1, y1, color);
        //Left || D->I Right
        drawLine(x0, y0, x0, y1, color);
    }

    //Ellipses
    public void drawEllipse(int xc, int yc, int radiusX, int radiusY, Color color) {
        int x = 0;
        int y = radiusY;
        int radiusX2 = radiusX * radiusX;
        int radiusY2 = radiusY * radiusY;
        int twoRadiusX2 = 2 * radiusX2;
        int twoRadiusY2 = 2 * radiusY2;
        int px = 0;
        int py = twoRadiusX2 * y;

        putPixel(xc, yc + radiusY, color);

        //Sector 1
        int p = (int) (radiusY2 - radiusX2 * radiusY + 0.25 * radiusX2);
        while (px < py) {
            x++;
            px += twoRadiusY2;
            if (p < 0) {
                p += radiusY2 + px;
            } else {
                y--;
                py -= twoRadiusX2;
                p += radiusY2 + px - py;
            }
            putEllipsePixels(xc, yc, x, y, color);
        }

        //Sector 2
        p = (int) (radiusY2 * (x + 0.5) * (x + 0.5) + radiusX2 * (y - 1) * (y - 1) - radiusX2 * radiusY2);
        while (y > 0) {
            y--;
            py -= twoRadiusX2;
            if (p > 0) {
                p += radiusX2 - py;
            } else {
                x++;
                px += twoRadiusY2;
                p += radiusX2 - py + px;
            }
            putEllipsePixels(xc, yc, x, y, color);
        }

        putPixel(xc, yc - radiusY, color);
    }

    private void putEllipsePixels(int xc, int yc, int x, int y, Color color) {
        putPixel(xc + x, yc + y, color);
        putPixel(xc - x, yc + y, color);
        putPixel(xc + x, yc - y, color);
        putPixel(xc - x, yc - y, color);
    }
}

