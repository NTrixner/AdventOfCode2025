package eu.ntrixner.aoc.utils;

import java.util.ArrayList;
import java.util.List;

public final class ArrayHelper {

    public static char[][] toMatrix(String input) {
        String[] lines = input.split("\n");
        char[][] matrix = new char[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            matrix[i] = lines[i].toCharArray();
        }
        return matrix;
    }


    private ArrayHelper() {
    }

    //Finds the 8 neighbours surrounding the position
    //0 is the top, then it goes clockwise
    public static char[] find8Neighbours(char[][] matrix, int i, int j) {
        char[] neighbours = new char[8];

        //0 - top
        if (i == 0) {
            neighbours[0] = ' ';
        } else {
            neighbours[0] = matrix[i - 1][j];
        }

        //1 - top right
        if (i == 0 || j == matrix[i].length - 1) {
            neighbours[1] = ' ';
        } else {
            neighbours[1] = matrix[i - 1][j + 1];
        }

        //2 - right
        if (j == matrix[i].length - 1) {
            neighbours[2] = ' ';
        } else {
            neighbours[2] = matrix[i][j + 1];
        }

        //3 - bottom right
        if (j == matrix[i].length - 1 || i == matrix.length - 1) {
            neighbours[3] = ' ';
        } else {
            neighbours[3] = matrix[i + 1][j + 1];
        }

        //4 - bottom
        if(i == matrix.length - 1){
            neighbours[4] = ' ';
        } else {
            neighbours[4] = matrix[i + 1][j];
        }

        //5- bottom left
        if(j == 0 || i == matrix.length - 1){
            neighbours[5] = ' ';
        } else {
            neighbours[5] = matrix[i + 1][j - 1];
        }

        //6 - left
        if(j == 0) {
            neighbours[6] = ' ';
        } else {
            neighbours[6] = matrix[i][j - 1];
        }

        //7 - top left
        if(i == 0 || j == 0) {
            neighbours[7] = ' ';
        } else {
            neighbours[7] = matrix[i - 1][j - 1];
        }

        return neighbours;
    }

    public static List<String> toLines(char[][] rotated) {
        List<String> lines = new ArrayList<>();
        for(char[] row : rotated) {
            lines.add(String.valueOf(row));
        }
        return lines;
    }

    public enum Direction {
        CCW, CW
    }
    public static char[][] rotate(char[][] input, Direction direction, int rotations) {
        char[][] output = input;
        for (int r = rotations; r > 0; r--) {
            char[][] outputNew = new char[output[0].length][output.length];
            for(int y = 0; y < output.length; y++){
                for(int x = 0; x < output[y].length; x++){
                    switch (direction) {
                        case CW:
                            outputNew[x][y] = output[output.length - 1 - y][x];
                            break;
                        case CCW:
                            outputNew[x][y] = output[y][output[y].length - 1 - x];
                            break;
                    }
                }
            }
            output = outputNew;
        }
        return output;
    }
}
