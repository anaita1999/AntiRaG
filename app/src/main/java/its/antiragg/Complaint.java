package its.antiragg;

public class Complaint {

    private String Complainant_Name, Phone_No, E_Mail, Victim_Name, Complaint_Details, status;
    private int id;
    public Complaint(){
    }
    public Complaint(String Complainant_Name, String Phone_No, String E_Mail, String Victim_Name, String Complaint_Details,int id) {
        this.Complainant_Name = Complainant_Name;
        this.Phone_No = Phone_No;
        this.E_Mail = E_Mail;
        this.Victim_Name = Victim_Name;
        this.Complaint_Details = Complaint_Details;
        this.id = id;
    }
    public String getComplainant_Name() {
        return Complainant_Name;
    }
    public String getPhone_No() {
        return Phone_No;
    }
    public String getE_Mail() {
        return E_Mail;
    }
    public String getVictim_Name() {
        return Victim_Name;
    }
    public String getComplaint_Details() {
        return Complaint_Details;
    }

    public void setE_Mail(String b) {
        E_Mail = b;
    }
    public void setVictim_Name(String c) {
        Victim_Name = c;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setComplaint_Details(String d) {
        Complaint_Details = d;
    }

}
// received, processing, solved