module com.team7.phonecardtopup {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;


    opens com.team7.phonecardtopup;
    exports com.team7.phonecardtopup;
}