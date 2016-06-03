package com.whenling.castle.support.core.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class Java8ApiByStringsNumbersMathFiles {

	@Test
	public void slicingStrings() {
		String.join(":", "foobar", "foo", "bar");
		// => foobar:foo:bar

		"foobar:foo:bar".chars().distinct().mapToObj(c -> String.valueOf((char) c)).sorted()
				.collect(Collectors.joining());
		// => :abfor

		Pattern.compile(":").splitAsStream("foobar:foo:bar").filter(s -> s.contains("bar")).sorted()
				.collect(Collectors.joining(":"));
		// => bar:foobar

		Pattern pattern = Pattern.compile(".*@gmail\\.com");
		Stream.of("bob@gmail.com", "alice@hotmail.com").filter(pattern.asPredicate()).count();
		// => 1
	}

	@Test
	public void crunchingNumbers() {
		System.out.println(Integer.MAX_VALUE); // 2147483647
		System.out.println(Integer.MAX_VALUE + 1); // -2147483648

		long maxUnsignedInt = (1l << 32) - 1;
		String string = String.valueOf(maxUnsignedInt);
		int unsignedInt = Integer.parseUnsignedInt(string, 10);
		String string2 = Integer.toUnsignedString(unsignedInt, 10);
		System.out.println(string2);
		try {
			Integer.parseInt(string, 10);
		} catch (NumberFormatException e) {
			System.err.println("could not parse signed int of " + maxUnsignedInt);
		}

	}

	@Test
	public void doTheMath() {
		System.out.println(Integer.MAX_VALUE); // 2147483647
		System.out.println(Integer.MAX_VALUE + 1); // -2147483648

		try {
			Math.addExact(Integer.MAX_VALUE, 1);
		} catch (ArithmeticException e) {
			System.err.println(e.getMessage());
			// => integer overflow
		}

		try {
			Math.toIntExact(Long.MAX_VALUE);
		} catch (ArithmeticException e) {
			System.err.println(e.getMessage());
			// => integer overflow
		}
	}

	@Test
	public void workingWithFiles() throws IOException {
		try (Stream<Path> stream = Files.list(Paths.get(""))) {
			String joined = stream.map(String::valueOf).filter(path -> !path.startsWith(".")).sorted()
					.collect(Collectors.joining("; "));
			System.out.println("List: " + joined);
		}

		Path start = Paths.get("");
		int maxDepth = 5;
		try (Stream<Path> stream = Files.find(start, maxDepth, (path, attr) -> String.valueOf(path).endsWith(".js"))) {
			String joined = stream.sorted().map(String::valueOf).collect(Collectors.joining("; "));
			System.out.println("Found: " + joined);
		}

		start = Paths.get("");
		maxDepth = 5;
		try (Stream<Path> stream = Files.walk(start, maxDepth)) {
			String joined = stream.map(String::valueOf).filter(path -> path.endsWith(".js")).sorted()
					.collect(Collectors.joining("; "));
			System.out.println("walk(): " + joined);
		}

		List<String> lines = Files.readAllLines(Paths.get("res/nashorn1.js"));
		lines.add("print('foobar');");
		Files.write(Paths.get("res/nashorn1-modified.js"), lines);

		try (Stream<String> stream = Files.lines(Paths.get("res/nashorn1.js"))) {
			stream.filter(line -> line.contains("print")).map(String::trim).forEach(System.out::println);
		}

		Path path = Paths.get("res/nashorn1.js");
		try (BufferedReader reader = Files.newBufferedReader(path)) {
			System.out.println(reader.readLine());
		}

		path = Paths.get("res/output.js");
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			writer.write("print('Hello World');");
		}

		path = Paths.get("res/nashorn1.js");
		try (BufferedReader reader = Files.newBufferedReader(path)) {
			long countPrints = reader.lines().filter(line -> line.contains("print")).count();
			System.out.println(countPrints);
		}

	}
}
