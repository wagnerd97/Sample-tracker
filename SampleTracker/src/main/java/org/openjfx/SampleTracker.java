package org.openjfx;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.List;

/*
Features to add:
    undo and redo button.

    maybe colour code rows

known bugs:
    in some instances a search or a removal of an item causes context menu to lock up. can't select any items.
    the fix required is to do another action using a permanent button.

 */


public class SampleTracker {

    private boolean columnsShownSet = false;

    private boolean  indexShow                    = false;
    private boolean  shipNameShow                 = false;
    private boolean  shipPhoneShow                = false;
    private boolean  shipCompanyShow              = false;
    private boolean  shipAddress1Show             = false;
    private boolean  shipAddress2Show             = false;
    private boolean  shipCityShow                 = false;
    private boolean  shipRegionShow               = false;
    private boolean  shipPostCodeShow             = false;
    private boolean  shipCountryShow              = false;
    private boolean  shipEmailShow                = false;
    private boolean  billNameShow                 = false;
    private boolean  billPhoneShow                = false;
    private boolean  billCompanyShow              = false;
    private boolean  billAddress1Show             = false;
    private boolean  billAddress2Show             = false;
    private boolean  billCityShow                 = false;
    private boolean  billRegionShow               = false;
    private boolean  billPostCodeShow             = false;
    private boolean  billCountryShow              = false;
    private boolean  billEmailShow                = false;
    private boolean  dateShippedShow              = false;
    private boolean  firstLicenseNumShow          = false;
    private boolean  firstCertificateCompanyShow  = false;
    private boolean  secondLicenseNumShow         = false;
    private boolean  secondCertificateCompanyShow = false;
    private boolean  commentsShow                 = false;
    private boolean  firstCertificateShow         = false;
    private boolean  secondCertificateShow        = false;
    private boolean  dateClientAddedShow          = false;
    private boolean  dateClientEditedShow         = false;

    private String fileLocation;
    private String fileName = "clientData.csv";


    private List<Client> clientList = new ArrayList<>();
    private List<Client> deletedClients = new ArrayList<>();
    private int dIndex = -1;
    private boolean saveRequired = false;

    private Integer current_csv_version = 2;


    private void importClients(String filename) throws IOException {
        clientList.clear();
        Scanner file = new Scanner(Paths.get(filename));
        String old_csv_version;
        if(file.hasNextLine()){ // First line of file is version information
            String str = file.nextLine();
            String[] versionInfo = str.split(",");
            old_csv_version = versionInfo[1];
        }
        if(file.hasNextLine()){ // ignore the Second line of file. it's the title line
            file.nextLine();
        }
        Integer counter = 1;
        while(file.hasNextLine()){
            String str = file.nextLine();
            String[] clientInfo = str.split(",");
            String[] finalInfo = new String[30];
            Arrays.fill(finalInfo, "");
            for(int i = 0;i<clientInfo.length;i++){
                finalInfo[i] = clientInfo[i];
            }
            Client tempClient = new Client();
            tempClient.setIndex(counter);

            tempClient.setShipName                (finalInfo[0]);
            tempClient.setShipPhone               (finalInfo[1]);
            tempClient.setShipCompany             (finalInfo[2]);
            tempClient.setShipAddress1            (finalInfo[3]);
            tempClient.setShipAddress2            (finalInfo[4]);
            tempClient.setShipCity                (finalInfo[5]);
            tempClient.setShipRegion              (finalInfo[6]);
            tempClient.setShipPostCode            (finalInfo[7]);
            tempClient.setShipCountry             (finalInfo[8]);
            tempClient.setShipEmail               (finalInfo[9]);
            tempClient.setBillName                (finalInfo[10]);
            tempClient.setBillPhone               (finalInfo[11]);
            tempClient.setBillCompany             (finalInfo[12]);
            tempClient.setBillAddress1            (finalInfo[13]);
            tempClient.setBillAddress2            (finalInfo[14]);
            tempClient.setBillCity                (finalInfo[15]);
            tempClient.setBillRegion              (finalInfo[16]);
            tempClient.setBillPostCode            (finalInfo[17]);
            tempClient.setBillCountry             (finalInfo[18]);
            tempClient.setBillEmail               (finalInfo[19]);
            tempClient.setDateShipped             (finalInfo[20]);
            tempClient.setFirstLicenseNum         (finalInfo[21]);
            tempClient.setFirstCertificateCompany (finalInfo[22]);
            tempClient.setSecondLicenseNum        (finalInfo[23]);
            tempClient.setSecondCertificateCompany(finalInfo[24]);
            tempClient.setComments                (finalInfo[25]);
            tempClient.setFirstCertificate        (finalInfo[26]);
            tempClient.setSecondCertificate       (finalInfo[27]);
            tempClient.setDateClientAdded         (finalInfo[28]);
            tempClient.setDateClientEdited        (finalInfo[29]);

            clientList.add(tempClient);
            counter++;
        }
        file.close();
    }

