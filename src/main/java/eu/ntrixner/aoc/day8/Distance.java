package eu.ntrixner.aoc.day8;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Distance {
    Pos3D a, b;
    double distance;
}
