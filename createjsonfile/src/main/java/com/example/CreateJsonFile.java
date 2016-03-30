package com.example;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class CreateJsonFile {

    private static final String ZShapeType = "zshape";
    private static final String InvertedZShapeType = "invertedzshape";
    private static final String HorizontalLine = "horizontalline";
    private static final String VerticalLine = "verticalline";
    private static final String VShapeType = "vshape";
    private static final String InvertedVShapeType = "invertedvshape";
    private static final String AlphaType = "alpha";
    private static final String GammaType = "gamma";

    private static int ZShapeFileCount = 3;
    private static int InvertedZShapeFileCount = 6;
    private static int HorizontalLineFileCount = 9;
    private static int VerticalLineFileCount = 6;
    private static int VShapeTypeFileCount = 8;
    private static int InvertedVShapeFileCount = 6;
    private static int AlphaFileCount = 5;
    private static int GammaFileCount = 5;

    private static int CurrentFileCount = 0;


    public static void main(String[] args) {

        while (true) {
            Scanner console = new Scanner(System.in);
            System.out.println("Input type of shape: ");
            String userInput = console.nextLine();

            if (userInput.equals("exit")) {
                break;

            } else {

                JSONObject jsonObject = new JSONObject();

                String typeOfShape = typeOfShape(userInput);
                jsonObject.put("Name", typeOfShape);
                System.out.println("Input Points (2D List): ");

                String listofpoints = console.nextLine();
            /*
              FORMAT
              eg = [[1,2],[3,4],[5,6]]
             */

                listofpoints = listofpoints.replace("[", "");
                //1,2],3,4],5,6]]

                listofpoints = listofpoints.substring(0, listofpoints.length() - 2);
                //1,2],3,4],5,6

                String[] listofPointsAsString = listofpoints.split("],");
                //["1,2","3,4","5,6"]

                JSONArray jsonArrayOfHashMaps = new JSONArray();
                //Json Point format is an array of hashmap

                for (String pointxandy : listofPointsAsString) {
                    String[] separatePoints = pointxandy.split(","); //["1","2"]
                    HashMap<String, Integer> hashMap = new HashMap<>();

                    hashMap.put("X", Integer.valueOf(separatePoints[0]));
                    hashMap.put("Y", Integer.valueOf(separatePoints[1]));

                    jsonArrayOfHashMaps.add(hashMap);
                }

                jsonObject.put("Points", jsonArrayOfHashMaps);

                try {

                    FileWriter file = new FileWriter(new File("C:\\Users\\yanyee\\" + typeOfShape + CurrentFileCount + ".json"));
                    file.write(jsonObject.toJSONString());
                    file.flush();
                    file.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println(jsonObject);
            }

        }

    }

    public static String typeOfShape(String userInput) {

        if (userInput.contains("zshape")) {
            ZShapeFileCount += 1;
            CurrentFileCount = ZShapeFileCount;
            return ZShapeType;

        } else if (userInput.contains("invz")) {
            InvertedZShapeFileCount += 1;
            CurrentFileCount = InvertedZShapeFileCount;
            return InvertedZShapeType;

        } else if (userInput.contains("horizontal")) {
            HorizontalLineFileCount += 1;
            CurrentFileCount = HorizontalLineFileCount;
            return HorizontalLine;

        } else if (userInput.contains("vertical")) {
            VerticalLineFileCount += 1;
            CurrentFileCount = VerticalLineFileCount;
            return VerticalLine;

        }else if (userInput.contains("vshape")) {
            VShapeTypeFileCount += 1;
            CurrentFileCount = VShapeTypeFileCount;
            return VShapeType;

        } else if (userInput.contains("invv")) {
            InvertedVShapeFileCount += 1;
            CurrentFileCount = InvertedVShapeFileCount;
            return InvertedVShapeType;

        } else if (userInput.contains("alpha")) {
            AlphaFileCount += 1;
            CurrentFileCount = AlphaFileCount;
            return AlphaType;

        } else if (userInput.contains("gamma")) {
            GammaFileCount += 1;
            CurrentFileCount = GammaFileCount;
            return GammaType;

        } else {
            return null;
        }
    }


}
