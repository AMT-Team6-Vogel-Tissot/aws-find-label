package HEIG.vd.interfaces;


import java.net.URL;
import java.nio.file.Path;

public interface IDataObjectHelper {
    URL createObject(String bucketName, String objectName, Path path);
}
