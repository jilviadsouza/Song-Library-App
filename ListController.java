package view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import object.Song;
import javafx.event.ActionEvent;

/*
 * 
 * @author Jean Fleurmond
 * @author Jilvia D'Souza
 * 
 */

public class ListController {
	
	@FXML ListView<String> listView;
	@FXML Button addButton, deleteButton, editButton, clearButton;
	@FXML TextField name, artist, album, year;
	
	private ObservableList<String> sl;
	private ArrayList<Song> ll;
	
	//comparator function: compares artist names if song names are the same
public boolean check(String song,String artist) { 
		for(int i = 0; i < sl.size(); i++) {
			if(ll.get(i).getSongName().compareToIgnoreCase(song) == 0) {
				if(ll.get(i).getArtistName().compareToIgnoreCase(artist) == 0) {
					return true;
				}
			}
		}
		
		return false;
	}

//lexicographically adds songs into the list, returns index of where song was put
	public static int lexoAdd(ArrayList<Song> ll, Song track) {
		//boolean did = false;
		for(int i = 0;i<ll.size();i++) {
			if(ll.get(i).getSongName().compareToIgnoreCase(track.getSongName()) < 0) {
				continue;
			}
			else if(ll.get(i).getSongName().compareToIgnoreCase(track.getSongName()) == 0) {
				if(ll.get(i).getArtistName().compareToIgnoreCase(track.getArtistName()) < 0) {
					continue;
				}
				ll.add(i,track);
				//did = true;
				//break;
				return i;
			}
			else {
				ll.add(i,track);
				//did = true;
				//break;
				return i;
			}
		}
		//if(did == false) {
			ll.add(track);
			return ll.size() - 1;
		//}
	}
	
	//loads array-list of song objects, and loads values into observable array-list
		public static void SongObjLL(ArrayList<Song> ll, ObservableList<String> sl) throws FileNotFoundException {
			Scanner input = new Scanner(new File("C:\\Users\\fleur\\CS213\\FileMan\\src\\fileMan\\songs.txt"));
			input.useDelimiter("\n|\\t");
			
			String temp[] = new String[4];
			
			while(input.hasNext()) {
				for(int i = 0; i < 4; i++) {
					temp[i] = input.next();
				}
				Song track = new Song(temp[0],temp[1],temp[2],temp[3]);
				if(ll.isEmpty()) {
					ll.add(0,track);
				}
				else {
					lexoAdd(ll,track);
				}
			}
	
			for(int i = 0; i < ll.size() ;i++) {
				sl.add(i,ll.get(i).getSongName() + " - " + ll.get(i).getArtistName());
			//	System.out.println(sl.get(i));
			}
			
			input.close();
		}
		
		//updates persistent save file
		public static void writeToFile(ArrayList<Song> list)
		{	
			try{
				BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\fleur\\CS213\\FileMan\\src\\fileMan\\songs.txt"));
				for(int i = 0;i < list.size();i++) 
				{
					writer.write(list.get(i).getSongName() + "\t" + list.get(i).getArtistName() + "\t" + list.get(i).getAlbum() + "\t" + list.get(i).getYear() + "\n");
				}
				writer.close();
			}
			catch(IOException e){e.getStackTrace();}
		}
		
		//deletes a song, and updates file
		public void delete(int index) {
			sl.remove(index);
			ll.remove(index);
			writeToFile(ll);
		}
		
