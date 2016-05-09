package youtubeaudio;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    String video_link;
    public static String home_dir = System.getProperty("user.home") + "/Desktop";
    public static String save_dir, Title, filename;
    public static String[] infoarray = new String[4];
    Boolean Success;

    @FXML
    private Button download_btn;
    public Button about_btn;
    public TextField url_field;
    @FXML
    public Text status_field;


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        about_btn.setOnAction(e -> {
            try {
                moveit();
            } catch (IOException e1) {
                e1.printStackTrace();
            }


        });


        download_btn.setOnAction(e -> {

            video_link = url_field.getText();


            boolean valid = Validate.isValidURL(video_link);


            if (valid) {

                try {
                    Task infotask = new Task<Void>() {
                        @Override
                        public Void call() {


                            status_field.setText("Fetching Video Info..");
                            return null;
                        }
                    };
                    new Thread(infotask).start();
                    GetVideoInfo(video_link);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }


            }


        });
    }


    public void GetVideoInfo(String video_url) throws IOException {
        String simulate_config = " --simulate --get-title --get-duration --all-subs ";

        StringBuilder scommand = new StringBuilder();
        scommand.append("youtube-dl.exe");
        scommand.append(simulate_config);
        // command.append("--all-subs");
        String commands = scommand.toString();
        ProcessBuilder infogetter = new ProcessBuilder(
                "cmd.exe", "/c", commands, video_url);

        infogetter.redirectErrorStream(true);
        Process p = infogetter.start();
        BufferedReader infor = new BufferedReader(new InputStreamReader(p.getInputStream()));

        int i = 0;
        while (i < 3) {
            infoarray[i] = infor.readLine();
            i++;
        }


        String duration, subs;

        if (infoarray[0].contains("WARNING")) {
            Title = infoarray[1];
            duration = infoarray[2];
            subs = "Not Available!!";
        } else {
            Title = infoarray[0];
            duration = infoarray[1];
            subs = "Available!!";
        }
        filename = Title.replaceAll("\\s", "");
        filename = filename.replaceAll("[^a-zA-Z]+", "");
        StringBuilder infobuild = new StringBuilder("");
        String vidinfo = "Title: " + Title + "\n \n" + "Duration: " + duration;
        infobuild.append(vidinfo).append("\n \n Subtitles: " + subs);
        infobuild.append("\n \n" + "Click 'OK' to proceed with Download");
        save_dir = home_dir + "/" + filename;
        File file = new File(save_dir);
        file.mkdir();
        save_dir = file.getAbsolutePath();
        //System.out.println(save_dir);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Video Information");
        alert.setHeaderText("Do You Want to Continue to Downloading this Video File?");
        alert.setContentText(infobuild.toString());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {


            new Thread() {
                public void run() {

                    try {
                        Task dinfotask = new Task<Void>() {
                            @Override
                            public Void call() {


                                status_field.setText("Starting Download..");
                                return null;
                            }
                        };
                        new Thread(dinfotask).start();
                        Success = download(video_url);
                        status_field.setText("");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();



        } else {
            url_field.clear();
            status_field.setText("");


        }


    }

    public boolean download(String video_link) throws IOException {
        String audio_config = " --extract-audio --audio-format=wav --audio-quality=3 -k --newline --all-subs --restrict-filenames -o %(title)s.%(ext)s ";
       // String audio_config = " --newline --all-subs --restrict-filenames -o %(title)s.%(ext)s ";

        StringBuilder command = new StringBuilder();
        command.append("youtube-dl.exe");
        command.append(audio_config);
        String commands = command.toString();
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", commands, video_link);

        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        boolean  done = false;


        while (true) {
            line = r.readLine();
            System.out.println(line);
            String[] ETA = line.split("ETA");

            if(line != "null")
            {
                ETA = line.split("ETA");
            }


            String[] finalETA = ETA;
            Task updatetask = new Task<Void>() {
                @Override
                public Void call() {



                    status_field.setText(finalETA[1]);

                    return null;
                }
            };
            new Thread(updatetask).start();


            if (line.contains("ERROR")) {
                Task otask2 = new Task<Void>() {
                    @Override
                    public Void call() {

                      //  AlertBox.display("Download Error!", "Error! Please Try Again", "Error");
                        status_field.setText("Download Error! Please Try Again..");

                        return null;
                    }
                };
                new Thread(otask2).start();

                break;
            }

            if (line.contains("100%")) {

                    done = true;
                    Task otask1 = new Task<Void>() {
                        @Override
                        public Void call() {

                            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                            alert1.setTitle("SUCCESS!");
                            alert1.setContentText("Download Completed!");

                            status_field.setText("Download Completed! Saving File..");
                            return null;
                        }
                    };
                    new Thread(otask1).start();
                    break;

            }


            if (line.contains("Deleting")) {

                Task finishtask = new Task<Void>() {
                    @Override
                    public Void call() {


                        status_field.setText("Download Completed! Saving File..");
                        return null;
                    }
                };
                new Thread(finishtask).start();

                break;
            }

        }




        Task finishtask = new Task<Void>() {
            @Override
            public Void call() {


                status_field.setText("Download Completed! Saving File..");
                return null;
            }
        };
        new Thread(finishtask).start();






//        String simulate_waste = " youtube-dl.exe --simulate --get-title --get-duration --dump-json "+ video_link;
//        Process w = Runtime.getRuntime().exec(simulate_waste);

        Task movetask = new Task<Void>() {
            @Override
            public Void call() {


                status_field.setText(" ");

                return null;
            }
        };
        new Thread(movetask).start();



            return done;

    }

    public void moveit() throws IOException {
        int titlesize = Title.length();
        String mfilename = Title.replaceAll("\\s", "_");
        String filem = "move " + mfilename.substring(0, 2) + "*.* " + save_dir;
        System.out.print(filem);
        ProcessBuilder mover = new ProcessBuilder(
                "cmd.exe", "/c", filem);
        mover.redirectErrorStream(true);
        Process m = null;
        try {
            m = mover.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}














