package com.test.keggdata.objects;

import java.util.ArrayList;
import java.util.List;

public class Reaction {
    private String id;
    private String entry;
    private String name; // potrebbe servire
    private String equation; // contiene i compound
    private List<String> pathway = new ArrayList<>(); // da controllare nel file se presente

    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEntry()
    {
        return this.entry;
    }

    public void setEntry(String entry)
    {
        this.entry = entry;
    }

    public String getEquation()
    {
        return this.equation;
    }

    public void setEquation(String equation)
    {
        this.equation = equation;
    }

    public List<String> getPathway()
    {
        return this.pathway;
    }

    public void setPathway(List<String> pathway)
    {
        this.pathway = pathway;
    }

}
