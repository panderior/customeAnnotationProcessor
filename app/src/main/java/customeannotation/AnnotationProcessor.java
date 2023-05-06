package customeannotation;

import com.google.auto.service.AutoService;
import customeannotation.annotations.SimLog;
import customeannotation.annotations.Simulator;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.tools.Diagnostic;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor
{
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        filer = env.getFiler();
        messager = env.getMessager();
    }
    @Override
    public Set<String> getSupportedAnnotationTypes()
    {
        Set<String> annotation_classes = new HashSet<>();
        annotation_classes.add(SimLog.class.getCanonicalName());
        annotation_classes.add(Simulator.class.getCanonicalName());
        return annotation_classes;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment)
    {
//        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Annotation working");
        messager.printMessage(Diagnostic.Kind.WARNING, "Annotation working");
        // iterate and get a list of annotations
        for (TypeElement annotation : set)
        {
            Set<? extends Element> annotatedElements = roundEnvironment.getElementsAnnotatedWith(annotation);
//            annotatedElements.forEach(element -> {
////                System.out.println(((ExecutableType) element.asType()).getClass().getSimpleName());
//                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, ((ExecutableType) element.asType()).getClass().getSimpleName());
//            });

        }

        return true;
    }
}
