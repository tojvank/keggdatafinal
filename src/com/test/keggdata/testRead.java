package com.test.keggdata;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

//  questo file contiene il parsing fatto
public class testRead {

    private static Map<String, String> pairedMap = new HashMap<>();


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


        File folder = new File("./K0");

        int totfiles=0;
        int lastIndex = 0;
        for (File file : folder.listFiles()) {
            System.out.println("executing file:"+ file.getName() +" ---------------------------------------------------");
            totfiles++;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            List<String> pathWayNumbers = new ArrayList<>();
            List<String> pathWays = new ArrayList<>(); // list will contain index and pathway number ex: 0;01100 (which will save in hsa_nl file)
            ArrayList<String> reactionList = new ArrayList<>();
            while(line!=null && !line.contains("GENES")) {

                if(line.contains("EC")){

                    enzymeList.add(file.getName()); //togliere .\k0\    .txt
                    // salvataggio delle varie cose per k0
                    line=reader.readLine();
                    String pathWayLine = "";
                    while(line!=null && !line.contains("GENES")){
                        if (line.contains("PATHWAY")) {

                            // preparing pathway number
                            while (line != null && line.contains("map")) {
                                pathWayNumbers.add(getNumberFromPathWay(line));
                                pathWays.add(lastIndex+";"+ getNumberFromPathWay(line));
                                line=reader.readLine();
                                lastIndex ++;
                            }
                            System.out.println(pathWays);
                        }
                        if(line.contains("RN: ")){
                            // aggiungo le reaction che trovo
                            reactionList.addAll(getReactionsFromLine(line)); //togliere .\k0\    .txt
                            // saving pathway data to file (hsa_nl.txt)
                            File hsaNlFile = getHsaNlFile("hsa_nl");
                            Path path = Paths.get(hsaNlFile.toURI());
                            Files.write(path, pathWays, StandardOpenOption.APPEND);


                            //chiamata rest api su rn
                            makeApiCall(reactionList, pathWayNumbers);
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


        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("----------------------------------------------");
        List<String> finalListToSave = new ArrayList<>();
        pairedMap.forEach((s, s2) -> finalListToSave.add(s+";"+s2));

        File hsaNlFile = getHsaNlFile("hsa_adj");
        Path path = Paths.get(hsaNlFile.toURI());
        Files.write(path, finalListToSave, StandardOpenOption.APPEND);

        // IsEnzymeGlobal = IsEnzyme;
        return enzymeList;
    }

    private static void makeApiCall(List<String> reactionList, List<String> pathWayNumbers) throws IOException {
        List<String> repeatedCompoundList = new ArrayList<>();
        Set<String> uniqueCompoundSet = new HashSet<>();

        List<List<String>> repList = new ArrayList<>();

        Set<String> matchedPathWayList = new HashSet<>();

        int counter = 0;
        for (String elem : reactionList) {
            String urlCreated = "https://rest.kegg.jp/get/" + elem;
            URL url = new URL(urlCreated);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            counter++;
            System.out.println("GET Response Code :: " + responseCode + "counter: " + counter);


            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {

                    // preparing repeated compound list and unique compound set
                    if (inputLine.contains("EQUATION")) {
                        List<String> compoundList = Arrays.stream(inputLine.split(" "))
                                .filter(s -> s.startsWith("C"))
                                .collect(Collectors.toList());
                        repeatedCompoundList.addAll(compoundList);
                        uniqueCompoundSet.addAll(compoundList);
                        repList.add(compoundList);
                    }

                    if (inputLine.contains("rn")) {
                        String reactionPathWayNumber = getNumberFromReactionPathWay(inputLine);
                        if (pathWayNumbers.contains(reactionPathWayNumber)) {
                            // we found a node
                            matchedPathWayList.add(reactionPathWayNumber);
                            System.out.println("matched->"+ reactionPathWayNumber);
                        }

                    }
                }

                in.close();
            } else {
                System.out.println("GET request not worked");
            }
        }

//        System.out.println(repeatedCompoundList);
//        System.out.println(uniqueCompoundSet);

        System.out.println(repList);
        //String outputStr = String.join(";", matchedPathWayList) + ";"+ repeatCount(repList);
        //System.out.println(outputStr+"--------------------------------------");

        createPairedList(new ArrayList<>(matchedPathWayList), repeatCount(repList));

    }

    private static int repeatCount(List<List<String>> repList) {
        // counting common in each compound with a map ex map.put("C00001", 3)
        Map<String, Integer> foundCountMap = new HashMap<>();
        Set<String> result = new HashSet<>(repList.get(0));
        result.forEach(s -> foundCountMap.put(s, 1));
        for (int i = 1; i < repList.size(); i++) {
            for (String s: result) {
                if (new HashSet<>(repList.get(i)).contains(s)) {
                    foundCountMap.put(s, foundCountMap.get(s) + 1);
                }
            }
        }

        AtomicInteger commonCount = new AtomicInteger();
        foundCountMap.forEach((compound, repeatNo) -> {
            if (repeatNo == repList.size()) {
                commonCount.getAndIncrement();
            }
        });
        return commonCount.get();
    }

    private static String getNumberFromPathWay(String pathWayLine) {
        int mapIndex = pathWayLine.indexOf("map");
        String ss = pathWayLine.substring(mapIndex, pathWayLine.indexOf(" ", mapIndex)).strip();
        return ss.replaceFirst("map", "");
    }

    private static String getNumberFromReactionPathWay(String pathWayLine) {
        int mapIndex = pathWayLine.indexOf("rn");
        String ss = pathWayLine.substring(mapIndex, pathWayLine.indexOf(" ", mapIndex)).strip();
        return ss.replaceFirst("rn", "");
    }

    public static List<String> getReactionsFromLine(String line) {
        String reactionStr = line.substring(line.lastIndexOf("RN:") + 3).strip();
        return Arrays.asList(reactionStr.split(" "));
    }

    public static File getHsaNlFile(String fileName) throws IOException {
        File file = new File("./output/"+fileName+".txt");
        File dir = new File("./output/");
        if (!dir.exists()) {
            dir.mkdir();
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }


    public static List<String> createPairedList(List<String> strList, int commonCompound) {
        List<String> pairedList = new ArrayList<>();
        for (int i = 0; i < strList.size(); i ++) {
            for (int j = i+1; j<=strList.size()-1; j++) {
                String key = strList.get(i) + ";" + strList.get(j);
                if (pairedMap.containsKey(key)) { // updating key with sum with previous one
                    String cc = pairedMap.get(key);
                    pairedMap.put(key, String.valueOf(Integer.parseInt(cc) + commonCompound));
                } else {
                    pairedMap.put(key, String.valueOf(commonCompound));
                }
                pairedList.add(strList.get(i) + ";" + strList.get(j) +";"+ commonCompound);
            }
        }
        System.out.println("paired list:"+pairedList);
        return pairedList;
    }
}
