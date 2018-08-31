package com.myip.run;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MyIp extends Application{

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		FXMLLoader fxml = new FXMLLoader(this.getClass().getResource("../gui/my_ip.fxml"));
		
		Parent root = fxml.load();
		
		Scene scene = new Scene(root);
		
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.setResizable(false);
		primaryStage.setTitle("My IP Address");
		primaryStage.initStyle(StageStyle.DECORATED);
		primaryStage.show();	
	}
	
}
