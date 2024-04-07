package annotations;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@javax.annotation.processing.SupportedAnnotationTypes("annotations.Immutable")
@javax.annotation.processing.SupportedSourceVersion(javax.lang.model.SourceVersion.RELEASE_8)
public class AnnotationProcessor extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        FileObject file = null;
        try {
            file = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "annotations-class/myfile.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (PrintWriter out = new PrintWriter(file.openWriter())) {
            out.println("Processed by MyCustomAnnotationProcessor");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}