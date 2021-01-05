package object;

/*
 * 
 * @author Jean Fleurmond
 * @author Jilvia D'Souza
 * 
 */

public class Song {
	private String songName;
	private String artistName;
	private String album;
	private String year;
	
	public Song(String songName,String artistname,String album,String year){
		this.songName = songName;
		this.artistName = artistname;
		this.album = album;
		this.year = year;
	}
	
	public String getSongName() {
		return songName;
	}
	
	public String getArtistName() {
		return artistName;
	}
	
	public String getAlbum() {
		return album;
	}
	
	public String getYear() {
		return year;
	}
	public void setSongName(String newName) {
		songName = newName;
	}
	
	public void setArtistName(String newArtist) {
		artistName = newArtist;
	}
	
	public void setAlbum(String newAlbum) {
		album = newAlbum;
	}
	
	public void setYear(String newYear) {
		year = newYear;
	}
	
}
