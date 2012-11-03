JPerfTester: A Simple Java Benchmark library
---------------------------------------------

This library is built as a modular facade to facilitate the execution of multi-threaded operations against various JAVA libraries and tools.
It contains and abstracts boiler plates functions such as threading, timing measurements, operation life cycle, etc... and ensures that this boiler plate is contention free.
That way, client loader/benchmarking programs can simply focus on implementing the specific operations needed to "load" the tools/library under test.
Built in java, this library run wherever JAVA runs (in other words, every OS)

The main advantages of this Java library are:
 - Direct execution of the multi-threaded tests against your application libraries
 - Create "loader" operations that use your own business objects, all using JAVA (no new processes / languages to learn)
 - Accurate and least intrusive timing measurements
 - Extensibility of code + framework under test (easy to add new frameworks to test against)
 - Simplicity of build / install / run
 
Build and Install the library using maven:
 1. Navigate to JPerfTester root folder
 2. Run: mvn install
 
To refer to this library from another project, simply add the following in your maven pom.xml (make sure to use the right version number):

	...
	<dependencies>
	...
		<dependency>
			<groupId>org.terracotta.utils</groupId>
			<artifactId>jperftester-engine</artifactId>
			<version>${jperftester.version}</version>
		</dependency>
	...
	</dependencies>
	...

If you want to create a test project for [Terracotta BigMemory or Ehcache](http://terracotta.org/), add the following (instead of the above) in your maven pom.xml:

	...
	<dependencies>
	...
		<dependency>
			<groupId>org.terracotta.utils</groupId>
			<artifactId>jperftester-cacheengine</artifactId>
			<version>${jperftester.version}</version>
		</dependency>
	...
	</dependencies>
	...

- more doc to come...
- sample client application to come as well...

