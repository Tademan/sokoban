/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kissa.peli;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JFrame;

/**
 *
 * @author taavi
 */
public class MapEditor extends Canvas {

    static int kartanNumero = 0;
    private static File MAP_FILE = new File("kartta" + Integer.toString(kartanNumero) + ".dat");

    Map map;
    Ukkeli pekka;
    int boxsit;
    static int kerroin = 30;
    Map.NodeType type = Map.NodeType.AIR;
    boolean suti = false;
    boolean save = true;
    boolean uudestaan = false;
    boolean tiedot = true;
    private static Font font = new Font("SanSerif", Font.PLAIN, 12);

    private void init() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                switch (ke.getKeyCode()) {
                    case KeyEvent.VK_DOWN:
                        if (suti) {
                            map.setNode(pekka.getX(), pekka.getY(), type);
                        }
                        movePlayer(0, 1);
                        break;
                    case KeyEvent.VK_UP:
                        if (suti) {
                            map.setNode(pekka.getX(), pekka.getY(), type);
                        }
                        movePlayer(0, -1);
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (suti) {
                            map.setNode(pekka.getX(), pekka.getY(), type);
                        }
                        movePlayer(1, 0);
                        break;
                    case KeyEvent.VK_LEFT:
                        if (suti) {
                            map.setNode(pekka.getX(), pekka.getY(), type);
                        }
                        movePlayer(-1, 0);
                        break;
                    case KeyEvent.VK_0:
                        type = Map.NodeType.AIR;
                        if (!(suti)) {
                            map.setNode(pekka.getX(), pekka.getY(), Map.NodeType.AIR);
                        }
                        break;
                    case KeyEvent.VK_5:
                        type = Map.NodeType.AIR;
                        if (!(suti)) {
                            map.setNode(pekka.getX(), pekka.getY(), Map.NodeType.AIR);
                        }
                        break;
                    case KeyEvent.VK_1:
                        type = Map.NodeType.WALL;
                        if (!(suti)) {
                            map.setNode(pekka.getX(), pekka.getY(), Map.NodeType.WALL);
                        }
                        break;
                    case KeyEvent.VK_2:
                        type = Map.NodeType.BOX;
                        if (!(suti)) {
                            map.setNode(pekka.getX(), pekka.getY(), Map.NodeType.BOX);
                        }
                        break;
                    case KeyEvent.VK_3:
                        type = Map.NodeType.TARGET;
                        if (!(suti)) {
                            map.setNode(pekka.getX(), pekka.getY(), Map.NodeType.TARGET);
                        }
                        break;
                    case KeyEvent.VK_4:
                        type = Map.NodeType.BOXINTHETARGET;
                        if (!(suti)) {
                            map.setNode(pekka.getX(), pekka.getY(), Map.NodeType.BOXINTHETARGET);
                        }
                        break;
                    case KeyEvent.VK_S:
                        save = false;
                        break;
                    case KeyEvent.VK_P:
                        suti = !suti;
                        break;
                    case KeyEvent.VK_D:
                        kartanNumero++;
                        uudestaan = true;
                        break;
                    case KeyEvent.VK_A:
                        kartanNumero--;
                        uudestaan = true;
                        break;
                    case KeyEvent.VK_T:
                        tiedot = !tiedot;

                        break;

                }
                tick();
            }

        });

    }

    private void tick() {
        repaint();
    }

    private void movePlayer(int x, int y) {
        if (map.isWithinMap(pekka.getX() + x, pekka.getY() + y)) {

            Map.NodeType type = map.getNode(pekka.getX() + x, pekka.getY() + y);

            pekka.move(x, y);
            map.setPlayerX(pekka.getX());
            map.setPlayerY(pekka.getY());

        }

    }

    @Override
    public void paint(Graphics g) {
        update();
        render(g);

    }

    private void update() {

        if (uudestaan) {

            try (FileInputStream i = new FileInputStream(new File("kartta" + Integer.toString(kartanNumero) + ".dat"))) {
                map = Map.readFromStream(i);
                pekka.setX(map.getPlayerX());
                pekka.setY(map.getPlayerY());

            } catch (IOException e) {
                /*
                e.printStackTrace();
                System.exit(1);
                 */
                kartanNumero = 0;
                update();
            }

            uudestaan = false;

        }
        if (save == false) {
            try (OutputStream i = new FileOutputStream(new File("kartta" + Integer.toString(kartanNumero) + ".dat"))) {
                map.writeToStream(i);
                System.out.println("t");
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

            map.setVoittoPisteet(0);
            boxsit = 0;
            for (int i = 0; i < map.getHeight(); i++) {
                for (int j = 0; j < map.getWidth(); j++) {
                    if (map.getNode(i, j) == Map.NodeType.TARGET || map.getNode(i, j) == Map.NodeType.BOXINTHETARGET) {
                        map.setVoittoPisteet(map.getVoittoPisteet() + 1);

                    }
                    if (map.getNode(i, j) == Map.NodeType.BOX || map.getNode(i, j) == Map.NodeType.BOXINTHETARGET) {
                        boxsit++;

                    }

                }
            }
        }

    }

    private void render(Graphics g) {
        g.setColor(Color.black);
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                if (map.getNode(i, j) == Map.NodeType.WALL) {
                    if (save) {
                        g.setColor(Color.black);
                    } else {
                        g.setColor(Color.darkGray);
                    }

                    g.fillRect(i * kerroin, j * kerroin, kerroin, kerroin);

                } else if (map.getNode(i, j) == Map.NodeType.BOX) {
                    if (save) {
                        g.setColor(Color.blue);
                    } else {
                        g.setColor(Color.cyan);
                    }
                    g.fillRect(i * kerroin, j * kerroin, kerroin, kerroin);

                } else if (map.getNode(i, j) == Map.NodeType.TARGET) {
                    g.setColor(Color.green);
                    g.fillRect(i * kerroin, j * kerroin, kerroin, kerroin);
                } else if (map.getNode(i, j) == Map.NodeType.BOXINTHETARGET) {
                    g.setColor(Color.orange);
                    g.fillRect(i * kerroin, j * kerroin, kerroin, kerroin);
                }

            }

        }
        if (save == false) {
            g.setColor(Color.pink);
            save = true;

        } else if (suti) {
            g.setColor(Color.red);
        } else {
            g.setColor(Color.yellow);
        }
        g.fillRect(pekka.getX() * kerroin, pekka.getY() * kerroin, kerroin, kerroin);
        g.setColor(Color.red);
        if (suti) {
            g.drawString("Suti", 10, 10);
        }
        g.drawString(type.toString(), 10, 20);
        if (tiedot) {

            g.drawString("P=sudin vaihto s=tallennus", 10, 30);
            g.drawString("Kenttä: " + Integer.toString(kartanNumero), 10, 40);
            g.drawString("Air=0/5", 10, 50);
            g.drawString("Wall=1", 10, 60);
            g.drawString("Box=2", 10, 70);
            g.drawString("Target=3", 10, 80);
            g.drawString("Box in the Target=4", 10, 90);
            g.drawString("Togle this on of = T", 10, 100);
            g.drawString("Voitto Pisteet: " + Integer.toString(map.getVoittoPisteet()), 10, 110);
            g.drawString("Boxsit: " + Integer.toString(boxsit), 10, 120);

        }
    }

    
    public MapEditor() {
        try (FileInputStream i = new FileInputStream(MAP_FILE)) {
            map = Map.readFromStream(i);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        pekka = new Ukkeli(map.getPlayerX(), map.getPlayerY());
    }

    public static void main(String[] args) {
        MapEditor editor = new MapEditor();

        JFrame frame = new JFrame("Editor");
        frame.setPreferredSize(new Dimension(editor.map.getWidth() * kerroin, editor.map.getHeight() * kerroin + 30));
        frame.setMaximumSize(new Dimension(editor.map.getWidth() * kerroin, editor.map.getHeight() * kerroin + 30));
        frame.setMinimumSize(new Dimension(editor.map.getWidth() * kerroin, editor.map.getHeight() * kerroin + 30));
        frame.add(editor);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        editor.init();
        System.out.println("moi");

    }

}
