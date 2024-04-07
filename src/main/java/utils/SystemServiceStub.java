package utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SystemServiceStub implements SystemService {

    private InputStream in;
    private final List<String> printedMessages = new ArrayList<>();

    @Override
    public InputStream getInput() {
        return in;
    }

    public void setInput(InputStream byteArrayInputStream){
        this.in = byteArrayInputStream;
    }

    public void println(String message) {
        printedMessages.add(message);
    }

    public List<String> getMessages() {
        return printedMessages;
    }

}
