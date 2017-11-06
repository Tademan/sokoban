/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kissa.peli;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author taavi
 */
public class Map {

    private final int width;

    private final int height;
    private int voittoPisteet;
    private int PlayerX;
    private int PlayerY;        
    private  NodeType[][] map;

    public Map(int x, int y,int v,int pX,int pY) {
        width = x;
        height = y;
        voittoPisteet = v;
        PlayerX = pX;
        PlayerY = pY;
        
        map = new NodeType[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                map[i][j] = NodeType.AIR;
            }
        }
    }

    public void setNode(int x, int y, NodeType type) {
        map[x][y] = type;
    }

    public NodeType getNode(int x, int y) {
        return map[x][y];
    }

    public void setPlayerX(int PlayerX) {
        this.PlayerX = PlayerX;
    }

    public void setPlayerY(int PlayerY) {
        this.PlayerY = PlayerY;
    }
    
    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getVoittoPisteet() {
        return voittoPisteet;
    }
    public void setVoittoPisteet(int v){
        this.voittoPisteet = v;
    }
    public int getPlayerX() {
        return PlayerX;
    }

    public int getPlayerY() {
        return PlayerY;
    }
    
    public boolean isWithinMap(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public void writeToStream(OutputStream s) throws IOException {
        s.write(width);
        s.write(height);
        s.write(voittoPisteet);
        s.write(PlayerX);
        s.write(PlayerY);
        
        byte[] bytes = new byte[width * height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                bytes[j * height + i] = map[i][j].getCode();
            }
        }

        s.write(bytes);
    }

    public static Map readFromStream(InputStream s) throws IOException {
        int width = s.read();
        int height = s.read();
        int voittoPisteet = s.read();
        int PlayerX = s.read();
        int PlayerY = s.read();
        

        byte[] bytes = new byte[width * height];
        s.read(bytes);

        Map map = new Map(width, height,voittoPisteet,PlayerX,PlayerY);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                NodeType type = NodeType.nodeTypeFromByte(bytes[j * height + i]);
                if (type == null) {
                    throw new RuntimeException("illegal map data, node type not found: " + bytes[j * height + i]);
                }
                map.map[i][j] = type;
            }
        }

        return map;
    }

    public enum NodeType {
        AIR(0),
        WALL(1),
        BOX(2),
        TARGET(3),
        BOXINTHETARGET(4);

        public static NodeType nodeTypeFromByte(byte i) {
            for (NodeType t : values()) {
                if (t.getCode() == i) {
                    return t;
                }
            }
            return null;
        }

        private byte code;

        private NodeType(int code) {
            this.code = (byte) code;
        }

        public byte getCode() {
            return code;
        }

    }

}
