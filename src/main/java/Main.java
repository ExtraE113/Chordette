import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
	static int counter = 0;
	static StringBloomFilter filter = new StringBloomFilter();

	public static void listOfFiles(File dirPath) {
		File filesList[] = dirPath.listFiles();
		for (File file : filesList) {
			if (file.isFile()) {
				countLines(file);
			} else {
				listOfFiles(file);
			}
		}
	}

	public static void countLines(File file) {
		try {
			Scanner sc = new Scanner(file);
			while (sc.hasNext()) {
				String str = sc.nextLine();
				counter++;

				try {
					filter.add(str.split("[:;]", 2)[1]);
				} catch (Exception ignored) {
					// we can't do perfect data sorting across billions of lines
					// this has to be good enough
				}
			}
			sc.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		//Creating a File object for directory
		File file = new File("C:\\Users\\Ezra Newman\\IdeaProjects\\Chordette\\src\\Extracted");
		//List of all files and directories
		listOfFiles(file);
		System.out.println(counter);
		System.out.println(filter);
		Scanner scanner = new Scanner(System.in);  // Create a Scanner object
		while (true) {
			System.out.println("Password");

			String pass = scanner.nextLine();  // Read user input
			System.out.println(filter.contains(pass) ? "You have been hacked." : "You haven't been hacked");  // Output user input
		}
	}
}