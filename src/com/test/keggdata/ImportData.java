package com.test.keggdata;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
public class ImportData {
    // funziona che importa il file K0.txt e salva in un ArrayList di stringhe
    public static ArrayList<String> importFromFile(){
        ArrayList<String> allData = new ArrayList<>();
        try{
            File object = new File("ko.txt");
            Scanner myReader = new Scanner(object);
            while(myReader.hasNextLine()){
                String data = myReader.nextLine();
                allData.add(data);
            }
            myReader.close();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("An errore occured");
        }
        return allData;
    }

    // chiamata API rest utilizzando il metodo di kegg e passando i singoli K0
    public static void RestAPICall() throws IOException {
        ArrayList<String> dataE = importFromFile();
        System.out.println("Total K0 in the file: " + dataE.size());
        int counter=0;
        for(String elem: dataE){
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
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                    response.append('\n');
                }
                in.close();
                BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(elem + ".txt")));

                counter++;
                bwr.write(response.toString());
                bwr.flush();
                bwr.close();
            } else {
                System.out.println("GET request not worked");
            }
        }
        System.out.println("Total K0:" + counter);
    }

    public static void main(String[] args) {
        System.out.println("Inizio chiamata REST API");
        try {
            RestAPICall();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Fine chiamata REST API");
    }
}
