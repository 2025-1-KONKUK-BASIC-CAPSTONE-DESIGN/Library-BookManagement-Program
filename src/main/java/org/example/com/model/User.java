package org.example.com.model;
import org.example.com.util.LoanManager;

public class User {
    private String id;
    private String password;
    private String phone;
    private String email;
    private String birth;
    private int loanCount = 0; // 대출 현황 기본값은 0권
    //2차 추가
    private int totalPenalty=0;

    public User(String id, String password, String phone, String email, String birth) {
        this.id = id;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.birth = birth;
    }

    public String toDataString() {
        // 파일에 저장할 형식: ID<TAB>PWD<TAB>전화번호<TAB>이메일<TAB>생년월일<TAB>대출수
        return String.join("\t", id, password, phone, email, birth, String.valueOf(loanCount));
    }

    // getter
    public String getId() { return id; }
    public String getPassword() { return password; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getBirth() { return birth; }
    public int getLoanCount() { return loanCount; }


    // setter
    public void setLoanCount(int count) { this.loanCount = count; }
    // 2차 추가
    public int getTotalPenalty(){return totalPenalty;}
    public void setTotalPenalty(int totalPenalty){
        this.totalPenalty = totalPenalty;
    }

    public boolean isOverdueDays() {
        return LoanManager.isUserOverdue(this.id);
    }
    public int getPenaltyDays() {
        return totalPenalty;
    }

    public void setPenaltyDays(int days) {
        this.totalPenalty = days;
    }

}