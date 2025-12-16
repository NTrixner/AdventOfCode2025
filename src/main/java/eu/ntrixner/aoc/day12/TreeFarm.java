package eu.ntrixner.aoc.day12;

import eu.ntrixner.aoc.ChallengeRunner;
import eu.ntrixner.aoc.utils.ArrayHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static eu.ntrixner.aoc.utils.ArrayHelper.getBitSet;

@Slf4j
public class TreeFarm implements ChallengeRunner<String> {
    public String input;
    Pattern spacePattern = Pattern.compile("\\d+x\\d+");

    @Override
    public void init(String params) {
        this.input = params;
    }

    @Override
    public void run() {
        Inputs inputs = extractInputsIntoShapesAndSpaces(input);

        long count = inputs.spaces
                .stream()
                .parallel()
                .filter(space -> spaceFits(space, inputs.shapes))
                .count();

        log.info("{} of the Spaces fit their shapes", count);
    }

    private boolean spaceFits(Space space, List<Shape> shapes) {
        int H = space.matrix.length;
        int W = space.matrix[0].length;
        int cells = W*H;

        int needArea = 0;
        for(int i = 0; i < shapes.size(); i++) {
            needArea += space.fitsRequired[i] * shapes.get(i).perms.getFirst().bitSet.cardinality();
        }
        if(needArea > cells) return false;

        // Precompute all placements for each shape type (for this specific space)
        List<List<Placement>> placementsByShape = new ArrayList<>();
        for (int i = 0; i < shapes.size(); i++) {
            placementsByShape.add(generatePlacements(shapes.get(i).perms, space.bitSet, W, H));
            // If we need copies but there are no placements at all, fail immediately
            if (space.fitsRequired[i] > 0 && placementsByShape.get(i).isEmpty()) return false;
        }

        // Order shapes by "tightness" baseline (few placements / copy)
        Integer[] order = new Integer[shapes.size()];
        for (int i = 0; i < order.length; i++) order[i] = i;
        Arrays.sort(order, Comparator.comparingInt(i -> placementsByShape.get(i).size()));

        // Occupied cells so far
        BitSet occupied = new BitSet(cells);

        // Symmetry breaking for identical copies:
        // For each shape type, enforce that successive copies use non-decreasing placement indices.
        int[] lastPlacementIdx = new int[shapes.size()];
        Arrays.fill(lastPlacementIdx, -1);

        return search(space.fitsRequired, placementsByShape, order, occupied, space.bitSet, lastPlacementIdx);
    }

    private boolean search(
            int[] remaining,
            List<List<Placement>> placementsByShape,
            Integer[] shapeOrder,
            BitSet occupied,
            BitSet free,
            int[] lastPlacementIdx
    ) {
        // Check if all pieces placed
        if(IntStream.of(remaining).sum() == 0) return true;

        // Pick next shape type to place: MRV = fewest currently-valid placements
        int bestShape = -1;
        int bestCount = Integer.MAX_VALUE;

        for (int si : shapeOrder) {
            if (remaining[si] <= 0) continue;

            int cnt = countValidPlacements(placementsByShape.get(si), occupied, lastPlacementIdx[si]);
            if (cnt == 0) return false; // fail fast
            if (cnt < bestCount) {
                bestCount = cnt;
                bestShape = si;
                if (bestCount == 1) break;
            }
        }

        // Try placements for bestShape
        List<Placement> plist = placementsByShape.get(bestShape);

        // Iterate smaller masks first (sometimes helps)
        plist.sort(Comparator.comparingInt(p -> p.mask.cardinality()));

        int startIdx = lastPlacementIdx[bestShape] + 1; // symmetry break for identical copies
        for (int idx = startIdx; idx < plist.size(); idx++) {
            Placement p = plist.get(idx);
            if (!isPlacementValid(p.mask, occupied)) continue;

            // Place it
            occupied.or(p.mask);
            remaining[bestShape]--;
            int prevLast = lastPlacementIdx[bestShape];
            lastPlacementIdx[bestShape] = idx;

            // Basic area check on remaining free cells
            if (remainingArea(remaining, placementsByShape) <= free.cardinality() - occupied.cardinality()) {
                if (search(remaining, placementsByShape, shapeOrder, occupied, free, lastPlacementIdx)) {
                    return true;
                }
            }

            // Undo
            lastPlacementIdx[bestShape] = prevLast;
            remaining[bestShape]++;
            occupied.andNot(p.mask);
        }

        return false;
    }

