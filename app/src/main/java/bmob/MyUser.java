package bmob;

import java.util.Date;

import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser {
    private String accountName;
    private int sequence,sequence2,sequence3,wordnum,hintnum;
    Date date1,date2;
    public String getAccountName() {
        return this.accountName;
    }
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    public int getSequence1() {
        return this.sequence;
    }
    public void setSequence1(int sequence) {
        this.sequence = sequence;
    }
    public int getSequence2() {
        return this.sequence2;
    }
    public void setSequence2(int sequence) {
        this.sequence2 = sequence;
    }
    public int getSequence3() {
        return this.sequence3;
    }
    public void setSequence3(int sequence) {
        this.sequence3 = sequence;
    }
    public int getwordnum() {
        return this.wordnum;
    }
    public void setwordnum(int wordnum) {
        this.wordnum = wordnum;
    }
    public int gethintnum() {
        return this.hintnum;
    }
    public void sethintnum(int hintnum) {
        this.hintnum = hintnum;
    }
    public Date getdate1() {
        return this.date2;
    }
    public void setdate1(Date date) {
        this.date1 = date;
    }
    public Date getdate2() {
        return this.date2;
    }
    public void setdate2(Date date) {
        this.date2= date;
    }
}
