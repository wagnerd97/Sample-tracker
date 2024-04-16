package org.openjfx;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Client {

    private Integer index;
    private String  shipName;
    private String  shipPhone;
    private String  shipCompany;
    private String  shipAddress1;
    private String  shipAddress2;
    private String  shipCity;
    private String  shipRegion;
    private String  shipPostCode;
    private String  shipCountry;
    private String  shipEmail;
    private String  billName;
    private String  billPhone;
    private String  billCompany;
    private String  billAddress1;
    private String  billAddress2;
    private String  billCity;
    private String  billRegion;
    private String  billPostCode;
    private String  billCountry;
    private String  billEmail;
    private String  dateShipped;
    private String  firstLicenseNum;
    private String  firstCertificateCompany;
    private String  secondLicenseNum;
    private String  secondCertificateCompany;
    private String  comments;
    private String  firstCertificate;
    private String  secondCertificate;
    private String  dateClientAdded;
    private String  dateClientEdited;

    private final String indexString =                    "#";
    private final String shipNameString =                 "Ship To";
    private final String shipPhoneString =                "Ship Phone";
    private final String shipCompanyString =              "Ship Company";
    private final String shipAddress1String =             "Ship Addr 1";
    private final String shipAddress2String =             "Ship Addr 2";
    private final String shipCityString =                 "Ship City";
    private final String shipRegionString =               "Ship Region";
    private final String shipPostCodeString =             "Ship Post Code";
    private final String shipCountryString =              "Ship Country";
    private final String shipEmailString =                "Ship Email";
    private final String billNameString =                 "Bill To";
    private final String billPhoneString =                "Bill Phone";
    private final String billCompanyString =              "Bill Company";
    private final String billAddress1String =             "Bill Addr 1";
    private final String billAddress2String =             "Bill Addr 2";
    private final String billCityString =                 "Bill City";
    private final String billRegionString =               "Bill Region";
    private final String billPostCodeString =             "Bill Post Code";
    private final String billCountryString =              "Bill Country";
    private final String billEmailString =                "Bill Email";
    private final String dateShippedString =              "Date Shipped";
    private final String firstLicenseNumString =          "First License #";
    private final String firstCertificateCompanyString =  "First License Company";
    private final String secondLicenseNumString =         "Second License #";
    private final String secondCertificateCompanyString = "Second License Company";
    private final String commentsString =                 "Comments";
    private final String firstCertificateString =         "First Certificate";
    private final String secondCertificateString =        "Second Certificate";
    private final String dateClientAddedString =          "Date Added";
    private final String dateClientEditedString =         "Date Last Edited";

    private Operation operation;

    //for undo and redo add some parameter like an enum that can store what action was performed. this object will be
    //stored before being replaced by the edited object or removed. be wary of index and location in client list.
    //undo and redo ability will likely be volatile. each open will require clearing of deleted images. keep a record of
    //images stored in the folder that need to be permanently removed on startup.

    private LocalDate formatDate(String dateString) {
        if(dateString.equals("")){
            return null;
        }
        String tempDate = "";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd yyyy");
        try {
            tempDate = dateString.substring(0,1) + dateString.substring(1,3).toLowerCase() + dateString.substring(3, 11);
            LocalDate formattedDate = LocalDate.parse(tempDate, dtf);
            return formattedDate;
        } catch (DateTimeParseException e) {
            System.out.println("The date: " + tempDate + " can not be parsed in format: " + dtf.toString());
        }
        return null;
    }


    public void setIndex                   (Integer index)                  {this.index =                    index;}
    public void setShipName                (String shipName)                {this.shipName =                 shipName;}
    public void setShipPhone               (String shipPhone)               {this.shipPhone =                shipPhone;}
    public void setShipCompany             (String shipCompany)             {this.shipCompany =              shipCompany;}
    public void setShipAddress1            (String shipAddress1)            {this.shipAddress1 =             shipAddress1;}
    public void setShipAddress2            (String shipAddress2)            {this.shipAddress2 =             shipAddress2;}
    public void setShipCity                (String shipCity)                {this.shipCity =                 shipCity;}
    public void setShipRegion              (String shipRegion)              {this.shipRegion =               shipRegion;}
    public void setShipPostCode            (String shipPostCode)            {this.shipPostCode =             shipPostCode;}
    public void setShipCountry             (String shipCountry)             {this.shipCountry =              shipCountry;}
    public void setShipEmail               (String shipEmail)               {this.shipEmail =                shipEmail;}
    public void setBillName                (String billName)                {this.billName =                 billName;}
    public void setBillPhone               (String billPhone)               {this.billPhone =                billPhone;}
    public void setBillCompany             (String billCompany)             {this.billCompany =              billCompany;}
    public void setBillAddress1            (String billAddress1)            {this.billAddress1 =             billAddress1;}
    public void setBillAddress2            (String billAddress2)            {this.billAddress2 =             billAddress2;}
    public void setBillCity                (String billCity)                {this.billCity =                 billCity;}
    public void setBillRegion              (String billRegion)              {this.billRegion =               billRegion;}
    public void setBillPostCode            (String billPostCode)            {this.billPostCode =             billPostCode;}
    public void setBillCountry             (String billCountry)             {this.billCountry =              billCountry;}
    public void setBillEmail               (String billEmail)               {this.billEmail =                billEmail;}
    public void setDateShipped             (String dateShipped)             {this.dateShipped =              dateShipped.toUpperCase().replaceAll(",", "");}
    public void setFirstLicenseNum         (String firstLicenseNum)         {this.firstLicenseNum =          firstLicenseNum;}
    public void setFirstCertificateCompany (String firstCertificateCompany) {this.firstCertificateCompany =  firstCertificateCompany;}
    public void setSecondLicenseNum        (String secondLicenseNum)        {this.secondLicenseNum =         secondLicenseNum;}
    public void setSecondCertificateCompany(String secondCertificateCompany){this.secondCertificateCompany = secondCertificateCompany;}
    public void setComments                (String comments)                {this.comments =                 comments;}
    public void setFirstCertificate        (String firstCertificate)        {this.firstCertificate =         firstCertificate;}
    public void setSecondCertificate       (String secondCertificate)       {this.secondCertificate =        secondCertificate;}
    public void setDateClientAdded         (String dateClientAdded)         {this.dateClientAdded =          dateClientAdded;}
    public void setDateClientEdited        (String dateClientEdited)        {this.dateClientEdited =         dateClientEdited;}

    public void setOperation(Operation operation){
        this.operation = operation;
    }

    public Integer   getIndex()                   {return this.index;}
    public String    getShipName()                {return this.shipName;}
    public String    getShipPhone()               {return this.shipPhone;}
    public String    getShipCompany()             {return this.shipCompany;}
    public String    getShipAddress1()            {return this.shipAddress1;}
    public String    getShipAddress2()            {return this.shipAddress2;}
    public String    getShipCity()                {return this.shipCity;}
    public String    getShipRegion()              {return this.shipRegion;}
    public String    getShipPostCode()            {return this.shipPostCode;}
    public String    getShipCountry()             {return this.shipCountry;}
    public String    getShipEmail()               {return this.shipEmail;}
    public String    getBillName()                {return this.billName;}
    public String    getBillPhone()               {return this.billPhone;}
    public String    getBillCompany()             {return this.billCompany;}
    public String    getBillAddress1()            {return this.billAddress1;}
    public String    getBillAddress2()            {return this.billAddress2;}
    public String    getBillCity()                {return this.billCity;}
    public String    getBillRegion()              {return this.billRegion;}
    public String    getBillPostCode()            {return this.billPostCode;}
    public String    getBillCountry()             {return this.billCountry;}
    public String    getBillEmail()               {return this.billEmail;}
    public LocalDate getDateShipped()             {return formatDate(this.dateShipped);}
    public String    getFirstLicenseNum()         {return this.firstLicenseNum;}
    public String    getFirstCertificateCompany() {return this.firstCertificateCompany;}
    public String    getSecondLicenseNum()        {return this.secondLicenseNum;}
    public String    getSecondCertificateCompany(){return this.secondCertificateCompany;}
    public String    getComments()                {return this.comments;}
    public String    getFirstCertificate()        {return this.firstCertificate;}
    public String    getSecondCertificate()       {return this.secondCertificate;}
    public LocalDate getDateClientAdded()         {return formatDate(this.dateClientAdded);}
    public LocalDate getDateClientEdited()        {return formatDate(this.dateClientEdited);}

    public Operation getOperation(){return this.operation; }

    public String getindexDispString()                   {return this.indexString;};
    public String getshipNameDispString()                {return this.shipNameString;};
    public String getshipPhoneDispString()               {return this.shipPhoneString;};
    public String getshipCompanyDispString()             {return this.shipCompanyString;};
    public String getshipAddress1DispString()            {return this.shipAddress1String;};
    public String getshipAddress2DispString()            {return this.shipAddress2String;};
    public String getshipCityDispString()                {return this.shipCityString;};
    public String getshipRegionDispString()              {return this.shipRegionString;};
    public String getshipPostCodeDispString()            {return this.shipPostCodeString;};
    public String getshipCountryDispString()             {return this.shipCountryString;};
    public String getshipEmailDispString()               {return this.shipEmailString;};
    public String getbillNameDispString()                {return this.billNameString;};
    public String getbillPhoneDispString()               {return this.billPhoneString;};
    public String getbillCompanyDispString()             {return this.billCompanyString;};
    public String getbillAddress1DispString()            {return this.billAddress1String;};
    public String getbillAddress2DispString()            {return this.billAddress2String;};
    public String getbillCityDispString()                {return this.billCityString;};
    public String getbillRegionDispString()              {return this.billRegionString;};
    public String getbillPostCodeDispString()            {return this.billPostCodeString;};
    public String getbillCountryDispString()             {return this.billCountryString;};
    public String getbillEmailDispString()               {return this.billEmailString;};
    public String getdateShippedDispString()             {return this.dateShippedString;};
    public String getfirstLicenseNumDispString()         {return this.firstLicenseNumString;};
    public String getfirstCertificateCompanyDispString() {return this.firstCertificateCompanyString;};
    public String getsecondLicenseNumDispString()        {return this.secondLicenseNumString;};
    public String getsecondCertificateCompanyDispString(){return this.secondCertificateCompanyString;};
    public String getcommentsDispString()                {return this.commentsString;};
    public String getfirstCertificateDispString()        {return this.firstCertificateString;};
    public String getsecondCertificateDispString()       {return this.secondCertificateString;};
    public String getdateClientAddedDispString()         {return this.dateClientAddedString;};
    public String getdateClientEditedDispString()        {return this.dateClientEditedString;};

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append(this.shipName).append(",");
        builder.append(this.shipPhone).append(",");
        builder.append(this.shipCompany).append(",");
        builder.append(this.shipAddress1).append(",");
        builder.append(this.shipAddress2).append(",");
        builder.append(this.shipCity).append(",");
        builder.append(this.shipRegion).append(",");
        builder.append(this.shipPostCode).append(",");
        builder.append(this.shipCountry).append(",");
        builder.append(this.shipEmail).append(",");
        builder.append(this.billName).append(",");
        builder.append(this.billPhone).append(",");
        builder.append(this.billCompany).append(",");
        builder.append(this.billAddress1).append(",");
        builder.append(this.billAddress2).append(",");
        builder.append(this.billCity).append(",");
        builder.append(this.billRegion).append(",");
        builder.append(this.billPostCode).append(",");
        builder.append(this.billCountry).append(",");
        builder.append(this.billEmail).append(",");
        builder.append(this.dateShipped).append(",");
        builder.append(this.firstLicenseNum).append(",");
        builder.append(this.firstCertificateCompany).append(",");
        builder.append(this.secondLicenseNum).append(",");
        builder.append(this.secondCertificateCompany).append(",");
        builder.append(this.comments.replaceAll(",", "~")).append(",");
        builder.append(this.firstCertificate).append(",");
        builder.append(this.secondCertificate).append(",");
        builder.append(this.dateClientAdded).append(",");
        builder.append(this.dateClientEdited).append("\n");

        return builder.toString();
    }
    

}
