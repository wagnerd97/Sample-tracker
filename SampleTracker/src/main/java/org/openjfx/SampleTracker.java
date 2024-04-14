package org.openjfx;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

/*
notes to self:
    data file location has to be different to run on my computer as opposed to moms computer
    can no longer run this program outside of IDE due to compatibility issues that have arisen now

Features to add:
    undo and redo button.

    maybe colour code rows

known bugs:
    in some instances a search or a removal of an item causes context menu to lock up. can't select any items.
    the fix required is to do another action using a permanent button.

build information:
    lib classes and sources:
    C:\Users\dusti\Documents\openjfx-11.0.2_windows-x64_bin-sdk\javafx-sdk-11.0.2\lib
    15 java version SDK java 15.0.1

 */


public class SampleTracker {

    // lets consider %HOMEPATH% or %USERPROFILE% as a way to more generically get to the users home directory
    //private String fileLocation = "C:\\Users\\User\\Documents\\SampleTrackerData\\";
    private String fileLocation;  // = "C:\\Users\\sandy\\Documents\\SampleTrackerData\\";
    private String fileName = "clientData.csv";


    private List<Client> clientList = new ArrayList<>();
    private List<Client> deletedClients = new ArrayList<>();
    private int dIndex = -1;
    private boolean saveRequired = false;


    private void importClients(String filename) throws IOException {
        Scanner file = new Scanner(Paths.get(filename));
        if(file.hasNextLine()){ // ignore the first line of file. it's the title line
            file.nextLine();
        }
        Integer counter = 1;
        while(file.hasNextLine()){
            String str = file.nextLine();
            String[] clientInfo = str.split(",");
            String[] finalInfo = new String[17];
            Arrays.fill(finalInfo, "");
            for(int i = 0;i<clientInfo.length;i++){
                finalInfo[i] = clientInfo[i];
            }
            Client tempClient = new Client();
            tempClient.setIndex(counter);
            tempClient.setSoldTo(finalInfo[0]);
            tempClient.setSoldPhone(finalInfo[1]);
            tempClient.setShipTo(finalInfo[2]);
            tempClient.setCompany(finalInfo[3]);
            tempClient.setAddress1(finalInfo[4]);
            tempClient.setAddress2(finalInfo[5]);
            tempClient.setCity(finalInfo[6]);
            tempClient.setRegion(finalInfo[7]);
            tempClient.setPostCode(finalInfo[8]);
            tempClient.setCountry(finalInfo[9]);
            tempClient.setShipPhone(finalInfo[10]);
            tempClient.setEmail(finalInfo[11]);
            tempClient.setShippedDate(finalInfo[12]);
            tempClient.setLicenseNum(finalInfo[13]);
            tempClient.setCertificateCompany(finalInfo[14]);
            tempClient.setComments(finalInfo[15].replaceAll("~", ","));
            tempClient.setCertificate(finalInfo[16]);

            clientList.add(tempClient);
            counter++;
        }
        file.close();
    }