		//adds an item, and updates file, returns index of where song was put
		public int add(Song track) {
			int z = lexoAdd(ll,track);
			sl.add(z,track.getSongName() + " - " + track.getArtistName());
			writeToFile(ll);
			return z;
		}
		//edits an item
		public int edit(int index,String song,String artist,String album,String year) {
			Song temp = new Song(song,artist,album,year);
			ll.remove(index);
			int x = lexoAdd(ll,temp);
			sl.remove(index);
			sl.add(x,song + " - " + artist);
			writeToFile(ll);
			return x;
		}
		
		
		
//Start of the Main stage Controller	
	public void start(Stage mainStage) throws FileNotFoundException {
		
		ll = new ArrayList<>();
		
		sl = FXCollections.observableArrayList();
		
		File f = new File("C:\\Users\\fleur\\CS213\\FileMan\\src\\fileMan\\songs.txt");
		boolean result;  
		try{  
			result = f.createNewFile();  //creates a new file  
			if(result){       // test if successfully created a new file   
				//System.out.println("file created "+ f.getCanonicalPath()); //returns the path string  
			}  
			else{  
				//System.out.println("File already exist at location: "+ f.getCanonicalPath());  
			}  
		}   
		catch (IOException e){  
			e.printStackTrace();    //prints exception if any  
		}
		
		SongObjLL(ll,sl);
		
		listView.setItems(sl);
		
		//selects the first item
		listView.getSelectionModel().select(0);
		String item = listView.getSelectionModel().getSelectedItem();
		if(item == null || item.isEmpty()) {
			name.clear();
			artist.clear();
			album.clear();
			year.clear();
		} else {
			//shows selected items on text fields
		name.setText(ll.get(listView.getSelectionModel().getSelectedIndex()).getSongName());
		artist.setText(ll.get(listView.getSelectionModel().getSelectedIndex()).getArtistName());
		album.setText(ll.get(listView.getSelectionModel().getSelectedIndex()).getAlbum());
		year.setText(ll.get(listView.getSelectionModel().getSelectedIndex()).getYear());
		}
		
		listView.getSelectionModel().selectedIndexProperty().addListener(
				(obs, oldVal, newVal) -> showItem(mainStage));
	}
	
	private void showItem(Stage mainStage) {                
		String item = listView.getSelectionModel().getSelectedItem();
		int index = listView.getSelectionModel().getSelectedIndex();
	}
	
