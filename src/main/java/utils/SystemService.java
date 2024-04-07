package utils;

import java.io.InputStream;

public interface SystemService {
    InputStream getInput();
    void println(String message);
}
