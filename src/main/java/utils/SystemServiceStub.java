package utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class SystemServiceStub implements SystemService {

    private InputStream in;
    @Override
    public InputStream getInput() {
        return in;
    }


    public void setInput(InputStream byteArrayInputStream){
        this.in = byteArrayInputStream;
    }
}
