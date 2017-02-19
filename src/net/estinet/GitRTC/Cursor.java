package net.estinet.GitRTC;

public class
Cursor {
    public String name, fileloc;
    public int row, column;
    public Cursor(String name, String fileloc, int row, int column){
        this.name = name;
        this.row = row;
        this.fileloc = fileloc;
        this.column = column;
    }
}
