package HEIG.vd.interfaces;

import java.util.Map;

public interface ILabelDetector {
    Map<String, String> execute(String imageUri, int[] params);
}
