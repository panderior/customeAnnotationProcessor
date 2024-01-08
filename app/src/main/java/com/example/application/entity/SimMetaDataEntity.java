package com.example.application.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class SimMetaDataEntity
{
    String pkg;
    String model;
    String agent;
    String table_name;
    ArrayList<SimDBEntity> db_data;
    ArrayList<SimParamEntity> params;

    public SimMetaDataEntity(
            String pkg,
            String model,
            String agent,
            String table_name,
            ArrayList<SimDBEntity> db_data,
            ArrayList<SimParamEntity> params
    ){
        this.pkg = pkg;
        this.model = model;
        this.agent = agent;
        this.table_name = table_name;
        this.db_data = db_data;
        this.params = params;
    }
}
