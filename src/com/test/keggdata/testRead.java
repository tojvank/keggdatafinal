package com.test.keggdata;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.stream.Stream;

//  questo file contiene il parsing fatto
public class testRead {
    public static ArrayList<String> IsEnzymeReader() throws IOException {
        boolean movetoNextFile = false;
        ArrayList<String> IsEnzyme = new ArrayList<>();
        ArrayList<String> IsEnzymeWithReaction = new ArrayList<>();

        File folder = new File("./K0");
        int totfiles=0;
        for (File file : folder.listFiles()) { //lettura singoli file
            totfiles++;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while(line!=null && !line.contains("GENES")) {
                if(line.contains("EC")){
                    IsEnzyme.add(file.getName()); //togliere .\k0\    .txt
                    // salvataggio delle varie cose per k0
                    line=reader.readLine();
                    while(line!=null){
                        if(line.contains("DBLINKS")){
                            if(line.contains("RN: ")){
                                // aggiungo le reaction che trovo
                                IsEnzymeWithReaction.add(file.getName()); //togliere .\k0\    .txt
                                //chiamata rest api su rn
                            }
                        }
                        line=reader.readLine();
                    }
                    break;
                }else{
                    line=reader.readLine();
                }
                System.out.println(line.contains("GENES"));
            }
        }
        System.out.println("Sono Enzimi: " + IsEnzyme.size() + " su totale di " + totfiles); // 1819 su 1915

        System.out.println("Sono Enzimi con reactions: " + IsEnzymeWithReaction.size() );
        // IsEnzymeGlobal = IsEnzyme;
        return IsEnzymeWithReaction;
    }

    public static ArrayList<String> IsEnzymeWithReactionReader(ArrayList<String> IsEnzyme) throws IOException {
        ArrayList<String> IsEnzymeWithR = new ArrayList<>();
        boolean Save=false;
        File folder = new File("./K0/");
        for (File file : folder.listFiles()) {
            for(String ss: IsEnzyme){
                if(ss.equals(file.getName())){
                    Save = true;
                }
                break;
            }
            if(Save){
                // vado in cerca della reaction
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                while(line!=null) {
                    if(line.contains("DBLINKS")){
                        if(line.contains("RN: ")){
                            IsEnzymeWithR.add(file.getName()); //togliere .\k0\    .txt
                            break;
                        }
                    }else{
                        line=reader.readLine();
                        //System.out.println("File: " + file + " non è un ENZIMA");
                    }
                    break;
               }
            }
        }
        return IsEnzymeWithR;
    }

    // dopo aggiungere nei parametri ArrayList<String> IsEnzymeWithR
    public static void KeggParser() throws IOException {
        // intanto con file singolo, una volta che funziona con singolo metterlo in un ciclo
        File file = new File("K0\\K01223.txt");

        BufferedReader br = new BufferedReader(new FileReader(file));
        String k0 = "";
        ArrayList<String> AllK0 = new ArrayList<>();
        ArrayList<String> AllEnzyme = new ArrayList<>();
        ArrayList<String> AllReaction = new ArrayList<>();
        String word = "";
        int c = 0;

        String st= br.readLine();

       for(String iterator: st.split(" ")){
            //word=st.lines(i);
           if(!(iterator.equals(""))){
               AllK0.add(iterator);
               c++;
               if(iterator.equals("GENES")){break;}
           }
       }
        System.out.println("==>" + AllK0);

        System.out.println("AllK0" + AllK0);
        System.out.println("AllEnzyme" + AllEnzyme);
        System.out.println("AllReaction" + AllReaction);
    }

    public static void main(String[] args) throws ParseException, IOException {
         ArrayList<String> IEnzyme = enzymeReader();

        //KeggParser();
        //System.out.println("Enzimi con reactions: " + enzymes + " con dimensione: " + enzymes.size());

        System.out.println("totale enzimi" + IEnzyme.size());
    }


    public static ArrayList<String> enzymeReader() throws IOException {
        boolean movetoNextFile = false;
        ArrayList<String> enzymeList = new ArrayList<>();
        ArrayList<String> IsEnzymeWithReaction = new ArrayList<>();



        /*Files.walk(Paths.get("./K0"))
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    try {
                        Stream<String> lineStream = Files.lines(path);
                        lineStream.forEach(line -> {
                            if (line.contains("GENES")) {
                                return;
                            }

                            System.out.println(line);

                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                });*/

        File folder = new File("./K0");



        int totfiles=0;
        for (File file : folder.listFiles()) { //lettura singoli file
            totfiles++;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while(line!=null && !line.contains("GENES")) {
                if(line.contains("EC")){
                    enzymeList.add(file.getName()); //togliere .\k0\    .txt
                    // salvataggio delle varie cose per k0
                    line=reader.readLine();
                    while(line!=null){
                        if(line.contains("DBLINKS")){
                            if(line.contains("RN: ")){
                                // aggiungo le reaction che trovo
                                IsEnzymeWithReaction.add(file.getName()); //togliere .\k0\    .txt
                                //chiamata rest api su rn
                            }
                        }
                        line=reader.readLine();
                    }
                    break;
                }else{
                    line=reader.readLine();
                }
                System.out.println(line.contains("GENES"));
            }
        }
        System.out.println("Sono Enzimi: " + enzymeList.size() + " su totale di " + totfiles); // 1819 su 1915

        System.out.println("Sono Enzimi con reactions: " + IsEnzymeWithReaction.size() );
        // IsEnzymeGlobal = IsEnzyme;
        return IsEnzymeWithReaction;
    }

}