    public boolean getColumnsShownSet() {
        return columnsShownSet;
    }

    public void setColumnsShownSet() {
        columnsShownSet = true;
    }

    public boolean getAttributeShown(String attributeName) {
        switch (attributeName) {
            case "index":                    return indexShow;
            case "shipName":                 return shipNameShow;
            case "shipPhone":                return shipPhoneShow;
            case "shipCompany":              return shipCompanyShow;
            case "shipAddress1":             return shipAddress1Show;
            case "shipAddress2":             return shipAddress2Show;
            case "shipCity":                 return shipCityShow;
            case "shipRegion":               return shipRegionShow;
            case "shipPostCode":             return shipPostCodeShow;
            case "shipCountry":              return shipCountryShow;
            case "shipEmail":                return shipEmailShow;
            case "billName":                 return billNameShow;
            case "billPhone":                return billPhoneShow;
            case "billCompany":              return billCompanyShow;
            case "billAddress1":             return billAddress1Show;
            case "billAddress2":             return billAddress2Show;
            case "billCity":                 return billCityShow;
            case "billRegion":               return billRegionShow;
            case "billPostCode":             return billPostCodeShow;
            case "billCountry":              return billCountryShow;
            case "billEmail":                return billEmailShow;
            case "dateShipped":              return dateShippedShow;
            case "firstLicenseNum":          return firstLicenseNumShow;
            case "firstCertificateCompany":  return firstCertificateCompanyShow;
            case "secondLicenseNum":         return secondLicenseNumShow;
            case "secondCertificateCompany": return secondCertificateCompanyShow;
            case "comments":                 return commentsShow;
            case "firstCertificate":         return firstCertificateShow;
            case "secondCertificate":        return secondCertificateShow;
            case "dateClientAdded":          return dateClientAddedShow;
            case "dateClientEdited":         return dateClientEditedShow;
            default: return false;
        }
    }

    public boolean setAttributeShown(String attributeName, boolean newState) {
        switch (attributeName) {
            case "index":                    indexShow = newState; break;
            case "shipName":                 shipNameShow = newState; break;
            case "shipPhone":                shipPhoneShow = newState; break;
            case "shipCompany":              shipCompanyShow = newState; break;
            case "shipAddress1":             shipAddress1Show = newState; break;
            case "shipAddress2":             shipAddress2Show = newState; break;
            case "shipCity":                 shipCityShow = newState; break;
            case "shipRegion":               shipRegionShow = newState; break;
            case "shipPostCode":             shipPostCodeShow = newState; break;
            case "shipCountry":              shipCountryShow = newState; break;
            case "shipEmail":                shipEmailShow = newState; break;
            case "billName":                 billNameShow = newState; break;
            case "billPhone":                billPhoneShow = newState; break;
            case "billCompany":              billCompanyShow = newState; break;
            case "billAddress1":             billAddress1Show = newState; break;
            case "billAddress2":             billAddress2Show = newState; break;
            case "billCity":                 billCityShow = newState; break;
            case "billRegion":               billRegionShow = newState; break;
            case "billPostCode":             billPostCodeShow = newState; break;
            case "billCountry":              billCountryShow = newState; break;
            case "billEmail":                billEmailShow = newState; break;
            case "dateShipped":              dateShippedShow = newState; break;
            case "firstLicenseNum":          firstLicenseNumShow = newState; break;
            case "firstCertificateCompany":  firstCertificateCompanyShow = newState; break;
            case "secondLicenseNum":         secondLicenseNumShow = newState; break;
            case "secondCertificateCompany": secondCertificateCompanyShow = newState; break;
            case "comments":                 commentsShow = newState; break;
            case "firstCertificate":         firstCertificateShow = newState; break;
            case "secondCertificate":        secondCertificateShow = newState; break;
            case "dateClientAdded":          dateClientAddedShow = newState; break;
            case "dateClientEdited":         dateClientEditedShow = newState; break;
            default: return false;
        }
        return true;
    }

