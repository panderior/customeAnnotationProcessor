package com.example.application;

import javax.annotation.processing.*;


//@SupportedSourceVersion(SourceVersion.RELEASE_17)
//@AutoService(Processor.class)
// public class OldProcessor extends AbstractProcessor
public class OldProcessor
{
    /*
    private Filer filer;
    private Messager messager;
    int round = 0;

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
        annotation_classes.add(Agent.class.getCanonicalName());
        annotation_classes.add(Model.class.getCanonicalName());
        return annotation_classes;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment)
    {
        messager.printMessage(Diagnostic.Kind.NOTE, "Annotation working " + round);

        // variables for generated class info
        String launcher_class = "SimulationLauncher";
        String publish2db_name = "publish_to_DB";
        String sim_table = null;
        ClassName model_class = null;
        ClassName agent_class = null;
        ArrayList<String> field_list = new ArrayList<>();
        ArrayList<MethodSpec> method_list = new ArrayList<>();

        // get annotations
        Set<? extends Element> simLog_elts = roundEnvironment.getElementsAnnotatedWith(SimLog.class);
        Set<? extends Element> agent_elts = roundEnvironment.getElementsAnnotatedWith(SimAgent.class);
        Set<? extends Element> model_elts = roundEnvironment.getElementsAnnotatedWith(SimModel.class);
        Set<? extends Element> param_elts = roundEnvironment.getElementsAnnotatedWith(SimParam.class);

        if(!simLog_elts.isEmpty() & !agent_elts.isEmpty() & !model_elts.isEmpty())
        {
            // get field names
            for (Element elt : simLog_elts)
            {
                field_list.add(elt.getSimpleName().toString());
            }

            // get agent and model names
            model_class = ClassName.get((TypeElement) model_elts.toArray()[0]);
            agent_class = ClassName.get((TypeElement) agent_elts.toArray()[0]);

            // set simulation table
            sim_table = ((Element) model_elts.toArray()[0]).getAnnotation(Model.class).table_name();

            // build the SimulationLauncher class
            // build the constructor
            MethodSpec sim_constructor = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addComment("This is the constructor")
                    .addStatement("sim_table = $S", sim_table)
                    .build();
            method_list.add(sim_constructor);
            // build the main method
            MethodSpec main_method = MethodSpec.methodBuilder("main")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addParameter(String[].class, "args")
                    .addComment("This is the main method")
                    .addStatement("$T model = new $T()", model_class, model_class)
                    .addStatement("$T launcher = new $T()", ClassName.bestGuess(launcher_class), ClassName.bestGuess(launcher_class))
                    .addStatement("launcher.$L(model, true)", publish2db_name)
                    .build();
            method_list.add(main_method);
            // build publish to db method
            MethodSpec.Builder publish2db_method_builder = MethodSpec.methodBuilder(publish2db_name)
                    .addParameter(model_class, "mdl")
                    .addParameter(boolean.class, "enabled")
                    .addStatement("$T agt = mdl.getAgent()", agent_class)
                    .addStatement("$T sim_data = new $T()", JSONObject.class, JSONObject.class);
            for(String fld: field_list)
            {
                publish2db_method_builder.addStatement("sim_data.put($S, agt.$L)", fld, fld);
            }
            MethodSpec publish2db_method = publish2db_method_builder
                    .addStatement("$T data = new $T()", JSONObject.class, JSONObject.class)
                    .addStatement("data.put(\"sim_table\", this.sim_table)")
                    .addStatement("data.put(\"sim_data\", sim_data)")
                    .addStatement("System.out.println(data.toString())")
                    .build();
            method_list.add(publish2db_method);

            // build the simulator class
            TypeSpec sim_class = TypeSpec.classBuilder(launcher_class)
                    .addModifiers(Modifier.PUBLIC)
                    .addField(String.class, "sim_table")
                    .addMethods(method_list)
                    .build();

            // save the sim code
            try
            {
                JavaFile.builder(model_class.packageName(), sim_class)
                        .build()
                        .writeTo(filer);
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }

        } else {
            messager.printMessage(Diagnostic.Kind.NOTE, "No Annotaton to process");
        }


        round++;

        return true;
    }

     */
}
