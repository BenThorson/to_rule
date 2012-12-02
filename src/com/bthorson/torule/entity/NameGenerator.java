package com.bthorson.torule.entity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * User: Ben Thorson
 * Date: 11/24/12
 * Time: 9:51 PM
 */
public class NameGenerator {
    private static NameGenerator ourInstance = new NameGenerator();

    private List<String> names = new ArrayList<String>();

    public static NameGenerator getInstance() {
        return ourInstance;
    }

    private NameGenerator() {
        try {
            Scanner s = new Scanner(new File("resources/maleGivenName"));
            while (s.hasNext()){
                names.add(s.next());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public String genName(){
        Random rand = new Random();
        return names.get(rand.nextInt(names.size())) + " " + names.get(rand.nextInt(names.size()));
    }

    public String genTownName(){
        Random rand = new Random();
        return names.get(rand.nextInt(names.size()));
    }
}
