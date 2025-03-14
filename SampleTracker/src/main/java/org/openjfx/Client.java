package org.openjfx;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Client {

    private Integer index;
    private String  clientID;
    private String  orderID;
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
    private final String clientIDString =                 "*Client ID";
    private final String orderIDString =                  "Order ID";
    private final String shipNameString =                 "*Ship To";
    private final String shipPhoneString =                "*Ship Phone";
    private final String shipCompanyString =              "Ship Company";
    private final String shipAddress1String =             "*Ship Addr 1";
    private final String shipAddress2String =             "Ship Addr 2";
    private final String shipCityString =                 "Ship City";
    private final String shipRegionString =               "Ship Region";
    private final String shipPostCodeString =             "Ship Post Code";
    private final String shipCountryString =              "Ship Country";
    private final String shipEmailString =                "*Ship Email";
    private final String billNameString =                 "*Bill To";
    private final String billPhoneString =                "*Bill Phone";
    private final String billCompanyString =              "Bill Company";
    private final String billAddress1String =             "*Bill Addr 1";
    private final String billAddress2String =             "Bill Addr 2";
    private final String billCityString =                 "Bill City";
    private final String billRegionString =               "Bill Region";
    private final String billPostCodeString =             "Bill Post Code";
    private final String billCountryString =              "Bill Country";
    private final String billEmailString =                "*Bill Email";
    private final String dateShippedString =              "Date Shipped";
    private final String firstLicenseNumString =          "*First License #";
    private final String firstCertificateCompanyString =  "*First License Company";
    private final String secondLicenseNumString =         "*Second License #";
    private final String secondCertificateCompanyString = "*Second License Company";
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

    public Client() {
        index = 0;
        clientID = "";
        orderID = "";
        shipName = "";
        shipPhone = "";
        shipCompany = "";
        shipAddress1 = "";
        shipAddress2 = "";
        shipCity = "";
        shipRegion = "";
        shipPostCode = "";
        shipCountry = "";
        shipEmail = "";
        billName = "";
        billPhone = "";
        billCompany = "";
        billAddress1 = "";
        billAddress2 = "";
        billCity = "";
        billRegion = "";
        billPostCode = "";
        billCountry = "";
        billEmail = "";
        dateShipped = "";
        firstLicenseNum = "";
        firstCertificateCompany = "";
        secondLicenseNum = "";
        secondCertificateCompany = "";
        comments = "";
        firstCertificate = "";
        secondCertificate = "";
        dateClientAdded = "";
        dateClientEdited = "";
    }

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
    public void setClientID                (String clientID)                {this.clientID =                 clientID.strip();}
    public void setOrderID                 (String orderID)                 {this.orderID =                  orderID.strip();}
    public void setShipName                (String shipName)                {this.shipName =                 shipName.strip();}
    public void setShipPhone               (String shipPhone)               {this.shipPhone =                shipPhone.strip();}
    public void setShipCompany             (String shipCompany)             {this.shipCompany =              shipCompany.strip();}
    public void setShipAddress1            (String shipAddress1)            {this.shipAddress1 =             shipAddress1.strip();}
    public void setShipAddress2            (String shipAddress2)            {this.shipAddress2 =             shipAddress2.strip();}
    public void setShipCity                (String shipCity)                {this.shipCity =                 shipCity.strip();}
    public void setShipRegion              (String shipRegion)              {this.shipRegion =               shipRegion.strip();}
    public void setShipPostCode            (String shipPostCode)            {this.shipPostCode =             shipPostCode.strip();}
    public void setShipCountry             (String shipCountry)             {this.shipCountry =              shipCountry.strip();}
    public void setShipEmail               (String shipEmail)               {this.shipEmail =                shipEmail.strip();}
    public void setBillName                (String billName)                {this.billName =                 billName.strip();}
    public void setBillPhone               (String billPhone)               {this.billPhone =                billPhone.strip();}
    public void setBillCompany             (String billCompany)             {this.billCompany =              billCompany.strip();}
    public void setBillAddress1            (String billAddress1)            {this.billAddress1 =             billAddress1.strip();}
    public void setBillAddress2            (String billAddress2)            {this.billAddress2 =             billAddress2.strip();}
    public void setBillCity                (String billCity)                {this.billCity =                 billCity.strip();}
    public void setBillRegion              (String billRegion)              {this.billRegion =               billRegion.strip();}
    public void setBillPostCode            (String billPostCode)            {this.billPostCode =             billPostCode.strip();}
    public void setBillCountry             (String billCountry)             {this.billCountry =              billCountry.strip();}
    public void setBillEmail               (String billEmail)               {this.billEmail =                billEmail.strip();}
    public void setDateShipped             (String dateShipped)             {this.dateShipped =              dateShipped.toUpperCase().replaceAll(",", "");}
    public void setFirstLicenseNum         (String firstLicenseNum)         {this.firstLicenseNum =          firstLicenseNum.strip();}
    public void setFirstCertificateCompany (String firstCertificateCompany) {this.firstCertificateCompany =  firstCertificateCompany.strip();}
    public void setSecondLicenseNum        (String secondLicenseNum)        {this.secondLicenseNum =         secondLicenseNum.strip();}
    public void setSecondCertificateCompany(String secondCertificateCompany){this.secondCertificateCompany = secondCertificateCompany.strip();}
    public void setComments                (String comments)                {this.comments =                 comments;}
    public void setFirstCertificate        (String firstCertificate)        {this.firstCertificate =         firstCertificate;}
    public void setSecondCertificate       (String secondCertificate)       {this.secondCertificate =        secondCertificate;}
    public void setDateClientAdded         (String dateClientAdded)         {this.dateClientAdded =          dateClientAdded.toUpperCase().replaceAll(",", "");}
    public void setDateClientEdited        (String dateClientEdited)        {this.dateClientEdited =         dateClientEdited.toUpperCase().replaceAll(",", "");}

    public void setOperation(Operation operation){
        this.operation = operation;
    }

    public Integer getIndex()                   {return this.index;}
    public String  getClientID()                {return this.clientID;}
    public String  getOrderID()                 {return this.orderID;}
    public String  getShipName()                {return this.shipName;}
    public String  getShipPhone()               {return this.shipPhone;}
    public String  getShipCompany()             {return this.shipCompany;}
    public String  getShipAddress1()            {return this.shipAddress1;}
    public String  getShipAddress2()            {return this.shipAddress2;}
    public String  getShipCity()                {return this.shipCity;}
    public String  getShipRegion()              {return this.shipRegion;}
    public String  getShipPostCode()            {return this.shipPostCode;}
    public String  getShipCountry()             {return this.shipCountry;}
    public String  getShipEmail()               {return this.shipEmail;}
    public String  getBillName()                {return this.billName;}
    public String  getBillPhone()               {return this.billPhone;}
    public String  getBillCompany()             {return this.billCompany;}
    public String  getBillAddress1()            {return this.billAddress1;}
    public String  getBillAddress2()            {return this.billAddress2;}
    public String  getBillCity()                {return this.billCity;}
    public String  getBillRegion()              {return this.billRegion;}
    public String  getBillPostCode()            {return this.billPostCode;}
    public String  getBillCountry()             {return this.billCountry;}
    public String  getBillEmail()               {return this.billEmail;}
    public String  getDateShipped()             {return this.dateShipped;}
    public String  getFirstLicenseNum()         {return this.firstLicenseNum;}
    public String  getFirstCertificateCompany() {return this.firstCertificateCompany;}
    public String  getSecondLicenseNum()        {return this.secondLicenseNum;}
    public String  getSecondCertificateCompany(){return this.secondCertificateCompany;}
    public String  getComments()                {return this.comments;}
    public String  getFirstCertificate()        {return this.firstCertificate;}
    public String  getSecondCertificate()       {return this.secondCertificate;}
    public String  getDateClientAdded()         {return this.dateClientAdded;}
    public String  getDateClientEdited()        {return this.dateClientEdited;}

    // These need for our own use. the string versions needed for table.show() function
    public LocalDate getDateShippedDate()         {return formatDate(this.dateShipped);}
    public LocalDate getDateClientAddedDate()     {return formatDate(this.dateClientAdded);}
    public LocalDate getDateClientEditedDate()    {return formatDate(this.dateClientEdited);}

    public Operation getOperation(){return this.operation; }

    public String getIndexDispString()                   {return this.indexString;};
    public String getclientIDDispString()                {return this.clientIDString;};
    public String getorderIDDispString()                 {return this.orderIDString;};
    public String getShipNameDispString()                {return this.shipNameString;};
    public String getShipPhoneDispString()               {return this.shipPhoneString;};
    public String getShipCompanyDispString()             {return this.shipCompanyString;};
    public String getShipAddress1DispString()            {return this.shipAddress1String;};
    public String getShipAddress2DispString()            {return this.shipAddress2String;};
    public String getShipCityDispString()                {return this.shipCityString;};
    public String getShipRegionDispString()              {return this.shipRegionString;};
    public String getShipPostCodeDispString()            {return this.shipPostCodeString;};
    public String getShipCountryDispString()             {return this.shipCountryString;};
    public String getShipEmailDispString()               {return this.shipEmailString;};
    public String getBillNameDispString()                {return this.billNameString;};
    public String getBillPhoneDispString()               {return this.billPhoneString;};
    public String getBillCompanyDispString()             {return this.billCompanyString;};
    public String getBillAddress1DispString()            {return this.billAddress1String;};
    public String getBillAddress2DispString()            {return this.billAddress2String;};
    public String getBillCityDispString()                {return this.billCityString;};
    public String getBillRegionDispString()              {return this.billRegionString;};
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

    public String getAttributeNameAsString(String displayName) {
        switch (displayName) {
            case indexString:                    return "index";
            case clientIDString:                 return "clientID";
            case orderIDString:                  return "orderID";
            case shipNameString:                 return "shipName";
            case shipPhoneString:                return "shipPhone";
            case shipCompanyString:              return "shipCompany";
            case shipAddress1String:             return "shipAddress1";
            case shipAddress2String:             return "shipAddress2";
            case shipCityString:                 return "shipCity";
            case shipRegionString:               return "shipRegion";
            case shipPostCodeString:             return "shipPostCode";
            case shipCountryString:              return "shipCountry";
            case shipEmailString:                return "shipEmail";
            case billNameString:                 return "billName";
            case billPhoneString:                return "billPhone";
            case billCompanyString:              return "billCompany";
            case billAddress1String:             return "billAddress1";
            case billAddress2String:             return "billAddress2";
            case billCityString:                 return "billCity";
            case billRegionString:               return "billRegion";
            case billPostCodeString:             return "billPostCode";
            case billCountryString:              return "billCountry";
            case billEmailString:                return "billEmail";
            case dateShippedString:              return "dateShipped";
            case firstLicenseNumString:          return "firstLicenseNum";
            case firstCertificateCompanyString:  return "firstCertificateCompany";
            case secondLicenseNumString:         return "secondLicenseNum";
            case secondCertificateCompanyString: return "secondCertificateCompany";
            case commentsString:                 return "comments";
            case firstCertificateString:         return "firstCertificate";
            case secondCertificateString:        return "secondCertificate";
            case dateClientAddedString:          return "dateClientAdded";
            case dateClientEditedString:         return "dateClientEdited";
            default: return null;
        }
    }

    public String getAttributeNameDispString(String attributeName) {
        switch (attributeName) {
            case "index":                    return indexString;
            case "clientID":                 return clientIDString;
            case "orderID":                  return orderIDString;
            case "shipName":                 return shipNameString;
            case "shipPhone":                return shipPhoneString;
            case "shipCompany":              return shipCompanyString;
            case "shipAddress1":             return shipAddress1String;
            case "shipAddress2":             return shipAddress2String;
            case "shipCity":                 return shipCityString;
            case "shipRegion":               return shipRegionString;
            case "shipPostCode":             return shipPostCodeString;
            case "shipCountry":              return shipCountryString;
            case "shipEmail":                return shipEmailString;
            case "billName":                 return billNameString;
            case "billPhone":                return billPhoneString;
            case "billCompany":              return billCompanyString;
            case "billAddress1":             return billAddress1String;
            case "billAddress2":             return billAddress2String;
            case "billCity":                 return billCityString;
            case "billRegion":               return billRegionString;
            case "billPostCode":             return billPostCodeString;
            case "billCountry":              return billCountryString;
            case "billEmail":                return billEmailString;
            case "dateShipped":              return dateShippedString;
            case "firstLicenseNum":          return firstLicenseNumString;
            case "firstCertificateCompany":  return firstCertificateCompanyString;
            case "secondLicenseNum":         return secondLicenseNumString;
            case "secondCertificateCompany": return secondCertificateCompanyString;
            case "comments":                 return commentsString;
            case "firstCertificate":         return firstCertificateString;
            case "secondCertificate":        return secondCertificateString;
            case "dateClientAdded":          return dateClientAddedString;
            case "dateClientEdited":         return dateClientEditedString;
            default: return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        builder.append(this.clientID).append(",");
        // builder.append(this.orderID).append(",");
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
