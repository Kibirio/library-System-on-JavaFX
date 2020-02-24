package librarysystem.UI.settings;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import librarysystem.alert.AlertMaker;
import org.apache.commons.codec.digest.DigestUtils;

public class Preferences {

   public static final String CONFIG_FILE = "config.txt";
   int nDaysWithoutFine;
   float finePerDay;
   String username;
   String password;

   public Preferences() {
      nDaysWithoutFine = 14;
      finePerDay = 2;
      username = "me";
      password = "me";
   }

   public int getnDaysWithoutFine() {
      return nDaysWithoutFine;
   }

   public void setnDaysWithoutFine(int nDaysWithoutFine) {
      this.nDaysWithoutFine = nDaysWithoutFine;
   }

   public float getFinePerDay() {
      return finePerDay;
   }

   public void setFinePerDay(float finePerDay) {
      this.finePerDay = finePerDay;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      if (password.length() < 16) {
         this.password = DigestUtils.sha1Hex(password); // this will hash the password when its set
      } else {
         this.password = password;
      }

   }

   public static void initConfig() {
      Writer writer = null;
      try {
         Preferences preference = new Preferences();
         Gson gson = new Gson();

         writer = new FileWriter(CONFIG_FILE);
         gson.toJson(preference, writer);
      } catch (IOException ex) {
         Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
         try {
            writer.close();
         } catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }

   public static Preferences getPreferences() {
      Gson gson = new Gson();
      Preferences preferences = new Preferences();
      try {
         preferences = gson.fromJson(new FileReader(CONFIG_FILE), Preferences.class);
      } catch (FileNotFoundException ex) {
         initConfig();
         Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
      }
      return preferences;
   }

   public static void writePreferenceToFile(Preferences preference) {
      Writer w = null;
      try {
         Gson gson = new Gson();
         w = new FileWriter(CONFIG_FILE);
         gson.toJson(preference, w);

         AlertMaker.showSimpleAlert("success", "setting was updated");

      } catch (IOException ex) {
         Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
         AlertMaker.showErrorMessage("Failed", "Unable to Save Configurations");
      } finally {
         try {
            w.close();
         } catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }
}
