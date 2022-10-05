package com.test.keggdata;
// https://rest.kegg.jp/get/K02863
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class KeggParsing {
    public static ArrayList<String> EnzymeCounter(){
        ArrayList<String> IsEnzyme = new ArrayList<>();
        int EnzymeCounter=0;
        /* for(String elem: dataE) {
            File file = new File("K0\\K01223.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            int c = 0;
            while (br.readLine() != null) {
                c++; // conteggio totale righe
            }
        } */
        return IsEnzyme;
    }

    public static int RowsCounter()throws IOException {
        File file = new File("K0\\K01223.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        int c=0;
        while(br.readLine() != null){
            c++; // conteggio totale righe
        }
        return c;
    }
    public static void KeggParser() throws IOException {
        // File file = new File("K0\\K03544.txt");
        File file = new File("K0\\K01223.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));
        String k0 = "";
        List<String> AllK0 = new ArrayList<>();
        List<String> AllEnzyme = new ArrayList<>();
        List<String> AllReaction = new ArrayList<>();
        int c = 0;
        String st= br.readLine();

        while (st  != null){
            c++;
            if(st.contains("ENTRY")){ // salvo il nome di K0 per referenza
                k0 = (String) st.subSequence(12,18);
                System.out.println("K0 -------> "+ k0);
                AllK0.add(k0);
            }

            if(st.contains("NAME")){ // controllo se enzima
                if(st.contains("[EC:")){
                    AllEnzyme.add(k0);
                }else{
                    System.out.println("ERROR: It is not an Enzyme");
                    break;
                }
            }

            // MEGLIO CON SCANNER LA LETTURA
            if(st.contains("DBLINKS")){
                st=br.readLine();
                if(st.contains("RN:")){
                    String rc="";
                    int idx = 16;
                    int lenstr = 6;
                    if(st.contains("R0")){
                        // modo per salvare le stringhe senza conteggio spazi.
                        idx+=lenstr;
                        rc=st.substring(idx);
                        AllReaction.add(rc);
                    }
                    break;
                }
            }
            // salvare anche pathways information --map ecc
            st= br.readLine();
        }
        System.out.println("AllK0" + AllK0);
        System.out.println("AllEnzyme" + AllEnzyme);
        System.out.println("AllReaction" + AllReaction);
    }

    public static void main(String[] args) throws IOException {
         KeggParser();
    }
}