    private void exportClients(String filename) throws IOException {
        StringBuilder builder = new StringBuilder();
        // add version line
        builder.append("Version").append(",");
        builder.append(current_csv_version.toString()).append("\n");
        //add header line
        Client tempClient = new Client();
        builder.append(tempClient.getShipNameDispString()).append(",");
        builder.append(tempClient.getShipPhoneDispString()).append(",");
        builder.append(tempClient.getShipCompanyDispString()).append(",");
        builder.append(tempClient.getShipAddress1DispString()).append(",");
        builder.append(tempClient.getShipAddress2DispString()).append(",");
        builder.append(tempClient.getShipCityDispString()).append(",");
        builder.append(tempClient.getShipRegionDispString()).append(",");
        builder.append(tempClient.getShipPostCodeDispString()).append(",");
        builder.append(tempClient.getShipCountryDispString()).append(",");
        builder.append(tempClient.getShipEmailDispString()).append(",");
        builder.append(tempClient.getBillNameDispString()).append(",");
        builder.append(tempClient.getBillPhoneDispString()).append(",");
        builder.append(tempClient.getBillCompanyDispString()).append(",");
        builder.append(tempClient.getBillAddress1DispString()).append(",");
        builder.append(tempClient.getBillAddress2DispString()).append(",");
        builder.append(tempClient.getBillCityDispString()).append(",");
        builder.append(tempClient.getBillRegionDispString()).append(",");
        builder.append(tempClient.getbillPostCodeDispString()).append(",");
        builder.append(tempClient.getbillCountryDispString()).append(",");
        builder.append(tempClient.getbillEmailDispString()).append(",");
        builder.append(tempClient.getdateShippedDispString()).append(",");
        builder.append(tempClient.getfirstLicenseNumDispString()).append(",");
        builder.append(tempClient.getfirstCertificateCompanyDispString()).append(",");
        builder.append(tempClient.getsecondLicenseNumDispString()).append(",");
        builder.append(tempClient.getsecondCertificateCompanyDispString()).append(",");
        builder.append(tempClient.getcommentsDispString()).append(",");
        builder.append(tempClient.getfirstCertificateDispString()).append(",");
        builder.append(tempClient.getsecondCertificateDispString()).append(",");
        builder.append(tempClient.getdateClientAddedDispString()).append(",");
        builder.append(tempClient.getdateClientEditedDispString()).append("\n");
        for (Client client : clientList) {
            builder.append(client.toString());
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write(builder.toString());
        writer.close();
    }


    public boolean populate(){
        String filename = this.fileLocation + "\\" + this.fileName;

        try {

            importClients(filename);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public void setFileLocation(String filePath) {
        fileLocation = filePath;
    }

    public boolean save(){
        String filename = this.fileLocation + "\\" + this.fileName;

        try {
            exportClients(filename);
        } catch (IOException e) {
            return false;
        }
        saveRequired = false;
        return true;
    }

    public String getCurrentDateString() {
        DateTimeFormatter dtf = new DateTimeFormatterBuilder().parseCaseSensitive().appendPattern("MMM dd yyyy").toFormatter();
        LocalDate currentDate = LocalDate.now();
        return currentDate.format(dtf);
    }


    public List<Client> getClientList(){
        return this.clientList;
    }


    //list clientInfo is expected to be ? items long
    public Boolean addClient(List<String> clientInfo){
        Client tempClient = new Client();

        tempClient.setIndex(clientList.size()+1);

        tempClient.setShipName                (clientInfo.get(0));
        tempClient.setShipPhone               (clientInfo.get(1));
        tempClient.setShipCompany             (clientInfo.get(2));
        tempClient.setShipAddress1            (clientInfo.get(3));
        tempClient.setShipAddress2            (clientInfo.get(4));
        tempClient.setShipCity                (clientInfo.get(5));
        tempClient.setShipRegion              (clientInfo.get(6));
        tempClient.setShipPostCode            (clientInfo.get(7));
        tempClient.setShipCountry             (clientInfo.get(8));
        tempClient.setShipEmail               (clientInfo.get(9));
        tempClient.setBillName                (clientInfo.get(10));
        tempClient.setBillPhone               (clientInfo.get(11));
        tempClient.setBillCompany             (clientInfo.get(12));
        tempClient.setBillAddress1            (clientInfo.get(13));
        tempClient.setBillAddress2            (clientInfo.get(14));
        tempClient.setBillCity                (clientInfo.get(15));
        tempClient.setBillRegion              (clientInfo.get(16));
        tempClient.setBillPostCode            (clientInfo.get(17));
        tempClient.setBillCountry             (clientInfo.get(18));
        tempClient.setBillEmail               (clientInfo.get(19));
        tempClient.setDateShipped             (clientInfo.get(20));
        tempClient.setFirstLicenseNum         (clientInfo.get(21));
        tempClient.setFirstCertificateCompany (clientInfo.get(22));
        tempClient.setSecondLicenseNum        (clientInfo.get(23));
        tempClient.setSecondCertificateCompany(clientInfo.get(24));
        tempClient.setComments                (clientInfo.get(25));
        tempClient.setFirstCertificate        ("");
        tempClient.setSecondCertificate       ("");
        // Add a function that can help us set these dates
        tempClient.setDateClientAdded         (getCurrentDateString());
        tempClient.setDateClientEdited        (getCurrentDateString());

        saveRequired = true;
        return clientList.add(tempClient);
    }

    public void deleteClient(String itemNum){
        int zeroIndex = Integer.parseInt(itemNum);
        zeroIndex--;
        if(zeroIndex < 0 || zeroIndex >= clientList.size()){
            return;
        }
        boolean imageProcessed = false;
        if(removeCertificateImage(zeroIndex + 1, 1) && removeCertificateImage(zeroIndex + 1, 2)){
            imageProcessed = true;
        }
        for(int i =zeroIndex+1; i<clientList.size();i++){
            clientList.get(i).setIndex(i);
        }
        clientList.remove(zeroIndex);
        saveRequired = !imageProcessed;
    }

    public Client getClient(String itemNum){
        int trueNum = Integer.parseInt(itemNum);
        trueNum--;
        if(trueNum >= 0 && trueNum < clientList.size()){
            return clientList.get(trueNum);
        }
        return null;
    }

    public Boolean makeChanges(List<String> clientInfo, Integer itemNum){
        if (clientInfo.size() != 26){
            return false;
        }

        Integer i = itemNum-1;

        clientList.get(i).setShipName                (clientInfo.get(0));
        clientList.get(i).setShipPhone               (clientInfo.get(1));
        clientList.get(i).setShipCompany             (clientInfo.get(2));
        clientList.get(i).setShipAddress1            (clientInfo.get(3));
        clientList.get(i).setShipAddress2            (clientInfo.get(4));
        clientList.get(i).setShipCity                (clientInfo.get(5));
        clientList.get(i).setShipRegion              (clientInfo.get(6));
        clientList.get(i).setShipPostCode            (clientInfo.get(7));
        clientList.get(i).setShipCountry             (clientInfo.get(8));
        clientList.get(i).setShipEmail               (clientInfo.get(9));
        clientList.get(i).setBillName                (clientInfo.get(10));
        clientList.get(i).setBillPhone               (clientInfo.get(11));
        clientList.get(i).setBillCompany             (clientInfo.get(12));
        clientList.get(i).setBillAddress1            (clientInfo.get(13));
        clientList.get(i).setBillAddress2            (clientInfo.get(14));
        clientList.get(i).setBillCity                (clientInfo.get(15));
        clientList.get(i).setBillRegion              (clientInfo.get(16));
        clientList.get(i).setBillPostCode            (clientInfo.get(17));
        clientList.get(i).setBillCountry             (clientInfo.get(18));
        clientList.get(i).setBillEmail               (clientInfo.get(19));
        clientList.get(i).setDateShipped             (clientInfo.get(20));
        clientList.get(i).setFirstLicenseNum         (clientInfo.get(21));
        clientList.get(i).setFirstCertificateCompany (clientInfo.get(22));
        clientList.get(i).setSecondLicenseNum        (clientInfo.get(23));
        clientList.get(i).setSecondCertificateCompany(clientInfo.get(24));
        clientList.get(i).setComments                (clientInfo.get(25));
        clientList.get(i).setDateClientEdited        (getCurrentDateString());

        saveRequired = true;
        return true;
    }


    public List<Client> filterClients(Map<String, String> clientInfo, List<Client> clientList){
        List<Client> filtered = new ArrayList<>();
        for (Client client : clientList) {
            if(clientInfo.get("shipName") != null && !clientInfo.get("shipName").isEmpty()){
                if (client.getShipName().toLowerCase().contains(clientInfo.get("shipName").toLowerCase())) {
                    filtered.add(client);
                    continue;
                }else if(client.getBillName().toLowerCase().contains(clientInfo.get("shipName").toLowerCase())){
                    filtered.add(client);
                    continue;
                }
            }
            if(clientInfo.get("soldName") != null && !clientInfo.get("soldName").isEmpty()) {
                if (client.getShipName().toLowerCase().contains(clientInfo.get("soldName").toLowerCase())) {
                    filtered.add(client);
                    continue;
                }else if(client.getBillName().toLowerCase().contains(clientInfo.get("soldName").toLowerCase())){
                    filtered.add(client);
                    continue;
                }
            }
            if(clientInfo.get("shipAddress1") != null && !clientInfo.get("shipAddress1").isEmpty()){
                if(client.getShipAddress1().toLowerCase().contains(clientInfo.get("shipAddress1").toLowerCase())){
                    filtered.add(client);
                    continue;
                } else if(client.getBillAddress1().toLowerCase().contains(clientInfo.get("shipAddress1").toLowerCase())){
                    filtered.add(client);
                    continue;
                }
            }
            if(clientInfo.get("billAddress1") != null && !clientInfo.get("billAddress1").isEmpty()){
                if(client.getShipAddress1().toLowerCase().contains(clientInfo.get("shipAddress1").toLowerCase())){
                    filtered.add(client);
                    continue;
                } else if(client.getBillAddress1().toLowerCase().contains(clientInfo.get("shipAddress1").toLowerCase())){
                    filtered.add(client);
                    continue;
                }
            }
            if(clientInfo.get("shipAddress2") != null && !clientInfo.get("shipAddress2").isEmpty()){
                if(client.getShipAddress2().toLowerCase().contains(clientInfo.get("shipAddress2").toLowerCase())){
                    filtered.add(client);
                    continue;
                } else if(client.getBillAddress2().toLowerCase().contains(clientInfo.get("shipAddress2").toLowerCase())){
                    filtered.add(client);
                    continue;
                }
            }
            if(clientInfo.get("billAddress2") != null && !clientInfo.get("billAddress2").isEmpty()){
                if(client.getShipAddress2().toLowerCase().contains(clientInfo.get("billAddress2").toLowerCase())){
                    filtered.add(client);
                    continue;
                } else if(client.getBillAddress2().toLowerCase().contains(clientInfo.get("billAddress2").toLowerCase())){
                    filtered.add(client);
                    continue;
                }
            }
            if(clientInfo.get("shipPhone") != null && !clientInfo.get("shipPhone").isEmpty()){
                if(client.getShipPhone().toLowerCase().replaceAll("-", "").
                        contains(clientInfo.get("shipPhone").toLowerCase().replaceAll("-",""))){
                    filtered.add(client);
                    continue;
                }
                if(client.getBillPhone().toLowerCase().replaceAll("-", "").
                        contains(clientInfo.get("shipPhone").toLowerCase().replaceAll("-",""))){
                    filtered.add(client);
                    continue;
                }
            }
            if(clientInfo.get("billPhone") != null && !clientInfo.get("billPhone").isEmpty()){
                if(client.getShipPhone().toLowerCase().replaceAll("-","").
                        contains(clientInfo.get("billPhone").toLowerCase().replaceAll("-",""))){
                    filtered.add(client);
                    continue;
                }
                if(client.getBillPhone().toLowerCase().replaceAll("-","").
                        contains(clientInfo.get("billPhone").toLowerCase().replaceAll("-",""))){
                    filtered.add(client);
                    continue;
                }
            }
            if(clientInfo.get("shipEmail") != null && !clientInfo.get("shipEmail").isEmpty()){
                if(client.getShipEmail().toLowerCase().contains(clientInfo.get("shipEmail").toLowerCase())){
                    filtered.add(client);
                    continue;
                } else if(client.getBillEmail().toLowerCase().contains(clientInfo.get("shipEmail").toLowerCase())){
                    filtered.add(client);
                    continue;
                }
            }
            if(clientInfo.get("billEmail") != null && !clientInfo.get("billEmail").isEmpty()){
                if(client.getShipEmail().toLowerCase().contains(clientInfo.get("billEmail").toLowerCase())){
                    filtered.add(client);
                    continue;
                } else if(client.getBillEmail().toLowerCase().contains(clientInfo.get("billEmail").toLowerCase())){
                    filtered.add(client);
                    continue;
                }
            }
            if(clientInfo.get("firstLicenseNum") != null && !clientInfo.get("firstLicenseNum").isEmpty()) {
                if (client.getFirstLicenseNum().toLowerCase().contains(clientInfo.get("firstLicenseNum").toLowerCase())) {
                    filtered.add(client);
                    continue;
                } else if (client.getSecondLicenseNum().toLowerCase().contains(clientInfo.get("firstLicenseNum").toLowerCase())) {
                    filtered.add(client);
                    continue;
                }
            }
            if(clientInfo.get("secondLicenseNum") != null && !clientInfo.get("secondLicenseNum").isEmpty()) {
                if (client.getFirstLicenseNum().toLowerCase().contains(clientInfo.get("secondLicenseNum").toLowerCase())) {
                    filtered.add(client);
                    continue;
                } else if (client.getSecondLicenseNum().toLowerCase().contains(clientInfo.get("secondLicenseNum").toLowerCase())) {
                    filtered.add(client);
                    continue;
                }
            }
            if(clientInfo.get("firstCertificateCompany") != null && !clientInfo.get("firstCertificateCompany").isEmpty()){
                if (client.getFirstCertificateCompany().toLowerCase().contains(clientInfo.get("firstCertificateCompany").toLowerCase())){
                    filtered.add(client);
                    continue;
                } else if (client.getSecondCertificateCompany().toLowerCase().contains(clientInfo.get("firstCertificateCompany").toLowerCase())){
                    filtered.add(client);
                    continue;
                }
            }
            if(clientInfo.get("secondCertificateCompany") != null && !clientInfo.get("secondCertificateCompany").isEmpty()){
                if (client.getFirstCertificateCompany().toLowerCase().contains(clientInfo.get("secondCertificateCompany").toLowerCase())){
                    filtered.add(client);
                    continue;
                } else if (client.getSecondCertificateCompany().toLowerCase().contains(clientInfo.get("secondCertificateCompany").toLowerCase())){
                    filtered.add(client);
                    continue;
                }
            }
            if(clientInfo.get("comments") != null && !clientInfo.get("comments").isEmpty()){
                if(commentsSearch(client.getComments().toLowerCase(), clientInfo.get("comments").toLowerCase())){
                    filtered.add(client);
                }
            }
        }

        return filtered;
    }

    private boolean commentsSearch(String permClient, String tempClient){
        String[] permWords = permClient.split(" ");
        String[] tempWords = tempClient.split(" ");
        for(int i =0; i<tempWords.length; i++){
            for(int j=0; j<permWords.length; j++){
                if(tempWords[i].contains(permWords[j]) || permWords[j].contains(tempWords[i])){
                    if(permWords[j].length()>2 && tempWords[i].length()>2) {
                        return true;
                    }
                }
            }
        }

        return false;
    }




    public List<String> parsePlain(String content){
        List<String> dataForClient;
        String[] initialParse = (content.split("\n"));
        int size = initialParse.length;

        //the inclusive indexes of the content
        int soldStart = 0;
        int soldEnd = 0;
        int shipStart = 0;
        int shipEnd = 0;
        for(int i=0; i<size; i++){
            if(initialParse[i].toLowerCase().contains("sold to")){
                initialParse[i] = initialParse[i].replaceAll("Sold To", "");
                soldStart = i;
            }
            if(initialParse[i].toLowerCase().contains("ship to")){
                soldEnd = i-1;
                shipStart = i+2;
            }
        }
        if(initialParse.length > 1){
            if(initialParse[initialParse.length-1].equals("Address Verified")){
                shipEnd = initialParse.length-2;
            }else {
                shipEnd = initialParse.length - 1;
            }
        }
        dataForClient = parseSold(initialParse, soldStart, soldEnd);
        dataForClient.addAll(parseShip(initialParse, shipStart, shipEnd));

        return dataForClient;
    }

    private List<String> parseSold(String[] data, int start, int end){
        //elements: name, phone number, email.
        List<String> sendIt = new ArrayList<>();
        sendIt.add(data[start].strip());
        int i;
        for(i = start;i<=end; i++){
            if(data[i].contains("+") || data[i].contains("-")){
                if(sendIt.size() == 1){
                    sendIt.add(data[i].strip());
                }
                else{
                    sendIt.clear();
                    return sendIt;
                }
            }
            if(data[i].contains(".") || data[i].contains("@")){
                if(sendIt.size() == 1){
                    sendIt.add("");
                    sendIt.add(data[i]);
                }else if(sendIt.size() == 2){
                    sendIt.add(data[i]);
                }
            }
        }

        if(sendIt.size() == 3){
            return sendIt;
        }
        sendIt = new ArrayList<>(3);
        for(i=0; i< 3; i++){
            sendIt.add("");
        }
        return sendIt;
    }

    private List<String> parseShip(String[] data, int start, int end){
        List<String> shipData = new ArrayList<>(9);
        for(int i =0; i < 9; i++){
            shipData.add("");
        }
        if(end - start < 2){
            return shipData;
        }
        shipData.set(0, data[start]);
        start++;


        List<String> address;
        if(data[end].contains(",")){
            address = parseAddress(data[end]);
            end--;
        }else{
            address = parseAddress(data[end-1]);
            shipData.set(8, data[end]);
            end = end-2;
        }
        if(address.size() == 4){
            for(int i = 0; i<4 ; i++){
                shipData.set(i+4, address.get(i));
            }
        }

        if(end - start == 2){
            shipData.set(1, data[start]);
            shipData.set(2, data[start + 1]);
            shipData.set(3, data[start + 2]);
        }else if(end - start == 0){
            shipData.set(2, data[start]);
        }else if(end - start == 1){
            boolean addressFlag = false;
            for(int i = 0; i < data[start].length(); i++){
                if(data[start].charAt(i) >= '0' && data[start].charAt(i) <= '9'){
                    addressFlag = true;
                }
            }
            if(addressFlag){
                shipData.set(2, data[start]);
                shipData.set(3, data[start + 1]);
            }else{
                shipData.set(1, data[start]);
                shipData.set(2,data[start + 1]);
            }
        }


        return shipData;
    }

    private List<String> parseAddress(String data){
        List<String> sendIt = new ArrayList<>();
        String[] temp = (data.split(","));
        temp[1] = temp[1].strip();
        if(temp[1].length() < 7){
            return sendIt;
        }
        sendIt.add(temp[0]);
        //break off province and add
        if(isAlphabet(temp[1].charAt(0)) && isAlphabet(temp[1].charAt(1)) && temp[1].charAt(2) == ' ') {
            String myString = String.valueOf(temp[1].charAt(0)) +
                    temp[1].charAt(1);
            sendIt.add(myString);

            //break off country and add
            if (temp[1].charAt(temp[1].length() - 3) == ' ') {
                myString = String.valueOf(temp[1].charAt(temp[1].length() - 2)) +
                        temp[1].charAt(temp[1].length() - 1);
                sendIt.add(myString);

                //pull out postal code and add it
                StringBuilder string = new StringBuilder();
                for(int i = 3; i< (temp[1].length()-3) ; i++){
                    string.append(temp[1].charAt(i));
                }
                sendIt.add(string.toString());
            }
        }
        return sendIt;
    }
    public Map<String, String> parseJson(String data) {
        Map<String, String> attributes = new HashMap<>();
        data = data.replaceAll("\\{", "").replaceAll("}", "");
        data = data.replaceAll("\"", "").replaceAll("\'", "");
        String[] toupleList = (data.split(","));
        for(int i = 0; i < toupleList.length; i++) {
            String[] tempList = (toupleList[i].split(":"));
            if (tempList.length == 2) {
                attributes.put(tempList[0].strip(), tempList[1].strip());
            } else {
                attributes.put(tempList[0].strip(), "");
            }
            
        }
        return attributes;
    }

    public String makeJson(Map<String, String> attributes) {
        StringBuilder builder = new StringBuilder();
        builder.append("\'");
        builder.append("\\{");
        builder.append("\n");

        for(Map.Entry<String, String> entry : attributes.entrySet()) {
            String entryKey = entry.getKey();
            String valueKey = entry.getValue();
            builder.append("\t\"");
            builder.append(entryKey);
            builder.append("\":\"");
            builder.append(valueKey);
            builder.append("\",\n");
        }
        builder.append("}");
        builder.append("\'");


        return builder.toString();
    }


    private boolean isAlphabet(char c){
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
    }


    public boolean isSaveRequired(){
        return saveRequired;
    }
    public void setSaveRequired(){
        saveRequired = true;
    }

    public boolean addCertificateImage(Integer itemNum, File file, Integer whichCertificate) {
        int i = itemNum-1;
        if(file != null){
            long millis = System.currentTimeMillis();
            String[] extension = file.toString().split("\\.");

            try {
                Files.move(file.toPath(), Paths.get(fileLocation + "\\" + millis + "." + extension[(extension.length - 1)]));
            } catch (Exception e) {
                System.out.println("Could not move file: " + e.getMessage());
            }
            
            switch(whichCertificate) {
                case 1:
                    clientList.get(i).setFirstCertificate(millis + "." + extension[extension.length - 1]);
                    break;
                case 2:
                    clientList.get(i).setSecondCertificate(millis + "." + extension[extension.length - 1]);
                    break;
                default:
                    return false;
            }
                
            save();

            return true;

        }
        return false;
    }
    public boolean addCertificateURL(Integer itemNum, String URL, Integer whichCertificate) {
        int i = itemNum-1;
        if(URL != ""){
            switch(whichCertificate) {
                case 1:
                    clientList.get(i).setFirstCertificate(URL);
                    break;
                case 2:
                    clientList.get(i).setSecondCertificate(URL);
                    break;
                default:
                    return false;
            }
                
            save();

            return true;

        }
        return false;
    }
    public boolean replaceCertificateImage(Integer itemNum, File file, Integer whichCertificate){
        removeCertificateImage(itemNum, whichCertificate);
        addCertificateImage(itemNum, file, whichCertificate);
        return true;
    }


    public boolean removeCertificateImage(Integer itemNum, Integer whichCertificate){
        int i = itemNum-1;
        String tempStringCertificate = new String("");
        switch (whichCertificate) {
            case 1:
                tempStringCertificate = clientList.get(i).getFirstCertificate();
                break;
            case 2:
                tempStringCertificate = clientList.get(i).getSecondCertificate();
                break;
            default:
                return false;
        }
        if(tempStringCertificate.equals("")){
            return false;
        }

        try {
            URI imageURI = new URI(tempStringCertificate);
            switch (whichCertificate) {
                case 1:
                    clientList.get(i).setFirstCertificate("");
                    break;
                case 2:
                    clientList.get(i).setSecondCertificate("");
                    break;
            }
            if (imageURI.toString() == "") {
                System.out.println("URL is unintentionally empty");
            }
            return false;
        } catch (Exception e) {
            System.out.println("Not a URL");
        }

        File f = new File(fileLocation + "\\" + tempStringCertificate);

        if(f.delete()){
            switch (whichCertificate) {
                case 1:
                    clientList.get(i).setFirstCertificate("");
                    break;
                case 2:
                    clientList.get(i).setSecondCertificate("");
                    break;
            }
            save();
            return true;
        }

        return false;
    }

    public boolean viewCertificateImage(Integer itemNum, Integer whichCertificate){
        int i = itemNum-1;
        String tempStringCertificate = new String("");
        switch (whichCertificate) {
            case 1:
                tempStringCertificate = clientList.get(i).getFirstCertificate();
                break;
            case 2:
                tempStringCertificate = clientList.get(i).getSecondCertificate();
                break;
            default:
                return false;
        }
        if(tempStringCertificate.equals("")){
            return false;
        }

        try {
            URI imageURI = new URI(tempStringCertificate);
            if (imageURI.toString() == "") {
                System.out.println("URL is unintentionally empty");
                return false;
            }
            Desktop.getDesktop().browse(imageURI);
            return false;
        } catch (Exception e) {
            System.out.println("Not a URL");
        }

        File f = new File(fileLocation + "\\" + tempStringCertificate);
        try {
            Desktop.getDesktop().open(f);
        }catch(IOException e){

        }

        return false;
    }

    //Implementation functions for undo and redo

    private boolean canUndo(){
        return dIndex >= 0;
    }
    private boolean canRedo(){

        return false;
    }

    public boolean undo(){
        if(!canUndo()){
            return false;
        }
        //Do the undo
        //change the value of dIndex
        Operation tempOp = deletedClients.get(dIndex).getOperation();
        // Change to switch case
        if(tempOp == Operation.ADD){
            //need to delete the client
        }else if(tempOp == Operation.DELETE){
            //need to put the client back in their spot
        }else if(tempOp == Operation.EDIT){
            //need to the restore the client to their previous version. swaps the old and new
        }else if(tempOp == Operation.ADDPicture){
            //need to move picture to deleted and delete record of picture from client
        }else if(tempOp == Operation.DELETEWithPicture){
            //need to restore the client in their position appropriately and put the picture back in the correct folder
        }else if(tempOp == Operation.REPLACEPicture){
            //put the current picture in deleted and take the old deleted picture and put it back.
        }else if(tempOp == Operation.DELETEPicture){
            //need to restore picture from deleted and put record back in client
        }


        return true;
    }
    public boolean redo(){
        if(!canRedo()){
            return false;
        }
        //Do the redo
        //change the value of dIndex
        Operation tempOp = deletedClients.get(dIndex).getOperation();
        // Change to switch case
        if(tempOp == Operation.ADD){
            //need to add the client back in at it's appropriate spot
        }else if(tempOp == Operation.DELETE){
            //need to delete client and store in deleted
        }else if(tempOp == Operation.EDIT){
            //need to the restore the client to their previous version. swaps the old and new
        }else if(tempOp == Operation.ADDPicture){
            //need to need to go get the picture back from deleted and restore the client data of it
        }else if(tempOp == Operation.DELETEWithPicture){
            //need to delete the client and move picture and store client in the deleted list
        }else if(tempOp == Operation.REPLACEPicture){
            //put the current picture in deleted and take the old deleted picture and put it back
        }else if(tempOp == Operation.DELETEPicture){
            //need to move picture to deleted and remove record from client
        }
        return true;
    }



}