	//buttons
	public void buttonFunction(ActionEvent e) {
		Button b = (Button)e.getSource();
		
		if(b == addButton) {
			//when song name or artist is blank
			if(name.getText().trim().isEmpty() || artist.getText().trim().isEmpty()) {
				//error pop up
				Alert errorAlert = new Alert(AlertType.ERROR);
				errorAlert.setTitle("Error!");
				errorAlert.setHeaderText("Invalid Input");
				errorAlert.setContentText("Enter a valid input for Song Name and Artist");
				errorAlert.showAndWait();
				
				//
			}
			//if song has already been added
			else if(check(name.getText().trim(),artist.getText().trim())) {
				//error pop up
				Alert errorAlert = new Alert(AlertType.ERROR);
				errorAlert.setTitle("Error!");
				errorAlert.setHeaderText("Invalid Input");
				errorAlert.setContentText("Song Already Exists!");
				errorAlert.showAndWait();
							
				//	
			}
			//add with confirmation
			else {
				//confirmation pop up
				Alert confo = new Alert(AlertType.CONFIRMATION);
				confo.setTitle("Confirm Action");
				confo.setHeaderText("Confirm Action");
				confo.setContentText("Are You Sure?");
				confo.showAndWait();
				
				if(confo.getResult() != ButtonType.CANCEL) {
					String al = album.getText();
					String yr = year.getText();
					if(al.trim().isEmpty()) {
						al = "n/a";
					}
					if(yr.trim().isEmpty()) {
						yr = "n/a";
					}
				
					Song temp = new Song(name.getText(),artist.getText(),al,yr);
					int z = add(temp);
					listView.getSelectionModel().select(z);
					
				}
			}
			
		} else if(b==editButton) {
			//check if no value for song name or artist name
			if(name.getText().trim().isEmpty() || artist.getText().trim().isEmpty()) {
			//error pop up	
				Alert errorAlert = new Alert(AlertType.ERROR);
				errorAlert.setTitle("Error!");
				errorAlert.setHeaderText("Input Invalid");
				errorAlert.setContentText("Enter a valid input for Song Name and Artist");
				errorAlert.showAndWait();
			
			//
			}
			//song is already in the list 
			else if(check(name.getText().trim(),artist.getText().trim())) {
				if(name.getText().trim().compareToIgnoreCase(ll.get(listView.getSelectionModel().getSelectedIndex()).getSongName()) == 0
				&& artist.getText().trim().compareToIgnoreCase(ll.get(listView.getSelectionModel().getSelectedIndex()).getArtistName()) == 0 ) 
				{
					//confirmation pop up
					Alert confo = new Alert(AlertType.CONFIRMATION);
					confo.setTitle("Confirm Action");
					confo.setHeaderText("Confirm Action");
					confo.setContentText("Are You Sure?");
					confo.showAndWait();
					
					if(confo.getResult() != ButtonType.CANCEL) {
						//if any text field is empty, changes to string n/a
						String al = album.getText();
						String yr = year.getText();
						if(al.trim().isEmpty()) {
							al = "n/a";
						}
						if(yr.trim().isEmpty()) {
							yr = "n/a";
						}
						edit(listView.getSelectionModel().getSelectedIndex(),name.getText(),artist.getText(),al,yr);
					
					}
				}
				else {
					//error pop up	
					Alert errorAlert = new Alert(AlertType.ERROR);
					errorAlert.setTitle("Error!");
					errorAlert.setHeaderText("Invalid Input");
					errorAlert.setContentText("Song Already Exists");
					errorAlert.showAndWait();
				}
			}
			else {
				//confirmation pop up
				Alert confo = new Alert(AlertType.CONFIRMATION);
				confo.setTitle("Confirm Action");
				confo.setHeaderText("Confirm Action");
				confo.setContentText("Are You Sure?");
				confo.showAndWait();
				
				if(confo.getResult() != ButtonType.CANCEL) {
					String al = album.getText();
					String yr = year.getText();
					if(al.trim().isEmpty()) {
						al = "n/a";
					}
					if(yr.trim().isEmpty()) {
						yr = "n/a";
					}
					int z = edit(listView.getSelectionModel().getSelectedIndex(),name.getText(),artist.getText(),al,yr);
					listView.getSelectionModel().select(z);
				}
			}
			
		}else if(b==deleteButton){
			//confirmation pop up
			Alert confo = new Alert(AlertType.CONFIRMATION);
			confo.setTitle("Confirm Action");
			confo.setHeaderText("Confirm Action");
			confo.setContentText("Are You Sure?");
			confo.showAndWait();
			
			if(confo.getResult() != ButtonType.CANCEL) {
				delete(listView.getSelectionModel().getSelectedIndex());
				
				
				//to select the item before or after the item just deleted
				if(listView.getSelectionModel().getSelectedIndex()+1 != 0) {
					listView.getSelectionModel().select(listView.getSelectionModel().getSelectedIndex()+1);
					String item = listView.getSelectionModel().getSelectedItem();
					if(item == null || item.isEmpty()) {
						name.clear();
						artist.clear();
						album.clear();
						year.clear();
					} else {
					name.setText(ll.get(listView.getSelectionModel().getSelectedIndex()).getSongName());
					artist.setText(ll.get(listView.getSelectionModel().getSelectedIndex()).getArtistName());
					album.setText(ll.get(listView.getSelectionModel().getSelectedIndex()).getAlbum());
					year.setText(ll.get(listView.getSelectionModel().getSelectedIndex()).getYear());
					}
					
				}else if(listView.getSelectionModel().getSelectedIndex()-1 != 0) {
					listView.getSelectionModel().select(listView.getSelectionModel().getSelectedIndex()-1);
					String item = listView.getSelectionModel().getSelectedItem();
					if(item == null || item.isEmpty()) {
						name.clear();
						artist.clear();
						album.clear();
						year.clear();
					} else {
					name.setText(ll.get(listView.getSelectionModel().getSelectedIndex()).getSongName());
					artist.setText(ll.get(listView.getSelectionModel().getSelectedIndex()).getArtistName());
					album.setText(ll.get(listView.getSelectionModel().getSelectedIndex()).getAlbum());
					year.setText(ll.get(listView.getSelectionModel().getSelectedIndex()).getYear());
					}
				} else {
					name.clear();
					artist.clear();
					album.clear();
					year.clear();
			}
		}
		} else if(b==clearButton) {
			name.clear();
			artist.clear();
			album.clear();
			year.clear();
		}	
	}
	
	//displays selected list item on text fields
	public void display(MouseEvent e) {
		String item = listView.getSelectionModel().getSelectedItem();
		
		if(item == null || item.isEmpty()) {
			name.setText("n/a");
			artist.setText("n/a");
			album.setText("n/a");
			year.setText("n/a");
		} else {
		name.setText(ll.get(listView.getSelectionModel().getSelectedIndex()).getSongName());
		artist.setText(ll.get(listView.getSelectionModel().getSelectedIndex()).getArtistName());
		album.setText(ll.get(listView.getSelectionModel().getSelectedIndex()).getAlbum());
		year.setText(ll.get(listView.getSelectionModel().getSelectedIndex()).getYear());
		}
	}
}