    private void exportClients(String filename) throws IOException {
        StringBuilder builder = new StringBuilder();
        //add header line
        Client tempClient = new Client();
        builder.append(tempClient.getsoldToDispString()).append(",");
        builder.append(tempClient.getsoldPhoneDispString()).append(",");
        builder.append(tempClient.getshipToDispString()).append(",");
        builder.append(tempClient.getcompanyDispString()).append(",");
        builder.append(tempClient.getaddress1DispString()).append(",");
        builder.append(tempClient.getaddress2DispString()).append(",");
        builder.append(tempClient.getcityDispString()).append(",");
        builder.append(tempClient.getregionDispString()).append(",");
        builder.append(tempClient.getpostCodeDispString()).append(",");
        builder.append(tempClient.getcountryDispString()).append(",");
        builder.append(tempClient.getshipPhoneDispString()).append(",");
        builder.append(tempClient.getemailDispString()).append(",");
        builder.append(tempClient.getshippedDateDispString()).append(",");
        builder.append(tempClient.getlicenseNumDispString()).append(",");
        builder.append(tempClient.getcertificateCompanyDispString()).append(",");
        builder.append(tempClient.getcommentsDispString()).append(",");
        builder.append(tempClient.getcertificateDispString()).append("\n");
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


    public List<Client> getClientList(){
        return this.clientList;
    }


    //list clientInfo is expected to be 14 items long
    public Boolean addClient(List<String> clientInfo){
        Client tempClient = new Client();
        tempClient.setSoldTo(clientInfo.get(0));
        tempClient.setSoldPhone(clientInfo.get(1));
        tempClient.setShipTo(clientInfo.get(2));
        tempClient.setCompany(clientInfo.get(3));
        tempClient.setAddress1(clientInfo.get(4));
        tempClient.setAddress2(clientInfo.get(5));
        tempClient.setCity(clientInfo.get(6));
        tempClient.setRegion(clientInfo.get(7));
        tempClient.setPostCode(clientInfo.get(8).toUpperCase());
        tempClient.setCountry(clientInfo.get(9));
        tempClient.setShipPhone(clientInfo.get(10));
        tempClient.setEmail(clientInfo.get(11));
        tempClient.setShippedDate(clientInfo.get(12));
        tempClient.setLicenseNum(clientInfo.get(13));
        tempClient.setCertificateCompany(clientInfo.get(14));
        tempClient.setComments(clientInfo.get(15));
        tempClient.setCertificate("");

        tempClient.setIndex(clientList.size()+1);

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
        if(removeCertificateImage(zeroIndex + 1)){
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
        if (clientInfo.size() != 16){
            return false;
        }

        Integer i = itemNum-1;
        clientList.get(i).setSoldTo(clientInfo.get(0));
        clientList.get(i).setSoldPhone(clientInfo.get(1));
        clientList.get(i).setShipTo(clientInfo.get(2));
        clientList.get(i).setCompany(clientInfo.get(3));
        clientList.get(i).setAddress1(clientInfo.get(4));
        clientList.get(i).setAddress2(clientInfo.get(5));
        clientList.get(i).setCity(clientInfo.get(6));
        clientList.get(i).setRegion(clientInfo.get(7));
        clientList.get(i).setPostCode(clientInfo.get(8).toUpperCase());
        clientList.get(i).setCountry(clientInfo.get(9));
        clientList.get(i).setShipPhone(clientInfo.get(10));
        clientList.get(i).setEmail(clientInfo.get(11));
        clientList.get(i).setShippedDate(clientInfo.get(12));
        clientList.get(i).setLicenseNum(clientInfo.get(13));
        clientList.get(i).setCertificateCompany(clientInfo.get(14));
        clientList.get(i).setComments(clientInfo.get(15));

        saveRequired = true;
        return true;
    }


    public List<Client> filterClients(List<String> clientInfo, List<Client> clientList){
        /*search by fields
        sold to
        shipped to
        address1
        address2
        sold phone
        shipped phone
        email
        license #
        company
        comments
        */
        List<Client> filtered = new ArrayList<>();
        for (Client client : clientList) {
            if(!clientInfo.get(0).equals("")){
                if (client.getSoldTo().toLowerCase().contains(clientInfo.get(0).toLowerCase())) {
                    filtered.add(client);
                    continue;
                }else if(client.getShipTo().toLowerCase().contains(clientInfo.get(0).toLowerCase())){
                    filtered.add(client);
                    continue;
                }
            }
            if(!clientInfo.get(1).equals("")) {
                if (client.getShipTo().toLowerCase().contains(clientInfo.get(1).toLowerCase())) {
                    filtered.add(client);
                    continue;
                }else if(client.getSoldTo().toLowerCase().contains(clientInfo.get(1).toLowerCase())){
                    filtered.add(client);
                    continue;
                }
            }
            if(!clientInfo.get(2).equals("")){
                if(client.getAddress1().toLowerCase().contains(clientInfo.get(2).toLowerCase())){
                    filtered.add(client);
                    continue;
                }
            }
            if(!clientInfo.get(3).equals("")){
                if(client.getAddress2().toLowerCase().contains(clientInfo.get(3).toLowerCase())){
                    filtered.add(client);
                    continue;
                }
            }
            if(!clientInfo.get(4).equals("")){
                if(client.getSoldPhone().toLowerCase().replaceAll("-", "").
                        contains(clientInfo.get(4).toLowerCase().replaceAll("-",""))){
                    filtered.add(client);
                    continue;
                }
                if(client.getShipPhone().toLowerCase().replaceAll("-", "").
                        contains(clientInfo.get(4).toLowerCase().replaceAll("-",""))){
                    filtered.add(client);
                    continue;
                }
            }
            if(!clientInfo.get(5).equals("")){
                if(client.getShipPhone().toLowerCase().replaceAll("-","").
                        contains(clientInfo.get(5).toLowerCase().replaceAll("-",""))){
                    filtered.add(client);
                    continue;
                }
                if(client.getSoldPhone().toLowerCase().replaceAll("-","").
                        contains(clientInfo.get(5).toLowerCase().replaceAll("-",""))){
                    filtered.add(client);
                    continue;
                }
            }
            if(!clientInfo.get(6).equals("")){
                if(client.getEmail().toLowerCase().contains(clientInfo.get(6).toLowerCase())){
                    filtered.add(client);
                    continue;
                }
            }
            if(!clientInfo.get(7).equals("")) {
                if (client.getLicenseNum().toLowerCase().contains(clientInfo.get(7).toLowerCase())) {
                    filtered.add(client);
                    continue;
                }
            }
            if(!clientInfo.get(8).equals("")){
                if (client.getCompany().toLowerCase().contains(clientInfo.get(8).toLowerCase())){
                    filtered.add(client);
                    continue;
                }
            }
            if(!clientInfo.get(9).equals("")){
                if(commentsSearch(client.getComments().toLowerCase(), clientInfo.get(9).toLowerCase())){
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
            attributes.put(tempList[0].strip(), tempList[1].strip());
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

    public boolean addCertificateImage(Integer itemNum, File file) throws IOException{
        int i = itemNum-1;
        if(file != null){
            long millis = System.currentTimeMillis();
            String[] extension = file.toString().split("\\.");

            Path temp = Files.move(file.toPath(), Paths.get(fileLocation + "\\" + millis + "." + extension[extension.length -1]));

            clientList.get(i).setCertificate(millis + "." + extension[extension.length-1]);

            save();

            return true;

        }
        return false;
    }
    public boolean replaceCertificateImage(Integer itemNum, File file){
        removeCertificateImage(itemNum);
        try{
            addCertificateImage(itemNum, file);
            return true;
        }catch(IOException e){

        }
        return false;
    }


    public boolean removeCertificateImage(Integer itemNum){
        int i = itemNum-1;
        if(clientList.get(i).getCertificate().equals("")){
            return false;
        }

        File f = new File(fileLocation + "\\" + clientList.get(i).getCertificate());

        if(f.delete()){
            clientList.get(i).setCertificate("");
            save();
            return true;
        }

        return false;
    }

    public boolean viewCertificateImage(Integer itemNum){
        int i = itemNum-1;
        if(clientList.get(i).getCertificate().equals("")){
            return false;
        }

        File f = new File(fileLocation + "\\" + clientList.get(i).getCertificate());
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
