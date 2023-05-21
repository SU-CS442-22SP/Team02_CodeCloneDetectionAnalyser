package jp.naist.se.simplecc;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CodeToken {

	private String text;
	private File file;
	private int line;
	private int charPositionInLine;
	private long hash;
	private int position; // New field to store the position of the token

	public CodeToken(String text, String normalized, File f, int line, int charPositionInLine, int position) {
		this.text = text;
		this.file = f;
		this.line = line;
		this.charPositionInLine = charPositionInLine;
		this.position = position; // Initialize the new field
		if (normalized != null) {
			this.hash = getHash(normalized);
		}
	}

	public static CodeToken getTerminalToken() {
		return new CodeToken(null, null, null, 0, 0, 0); // Pass 0 as the position
	}

	private long getHash(String s) {
		long hash = 0;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(s.getBytes());
			byte[] b = digest.digest();
			for (int i=0; i<8; i++) {
				hash = (hash << 8) + (long)b[i];
			}
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		return hash;
	}

	public boolean isSameToken(CodeToken another) {
		return this.text != null && another.text != null && this.hash == another.hash && this.text.equals(another.text) && this.position != another.position;
		// Check if the tokens are at the same position
	}

	public String getText() {
		return text;
	}

	public File getFile() {
		return file;
	}

	public int getLine() {
		return line;
	}

	public int getCharPositionInLine() {
		return charPositionInLine;
	}

	public int getEndCharPositionInLine() {
		return charPositionInLine + text.length();
	}

	@Override
	public String toString() {
		return file.getAbsolutePath() + "," + line + "," + charPositionInLine + "," + text + "," + hash;
	}
}
