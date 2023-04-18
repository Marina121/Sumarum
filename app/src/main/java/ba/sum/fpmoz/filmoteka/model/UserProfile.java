package ba.sum.fpmoz.filmoteka.model;


public class UserProfile {
    private String fullNameTxt;
    private String email;
    private String phoneTxt;
    private String image;


    public UserProfile(String fullNameTxt, String email, String phoneTxt, String image) {
        this.fullNameTxt = fullNameTxt;
        this.email = email;
        this.phoneTxt = phoneTxt;
        this.image = image;


    }

    public String getFullNameTxt() {
        return fullNameTxt;
    }

    public void setFullNameTxt(String fullNameTxt) {
        this.fullNameTxt = fullNameTxt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneTxt() {
        return phoneTxt;
    }

    public void setPhoneTxt(String phoneTxt) {
        this.phoneTxt = phoneTxt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}