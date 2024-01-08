package com.example.application.entity;

public class SimParamEntity
{
    String name;
    String type;
    String default_value;

    public SimParamEntity(String name, String type, String default_value)
    {
        this.name = name;
        this.type = type;
        this.default_value = default_value;
    }
}
