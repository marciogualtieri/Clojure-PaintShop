## TABLE OF CONTENTS
### [TEST & BUILD THE APPLICATION](#test-and-build-the-application)
### [EXECUTE THE JAR IN THE COMMAND LINE](#execute-the-jar-in-the-command-line)
### [ON THE DESIGN CHOICES](#on-the-design-choices)
### [PERFORMANCE](#performance)
### [HOW TO VIEW THIS DOCUMENT](#how-to-view-this-document)

### <a name="test-and-build-the-application"></a> TEST & BUILD THE APPLICATION

    $ lein do clean, compile, test, uberjar, codox

Codox generated documentation will be available in the following file:

    ./target/doc/index.html

This application was developed using Leiningen 2.5.3 and Java 8.

### <a name="execute-the-jar-in-the-command-line"></a> EXECUTE THE JAR IN THE COMMAND LINE

    java -jar ./target/paintshop-0.1.0-SNAPSHOT-standalone.jar \
    {<input file with test cases>} \
    [-o <output file with batches solutions>]



Examples:

    java -jar target/paintshop-0.1.0-SNAPSHOT-standalone.jar \
    ./resources/input/success.txt
    
    java -jar target/paintshop-0.1.0-SNAPSHOT-standalone.jar \
    ./resources/input/performance/large_dataset.txt
    
    java -jar target/paintshop-0.1.0-SNAPSHOT-standalone.jar \
    ./resources/input/performance/small_dataset.txt


For help execute the following command:

    java -jar target/paintshop-0.1.0-SNAPSHOT-standalone.jar --help

Which will print the following:

    Usage: java -jar <jar name> <input file> [options]
    
    Options:
      -o, --output-file FILE  Name of the output file. If not provided \
          will print output to console.
      -h, --help
    
    <input-file>: Name of the input file containing test cases.

### <a name="on-the-design-choices"></a> ON THE DESIGN CHOICES

This project mirrors in basic lines the Java one available in the package regarding architecture and algorithm. Please refer to the Java project for details on the design, as well as algorithm analysis.

Clojure is not an object-oriented language, but still some object-oriented programming principles can be emulated.

In the Java and Scala project, the input iterator, input parser and output formatter were defined in abstract form using interfaces and traits respectively.

In Clojure we can achieve a similar structure by using protocols and records, thus, the following protocols have been defined:

    (defprotocol InputIterator
      (read-lines [this])
      )
  
    (defprotocol InputParser
      (parse-input [this input-iterator])
      )
    
    (defprotocol OutputFormatter
      (format-outputs [this test-cases solutions])
      )

These have their concrete implementations given by the records ```PlainTextFileInputIterator```, ```PlainTextInputParser``` and ```PlainTextOutputFormatter``` respectively.

As in the Java and Scala projects, I can't think of a reason for doing the same for the test case processor, which should not have multiple implementations.

In the Java and Scala projects the test case processor is a concrete class and a trait respectively. In the Clojure project, the test case processor is simply a function in the ```paintshop.test-case``` namespace named ```process-test-cases```.

The main function in the ```paintshop.core``` namespace uses these concrete implementations as components to read the input file, parse it to test cases, process the test cases and output the results.

Method encapsulation and polymorphism can be also achieved by using Clojure's multi-methods. I used them for the data structure which represents a customer:


    (defrecord Customer [glossies matte])
    ...
    (defmulti is-batches-satisfactory? 
        (fn [customer batches] [(class customer) (class batches)])
      )
    (defmethod is-batches-satisfactory?
        [Customer BigInteger] [customer batches]
    ...


I'm happy to say that I was able to achieve 100% functional code in this project. All data structures in this project are immutable and all functions are side-effect free.

In the Java and Scala project I'm using ```BitSet``` to store the sets of colors and respective finishes. Clojure doesn't have an equivalent immutable for ```BitSet```, but we can use Clojure's ```biginteger``` which maps directly to Java's immutable ```BigInteger```.

I could have used ```BigInteger``` in Scala (and Java as well) to achieve a more functional code, but that could affect performance due to the memory allocation overhead from copying immutable data over and over. Clojure's [structural sharing techniques](http://blog.higher-order.net/2009/02/01/understanding-clojures-persistentvector-implementation) makes it much faster than Java and Scala in this department though.

Java Interop from Clojure allows me to use ```BitSet``` as well, but I decided to go 100% functional on this project.

For parsing CLI arguments I'm using [tools.cli](https://github.com/clojure/tools.cli). For handling exceptions I'm using [Slingshot](https://github.com/scgilardi/slingshot), which I feel is nicer than Clojure's standard exception handling.

I became interested in function programming after reading [this article by Robert C. Martin](http://thecleancoder.blogspot.ie/2010/08/why-clojure.html), so my first stab at functional programming was Clojure. Along the way I was seduced by Scala, which I really enjoy, so I ended up spending more time playing with Scala more recently. Scala is much easier to learn if you come from a Java's background and Clojure's learning curve is somehow steep (unless you are a LISP or Erlang programmer), but, at present, I can't really choose between the two. They are both breaths of fresh air and have been equally enjoyable. I would gladly switch from my ol' pal Java to Scala or Clojure in a heart beat.

### <a name="performance"></a> PERFORMANCE

There is a Python script in the Java project, which has been used to generate the "large data set" and "small data set". Please refer to the Java project for details.

Running on my computer, a Lenovo Yoga 2 laptop running Ubuntu 12.04, I got the following results:

Large data set:

    10:22:06 {master} \
    ~/workspace/IdeaProjects/Zalando/Clojure/paintshop$
    java -jar target/paintshop-0.1.0-SNAPSHOT-standalone.jar \
    ./resources/input/performance/large_dataset.txt -o \
    large_dataset_output.txt
    
    Total processing time: 52 ms


Small data set:

    10:20:00 {master} \
    ~/workspace/IdeaProjects/Zalando/Clojure/paintshop$ 
    java -jar target/paintshop-0.1.0-SNAPSHOT-standalone.jar \
    ./resources/input/performance/small_dataset.txt \
    -o small_dataset_output.txt
    
    Total processing time: 25 ms

### <a name="how-to-view-this-document"></a> HOW TO VIEW THIS DOCUMENT

This document is better viewed using IntelliJ's Markdown Plugin. In case it isn't available, there is a PDF version of this document in the same directory.

For my own reference, to convert markdown to PDF use the following command:

    pandoc README.md -f markdown -t latex -s -o README.pdf