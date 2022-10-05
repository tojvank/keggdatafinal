package com.test.keggdata.objects;

// oggetto enzima , contiene tutte le informazioni che sono contenute in un K0

import java.util.ArrayList;
import java.util.List;

public class Enzyme {

    // intanto prendo in considerazione queste
    private String id;
    private String entry;
    private String name;
    private List<String> pathway = new ArrayList<>(); // serve se non faccio il giro lungo, oppure per doppio controllo
    private List<String> dbLinks = new ArrayList<>(); // le reaction, se c'è RN:

    public String getId()
    {
        return this.id;
    }

    public Enzyme setId(String id)
    {
        this.id = id;
        return this;
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

    public List<String> getPathway()
    {
        return this.pathway;
    }

    public void setPathway(List<String> pathway)
    {
        this.pathway = pathway;
    }

    public List<String> getDbLinks()
    {
        return this.dbLinks;
    }

    public void setDbLinks(List<String> dbLinks)
    {
        this.dbLinks = dbLinks;
    }


}
