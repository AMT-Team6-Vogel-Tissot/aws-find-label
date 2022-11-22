package HEIG.vd.interfaces;


import java.net.URL;
import java.nio.file.Path;

public interface IDataObjectHelper {
    URL publish(String objectName);

    boolean exist();

    boolean exist(String objectName);

    String list();

    void create(String objectName, byte[] contentFile);

    byte[] get(String objectName);

    void delete(String objectName);

}
