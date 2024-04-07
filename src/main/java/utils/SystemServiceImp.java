package utils;

import java.io.InputStream;

public class SystemServiceImp implements SystemService{
    @Override
    public InputStream getInput() {
        return System.in;
    }
}
