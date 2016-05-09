package youtubeaudio;

/**
 * Created by Nithesh on 2/28/2016.
 */


import javafx.application.Platform;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Validate {

    public static boolean isValidURL(String YTURL) {

        boolean isvalid = false;

        if(YTURL.isEmpty())
        {
            Platform.runLater(new Runnable(){
            @Override
            public void run()
            {
                AlertBox.display("Empty!", "Null: You did not enter anything,Please Enter a Valid URL" , "Error");

            }
          });

            isvalid = false;
        }
        else if (YTURL.endsWith("tube.com/"))
        {
            AlertBox.display("HomePage!", "You Entered Youtube Homepage URL, Please Enter a Valid URL of a Video" , "Error");
            isvalid = false;
        }
        else
        {
            try
            {
                new URL(YTURL).openStream().close();
                System.out.println("Valid Link");
                isvalid = true;
            }

            catch (MalformedURLException e)
            {
                System.out.println("InValid Link");
                AlertBox.display("InValid Link", "Error: Enter a Valid URL" , "Error");
                isvalid = false;
            }

            catch (IOException e)
            {
                e.printStackTrace();
                System.out.println("Link Not Available");
                AlertBox.display("Not Available", "Error: Link Not Available" , "Error");
                isvalid = false;
            }

        }

        return isvalid;
    }
}
