package HEIG.vd.interfaces;

import java.util.Map;

public interface ILabelDetector {

    // TODO ajouter des explications, on comprends pas ce qu'il faut mettre dans
    // params, ici j'utiliserais la javadoc pour que ce soit disponible facilement à
    // l'utilisation et à l'implémentation
    Map<String, String> execute(String imageUri, int[] params);

    // TODO method to process a base64 image
}
