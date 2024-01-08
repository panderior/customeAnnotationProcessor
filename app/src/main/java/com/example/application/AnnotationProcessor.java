package com.example.application;

import com.example.application.annotations.*;
import com.example.application.entity.SimDBEntity;
import com.example.application.entity.SimMetaDataEntity;
import com.example.application.entity.SimParamEntity;
import com.google.auto.service.AutoService;
import com.google.gson.Gson;
import com.squareup.javapoet.*;
import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor
{
    private Filer filer;
    private Messager messager;
    int round = 0;
    private ArrayList<SimDBEntity> simLogs_list;
    private ArrayList<SimParamEntity> simParams_list;
    private String launcher_class, publish2db_name;
    private String pkg_path, sim_table;
    private ClassName model_class, agent_class;
    private ArrayList<MethodSpec> method_list;

    public AnnotationProcessor()
    {
        simLogs_list = new ArrayList<>();
        simParams_list = new ArrayList<>();
        launcher_class = "SimulationLauncher";
        publish2db_name = "publish_to_DB";
        method_list = new ArrayList<>();
    }

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
        annotation_classes.add(SimModel.class.getCanonicalName());
        annotation_classes.add(SimAgent.class.getCanonicalName());
        annotation_classes.add(SimParam.class.getCanonicalName());
        annotation_classes.add(SimVisual.class.getCanonicalName());
        annotation_classes.add(SimChart.class.getCanonicalName());
        return annotation_classes;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment)
    {
        messager.printMessage(Diagnostic.Kind.NOTE, "Annotation working " + round);

        // get annotations
        Set<? extends Element> simLog_elts = roundEnvironment.getElementsAnnotatedWith(SimLog.class);
        Set<? extends Element> agent_elts = roundEnvironment.getElementsAnnotatedWith(SimAgent.class);
        Set<? extends Element> model_elts = roundEnvironment.getElementsAnnotatedWith(SimModel.class);
        Set<? extends Element> param_elts = roundEnvironment.getElementsAnnotatedWith(SimParam.class);
        Set<? extends Element> visual_elts = roundEnvironment.getElementsAnnotatedWith(SimVisual.class);
        Set<? extends Element> chart_elts = roundEnvironment.getElementsAnnotatedWith(SimChart.class);

        try
        {
            if (!agent_elts.isEmpty() & !model_elts.isEmpty() & round == 0)
            {
                generateMetaData(model_elts, agent_elts, simLog_elts, param_elts);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        round++;
        return true;
    }

    public void generateMetaData(Set<? extends Element> model_elts, Set<? extends Element> agent_elts,
                                 Set<? extends Element> simLog_elts, Set<? extends Element> simParam_elts) throws IOException
    {
        // get package path and model class
        model_class = ClassName.get((TypeElement) model_elts.toArray()[0]);
        pkg_path = model_class.packageName();
        // store list of Agents class & names
        agent_class = ClassName.get((TypeElement) agent_elts.toArray()[0]);
        // set simulation table
        sim_table = ((Element) model_elts.toArray()[0]).getAnnotation(SimModel.class).table_name();
        // store member variables to persist to database
        for (Element db_elt : simLog_elts)
        {
            SimDBEntity temp_db_entity = new SimDBEntity(
                    db_elt.getSimpleName().toString(),
                    getFieldType(db_elt)
            );
            simLogs_list.add(temp_db_entity);
        }
        // store list of model parameters
        for (Element param_elt : simParam_elts)
        {
            SimParamEntity temp_param_entity = new SimParamEntity(param_elt.getSimpleName().toString(),
                    getFieldType(param_elt),
                    param_elt.getAnnotation(SimParam.class).default_value());
            simParams_list.add(temp_param_entity);
        }
        simParams_list.add(new SimParamEntity("steps", "int", "100"));
        simParams_list.add(new SimParamEntity("tick_delay", "int", "100"));

        // generate sim meta data file
        SimMetaDataEntity sim_meta_data = new SimMetaDataEntity(
                pkg_path,
                model_class.simpleName(),
                agent_class.simpleName(),
                sim_table,
                simLogs_list,
                simParams_list
        );
        Gson gson_converter = new Gson();
        FileObject metaData_fileObject = filer.createResource(StandardLocation.CLASS_OUTPUT,"","metaData.json");
        Writer metaData_writer = metaData_fileObject.openWriter();
        gson_converter.toJson(sim_meta_data, metaData_writer);
        metaData_writer.close();
    }

    private String getFieldType(Element field_elt)
    {
        TypeMirror fieldType = ((VariableElement) field_elt).asType();
        String dbData_type_name;
        if (fieldType.getKind().isPrimitive()) {
            dbData_type_name = fieldType.toString();
        } else {
            dbData_type_name = processingEnv.getTypeUtils().asElement(fieldType).getSimpleName().toString();
        }
        return dbData_type_name;
    }
}
