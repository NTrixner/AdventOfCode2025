package eu.ntrixner.aoc.utils;

import eu.ntrixner.aoc.day12.TreeFarm;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class ArrayHelper {

    public static char[][] toMatrix(String input) {
        String[] lines = input.split("\n");
        char[][] matrix = new char[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            matrix[i] = lines[i].replace("\r", "").toCharArray();
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
        if (i == matrix.length - 1) {
            neighbours[4] = ' ';
        } else {
            neighbours[4] = matrix[i + 1][j];
        }

        //5- bottom left
        if (j == 0 || i == matrix.length - 1) {
            neighbours[5] = ' ';
        } else {
            neighbours[5] = matrix[i + 1][j - 1];
        }

        //6 - left
        if (j == 0) {
            neighbours[6] = ' ';
        } else {
            neighbours[6] = matrix[i][j - 1];
        }

        //7 - top left
        if (i == 0 || j == 0) {
            neighbours[7] = ' ';
        } else {
            neighbours[7] = matrix[i - 1][j - 1];
        }

        return neighbours;
    }

    public static List<String> toLines(char[][] rotated) {
        List<String> lines = new ArrayList<>();
        for (char[] row : rotated) {
            lines.add(String.valueOf(row));
        }
        return lines;
    }

    public static String toString(char[][] rotated) {
        return String.join("\n", toLines(rotated));
    }

    public static List<char[][]> getPermutations(List<String> shapeLines) {
        HashMap<String, char[][]> permutations = new HashMap<>();
        char[][] standard = toMatrix(String.join("\n", shapeLines));
        char[][] rotRightOnce = rotate(standard, Direction.CW, 1);
        char[][] rotLeftOnce = rotate(standard, Direction.CCW, 1);
        char[][] rotTwice = rotate(standard, Direction.CW, 2);
        char[][] mirrorStandard = flipVertical(standard);
        char[][] mirrorRotRightOnce = flipVertical(rotRightOnce);
        char[][] mirrorRotLeftOnce = flipVertical(rotLeftOnce);
        char[][] mirrorRotTwice = flipVertical(rotTwice);
        permutations.put(toString(standard), standard);
        permutations.put(toString(rotRightOnce), rotRightOnce);
        permutations.put(toString(rotLeftOnce), rotLeftOnce);
        permutations.put(toString(rotTwice), rotTwice);
        permutations.put(toString(mirrorStandard), mirrorStandard);
        permutations.put(toString(mirrorRotRightOnce), mirrorRotRightOnce);
        permutations.put(toString(mirrorRotLeftOnce), mirrorRotLeftOnce);
        permutations.put(toString(mirrorRotTwice), mirrorRotTwice);
        return new ArrayList<>(permutations.values());
    }

    public enum Direction {
        CCW, CW
    }

    public static char[][] rotate(char[][] input, Direction direction, int rotations) {
        char[][] out = input;
        rotations = ((rotations % 4) + 4) % 4;
        for (int r = 0; r < rotations; r++) {
            out = (direction == Direction.CW) ? rotateCWOnce(out) : rotateCCWOnce(out);
        }
        return out;
    }

    private static char[][] rotateCWOnce(char[][] in) {
        int h = in.length;
        int w = in[0].length;
        char[][] out = new char[w][h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                out[x][h - 1 - y] = in[y][x];
            }
        }
        return out;
    }

    private static char[][] rotateCCWOnce(char[][] in) {
        int h = in.length;
        int w = in[0].length;
        char[][] out = new char[w][h];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                out[w - 1 - x][y] = in[y][x];
            }
        }
        return out;
    }

    public static char[][] flipVertical(char[][] input) {
        char[][] output = new char[input.length][input[0].length];
        for (int y = 0; y < output.length; y++) {
            for (int x = 0; x < output[y].length; x++) {
                output[y][x] = input[output.length - 1 - y][x];
            }
        }
        return output;
    }

    public static BitSet getBitSet(char[][] matrix, char bitMarker) {
        int h = matrix.length;
        int w = matrix[0].length;
        BitSet b = new BitSet(h * w);
        for (int r = 0; r < h; r++) {
            for (int c = 0; c < w; c++) {
                if (matrix[r][c] == bitMarker) b.set(r * w + c);
            }
        }
        return b;
    }
}
