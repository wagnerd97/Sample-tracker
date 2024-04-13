package org.openjfx;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Client {
    private Integer index;
    private String soldTo;
    private String soldPhone;
    private String shipTo;
    private String company;
    private String address1;
    private String address2;
    private String city;
    private String region;
    private String postCode;
    private String country;
    private String shipPhone;
    private String email;
    private String shippedDate;
    private String licenseNum;
    private String certificateCompany;
    private String comments;
    private String certificate;

    private Operation operation;

    //for undo and redo add some parameter like an enum that can store what action was performed. this object will be
    //stored before being replaced by the edited object or removed. be wary of index and location in client list.
    //undo and redo ability will likely be volatile. each open will require clearing of deleted images. keep a record of
    //images stored in the folder that need to be permanently removed on startup.


    public void setIndex(Integer index){
        this.index = index;
    }
    public void setSoldTo(String soldTo){
        this.soldTo = soldTo;
    }
    public void setSoldPhone(String soldPhone){
        this.soldPhone = soldPhone;
    }
    public void setShipTo(String shipTo){
        this.shipTo = shipTo;
    }
    public void setCompany(String company){this.company = company;}
    public void setAddress1(String address1){
        this.address1 = address1;
    }
    public void setAddress2(String address2){
        this.address2 = address2;
    }
    public void setCity(String city){
        this.city = city;
    }
    public void setRegion(String region){
        this.region = region;
    }
    public void setPostCode(String postCode){
        this.postCode = postCode;
    }
    public void setCountry(String country){
        this.country = country;
    }
    public void setShipPhone(String shipPhone){
        this.shipPhone = shipPhone;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setShippedDate(String shippedDate){
        this.shippedDate = shippedDate.toUpperCase().replaceAll(",", "");
    }
    public void setLicenseNum(String licenseNum){
        this.licenseNum = licenseNum;
    }
    public void setCertificateCompany(String certificateCompany){
        this.certificateCompany = certificateCompany;
    }
    public void setComments(String comments){
        this.comments = comments;
    }
    public void setCertificate(String certificate){
        this.certificate = certificate;
    }

    public void setOperation(Operation operation){
        this.operation = operation;
    }





    public Integer getIndex(){return this.index;}
    public String getSoldTo(){
        return this.soldTo;
    }
    public String getSoldPhone(){
        return this.soldPhone;
    }
    public String getShipTo(){
        return this.shipTo;
    }
    public String getCompany(){return this.company; }
    public String getAddress1(){
        return this.address1;
    }
    public String getAddress2(){
        return this.address2;
    }
    public String getCity(){
        return this.city;
    }
    public String getRegion(){
        return this.region;
    }
    public String getPostCode(){
        return this.postCode;
    }
    public String getCountry(){
        return this.country;
    }
    public String getShipPhone(){
        return this.shipPhone;
    }
    public String getEmail(){
        return this.email;
    }
    public String getShippedDate(){
        return this.shippedDate;
    }
    public LocalDate getDateFormat(){
        if(shippedDate.equals("")){
            return null;
        }
        String tempDate = shippedDate.substring(0,1) + shippedDate.substring(1,3).toLowerCase() + "." + shippedDate.substring(3, 11);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd yyyy");
        return LocalDate.parse(tempDate, dtf);
    }
    public String getLicenseNum(){
        return this.licenseNum;
    }
    public String getCertificateCompany(){
        return this.certificateCompany;
    }
    public String getComments(){return this.comments; }
    public String getCertificate(){return this.certificate; }

    public Operation getOperation(){return this.operation; }

    @Override
    public String toString() {
        return this.soldTo + "," +
                this.soldPhone + "," +
                this.shipTo + "," +
                this.company + "," +
                this.address1 + "," +
                this.address2 + "," +
                this.city + "," +
                this.region + "," +
                this.postCode + "," +
                this.country + "," +
                this.shipPhone + "," +
                this.email + "," +
                this.shippedDate + "," +
                this.licenseNum + "," +
                this.certificateCompany + "," +
                this.comments.replaceAll(",", "~") + "," +
                this.certificate + "\n";
    }
}
