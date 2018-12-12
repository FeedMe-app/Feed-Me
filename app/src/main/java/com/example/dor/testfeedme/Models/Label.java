package com.example.dor.testfeedme.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Label
{
    String key;
    String value;

    public Label(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String type)
    {
        this.key = type;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}
