/*package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Stack;
import java.util.logging.Level;
import javafx.scene.transform.*;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.converter.ColorConverter;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.layout.FlowPane;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.Menu;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
		System.out.println("Hello World!");
		try {
			Thread.sleep(20000l);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return ;
	}
	Stage prmStage;
	Pane parentNode;
	Pane loader = new Pane();
	boolean isLoading = false;
	Pane movementButtons = new Pane();
	StackPane imageWrapper = new StackPane();
	ImageView imageView = new ImageView();
	// TODO : Complete this thread and associate it with the loaders : 
	Thread loaderThread;
	Pane r = new Pane();
	ColorAdjust colorAdjust = new ColorAdjust();
	Slider[] colorSliders = new Slider[3];
	ChangeListener<? super Number> onRGBChange;
	short rotation = 0;
	@Override
	public void start(Stage primaryStage) {
		// setup : 
		// TODO : Fix this FlowPane it is causing the window to be a miss when an image!
		BorderPane root = new BorderPane();
		Scene scene = new Scene(r, 300, 300);
		r.getChildren().add(root);
		prmStage = primaryStage;
		parentNode = r;
		
		// imageWrapper & imageView : 
		imageWrapper.getChildren().add(imageView);
		imageWrapper.setMargin(imageView, new Insets(15, 15, 15, 15));
		initRGBMethod(); // connect all the events!
		imageView.setEffect(colorAdjust);
		
		// init loader : 
		Rectangle whiteRect = new Rectangle(110, 135, 80, 15)
				, greenRect = new Rectangle(122, 145, 68, 5);
		loader.getChildren().addAll(whiteRect, greenRect);
		
		// init menus : 
		MenuBar mainMenu = new MenuBar();
		mainMenu.prefWidthProperty().bind(primaryStage.widthProperty().subtract(16));
		mainMenu.setPrefHeight(25);
		Menu fileMenu = new Menu("File")
				, rotateMenu = new Menu("Rotate")
				, colorMenu = new Menu("Color Adjust");
		MenuItem openItem = new MenuItem("Open"), saveItem = new MenuItem("Save As"), quitItem = new MenuItem("Quit");
		// openMenu : 
		openItem.setOnAction(((event)->{
			
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("choose the image to open");
			fileChooser.setInitialDirectory(new File("C:\\Users\\Hassan\\Pictures\\"));
			File file = fileChooser.showOpenDialog(prmStage);
			System.out.println("Loading "+file.getPath());
			startLoader();
			Image img = new Image("file:"+file.getPath(), false);
			endLoader();
			System.out.println("Finished loading!");
			setImage(img);
				
		}));
		// openItem ; 
		// saveItem :
		saveItem.setOnAction((event)->{
			File outputFile = new File("C:\\Users\\Hassan\\Pictures\\FormattedImage.png");
			BufferedImage bImg = SwingFXUtils.fromFXImage(imageView.snapshot(null, null), null);
			try {
				ImageIO.write(bImg, "png", outputFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		MenuItem rotateRight = new MenuItem("Rotate 90 to the right")
				, rotateLeft = new MenuItem("Rotate 90 to the left");
		fileMenu.getItems().addAll(openItem, saveItem, quitItem);
		
		rotateMenu.getItems().addAll(rotateRight, rotateLeft);
		rotateRight.setOnAction((event)->{
			rotation += 90;
			imageView.setRotate(rotation);
		});
		rotateLeft.setOnAction((event)->{
			rotation -= 90;
			imageView.setRotate(rotation);
		});
		quitItem.setOnAction((event)-> {
			primaryStage.close();
		});
		Label redLabel = new Label("Red"), 
				greenLabel = new Label("Green"), blueLabel = new Label("Blue");
		redLabel.setTextFill(Color.BLACK);
		greenLabel.setTextFill(Color.BLACK);
		blueLabel.setTextFill(Color.BLACK);
		MenuItem red = new MenuItem();
		Slider redSlider = new Slider(0d, 100d, 50d);
		redSlider.setShowTickMarks(true);
		redSlider.setShowTickLabels(true);
		redSlider.setMajorTickUnit(10d);
		redSlider.setBlockIncrement(5d);
		redSlider.setMinorTickCount(1);
		redSlider.setId("red");
		redLabel.setGraphic(redSlider);
		redLabel.setContentDisplay(ContentDisplay.RIGHT);
		red.setGraphic(redLabel);
		MenuItem green = new MenuItem();
		Slider greenSlider = new Slider(0d, 100d, 50d);
		greenSlider.setShowTickMarks(true);
		greenSlider.setShowTickLabels(true);
		greenSlider.setMajorTickUnit(10d);
		greenSlider.setBlockIncrement(5d);
		greenSlider.setMinorTickCount(1);
		greenSlider.setId("green");
		greenLabel.setContentDisplay(ContentDisplay.RIGHT);
		greenLabel.setGraphic(greenSlider);
		green.setGraphic(greenLabel);
		MenuItem blue = new MenuItem();
		Slider blueSlider = new Slider(0d, 100d, 50d);
		blueSlider.setShowTickMarks(true);
		blueSlider.setShowTickLabels(true);
		blueSlider.setMajorTickUnit(10d);
		blueSlider.setBlockIncrement(5d);
		blueSlider.setMinorTickCount(1);
		blueSlider.setId("blue");
		blueLabel.setGraphic(blueSlider);
		blueLabel.setContentDisplay(ContentDisplay.RIGHT);
		blue.setGraphic(blueLabel);
		colorMenu.getItems().addAll(red, green, blue);
		mainMenu.getMenus().addAll(fileMenu, rotateMenu, colorMenu);
		colorSliders[0] = redSlider;
		colorSliders[1] = greenSlider;
		colorSliders[2] = blueSlider;
		redSlider.valueProperty().addListener(onRGBChange);
		greenSlider.valueProperty().addListener(onRGBChange);
		blueSlider.valueProperty().addListener(onRGBChange);
		
		// moving buttons : 
		movementButtons.layoutXProperty().bind(r.widthProperty().subtract(55));
		movementButtons.layoutYProperty().bind(r.heightProperty().subtract(30));
		Button leftButton = new Button();
		Button rightButton = new Button();
		Shape leftGraphic = new Polygon(4f, 6f, 8f, 3f, 8f, 9f)
				, rightGraphic = new Polygon(8f, 6f, 4f, 9f, 4f, 3f);
		leftButton.setGraphic(leftGraphic);
		rightButton.setGraphic(rightGraphic);
		leftButton.setPrefSize(12, 12);
		rightButton.setPrefSize(12, 12);
		leftButton.setLayoutX(0);
		leftButton.setLayoutY(0);
		rightButton.layoutXProperty().bind(leftButton.widthProperty());
		rightButton.setLayoutY(0);
		movementButtons.getChildren().addAll(leftButton, rightButton);
		turnOffMovementButtons();
		
		root.setTop(mainMenu);
		root.setBottom(imageWrapper);
		r.getChildren().add(movementButtons);
		
		primaryStage.setScene(scene);
		primaryStage.show();
		
		return ;
	}
	@SuppressWarnings("unchecked")
	private void initRGBMethod() {
		onRGBChange = new ChangeListener() {
			@Override
			public void changed(ObservableValue ovo, Object ov, Object nv) {
				int oldV = (int)((double)ov);
				int newV = (int)((double)nv);
				Color c = Color.rgb((int)colorSliders[0].getValue()
				, (int)colorSliders[1].getValue(), (int)colorSliders[2].getValue());
				colorAdjust.setHue(c.getHue());
				colorAdjust.setSaturation(c.getSaturation());
				colorAdjust.setBrightness(c.getBrightness());
			}
		};
	}
	public double min(double a, double b, double c) {
		if (a<b) {
			if (a<c) {
				return a;
			}
		}
		else if (b<a) {
			if (b<c) {
				return b;
			}
		}
		else if (c<a) {
			if (c<b) {
				return c;
			}
		}
		if (a==b) {
			return a;
		}
		else if (b==c) {
			return b;
		}
		else if (a==c) {
			return a;
		}
		return a;
	}
	public double max(double a, double b, double c) {
		if (a>b) {
			if (a>c) {
				return a;
			}
		}
		else if (b>a) {
			if (b>c) {
				return b;
			}
		}
		else if (c>a) {
			if (c>b) {
				return c;
			}
		}
		if (a==b) {
			return a;
		}
		else if (b==c) {
			return b;
		}
		else if (a==c) {
			return a;
		}
		return a;
	}
/*double[] HSV = new double[3];
		double min, max, delta;
		min = min(r, g, b);
		max = max(r, g, b);
		HSV[2] = max; // v
		delta = max - min;
		System.out.println(min+" : "+max);
		if(max != 0)
			HSV[1] = delta / max; // s
		else {
			// r = g = b = 0 // s = 0, v is undefined
			HSV[1] = 0;
			HSV[0] = -1;
			System.out.println("HSV : "+Arrays.toString(HSV));
			return HSV;
		}
		float n1 = (float)r, n2 = (float)g, n3 = (float)b;
		//r = (double)n1;
		//g = (double)n2;
		//b = (double)n3;
		//System.out.println("After truncating : "+r+" "+g+" "+b);
		if(r == max) {
			HSV[0] = (g - b) / delta; // between yellow & magenta
		}
		else if(g == max) {
			HSV[0] = 2 + (b - r) / delta; // between cyan & yellow
		}
		else {
			HSV[0] = 4 + ( r - g ) / delta; // between magenta & cyan
			HSV[0] *= 60; // degrees
		}
		if (HSV[0] < 0) {
			HSV[0] += 360;
		}
		System.out.println("HSV : "+Arrays.toString(HSV));
		return HSV;
	// TODO : Complete this method : 
	void startLoader() {
		isLoading = true;
		parentNode.getChildren().add(loader);
	}
	// TODO : Complete this method : 
	void endLoader() {
		isLoading = false;
		parentNode.getChildren().remove(loader);
	} 
	void setImage(Image img) {
		imageView.setImage(img);
		prmStage.setWidth(img.getWidth()+16+30);
		prmStage.setHeight(img.getHeight()+25+39+30);
		// 25 pixels for the menu!
		// 16 width+ & 39 height+
		// 30 width+ & 30 height for imageView margin!
		System.out.println(imageView.getImage().getWidth()+" : "+imageView.getImage().getHeight());
		prmStage.setResizable(false);
		
		return ;
	} 
	void turnOnMovementButtons() {
		movementButtons.setOpacity(1.0);
		movementButtons.setDisable(false);
	}
	void turnOffMovementButtons() {
		movementButtons.setOpacity(0.0);
		movementButtons.setDisable(true);
	}
}

*/