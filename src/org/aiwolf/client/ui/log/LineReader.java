package org.aiwolf.client.ui.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * 一行ずつファイルを読み込むiteratorクラス
 * @author tori
 *
 */
public class LineReader implements Iterable<String>{

	
	/**
	 * LineReaderを作成する
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	static public LineReader read(String fileName) throws IOException{
		return read(new File(fileName));
	}
	
	/**
	 * LineReaderを作成する
	 * @param fileName
	 * @encode encode
	 * @return
	 * @throws IOException
	 */
	static public LineReader read(String fileName, String encode) throws IOException{
		FileInputStream is = new FileInputStream(fileName);
		InputStreamReader in = new InputStreamReader(is, encode);
		return read(in);
	}
	
	/**
	 * LineReaderを作成する
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static LineReader read(File file, String encode) throws IOException {
		FileInputStream is = new FileInputStream(file);
		InputStreamReader in = new InputStreamReader(is, encode);
		return read(in);
	}
	
	/**
	 * LineReaderを作成する
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static LineReader read(File file) throws IOException {
		return new LineReader(file);
	}
	
	/**
	 * LineReaderを作成する
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public static LineReader read(Reader reader) throws IOException {
		return new LineReader(reader);
	}

	
	
	BufferedReader br;
	FileIterator iterator;
	private LineReader(File file) throws IOException{
		FileInputStream is = new FileInputStream(file);
		InputStreamReader in = new InputStreamReader(is, "JisAutoDetect");
		br = new BufferedReader(in);
		
//		br = new BufferedReader(new FileReader(file));
		iterator = new FileIterator(br);
	}

	private LineReader(Reader reader) throws IOException {
		br = new BufferedReader(reader);
		iterator = new FileIterator(br);
	}

	@Override
	public Iterator<String> iterator() {
		return iterator;
	}

}



class FileIterator implements Iterator<String>{
	String next;
	
	BufferedReader br;
	FileIterator(BufferedReader br) throws IOException{
		this.br = br;
		next = br.readLine();
	}
	
	@Override
	public boolean hasNext() {
		return next != null;
	}

	@Override
	public String next() {
		if(next == null){
			throw new NoSuchElementException();
		}
		String line = next;
		try {
			next = br.readLine();
			if(next == null){
				br.close();
				br = null;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return line;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
