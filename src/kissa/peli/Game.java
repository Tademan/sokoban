/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kissa.peli;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.JFrame;

/**
 *
 * @author taavi
 */
public class Game extends Canvas {

    static int kartanNumero = 0;
    private static File MAP_FILE = new File("kartta" + Integer.toString(kartanNumero) + ".dat");
    Map map;
    Ukkeli pekka;
    int pisteet;
    static int kerroin = 30;
    boolean uudestaan = false;

    private void init() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent ke) {
                switch (ke.getKeyCode()) {
                    case KeyEvent.VK_DOWN:
                        movePlayer(0, 1);
                        break;
                    case KeyEvent.VK_UP:
                        movePlayer(0, -1);
                        break;
                    case KeyEvent.VK_RIGHT:
                        movePlayer(1, 0);
                        break;
                    case KeyEvent.VK_LEFT:
                        movePlayer(-1, 0);
                        break;
                    case KeyEvent.VK_R:
                        uudestaan = true;
                        break;
                    case KeyEvent.VK_D:
                        kartanNumero++;
                        uudestaan = true;
                        break;
                    case KeyEvent.VK_A:
                        kartanNumero--;
                        uudestaan = true;
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

            if (type == Map.NodeType.AIR || type == Map.NodeType.TARGET) {
                pekka.move(x, y);
            } else if (type == Map.NodeType.BOX) {
                int nx = pekka.getX() + 2 * x;
                int ny = pekka.getY() + 2 * y;
                if (map.isWithinMap(nx, ny) && map.getNode(nx, ny) == Map.NodeType.AIR) {
                    pekka.move(x, y);
                    map.setNode(pekka.getX(), pekka.getY(), Map.NodeType.AIR);
                    map.setNode(nx, ny, Map.NodeType.BOX);
                } else if (map.isWithinMap(nx, ny) && map.getNode(nx, ny) == Map.NodeType.TARGET) {
                    pekka.move(x, y);
                    map.setNode(pekka.getX(), pekka.getY(), Map.NodeType.AIR);
                    map.setNode(nx, ny, Map.NodeType.BOXINTHETARGET);
                    pisteet++;
                    

                }
            } else if (type == Map.NodeType.BOXINTHETARGET) {
                int nx = pekka.getX() + 2 * x;
                int ny = pekka.getY() + 2 * y;
                if (map.isWithinMap(nx, ny) && map.getNode(nx, ny) == Map.NodeType.AIR) {
                    pekka.move(x, y);
                    map.setNode(pekka.getX(), pekka.getY(), Map.NodeType.TARGET);
                    map.setNode(nx, ny, Map.NodeType.BOX);
                    pisteet--;
                } else if (map.isWithinMap(nx, ny) && map.getNode(nx, ny) == Map.NodeType.TARGET) {
                    pekka.move(x, y);
                    map.setNode(pekka.getX(), pekka.getY(), Map.NodeType.TARGET);
                    map.setNode(nx, ny, Map.NodeType.BOXINTHETARGET);

                }
            }
        }
        System.out.println(pisteet);
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
                pisteet = 0;
                for (int j = 0; j < map.getHeight(); j++) {
                    for (int k = 0; k < map.getWidth(); k++) {
                        if (map.getNode(j, k) == Map.NodeType.BOXINTHETARGET) {
                            pisteet++;
                        }
                    }
                }

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
        if (pisteet >= map.getVoittoPisteet()) {
            System.out.println("Voitit");
            kartanNumero++;
            uudestaan = true;

        }
    }

    private void render(Graphics g) {
        g.setColor(Color.black);
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                if (map.getNode(i, j) == Map.NodeType.WALL) {
                    g.setColor(Color.black);
                    g.fillRect(i * kerroin, j * kerroin, kerroin, kerroin);

                } else if (map.getNode(i, j) == Map.NodeType.BOX) {
                    g.setColor(Color.blue);
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
        g.setColor(Color.red);
        g.fillRect(pekka.getX() * kerroin, pekka.getY() * kerroin, kerroin, kerroin);
        g.drawString(Integer.toString(pisteet), 10, 10);
        g.drawString(Integer.toString(kartanNumero), 10, 20);
        g.drawString(Integer.toString(map.getVoittoPisteet()), 10, 30);
        if (pisteet >= map.getVoittoPisteet()) {
            g.setFont(g.getFont().deriveFont(18.0f));
            g.drawString("VOITIT", 50, 50);
        }

    }

    /**
     * @param args the command line arguments
     */
    public Game() {
        try (FileInputStream i = new FileInputStream(MAP_FILE)) {
            map = Map.readFromStream(i);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println(map.getPlayerX());
        System.out.println(map.getPlayerY());
        System.out.println("moi");
        pekka = new Ukkeli(map.getPlayerX(), map.getPlayerY());

    }

    public static void main(String[] args) {
        Game game = new Game();

        JFrame frame = new JFrame("peli");
        frame.setPreferredSize(new Dimension(game.map.getWidth() * kerroin, game.map.getHeight() * kerroin + 30));
        frame.setMaximumSize(new Dimension(game.map.getWidth() * kerroin, game.map.getHeight() * kerroin + 30));
        frame.setMinimumSize(new Dimension(game.map.getWidth() * kerroin, game.map.getHeight() * kerroin + 30));
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.init();

    }

}
