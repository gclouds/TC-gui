package utils;

import controller.MainPageController;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * This utility extracts files and directories of a standard zip file to a
 * destination directory.
 *
 */
public class UnzipUtility {
	public final static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(UnzipUtility.class);

    /**
     * Size of the buffer to read/write data
     */
    private static final int BUFFER_SIZE = 4096;

    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified
     * by destDirectory (will be created if does not exists)
     *
     * @param zipFilePath
     * @param destDirectory
     * @throws IOException
     */
    public void unzip(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }

        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));

        ZipEntry entry = zipIn.getNextEntry();

        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    /**
     * Extracts a zip entry (file entry)
     *
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    public void unzip(String inputFilePath, String outputfolder, String pwd) {
        try {
            ZipFile zipFile = new ZipFile(inputFilePath);
            if (zipFile.isEncrypted()) {
                Stage stage = new Stage();
                PasswordField text = new PasswordField();
                Button button = new Button("ok");
                Button showPwdButton = new Button("show password");
                showPwdButton.setAlignment(Pos.CENTER);
                GridPane gridPane = new GridPane();
                gridPane.add(text, 0, 0);
                gridPane.add(button, 1, 0);
                gridPane.add(showPwdButton, 0, 1,2,1);
                gridPane.setAlignment(Pos.CENTER);
                gridPane.setVgap(5.0);
                gridPane.setHgap(5.0);
                gridPane.setPrefSize(300, 75);
                Scene scene = new Scene(gridPane);
                stage.setTitle(" Please enter password");

                stage.setScene(scene);
                //stage.setFullScreen(true);
                stage.show();
                button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        try {
                            zipFile.setPassword(text.getText());
                            zipFile.extractAll(outputfolder);
                            stage.close();
                            MainPageController.listTestCase.refreshMainContainer();
                        } catch (ZipException ex) {
                            Logger.getLogger(UnzipUtility.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });

                showPwdButton.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        showPwdButton.setText(text.getText());
                    }
                }
                );
                showPwdButton.setOnMouseReleased(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        showPwdButton.setText("show password");

                    }
                }
                );

            } else {
                zipFile.extractAll(outputfolder);

            }
        } catch (Exception e) {
        	log.error(e);
        }

    }

}