    private List<Placement> generatePlacements(List<ShapePerm> permutations, BitSet free, int W, int H) {
        List<Placement> result = new ArrayList<>();

        for (ShapePerm  perm: permutations) {
            char[][] sh = perm.matrix;
            int shH = sh.length;
            int shW = sh[0].length;

            // Precompute filled cells of shape relative coords
            List<int[]> filled = new ArrayList<>();
            for (int r = 0; r < shH; r++) {
                for (int c = 0; c < shW; c++) {
                    if (sh[r][c] == '#') filled.add(new int[]{r, c});
                }
            }

            for (int top = 0; top <= H - shH; top++) {
                for (int left = 0; left <= W - shW; left++) {
                    BitSet mask = new BitSet(W * H);
                    boolean ok = true;
                    for (int[] rc : filled) {
                        int rr = top + rc[0];
                        int cc = left + rc[1];
                        int idx = rr * W + cc;
                        if (!free.get(idx)) { ok = false; break; }
                        mask.set(idx);
                    }
                    if (ok) result.add(new Placement(mask));
                }
            }
        }

        return result;
    }

    private int countValidPlacements(List<Placement> plist, BitSet occupied, int lastIdx) {
        int cnt = 0;
        for (int i = lastIdx + 1; i < plist.size(); i++) {
            if (isPlacementValid(plist.get(i).mask, occupied)) cnt++;
        }
        return cnt;
    }

    private boolean isPlacementValid(BitSet placementMask, BitSet occupied) {
        // free is not needed here if placements were generated using it
        return !placementMask.intersects(occupied);
    }

    // Conservative lower bound on required area remaining (safe pruning)
    private int remainingArea(int[] remaining, List<List<Placement>> placementsByShape) {
        // We don't know exact shape area from placements list directly, so compute from any placement mask size.
        // If a shape has no placements, this isn't used (we early-fail).
        int area = 0;
        for (int i = 0; i < remaining.length; i++) {
            if (remaining[i] <= 0) continue;
            int shapeArea = placementsByShape.get(i).isEmpty() ? 0 : placementsByShape.get(i).getFirst().mask.cardinality();
            area += remaining[i] * shapeArea;
        }
        return area;
    }

    private Inputs extractInputsIntoShapesAndSpaces(String input) {

        boolean readingShapes = true;
        String[] lines = input.split("\n");
        int shapeIndex = 0;
        List<String> shapeLines = new ArrayList<>();

        List<Shape> shapes = new ArrayList<>();
        List<Space> spaces = new ArrayList<>();

        int spaceIndex = 0;

        for (String line : lines) {
            line = line.trim();

            Matcher matcher = spacePattern.matcher(line);

            if (readingShapes) {
                if (line.endsWith(":")) {
                    shapeIndex = Integer.parseInt(line.substring(0, line.length() - 1));
                } else if (line.startsWith(".") || line.startsWith("#")) {
                    shapeLines.add(line);
                } else if (line.isEmpty()) {
                    Shape shape = new Shape();
                    shape.index = shapeIndex;
                    shape.perms = ArrayHelper.getPermutations(shapeLines)
                            .stream()
                            .map(m -> {
                                ShapePerm perm = new ShapePerm();
                                perm.matrix = m;
                                perm.bitSet = getBitSet(perm.matrix, '#');
                                return perm;
                            })
                            .collect(Collectors.toList());
                    shapes.add(shape);

                    shapeLines = new ArrayList<>();
                } else if(matcher.find()) {
                    readingShapes = false;
                }
            }
            if(!readingShapes && !line.isEmpty()) {
                Space space = new Space();

                int w = Integer.parseInt(line.substring(0, line.indexOf("x")));
                int h = Integer.parseInt(line.substring(line.indexOf("x") + 1, line.indexOf(":")));

                space.matrix = new char[h][w];
                for(char[] mLine : space.matrix) {
                    Arrays.fill(mLine, '.');
                }
                space.bitSet = getBitSet(space.matrix, '.');

                String fits = line.substring(line.indexOf(":") + 1);
                String[] fitsArr = fits.split(" ");

                space.fitsRequired = Arrays.stream(fitsArr).filter(s -> !StringUtils.isAllBlank(s)).mapToInt(Integer::parseInt).toArray();

                space.index = spaceIndex;
                spaceIndex++;
                spaces.add(space);
            }
        }
        Inputs inputs = new Inputs();
        inputs.shapes = shapes;
        inputs.spaces = spaces;
        return inputs;
    }

    public static class Inputs {
        List<Shape> shapes;
        List<Space> spaces;
    }

    public static class Space {
        int index;
        char[][] matrix;
        BitSet bitSet;
        int[] fitsRequired;
    }

    public static class Shape {
        List<ShapePerm> perms = new ArrayList<>();
        int index;
    }

    public static class ShapePerm {
        char[][] matrix;
        BitSet bitSet;
    }

    private static class Placement {
        final BitSet mask;
        Placement(BitSet mask) { this.mask = mask; }
    }
}
