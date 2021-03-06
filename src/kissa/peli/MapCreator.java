/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kissa.peli;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import kissa.peli.Map.NodeType;

/**
 *
 * @author taavi
 */
public class MapCreator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Random r = new Random();

       
        for (int i = 0; i < 100; i++) {
            File file = new File("kartat/kartta"+String.format("%03d", i)+".dat");
             Map map = new Map(30, 30, 50, 5, 5);
               for(int j = 0; j<map.getHeight();j++){
                   for(int k = 0; k<map.getWidth();k++){
                       map.setNode(j, k, NodeType.WALL);
                   }
               }
               
            try (FileOutputStream o = new FileOutputStream(file)) {
                map.writeToStream(o);
            
            }
        }

    }

}
