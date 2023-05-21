package jp.naist.se.simplecc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Token;

/**
 * A simple type-2 clone detector.
 * @author Takashi Ishio
 */
public class CloneDetectionMain {

	private static int MIN_TOKENS = 20;

	public static void main(String[] args) {
		System.out.println("Comparing files " + args[0] + " and " + args[1]);
		ArrayList<CodeToken> tokens1 = readFile(args[0]);
		ArrayList<CodeToken> tokens2 = readFile(args[1]);
		detectClones(tokens1, tokens2, MIN_TOKENS);
	}

	private static ArrayList<CodeToken> readFile(String path) {
		ArrayList<CodeToken> tokens = new ArrayList<>();
		File f = new File(path);
		if (f.getName().toLowerCase().endsWith(".java")) {
			try {
				Java8Lexer lexer = new Java8Lexer(CharStreams.fromPath(f.toPath()));
				int position = 0; // A variable to store the position of each token
				for (Token t = lexer.nextToken(); t.getType() != Lexer.EOF; t = lexer.nextToken(), position++) {
					CodeToken token = new CodeToken(t.getText(), getNormalizedText(t), f, t.getLine(), t.getCharPositionInLine(), position);
					tokens.add(token);
				}
				tokens.add(CodeToken.getTerminalToken());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return tokens;
	}

	/*
        private static ArrayList<CodeToken> readFiles(String[] args) {
            ArrayList<CodeToken> tokens = new ArrayList<>();
            for (String arg: args) {
                DirectoryScan.scan(new File(arg), new DirectoryScan.Action() {
                    @Override
                    public void process(File f) {
                        if (f.getName().toLowerCase().endsWith(".java")) {
                            try {
                                Java8Lexer lexer = new Java8Lexer(CharStreams.fromPath(f.toPath()));
                                for (Token t = lexer.nextToken(); t.getType() != Lexer.EOF; t = lexer.nextToken()) {
                                    CodeToken token = new CodeToken(t.getText(), getNormalizedText(t), f, t.getLine(), t.getCharPositionInLine());
                                    tokens.add(token);
                                }
                                tokens.add(CodeToken.getTerminalToken());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
            return tokens;
        }
    */
	private static String getNormalizedText(Token t) {
		switch (t.getType()) {
		case Java8Lexer.Identifier:
		case Java8Lexer.INT:
		case Java8Lexer.LONG:
		case Java8Lexer.FLOAT:
		case Java8Lexer.DOUBLE:
		case Java8Lexer.SHORT:
		case Java8Lexer.BOOLEAN:
		case Java8Lexer.BYTE:
		case Java8Lexer.VOID:
			return "$p";
		default:
			return t.getText();
		}
	}

	private static void detectClones(ArrayList<CodeToken> tokens1, ArrayList<CodeToken> tokens2, int threshold) {
		for (int i = 0; i < tokens1.size(); i++) {
			for (int j = 0; j < tokens2.size(); j++) {
				try {
					// Ensure the tokens are from different files and are identical
					if (!tokens1.get(i).getFile().getAbsolutePath().equals(tokens2.get(j).getFile().getAbsolutePath()) && tokens1.get(i).isSameToken(tokens2.get(j))) {
						int match = 0;
						while ((i + match) < tokens1.size() && (j + match) < tokens2.size() && tokens1.get(i + match).isSameToken(tokens2.get(j + match))) {
							match++;
						}
						if (match >= threshold) {
							reportClone(tokens1, tokens2, i, j, match);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}




	private static void reportClone(ArrayList<CodeToken> tokens1, ArrayList<CodeToken> tokens2, int start1, int start2, int length) {
		System.out.println("<pair>");
		printCode(tokens1, start1, length);
		printCode(tokens2, start2, length);
		System.out.println("</pair>");
	}

	private static void printCode(ArrayList<CodeToken> tokens, int start, int length) {
		CodeToken startToken = tokens.get(start);
		CodeToken endToken = tokens.get(start + length - 1);
		assert startToken.getFile() == endToken.getFile();
		System.out.print(startToken.getFile().getAbsolutePath() + "-> " + startToken.getLine() + "," + startToken.getCharPositionInLine() + "," + endToken.getLine() + "," + endToken.getEndCharPositionInLine() +  "\n");
		//for (int i=0; i<length; i++) {
		//	System.out.print(tokens.get(start + i).getText() + "\t");
		//}
		//System.out.println();
	}

}
